package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
import org.w3c.dom.Text;
import repository.UserRepository;
import util.SceneChanger;

import java.io.IOException;
import java.sql.SQLException;

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

    public void goToLogin(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/Login.fxml","Login",event);
    }

    public void register(){
        User user = new User(firstName.getText(), lastName.getText(), email.getText(), passwordTextField.getText());
        userRepository.add(user);
    }

    public void togglePassword(){

    }

    public void back(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/LandingPage.fxml","ShelfAware",event);
    }
}
