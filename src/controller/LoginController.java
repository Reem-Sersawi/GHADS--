/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import utils.SessionManager;
import utils.ThemeManager;

/**
 *
 * @author TOP
 */
public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private UserDAO userDAO = new UserDAO();

    @FXML
    public void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("الرجاء إدخال اسم المستخدم وكلمة المرور");
            return;
        }

        if (userDAO.authenticate(username, password)) {
            User user = userDAO.getUserByUsername(username);
            SessionManager.getInstance().login(user);

            try {
                String fxmlFile = user.getRole().equals("ADMIN")
                        ? "/views/admin_dashboard.fxml"
                        : "/views/coordinator_dashboard.fxml";
                Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
                Scene scene = new Scene(root, 1200, 700);
                ThemeManager.getInstance().setScene(scene);

                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("GHADS - " + (user.getRole().equals("ADMIN")
                        ? "لوحة تحكم المدير" : "لوحة تحكم المنسق"));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                errorLabel.setText("حدث خطأ في تحميل لوحة التحكم");
            }
        } else {
            errorLabel.setText("اسم المستخدم أو كلمة المرور غير صحيحة");
        }
    }

    @FXML
    public void handleExit() {
        System.exit(0);
    }

    @FXML
    private void handleFontSize() {
        TextInputDialog dialog = new TextInputDialog("14");
        dialog.setTitle("تغيير حجم الخط");
        dialog.setHeaderText(null);
        dialog.setContentText("أدخل حجم الخط (بكسل):");
        dialog.showAndWait().ifPresent(size -> {
            try {
                int fontSize = Integer.parseInt(size);
                usernameField.getScene().getRoot()
                        .setStyle("-fx-font-size: " + fontSize + "px;");
            } catch (NumberFormatException e) {
                // تجاهل
            }
        });
    }

    @FXML
    private void handleFontFamily() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Segoe UI",
                "Segoe UI", "Arial", "Tahoma", "Times New Roman", "Roboto");
        dialog.setTitle("تغيير نوع الخط");
        dialog.setHeaderText(null);
        dialog.setContentText("اختر نوع الخط:");
        dialog.showAndWait().ifPresent(font ->
                usernameField.getScene().getRoot()
                        .setStyle("-fx-font-family: '" + font + "';")
        );
    }

    @FXML
    private void handleLightTheme() {
        ThemeManager.getInstance().setDarkMode(false);
    }

    @FXML
    private void handleDarkTheme() {
        ThemeManager.getInstance().setDarkMode(true);
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("عن النظام");
        alert.setHeaderText("نظام توزيع المساعدات الإنسانية في غزة (GHADS)");
        alert.setContentText("الإصدار: 1.0\nريم سعيد السرساوي : فريق GHADS\nجميع الحقوق محفوظة © 2026");
        alert.showAndWait();
    }
}
