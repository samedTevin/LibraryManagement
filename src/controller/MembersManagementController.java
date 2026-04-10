package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import model.Member;
import repository.MemberRepository;
import util.Alerts;
import util.SceneChanger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.ResourceBundle;

public class MembersManagementController implements Initializable {

    private final SceneChanger sceneChanger = new SceneChanger();
    private final MemberRepository memberRepository = new MemberRepository();

    @FXML private TextField searchField;
    @FXML private TableView<Member> membersTable;
    @FXML private TableColumn<Member, Integer> colId;
    @FXML private TableColumn<Member, String> colFirstName;
    @FXML private TableColumn<Member, String> colLastName;
    @FXML private TableColumn<Member, String> colEmail;
    @FXML private TableColumn<Member, String> colPhone;

    @FXML private ImageView memberImagePreview;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;

    private String currentImagePath = "/view/assets/placeholder_member.png";
    private Member selectedMember = null;
    private ObservableList<Member> memberList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        loadMembers();

        membersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            }
        });
    }

    private void loadMembers() {
        List<Member> list = memberRepository.getAll();
        memberList = FXCollections.observableArrayList(list);
        membersTable.setItems(memberList);
    }

    private void populateForm(Member member) {
        this.selectedMember = member;
        firstNameField.setText(member.getFirstName());
        lastNameField.setText(member.getLastName());
        emailField.setText(member.getEmail());
        phoneField.setText(member.getPhone());
        
        if (currentImagePath != null && !currentImagePath.isEmpty()) {
            try {
                if (currentImagePath.startsWith("/view/assets")) {
                    var stream = getClass().getResourceAsStream(currentImagePath);
                    if (stream != null) {
                        memberImagePreview.setImage(new Image(stream));
                    } else {
                        memberImagePreview.setImage(new Image(getClass().getResourceAsStream("/view/assets/placeholder_member.png")));
                    }
                } else {
                    File file = new File(currentImagePath);
                    if (file.exists()) {
                        memberImagePreview.setImage(new Image(file.toURI().toString()));
                    } else {
                        var stream = getClass().getResourceAsStream("/view/assets/placeholder_member.png");
                        if (stream != null) memberImagePreview.setImage(new Image(stream));
                    }
                }
            } catch (Exception e) {
                // Ignore image load error
            }
        }
    }

    @FXML
    void handleSearch(ActionEvent event) {
        String query = searchField.getText();
        if (query == null || query.trim().isEmpty()) {
            loadMembers();
            return;
        }
        List<Member> list = memberRepository.search(query);
        membersTable.setItems(FXCollections.observableArrayList(list));
    }

    @FXML
    void handleClearSearch(ActionEvent event) {
        searchField.clear();
        loadMembers();
    }

    @FXML
    void chooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                String projectDir = System.getProperty("user.dir");
                File assetsDir = new File(projectDir, "src/view/assets");
                if (!assetsDir.exists()) assetsDir.mkdirs();

                File destFile = new File(assetsDir, selectedFile.getName());
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                currentImagePath = "/view/assets/" + selectedFile.getName();
                memberImagePreview.setImage(new Image(destFile.toURI().toString()));
            } catch (IOException e) {
                Alerts.showError("Failed to save image: " + e.getMessage());
            }
        }
    }

    @FXML
    void handleAdd(ActionEvent event) {
        if (!validateInput()) return;

        Member member = new Member();
        member.setFirstName(firstNameField.getText());
        member.setLastName(lastNameField.getText());
        member.setEmail(emailField.getText());
        member.setPhone(phoneField.getText());
        member.setImagePath(currentImagePath);

        if (memberRepository.add(member)) {
            Alerts.showInformation("Member added successfully.");
            loadMembers();
            clearForm();
        } else {
            Alerts.showError("Failed to add member to the database.");
        }
    }

    @FXML
    void handleUpdate(ActionEvent event) {
        if (selectedMember == null) {
            Alerts.showError("Please select a member from the table to update.");
            return;
        }
        if (!validateInput()) return;

        selectedMember.setFirstName(firstNameField.getText());
        selectedMember.setLastName(lastNameField.getText());
        selectedMember.setEmail(emailField.getText());
        selectedMember.setPhone(phoneField.getText());
        selectedMember.setImagePath(currentImagePath);

        if (memberRepository.update(selectedMember)) {
            Alerts.showInformation("Member updated successfully.");
            loadMembers();
            membersTable.refresh();
        } else {
            Alerts.showError("Failed to update member in the database.");
        }
    }

    @FXML
    void handleDelete(ActionEvent event) {
        if (selectedMember == null) {
            Alerts.showError("Please select a member from the table to delete.");
            return;
        }

        if (Alerts.showConfirmation("Are you sure you want to delete this member?")) {
            boolean success = memberRepository.delete(selectedMember.getId());
            if (success) {
                Alerts.showInformation("Member deleted successfully.");
                loadMembers();
                clearForm();
            } else {
                Alerts.showError("Failed to delete member.");
            }
        }
    }

    @FXML
    void clearForm() {
        this.selectedMember = null;
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneField.clear();
        this.currentImagePath = "/view/assets/placeholder_member.png";
        try {
            var stream = getClass().getResourceAsStream(currentImagePath);
            if (stream != null) {
                memberImagePreview.setImage(new Image(stream));
            }
        } catch(Exception e) {}
        membersTable.getSelectionModel().clearSelection();
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/Menu.fxml", "Menu", event);
    }

    private boolean validateInput() {
        if (firstNameField.getText().trim().isEmpty()) {
            Alerts.showError("First Name cannot be empty.");
            return false;
        }
        if (lastNameField.getText().trim().isEmpty()) {
            Alerts.showError("Last Name cannot be empty.");
            return false;
        }
        String mail = emailField.getText().trim();
        if (mail.isEmpty() || !mail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            Alerts.showError("Please enter a valid email address.");
            return false;
        }
        
        String phone = phoneField.getText().trim();
        if (phone.isEmpty() || !phone.matches("^\\+?\\d{10,15}$")) {
            Alerts.showError("Please enter a valid phone number (digits only, optionally starting with +, between 10 to 15 characters).");
            return false;
        }
        return true;
    }
}
