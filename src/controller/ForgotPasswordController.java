package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import repository.UserRepository;
import util.Alerts;
import util.SceneChanger;

import java.io.IOException;
import java.sql.SQLException;

public class ForgotPasswordController {

    SceneChanger sceneChanger = new SceneChanger();
    UserRepository userRepository = new UserRepository();

    @FXML
    private TextField email;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField confirmPasswordTextField;
    @FXML
    private PasswordField confirmPasswordPasswordField;


    @FXML
    private CheckBox showPasswordCheckBox;

    @FXML
    public void initialize() {
        passwordTextField.textProperty().bindBidirectional(passwordPasswordField.textProperty());
        confirmPasswordTextField.textProperty().bindBidirectional(confirmPasswordPasswordField.textProperty());
        passwordTextField.setVisible(false);
        confirmPasswordTextField.setVisible(false);
    }

    public void updatePassword() {
        String mail = email.getText();
        String pass = passwordPasswordField.getText();
        if (pass == null || pass.isEmpty()) pass = passwordTextField.getText();
        
        String confirmPass = confirmPasswordPasswordField.getText();
        if (confirmPass == null || confirmPass.isEmpty()) confirmPass = confirmPasswordTextField.getText();

        if (mail == null || mail.trim().isEmpty() || pass == null || pass.trim().isEmpty()) {
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

        if (Alerts.showConfirmation("Are you sure to update password ?")) {
            userRepository.updatePassword(mail.trim(), pass.trim());
            Alerts.showInformation("Password updated successfully.");
        }
    }

    public void back(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/Login.fxml","Login",event);
    }

    public void togglePassword(ActionEvent event) {
        boolean show = showPasswordCheckBox.isSelected();
        passwordTextField.setVisible(show);
        passwordPasswordField.setVisible(!show);
        confirmPasswordTextField.setVisible(show);
        confirmPasswordPasswordField.setVisible(!show);
    }
}
