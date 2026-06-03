/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package controller;

import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

/**
 *
 * @author TOP
 */

public class ChangePasswordController {
    
    @FXML private PasswordField oldPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    
    private UserDAO userDAO = new UserDAO();
    private int userId;
    private String currentPassword;
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
    
    @FXML
    private void handleSave() {
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        if (!oldPassword.equals(currentPassword)) {
            showAlert("خطأ", "كلمة المرور الحالية غير صحيحة", Alert.AlertType.ERROR);
            return;
        }
        
        if (newPassword.length() < 8) {
            showAlert("خطأ", "كلمة المرور الجديدة يجب أن تكون 8 أحرف على الأقل", Alert.AlertType.ERROR);
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            showAlert("خطأ", "كلمة المرور الجديدة وتأكيدها غير متطابقين", Alert.AlertType.ERROR);
            return;
        }
        
        if (userDAO.updatePassword(userId, newPassword)) {
            showAlert("نجاح", "تم تغيير كلمة المرور بنجاح", Alert.AlertType.INFORMATION);
            close();
        } else {
            showAlert("خطأ", "حدث خطأ في تغيير كلمة المرور", Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleCancel() {
        close();
    }
    
    private void close() {
        Stage stage = (Stage) oldPasswordField.getScene().getWindow();
        stage.close();
    }
    
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}