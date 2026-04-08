package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
import repository.UserRepository;
import util.Alerts;
import util.SceneChanger;

import java.io.IOException;

public class RegisterController {

    SceneChanger sceneChanger = new SceneChanger();
    UserRepository userRepository = new UserRepository();

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private PasswordField confirmPasswordPasswordField;
    @FXML
    private TextField confirmPasswordTextField;
    @FXML
    private CheckBox showPasswordCheckBox;

    @FXML
    public void initialize() {
        // Sync texts bidirectionally
        passwordTextField.textProperty().bindBidirectional(passwordPasswordField.textProperty());
        confirmPasswordTextField.textProperty().bindBidirectional(confirmPasswordPasswordField.textProperty());

        // Hide text fields by default
        passwordTextField.setVisible(false);
        confirmPasswordTextField.setVisible(false);
    }

    public void goToLogin(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/Login.fxml","Login",event);
    }

    public void register(ActionEvent event) {
        String fName = firstName.getText();
        String lName = lastName.getText();
        String mail = email.getText();
        String pass = passwordPasswordField.getText();
        String confirmPass = confirmPasswordPasswordField.getText();

        if (fName == null || fName.trim().isEmpty() ||
            lName == null || lName.trim().isEmpty() ||
            mail == null || mail.trim().isEmpty() ||
            pass == null || pass.trim().isEmpty()) {
            Alerts.showError("Please fill in all fields.");
            return;
        }

        if (!mail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            Alerts.showError("Please enter a valid email address.");
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

        User user = new User(fName, lName, mail, pass);
        userRepository.add(user);
        Alerts.showInformation("Registration successful!");
        try {
            sceneChanger.changeScene("/view/fxml/Login.fxml", "Login", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void togglePassword() {
        boolean show = showPasswordCheckBox.isSelected();
        passwordTextField.setVisible(show);
        passwordPasswordField.setVisible(!show);
        confirmPasswordTextField.setVisible(show);
        confirmPasswordPasswordField.setVisible(!show);
    }

    public void back(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/LandingPage.fxml","ShelfAware",event);
    }
}
