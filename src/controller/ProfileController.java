package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import repository.UserRepository;
import session.Session;
import util.Alerts;
import util.SceneChanger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;



public class ProfileController implements Initializable {


    UserRepository userRepository = new UserRepository();
    SceneChanger sceneChanger = new SceneChanger();

    @FXML
    private TextField confirmPasswordEditText;

    @FXML
    private Label creationDate;

    @FXML
    private Label email;

    @FXML
    private TextField emailEditText;

    @FXML
    private Label firstAndLastName;

    @FXML
    private TextField firstNameEditText;

    @FXML
    private TextField lastNameEditText;

    @FXML
    private TextField passwordEditText;

    @FXML
    private ImageView profileImage;

    @FXML
    private Label updatedDate;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadProfileImage();
        loadUserData();
    }


    public void back(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/Menu.fxml", "Menu", event);
    }


    public void deleteAccount(ActionEvent event) throws IOException {
        if (Alerts.showConfirmation("Are you sure you want to delete your account?")) {


            boolean deleted = userRepository.delete(Session.getUser().getId());

            if (deleted) {
                Session.setUser(null);
                sceneChanger.changeScene("/view/fxml/LandingPage.fxml", "Landing Page", event);
            }
        }
    }


    public void editImage(ActionEvent event) {
        FileChooser filechooser = new FileChooser();
        File file = filechooser.showOpenDialog(null);

        if (file != null) {
            Image image = new Image(file.toURI().toString());
            profileImage.setImage(image);
            String path = file.getAbsolutePath();
            userRepository.updateProfileImage(path, Session.getUser().getEmail());
            Session.getUser().setImagePath(path);
            loadProfileImage();
        }
    }


    public void updateProfile(ActionEvent event) {
        String fName = firstNameEditText.getText().trim();
        String lName = lastNameEditText.getText().trim();
        String mail = emailEditText.getText().trim();

        if (fName.isEmpty() || lName.isEmpty() || mail.isEmpty()) {
            Alerts.showError("Please fill in all profile fields.");
            return;
        }

        if (!mail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            Alerts.showError("Please enter a valid email address.");
            return;
        }

        Session.getUser().setFirstName(fName);
        Session.getUser().setLastName(lName);
        Session.getUser().setEmail(mail);

        userRepository.updateProfileDetails(Session.getUser());

        Session.setUser(userRepository.getByEmailAndPassword(Session.getUser().getEmail(), Session.getUser().getPassword()));

        Alerts.showInformation("Profile Details updated successfully.");

        loadUserData();
    }

    public void updatePassword(ActionEvent event) {
        String pass = passwordEditText.getText();
        String confirmPass = confirmPasswordEditText.getText();

        if (pass == null || pass.trim().isEmpty() || confirmPass == null || confirmPass.trim().isEmpty()) {
            Alerts.showError("Please enter your new password in both fields.");
            return;
        }

        if (pass.length() < 6) {
            Alerts.showError("Password must be at least 6 characters long.");
            return;
        }

        if (!pass.equals(confirmPass)) {
            Alerts.showError("Passwords do not match.");
            return;
        }

        Session.getUser().setPassword(pass);

        userRepository.updatePassword(Session.getUser().getEmail(), Session.getUser().getPassword());

        Session.setUser(userRepository.getByEmailAndPassword(Session.getUser().getEmail(), Session.getUser().getPassword()));

        Alerts.showInformation("Password updated successfully.");

        loadUserData();
    }


    // Non-FXML functions
    public void loadProfileImage() {
        if (Session.getUser().getImagePath() != null && !Session.getUser().getImagePath().isEmpty()) {
            try {
                Image image = new Image(new File(Session.getUser().getImagePath()).toURI().toString());
                profileImage.setImage(image);
            } catch (Exception e) {
                profileImage.setImage(new Image(getClass().getResourceAsStream("/view/assets/placeholder_member.png")));
            }
        } else {
            profileImage.setImage(new Image(getClass().getResourceAsStream("/view/assets/placeholder_member.png")));
        }
    }

    public void loadUserData() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        firstAndLastName.setText(Session.getUser().getFirstName() + " " + Session.getUser().getLastName());
        email.setText(Session.getUser().getEmail());

        creationDate.setText(Session.getUser().getCreatedAt().format(formatter));
        updatedDate.setText(Session.getUser().getUpdatedAt().format(formatter));

        firstNameEditText.setText(Session.getUser().getFirstName());
        lastNameEditText.setText(Session.getUser().getLastName());
        emailEditText.setText(Session.getUser().getEmail());
        passwordEditText.setText(Session.getUser().getPassword());
        confirmPasswordEditText.setText(Session.getUser().getPassword());
    }
}