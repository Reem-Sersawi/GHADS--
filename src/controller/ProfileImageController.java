/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.UserDAO;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import model.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 *
 * @author TOP
 */

public class ProfileImageController {
    
    @FXML private ImageView profileImageView;
    @FXML private Button uploadBtn;
    @FXML private Button removeBtn;
    
    private UserDAO userDAO = new UserDAO();
    private User currentUser;
    private Runnable onImageUpdate;
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        loadProfileImage();
    }
    
    public void setOnImageUpdate(Runnable callback) {
        this.onImageUpdate = callback;
    }
    
    private void loadProfileImage() {
        if (currentUser.getProfileImage() != null) {
            Image image = new Image(new ByteArrayInputStream(currentUser.getProfileImage()));
            profileImageView.setImage(image);
            setCircularClip();
        }
    }
    
    private void setCircularClip() {
        Circle clip = new Circle(50, 50, 50);
        profileImageView.setClip(clip);
    }
    
    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("اختر صورة شخصية");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(selectedFile);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", baos);
                byte[] imageBytes = baos.toByteArray();
                
                if (userDAO.updateUserProfileImage(currentUser.getUserId(), imageBytes, "png")) {
                    currentUser.setProfileImage(imageBytes);
                    loadProfileImage();
                    showAlert("نجاح", "تم تحديث الصورة الشخصية", Alert.AlertType.INFORMATION);
                    if (onImageUpdate != null) onImageUpdate.run();
                } else {
                    showAlert("خطأ", "حدث خطأ في حفظ الصورة", Alert.AlertType.ERROR);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("خطأ", "حدث خطأ في قراءة الصورة", Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void handleRemoveImage() {
        if (userDAO.updateUserProfileImage(currentUser.getUserId(), null, null)) {
            currentUser.setProfileImage(null);
            profileImageView.setImage(null);
            showAlert("نجاح", "تم إزالة الصورة الشخصية", Alert.AlertType.INFORMATION);
            if (onImageUpdate != null) onImageUpdate.run();
        } else {
            showAlert("خطأ", "حدث خطأ في إزالة الصورة", Alert.AlertType.ERROR);
        }
    }
    
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}