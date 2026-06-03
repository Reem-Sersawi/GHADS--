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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;
import utils.SessionManager;
import utils.ThemeManager;
import utils.ValidationUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AdminDashboardController implements Initializable {

    // Dashboard Labels
    @FXML private Label totalOrgsLabel;
    @FXML private Label totalUsersLabel;
    @FXML private Label totalFamiliesLabel;
    @FXML private Label totalServedLabel;
    @FXML private Label totalUnservedLabel;

    // Organization Table
    @FXML private TableView<Organization> orgTable;
    @FXML private TableColumn<Organization, Integer> orgIdCol;
    @FXML private TableColumn<Organization, String> orgNameCol;
    @FXML private TableColumn<Organization, String> orgTypeCol;
    @FXML private TableColumn<Organization, String> orgContactCol;

    // Organization Form
    @FXML private TextField orgNameField;
    @FXML private ComboBox<String> orgTypeCombo;
    @FXML private TextField orgContactField;
    @FXML private Button orgSaveBtn;
    @FXML private Button orgResetBtn;
    @FXML private Button orgDeleteBtn;

    // User Table
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> userIdCol;
    @FXML private TableColumn<User, String> userUsernameCol;
    @FXML private TableColumn<User, String> userFullNameCol;
    @FXML private TableColumn<User, String> userEmailCol;
    @FXML private TableColumn<User, String> userRoleCol;

    // User Form
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> roleCombo;
    @FXML private ComboBox<Organization> orgCombo;
    @FXML private Button userSaveBtn;
    @FXML private Button userResetBtn;
    @FXML private Button userDeleteBtn;

    // User Image Upload (Bonus)
    @FXML private Label imageStatusLabel;

    // Family Table
    @FXML private TableView<Family> familyTable;
    @FXML private TableColumn<Family, Integer> familyIdCol;
    @FXML private TableColumn<Family, String> householdNameCol;
    @FXML private TableColumn<Family, String> familyPhoneCol;
    @FXML private TableColumn<Family, String> familyLocationCol;
    @FXML private TableColumn<Family, Integer> familySizeCol;
    @FXML private TableColumn<Family, String> familyNationalIdCol;
    @FXML private TableColumn<Family, String> vulnerabilityCol;
    @FXML private TableColumn<Family, String> regDateCol;

    // Family Form
    @FXML private TextField householdNameField;
    @FXML private TextField phoneField;
    @FXML private TextField locationField;
    @FXML private TextField familySizeField;
    @FXML private TextField nationalIdField;
    @FXML private ComboBox<String> vulnerabilityCombo;
    @FXML private DatePicker regDatePicker;
    @FXML private Button familySaveBtn;
    @FXML private Button familyResetBtn;
    @FXML private Button familyDeleteBtn;

    // Distribution Table
    @FXML private TableView<AidDistribution> distTable;
    @FXML private TableColumn<AidDistribution, Integer> distIdCol;
    @FXML private TableColumn<AidDistribution, String> distFamilyCol;
    @FXML private TableColumn<AidDistribution, String> distOrgCol;
    @FXML private TableColumn<AidDistribution, String> distDateCol;
    @FXML private TableColumn<AidDistribution, String> distTypeCol;

    // Distribution Search
    @FXML private ComboBox<Organization> searchOrgCombo;
    @FXML private TextField searchDistField;
    @FXML private Button searchOrgBtn;
    @FXML private Button searchFamilyBtn;
    @FXML private Button resetDistSearchBtn;

    @FXML private TabPane mainTabPane;

    private OrganizationDAO orgDAO = new OrganizationDAO();
    private UserDAO userDAO = new UserDAO();
    private FamilyDAO familyDAO = new FamilyDAO();
    private AidDistributionDAO aidDAO = new AidDistributionDAO();

    private Organization selectedOrg;
    private User selectedUser;
    private Family selectedFamily;

    // لحفظ الصورة المختارة عند إنشاء مستخدم جديد
    private byte[] selectedUserImage = null;
    private String selectedUserImageType = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupOrganizationTable();
        setupUserTable();
        setupFamilyTable();
        setupDistributionTable();
        setupComboBoxes();
        loadDashboardData();
        loadOrganizations();
        loadUsers();
        loadFamilies();
        loadDistributions();
        setupTableSelectionListeners();
    }

    private void setupComboBoxes() {
        orgTypeCombo.setItems(FXCollections.observableArrayList("NGO", "UN", "Local", "International"));
        roleCombo.setItems(FXCollections.observableArrayList("ADMIN", "COORDINATOR"));
        vulnerabilityCombo.setItems(FXCollections.observableArrayList("HIGH", "MEDIUM", "LOW"));
    }

    private void setupOrganizationTable() {
        orgIdCol.setCellValueFactory(new PropertyValueFactory<>("orgId"));
        orgNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        orgTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        orgContactCol.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));
    }

    private void setupUserTable() {
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userUsernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        userFullNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        userEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        userRoleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
    }

    private void setupFamilyTable() {
        familyIdCol.setCellValueFactory(new PropertyValueFactory<>("familyId"));
        householdNameCol.setCellValueFactory(new PropertyValueFactory<>("householdName"));
        familyPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        familyLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        familySizeCol.setCellValueFactory(new PropertyValueFactory<>("familySize"));
        familyNationalIdCol.setCellValueFactory(new PropertyValueFactory<>("nationalId"));
        vulnerabilityCol.setCellValueFactory(new PropertyValueFactory<>("vulnerabilityLevel"));
        regDateCol.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getRegistrationDate().toString()));
    }

    private void setupDistributionTable() {
        distIdCol.setCellValueFactory(new PropertyValueFactory<>("distributionId"));
        distFamilyCol.setCellValueFactory(cellData -> {
            Family f = familyDAO.getFamilyById(cellData.getValue().getFamilyId());
            return new javafx.beans.property.SimpleStringProperty(
                f != null ? f.getHouseholdName() : "N/A");
        });
        distOrgCol.setCellValueFactory(cellData -> {
            Organization o = orgDAO.getOrganizationById(cellData.getValue().getOrgId());
            return new javafx.beans.property.SimpleStringProperty(
                o != null ? o.getName() : "N/A");
        });
        distDateCol.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getDistributionDate().toString()));
        distTypeCol.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(
                ValidationUtils.getArabicAidType(cellData.getValue().getAidType())));
    }

    private void setupTableSelectionListeners() {
        orgTable.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) {
                selectedOrg = newVal;
                orgNameField.setText(newVal.getName());
                orgTypeCombo.setValue(newVal.getType());
                orgContactField.setText(newVal.getContactInfo());
                orgDeleteBtn.setDisable(false);
            } else {
                orgDeleteBtn.setDisable(true);
            }
        });

        userTable.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) {
                selectedUser = newVal;
                usernameField.setText(newVal.getUsername());
                fullNameField.setText(newVal.getFullName());
                emailField.setText(newVal.getEmail());
                roleCombo.setValue(newVal.getRole());
                passwordField.clear();
                if (newVal.getOrgId() != null) {
                    Organization org = orgDAO.getOrganizationById(newVal.getOrgId());
                    orgCombo.setValue(org);
                } else {
                    orgCombo.setValue(null);
                }
                // إعادة تعيين الصورة المختارة عند تحديد مستخدم موجود
                selectedUserImage = null;
                selectedUserImageType = null;
                if (imageStatusLabel != null) {
                    imageStatusLabel.setText(newVal.getProfileImage() != null
                            ? "✅ يوجد صورة" : "لا توجد صورة");
                }
                userDeleteBtn.setDisable(false);
            } else {
                userDeleteBtn.setDisable(true);
            }
        });

        familyTable.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) {
                selectedFamily = newVal;
                householdNameField.setText(newVal.getHouseholdName());
                phoneField.setText(newVal.getPhone());
                locationField.setText(newVal.getLocation());
                familySizeField.setText(String.valueOf(newVal.getFamilySize()));
                nationalIdField.setText(newVal.getNationalId());
                vulnerabilityCombo.setValue(newVal.getVulnerabilityLevel());
                regDatePicker.setValue(newVal.getRegistrationDate());
                familyDeleteBtn.setDisable(false);
            } else {
                familyDeleteBtn.setDisable(true);
            }
        });
    }

    private void loadDashboardData() {
        int orgCount = orgDAO.getOrganizationCount();
        int coordinatorCount = userDAO.getCoordinatorCount();
        int familyCount = familyDAO.getTotalFamiliesCount();
        int served = aidDAO.getTotalFamiliesServed();

        totalOrgsLabel.setText(String.valueOf(orgCount));
        totalUsersLabel.setText(String.valueOf(coordinatorCount));
        totalFamiliesLabel.setText(String.valueOf(familyCount));
        totalServedLabel.setText(String.valueOf(served));
        totalUnservedLabel.setText(String.valueOf(familyCount - served));
    }

    private void loadOrganizations() {
        List<Organization> orgs = orgDAO.getAllOrganizations();
        orgTable.setItems(FXCollections.observableArrayList(orgs));
        orgCombo.setItems(FXCollections.observableArrayList(orgs));
        searchOrgCombo.setItems(FXCollections.observableArrayList(orgs));
    }

    private void loadUsers() {
        List<User> users = userDAO.getAllUsers();
        userTable.setItems(FXCollections.observableArrayList(users));
    }

    private void loadFamilies() {
        List<Family> families = familyDAO.getAllFamilies();
        familyTable.setItems(FXCollections.observableArrayList(families));
    }

    private void loadDistributions() {
        List<AidDistribution> dists = aidDAO.getAllDistributions();
        distTable.setItems(FXCollections.observableArrayList(dists));
    }

    // Organization CRUD

    @FXML
    private void handleOrgSave() {
        String name = orgNameField.getText().trim();
        String type = orgTypeCombo.getValue();
        String contact = orgContactField.getText().trim();

        if (!ValidationUtils.isNotEmpty(name, contact) || type == null) {
            showAlert("خطأ", "جميع الحقول مطلوبة", Alert.AlertType.ERROR);
            return;
        }

        Organization org = new Organization();
        org.setName(name);
        org.setType(type);
        org.setContactInfo(contact);

        boolean success;
        if (selectedOrg == null) {
            if (!orgDAO.isNameUnique(name, 0)) {
                showAlert("خطأ", "اسم المنظمة موجود مسبقاً", Alert.AlertType.ERROR);
                return;
            }
            success = orgDAO.addOrganization(org);
        } else {
            org.setOrgId(selectedOrg.getOrgId());
            if (!orgDAO.isNameUnique(name, selectedOrg.getOrgId())) {
                showAlert("خطأ", "اسم المنظمة موجود مسبقاً", Alert.AlertType.ERROR);
                return;
            }
            success = orgDAO.updateOrganization(org);
        }

        if (success) {
            showAlert("نجاح", selectedOrg == null ? "تمت إضافة المنظمة" : "تم تعديل المنظمة",
                    Alert.AlertType.INFORMATION);
            resetOrgForm();
            loadOrganizations();
            loadDashboardData();
        } else {
            showAlert("خطأ", "حدث خطأ في حفظ المنظمة", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void resetOrgForm() {
        selectedOrg = null;
        orgNameField.clear();
        orgTypeCombo.setValue(null);
        orgContactField.clear();
        orgDeleteBtn.setDisable(true);
        orgTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleOrgDelete() {
        if (selectedOrg == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("تأكيد الحذف");
        confirm.setHeaderText(null);
        confirm.setContentText("هل أنت متأكد من حذف المنظمة " + selectedOrg.getName() + "؟");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (orgDAO.deleteOrganization(selectedOrg.getOrgId())) {
                showAlert("نجاح", "تم حذف المنظمة", Alert.AlertType.INFORMATION);
                resetOrgForm();
                loadOrganizations();
                loadDashboardData();
            } else {
                showAlert("خطأ", "لا يمكن حذف المنظمة - قد يكون لديها مستخدمين مرتبطين",
                        Alert.AlertType.ERROR);
            }
        }
    }

    // ==================== User CRUD ====================

    @FXML
    private void handleUploadUserImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("اختر صورة شخصية");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", baos);
                selectedUserImage = baos.toByteArray();
                selectedUserImageType = "png";
                if (imageStatusLabel != null) {
                    imageStatusLabel.setText("✅ تم اختيار الصورة");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("خطأ", "حدث خطأ في قراءة الصورة", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleUserSave() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String role = roleCombo.getValue();
        Organization org = orgCombo.getValue();

        if (!ValidationUtils.isNotEmpty(username, fullName, email) || role == null) {
            showAlert("خطأ", "جميع الحقول مطلوبة", Alert.AlertType.ERROR);
            return;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            showAlert("خطأ", "البريد الإلكتروني غير صالح", Alert.AlertType.ERROR);
            return;
        }

        if (!ValidationUtils.isValidUsername(username)) {
            showAlert("خطأ", "اسم المستخدم يجب أن يحتوي على 3-20 حرف أو رقم",
                    Alert.AlertType.ERROR);
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setRole(role);
        user.setOrgId(org != null ? org.getOrgId() : null);

        boolean success;
        if (selectedUser == null) {
            if (!ValidationUtils.isValidPassword(password)) {
                showAlert("خطأ", "كلمة المرور يجب أن تكون 8 أحرف على الأقل",
                        Alert.AlertType.ERROR);
                return;
            }
            if (!userDAO.isUsernameUnique(username, 0)) {
                showAlert("خطأ", "اسم المستخدم موجود مسبقاً", Alert.AlertType.ERROR);
                return;
            }
            if (!userDAO.isEmailUnique(email, 0)) {
                showAlert("خطأ", "البريد الإلكتروني موجود مسبقاً", Alert.AlertType.ERROR);
                return;
            }
            user.setPassword(password);

            // إضافة الصورة إن تم اختيارها (Bonus)
            if (selectedUserImage != null) {
                user.setProfileImage(selectedUserImage);
                user.setProfileImageType(selectedUserImageType);
            }

            success = userDAO.addUser(user);
        } else {
            if (!userDAO.isUsernameUnique(username, selectedUser.getUserId())) {
                showAlert("خطأ", "اسم المستخدم موجود مسبقاً", Alert.AlertType.ERROR);
                return;
            }
            if (!userDAO.isEmailUnique(email, selectedUser.getUserId())) {
                showAlert("خطأ", "البريد الإلكتروني موجود مسبقاً", Alert.AlertType.ERROR);
                return;
            }
            user.setUserId(selectedUser.getUserId());
            user.setPassword(selectedUser.getPassword());

            // تحديث الصورة إن تم اختيار صورة جديدة
            if (selectedUserImage != null) {
                user.setProfileImage(selectedUserImage);
                user.setProfileImageType(selectedUserImageType);
            } else {
                user.setProfileImage(selectedUser.getProfileImage());
                user.setProfileImageType(selectedUser.getProfileImageType());
            }

            success = userDAO.updateUser(user);

            if (!password.isEmpty() && ValidationUtils.isValidPassword(password)) {
                userDAO.updatePassword(selectedUser.getUserId(), password);
            }
        }

        if (success) {
            showAlert("نجاح", selectedUser == null ? "تمت إضافة المستخدم" : "تم تعديل المستخدم",
                    Alert.AlertType.INFORMATION);
            resetUserForm();
            loadUsers();
            loadDashboardData();
        } else {
            showAlert("خطأ", "حدث خطأ في حفظ المستخدم", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void resetUserForm() {
        selectedUser = null;
        usernameField.clear();
        passwordField.clear();
        fullNameField.clear();
        emailField.clear();
        roleCombo.setValue(null);
        orgCombo.setValue(null);
        selectedUserImage = null;
        selectedUserImageType = null;
        if (imageStatusLabel != null) imageStatusLabel.setText("لا توجد صورة");
        userDeleteBtn.setDisable(true);
        userTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleUserDelete() {
        if (selectedUser == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("تأكيد الحذف");
        confirm.setHeaderText(null);
        confirm.setContentText("هل أنت متأكد من حذف المستخدم " + selectedUser.getFullName() + "؟");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (userDAO.deleteUser(selectedUser.getUserId())) {
                showAlert("نجاح", "تم حذف المستخدم", Alert.AlertType.INFORMATION);
                resetUserForm();
                loadUsers();
                loadDashboardData();
            } else {
                showAlert("خطأ", "حدث خطأ في حذف المستخدم", Alert.AlertType.ERROR);
            }
        }
    }

    // ==================== Family CRUD ====================

    @FXML
    private void handleFamilySave() {
        String householdName = householdNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String location = locationField.getText().trim();
        String familySizeStr = familySizeField.getText().trim();
        String nationalId = nationalIdField.getText().trim();
        String vulnerability = vulnerabilityCombo.getValue();
        LocalDate regDate = regDatePicker.getValue();

        if (!ValidationUtils.isNotEmpty(householdName, phone, location, familySizeStr, nationalId)
                || vulnerability == null || regDate == null) {
            showAlert("خطأ", "جميع الحقول مطلوبة", Alert.AlertType.ERROR);
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
            showAlert("خطأ", "رقم الهاتف غير صالح (يجب أن يبدأ 05 ويتكون من 10 أرقام)",
                    Alert.AlertType.ERROR);
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

        Family family = new Family();
        family.setHouseholdName(householdName);
        family.setPhone(phone);
        family.setLocation(location);
        family.setFamilySize(familySize);
        family.setNationalId(nationalId);
        family.setVulnerabilityLevel(vulnerability);
        family.setRegistrationDate(regDate);

        boolean success;
        if (selectedFamily == null) {
            if (!familyDAO.isNationalIdUnique(nationalId, 0)) {
                showAlert("خطأ", "رقم الهوية موجود مسبقاً", Alert.AlertType.ERROR);
                return;
            }
            success = familyDAO.addFamily(family);
        } else {
            family.setFamilyId(selectedFamily.getFamilyId());
            if (!familyDAO.isNationalIdUnique(nationalId, selectedFamily.getFamilyId())) {
                showAlert("خطأ", "رقم الهوية موجود مسبقاً", Alert.AlertType.ERROR);
                return;
            }
            success = familyDAO.updateFamily(family);
        }

        if (success) {
            showAlert("نجاح", selectedFamily == null ? "تمت إضافة العائلة" : "تم تعديل العائلة",
                    Alert.AlertType.INFORMATION);
            resetFamilyForm();
            loadFamilies();
            loadDashboardData();
        } else {
            showAlert("خطأ", "حدث خطأ في حفظ العائلة", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void resetFamilyForm() {
        selectedFamily = null;
        householdNameField.clear();
        phoneField.clear();
        locationField.clear();
        familySizeField.clear();
        nationalIdField.clear();
        vulnerabilityCombo.setValue(null);
        regDatePicker.setValue(null);
        familyDeleteBtn.setDisable(true);
        familyTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleFamilyDelete() {
        if (selectedFamily == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("تأكيد الحذف");
        confirm.setHeaderText(null);
        confirm.setContentText("هل أنت متأكد من حذف عائلة " + selectedFamily.getHouseholdName() + "؟");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (familyDAO.deleteFamily(selectedFamily.getFamilyId())) {
                showAlert("نجاح", "تم حذف العائلة", Alert.AlertType.INFORMATION);
                resetFamilyForm();
                loadFamilies();
                loadDashboardData();
            } else {
                showAlert("خطأ", "حدث خطأ في حذف العائلة", Alert.AlertType.ERROR);
            }
        }
    }

    //Distribution Search

    @FXML
    private void handleSearchDistributionsByOrg() {
        Organization selectedOrg = searchOrgCombo.getValue();
        if (selectedOrg != null) {
            List<AidDistribution> dists = aidDAO.getDistributionsByOrg(selectedOrg.getOrgId());
            distTable.setItems(FXCollections.observableArrayList(dists));
            showAlert("بحث", "تم عرض توزيعات منظمة: " + selectedOrg.getName(),
                    Alert.AlertType.INFORMATION);
        } else {
            loadDistributions();
        }
    }

    @FXML
    private void handleSearchDistributionsByFamily() {
        String searchText = searchDistField.getText().trim();
        if (!searchText.isEmpty()) {
            List<AidDistribution> allDists = aidDAO.getAllDistributions();
            List<AidDistribution> filtered = allDists.stream()
                .filter(d -> {
                    Family f = familyDAO.getFamilyById(d.getFamilyId());
                    return f != null && f.getHouseholdName().contains(searchText);
                })
                .collect(Collectors.toList());
            distTable.setItems(FXCollections.observableArrayList(filtered));
            showAlert("بحث", "تم العثور على " + filtered.size() + " نتيجة",
                    Alert.AlertType.INFORMATION);
        } else {
            loadDistributions();
        }
    }

    @FXML
    private void resetDistributionSearch() {
        searchOrgCombo.setValue(null);
        searchDistField.clear();
        loadDistributions();
        showAlert("بحث", "تم إعادة تعيين البحث وعرض جميع التوزيعات",
                Alert.AlertType.INFORMATION);
    }

    //Password & Logout

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
                Stage stage = (Stage) mainTabPane.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("GHADS - تسجيل الدخول");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Menu Actions 

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
                mainTabPane.getScene().getRoot().setStyle("-fx-font-size: " + fontSize + "px;");
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
        result.ifPresent(font ->
            mainTabPane.getScene().getRoot().setStyle("-fx-font-family: '" + font + "';")
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

    @FXML
    private void handleHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("التعليمات");
        alert.setHeaderText("دليل استخدام المدير");
        alert.setContentText("""
            1. إدارة المنظمات: إضافة، تعديل، حذف المنظمات
            2. إدارة المستخدمين: إضافة، تعديل، حذف المنسقين
            3. إدارة العائلات: تسجيل وتعديل وحذف العائلات
            4. سجل التوزيعات: عرض جميع التوزيعات والبحث فيها
            5. تغيير كلمة المرور: من قائمة ملف
            """);
        alert.showAndWait();
    }

    private void openChangePasswordDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/change_password.fxml"));
            Parent root = loader.load();

            ChangePasswordController controller = loader.getController();
            controller.setUserId(SessionManager.getInstance().getCurrentUser().getUserId());
            controller.setCurrentPassword(
                    SessionManager.getInstance().getCurrentUser().getPassword());

            Stage stage = new Stage();
            stage.setTitle("تغيير كلمة المرور");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root, 400, 350));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
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
