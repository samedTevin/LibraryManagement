package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
import repository.UserRepository;
import session.Session;
import util.Alerts;
import util.SceneChanger;

import java.io.IOException;
import java.sql.SQLException;


public class LoginController {

    UserRepository userRepository = new UserRepository();
    SceneChanger sceneChanger = new SceneChanger();
    @FXML
    private TextField email;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private TextField passwordTextField;




    @FXML
    private CheckBox showPasswordCheckBox;

    @FXML
    public void initialize() {
        passwordTextField.textProperty().bindBidirectional(passwordPasswordField.textProperty());
        passwordTextField.setVisible(false);
    }

    public void login(ActionEvent event) throws IOException {

        String mail = email.getText();
        String pass = passwordTextField.getText() != null && !passwordTextField.getText().isEmpty() ? passwordTextField.getText() : passwordPasswordField.getText();

        if (mail == null || mail.trim().isEmpty() || pass == null || pass.trim().isEmpty()) {
            Alerts.showError("Please enter your email and password.");
            return;
        }

        if (!mail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            Alerts.showError("Please enter a valid email address.");
            return;
        }

        User user = userRepository.getByEmailAndPassword(mail, pass);

        if(user == null) {
            Alerts.showError("Invalid email or password");
        }
        else{
            Session.setUser(user);
            Alerts.showInformation("Login Successful");
            sceneChanger.changeScene("/view/fxml/Menu.fxml","Menu",event);
        }
    }

    public void forgotPassword(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/ForgotPassword.fxml","Forgot Password",event);
    }

    public void goToRegister(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/Register.fxml","Register",event);
    }

    public void togglePassword(ActionEvent event) {
        boolean show = showPasswordCheckBox.isSelected();
        passwordTextField.setVisible(show);
        passwordPasswordField.setVisible(!show);
    }

    public void back(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/LandingPage.fxml","ShelfAware",event);
    }
}
