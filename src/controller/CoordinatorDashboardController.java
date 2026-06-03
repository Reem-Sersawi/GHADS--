/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.*;
import utils.SessionManager;
import utils.ThemeManager;
import utils.ValidationUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.paint.ImagePattern;

/**
 *
 * @author TOP
 */
public class CoordinatorDashboardController implements Initializable {

    @FXML
    private Label welcomeLabel;
    @FXML
    private Label totalFamiliesLabel;
    @FXML
    private Label servedByOrgLabel;
    @FXML
    private Label unservedLabel;

    @FXML
    private TableView<Family> familyTable;
    @FXML
    private TableColumn<Family, Integer> familyIdCol;
    @FXML
    private TableColumn<Family, String> householdNameCol;
    @FXML
    private TableColumn<Family, String> familyPhoneCol;
    @FXML
    private TableColumn<Family, String> familyLocationCol;
    @FXML
    private TableColumn<Family, Integer> familySizeCol;
    @FXML
    private TableColumn<Family, String> vulnerabilityCol;
    @FXML
    private TableColumn<Family, String> lastAidCol;

    @FXML
    private ComboBox<Family> familyCombo;
    @FXML
    private ComboBox<String> aidTypeCombo;

    @FXML
    private TextField profileFullNameField;
    @FXML
    private TextField profileEmailField;
    @FXML
    private Label profileUsernameLabel;
    @FXML
    private Label profileOrgLabel;
    @FXML
    private Circle profileImageView;

    private FamilyDAO familyDAO = new FamilyDAO();
    private AidDistributionDAO aidDAO = new AidDistributionDAO();
    private OrganizationDAO orgDAO = new OrganizationDAO();
    private UserDAO userDAO = new UserDAO();

    private User currentUser;
    private List<Family> allFamilies;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentUser = SessionManager.getInstance().getCurrentUser();
        welcomeLabel.setText("مرحباً، " + currentUser.getFullName());

        setupFamilyTable();
        setupComboBoxes();
        setupFamilyComboBox();
        loadDashboardData();
        loadFamilies();
        loadProfile();
        setupProfileImage();
    }

    private void setupFamilyTable() {
        familyIdCol.setCellValueFactory(new PropertyValueFactory<>("familyId"));
        householdNameCol.setCellValueFactory(new PropertyValueFactory<>("householdName"));
        familyPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        familyLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        familySizeCol.setCellValueFactory(new PropertyValueFactory<>("familySize"));
        vulnerabilityCol.setCellValueFactory(new PropertyValueFactory<>("vulnerabilityLevel"));
        lastAidCol.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getLastAidDate();
            return new javafx.beans.property.SimpleStringProperty(date != null ? date.toString() : "لم يستلم");
        });
    }

    private void setupComboBoxes() {
        aidTypeCombo.setItems(FXCollections.observableArrayList(
                "طعام", "ماء", "دواء", "خيمة", "بطانيات", "ملابس", "نقدي"
        ));
    }

    private void setupFamilyComboBox() {

        familyCombo.setCellFactory(param -> new ListCell<Family>() {
            @Override
            protected void updateItem(Family item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // عرض اسم العائلة مع رقم الهاتف للتمييز
                    setText(item.getHouseholdName() + " - " + item.getPhone());
                }
            }
        });

        familyCombo.setButtonCell(new ListCell<Family>() {
            @Override
            protected void updateItem(Family item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getHouseholdName());
                }
            }
        });

        familyCombo.setConverter(new StringConverter<Family>() {
            @Override
            public String toString(Family family) {
                if (family == null) {
                    return null;
                }
                return family.getHouseholdName();
            }

            @Override
            public Family fromString(String string) {
                return null;
            }
        });
    }

    private void setupProfileImage() {
        if (currentUser.getProfileImage() != null) {
            Image image = new Image(new ByteArrayInputStream(currentUser.getProfileImage()));
            profileImageView.setFill(new ImagePattern(image));
        }
    }

    private void loadDashboardData() {
        allFamilies = familyDAO.getAllFamilies();
        int served = aidDAO.getFamiliesServedByOrg(currentUser.getOrgId());

        totalFamiliesLabel.setText(String.valueOf(allFamilies.size()));
        servedByOrgLabel.setText(String.valueOf(served));
        unservedLabel.setText(String.valueOf(allFamilies.size() - served));
    }

    private void loadFamilies() {
        allFamilies = familyDAO.getAllFamilies();
        familyTable.setItems(FXCollections.observableArrayList(allFamilies));
        familyCombo.setItems(FXCollections.observableArrayList(allFamilies));
    }

    private void loadProfile() {
        profileFullNameField.setText(currentUser.getFullName());
        profileEmailField.setText(currentUser.getEmail());
        profileUsernameLabel.setText(currentUser.getUsername());

        if (currentUser.getOrgId() != null) {
            Organization org = orgDAO.getOrganizationById(currentUser.getOrgId());
            profileOrgLabel.setText(org != null ? org.getName() : "غير محدد");
        } else {
            profileOrgLabel.setText("غير محدد");
        }
    }

    @FXML
    private void handleSearchMostVulnerable() {
        List<Family> vulnerable = familyDAO.getMostVulnerableFamilies();
        familyTable.setItems(FXCollections.observableArrayList(vulnerable));
        showAlert("بحث", "تم عرض العائلات الأكثر هشاشة (HIGH)", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleSearchUnserved() {
        List<Family> unserved = familyDAO.getUnderservedFamilies();
        familyTable.setItems(FXCollections.observableArrayList(unserved));
        showAlert("بحث", "تم عرض العائلات غير المخدومة", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleResetFamilyTable() {
        loadFamilies();
    }

    @FXML
    private void handleDistributeAid() {
        Family selectedFamily = familyCombo.getValue();
        String aidTypeArabic = aidTypeCombo.getValue();

        if (selectedFamily == null) {
            showAlert("خطأ", "الرجاء اختيار عائلة", Alert.AlertType.ERROR);
            return;
        }

        if (aidTypeArabic == null) {
            showAlert("خطأ", "الرجاء اختيار نوع المساعدة", Alert.AlertType.ERROR);
            return;
        }

        String aidType = ValidationUtils.getEnglishAidType(aidTypeArabic);
        LocalDate today = LocalDate.now();

        // Check for duplicate
        var duplicateCheck = aidDAO.checkDuplicateAid(selectedFamily.getFamilyId(), aidType, today);

        if ((boolean) duplicateCheck.get("isDuplicate")) {
            String vulnerability = (String) duplicateCheck.get("vulnerability");
            String orgName = (String) duplicateCheck.get("orgName");
            LocalDate lastDate = (LocalDate) duplicateCheck.get("lastDate");
            String dupAidType = (String) duplicateCheck.get("aidType");
            String familyName = (String) duplicateCheck.get("familyName");

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("️ منع توزيع مكرر");
            alert.setHeaderText("لا يمكن توزيع المساعدة على عائلة: " + familyName);
            alert.setContentText(String.format(
                    "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n"
                    + "                     تفاصيل المنع\n"
                    + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n"
                    + " اسم العائلة        : %s\n"
                    + " مستوى الهشاشة       : %s\n"
                    + " نوع المساعدة المطلوبة : %s\n"
                    + " المنظمة المانحة السابقة : %s\n"
                    + "تاريخ آخر توزيع     : %s\n"
                    + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n"
                    + " سبب المنع:\n"
                    + "   لا يمكن إعادة توزيع نفس نوع المساعدة خلال 30 يوماً\n"
                    + "   للعائلات ذات الهشاشة المتوسطة أو المنخفضة.",
                    familyName,
                    getArabicVulnerability(vulnerability),
                    aidTypeArabic,
                    orgName,
                    lastDate
            ));
            alert.showAndWait();
            return;
        }

        // Allow distribution
        AidDistribution dist = new AidDistribution();
        dist.setFamilyId(selectedFamily.getFamilyId());
        dist.setOrgId(currentUser.getOrgId());
        dist.setDistributedBy(currentUser.getUserId());
        dist.setDistributionDate(today);
        dist.setAidType(aidType);
        dist.setQuantity(1);

        if (aidDAO.recordDistribution(dist)) {
            showAlert("✅ نجاح", "تم تسجيل توزيع المساعدة بنجاح", Alert.AlertType.INFORMATION);
            familyCombo.setValue(null);
            aidTypeCombo.setValue(null);
            loadDashboardData();
            loadFamilies();
        } else {
            showAlert("خطأ", "حدث خطأ في تسجيل التوزيع", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleRegisterFamily() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/register_family.fxml"));
            Parent root = loader.load();

            RegisterFamilyController controller = loader.getController();
            controller.setOnSaveCallback(() -> {
                loadFamilies();
                loadDashboardData();
            });

            Stage stage = new Stage();
            stage.setTitle("تسجيل عائلة جديدة");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root, 600, 550));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("خطأ", "حدث خطأ في فتح نافذة التسجيل", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleUpdateProfile() {
        String fullName = profileFullNameField.getText().trim();
        String email = profileEmailField.getText().trim();

        if (!ValidationUtils.isNotEmpty(fullName, email)) {
            showAlert("خطأ", "جميع الحقول مطلوبة", Alert.AlertType.ERROR);
            return;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            showAlert("خطأ", "البريد الإلكتروني غير صالح", Alert.AlertType.ERROR);
            return;
        }

        currentUser.setFullName(fullName);
        currentUser.setEmail(email);

        if (userDAO.updateUser(currentUser)) {
            showAlert("نجاح", "تم تحديث الملف الشخصي", Alert.AlertType.INFORMATION);
            welcomeLabel.setText("مرحباً، " + fullName);
        } else {
            showAlert("خطأ", "حدث خطأ في تحديث الملف الشخصي", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleChangeProfileImage() {
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
                    setupProfileImage();
                    showAlert("نجاح", "تم تحديث الصورة الشخصية", Alert.AlertType.INFORMATION);
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
    private void handleChangePassword() {
        openChangePasswordDialog();
    }

    @FXML
    private void handleLogout() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("تأكيد الخروج");
        confirm.setHeaderText(null);
        confirm.setContentText("هل أنت متأكد من الخروج من النظام؟");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            SessionManager.getInstance().logout();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
                Scene scene = new Scene(root, 900, 600);
                ThemeManager.getInstance().setScene(scene);
                Stage stage = (Stage) familyTable.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("GHADS - تسجيل الدخول");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }

    @FXML
    private void handleFontSize() {
        TextInputDialog dialog = new TextInputDialog("14");
        dialog.setTitle("تغيير حجم الخط");
        dialog.setHeaderText(null);
        dialog.setContentText("أدخل حجم الخط (بكسل):");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(size -> {
            try {
                int fontSize = Integer.parseInt(size);
                familyTable.getScene().getRoot().setStyle("-fx-font-size: " + fontSize + "px;");
            } catch (NumberFormatException e) {
                showAlert("خطأ", "حجم الخط غير صالح", Alert.AlertType.ERROR);
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
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(font -> {
            familyTable.getScene().getRoot().setStyle("-fx-font-family: '" + font + "';");
        });
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

    @FXML
    private void handleHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("التعليمات");
        alert.setHeaderText("دليل استخدام المنسق");
        alert.setContentText("""
            1. تسجيل عائلات جديدة: اضغط ملف ← تسجيل عائلة جديدة
                              
            2. توزيع المساعدات: اختر عائلة ونوع مساعدة ثم اضغط توزيع
                             
            3. البحث: يمكنك تصفية العائلات الأكثر هشاشة أو غير المخدومة
                             
            4. فحص التكرار: النظام يمنع توزيع نفس المساعدة لنفس العائلة خلال 30 يوماً
                             
            """);
        alert.showAndWait();
    }

    private void openChangePasswordDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/change_password.fxml"));
            Parent root = loader.load();

            ChangePasswordController controller = loader.getController();
            controller.setUserId(currentUser.getUserId());
            controller.setCurrentPassword(currentUser.getPassword());

            Stage stage = new Stage();
            stage.setTitle("تغيير كلمة المرور");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root, 400, 350));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getArabicVulnerability(String level) {
        switch (level) {
            case "HIGH":
                return "مرتفع";
            case "MEDIUM":
                return "متوسط";
            case "LOW":
                return "منخفض";
            default:
                return level;
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
