/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.FamilyDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Family;
import utils.ValidationUtils;

import java.time.LocalDate;

/**
 *
 * @author TOP
 */

public class RegisterFamilyController {

    @FXML private TextField householdNameField;
    @FXML private TextField phoneField;
    @FXML private TextField locationField;
    @FXML private TextField familySizeField;
    @FXML private TextField nationalIdField;
    @FXML private ComboBox<String> vulnerabilityCombo;
    @FXML private DatePicker regDatePicker;
    @FXML private TextArea notesField;
    @FXML private Button saveBtn;
    @FXML private Button resetBtn;

    private FamilyDAO familyDAO = new FamilyDAO();
    private Runnable onSaveCallback;

    @FXML
    public void initialize() {
        vulnerabilityCombo.setItems(FXCollections.observableArrayList("HIGH", "MEDIUM", "LOW"));
        regDatePicker.setValue(LocalDate.now());
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    @FXML
    private void handleSave() {
        String householdName = householdNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String location = locationField.getText().trim();
        String familySizeStr = familySizeField.getText().trim();
        String nationalId = nationalIdField.getText().trim();
        String vulnerability = vulnerabilityCombo.getValue();
        LocalDate regDate = regDatePicker.getValue();
        String notes = notesField.getText();

        if (!ValidationUtils.isNotEmpty(householdName, phone, location, familySizeStr, nationalId) || 
            vulnerability == null || regDate == null) {
            showAlert("خطأ", "جميع الحقول المطلوبة يجب تعبئتها", Alert.AlertType.ERROR);
            return;
        }

        int familySize;
        try {
            familySize = Integer.parseInt(familySizeStr);
        } catch (NumberFormatException e) {
            showAlert("خطأ", "عدد الأفراد يجب أن يكون رقماً", Alert.AlertType.ERROR);
            return;
        }

        if (!ValidationUtils.isValidPhone(phone)) {
            showAlert("خطأ", "رقم الهاتف غير صالح (يجب أن يبدأ 05 ويتكون من 10 أرقام)", Alert.AlertType.ERROR);
            return;
        }

        if (!ValidationUtils.isValidNationalId(nationalId)) {
            showAlert("خطأ", "رقم الهوية غير صالح (9-15 رقم)", Alert.AlertType.ERROR);
            return;
        }

        if (!ValidationUtils.isValidFamilySize(familySize)) {
            showAlert("خطأ", "عدد الأفراد يجب أن يكون بين 1 و 20", Alert.AlertType.ERROR);
            return;
        }

        if (!familyDAO.isNationalIdUnique(nationalId, 0)) {
            showAlert("خطأ", "رقم الهوية موجود مسبقاً", Alert.AlertType.ERROR);
            return;
        }

        Family family = new Family();
        family.setHouseholdName(householdName);
        family.setPhone(phone);
        family.setLocation(location);
        family.setFamilySize(familySize);
        family.setNationalId(nationalId);
        family.setVulnerabilityLevel(vulnerability);
        family.setRegistrationDate(regDate);
        family.setNotes(notes);

        if (familyDAO.addFamily(family)) {
            showAlert("نجاح", "تم تسجيل العائلة بنجاح", Alert.AlertType.INFORMATION);
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
            handleReset();
            close();
        } else {
            showAlert("خطأ", "حدث خطأ في تسجيل العائلة", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleReset() {
        householdNameField.clear();
        phoneField.clear();
        locationField.clear();
        familySizeField.clear();
        nationalIdField.clear();
        vulnerabilityCombo.setValue(null);
        regDatePicker.setValue(LocalDate.now());
        notesField.clear();
    }

    @FXML
    private void handleCancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) saveBtn.getScene().getWindow();
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
