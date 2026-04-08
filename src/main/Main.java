package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
       Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/fxml/LandingPage.fxml")));
       stage.setTitle("ShelfAware");
       stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/view/assets/shelfaware_app_icon.png"))));
       stage.setScene(new Scene(root, 1500, 800));
       stage.setResizable(false);
       stage.show();
    }
}