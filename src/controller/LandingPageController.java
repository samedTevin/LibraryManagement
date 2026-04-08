package controller;

import javafx.event.ActionEvent;
import util.SceneChanger;

import java.io.IOException;

public class LandingPageController {


    SceneChanger sceneChanger = new SceneChanger();

    public void register(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/Register.fxml","Register",event);
    }

    public void login(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/Login.fxml","Login",event);
    }

}
