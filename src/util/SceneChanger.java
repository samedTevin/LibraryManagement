package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneChanger {

    public void changeScene(String path, String title, javafx.event.ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(scene);
    }
}
