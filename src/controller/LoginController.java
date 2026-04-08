package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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




    public void login(ActionEvent event) throws IOException {


            User user = userRepository.getByEmailAndPassword(email.getText(), passwordTextField.getText());


            if(user == null) {
                Alerts.showInformation("Invalid email or password");
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

    public void togglePassword(ActionEvent event) throws IOException {

    }

    public void back(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/LandingPage.fxml","ShelfAware",event);
    }
}
