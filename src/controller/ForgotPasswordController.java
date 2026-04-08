package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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


    public void updatePassword(){
        if(Alerts.showConfirmation("Are you sure to update password ?")){
            userRepository.updatePassword(email.getText().trim(),passwordTextField.getText().trim());

        }
    }

    public void back(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/Login.fxml","Login",event);
    }

    public void togglePassword(ActionEvent event) throws IOException {

    }
}
