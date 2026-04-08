package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Book;
import session.Session;
import util.Alerts;
import util.SceneChanger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class MenuController implements Initializable {



    @FXML
    private HBox cardLayout;
    @FXML
    private GridPane bookContainer;
    @FXML
    private ImageView profileImage;
    @FXML
    private Label profileName;

    private final SceneChanger sceneChanger = new  SceneChanger();

    private List<Book> recentlyAdded;
    private List<Book> recommendedBooks;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        profileName.setText(Session.getUser().getFirstName() + " " + Session.getUser().getLastName());

        if(Session.getUser().getImagePath() != null){
            Image image = new Image(new File(Session.getUser().getImagePath()).toURI().toString());
            profileImage.setImage(image);
        }

        recentlyAdded = new ArrayList<>(recentlyAdded());
        recommendedBooks = new ArrayList<>(recommendedBooks());
        int column = 0;
        int row = 1;
        try {
            for (Book value : recentlyAdded) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/fxml/Card.fxml"));
                HBox cardBox = fxmlLoader.load();
                CardController cardController = fxmlLoader.getController();
                cardController.setData(value);
                cardLayout.getChildren().add(cardBox);
            }

            for ( Book book : recommendedBooks){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/fxml/Book.fxml"));
                VBox bookBox = fxmlLoader.load();
                BookController bookController = fxmlLoader.getController();
                bookController.setData(book);

                if(column == 6){
                    column = 0;
                    ++ row;
                }

                bookContainer.add(bookBox,column++,row);
                GridPane.setMargin(bookBox,new Insets(10));

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Book> recentlyAdded(){
        List<Book> books = new ArrayList<>();
        Book book = new Book();
        book.setTitle("THE PRINCE");
        book.setImageSrc("/view/assets/test.png");
        book.setAuthor("Machiavelli");

        books.add(book);

        book = new Book();
        book.setTitle("THE PRINCE");
        book.setImageSrc("/view/assets/test.png");
        book.setAuthor("Machiavelli");

        books.add(book);

        book = new Book();
        book.setTitle("THE PRINCE");
        book.setImageSrc("/view/assets/test.png");
        book.setAuthor("Machiavelli");

        books.add(book);

        book = new Book();
        book.setTitle("THE PRINCE");
        book.setImageSrc("/view/assets/test.png");
        book.setAuthor("Machiavelli");

        books.add(book);



        return books;
    }


    private List<Book> recommendedBooks(){
        List<Book> books = new ArrayList<>();
        Book book = new Book();
        book.setTitle("THE PRINCE");
        book.setImageSrc("/view/assets/test.png");
        book.setAuthor("Machiavelli");

        books.add(book);

        book = new Book();
        book.setTitle("THE PRINCE");
        book.setImageSrc("/view/assets/test.png");
        book.setAuthor("Machiavelli");

        books.add(book);

        book = new Book();
        book.setTitle("THE PRINCE");
        book.setImageSrc("/view/assets/test.png");
        book.setAuthor("Machiavelli");

        books.add(book);

        book = new Book();
        book.setTitle("THE PRINCE");
        book.setImageSrc("/view/assets/test.png");
        book.setAuthor("Machiavelli");

        books.add(book);

        book = new Book();
        book.setTitle("THE PRINCE");
        book.setImageSrc("/view/assets/test.png");
        book.setAuthor("Machiavelli");

        books.add(book);

        book = new Book();
        book.setTitle("THE PRINCE");
        book.setImageSrc("/view/assets/test.png");
        book.setAuthor("Machiavelli");

        books.add(book);

        book = new Book();
        book.setTitle("THE PRINCE");
        book.setImageSrc("/view/assets/test.png");
        book.setAuthor("Machiavelli");

        books.add(book);


        book = new Book();
        book.setTitle("THE PRINCE");
        book.setImageSrc("/view/assets/test.png");
        book.setAuthor("Machiavelli");

        books.add(book);

        book = new Book();
        book.setTitle("THE PRINCE");
        book.setImageSrc("/view/assets/test.png");
        book.setAuthor("Machiavelli");

        books.add(book);

        book = new Book();
        book.setTitle("THE PRINCE");
        book.setImageSrc("/view/assets/test.png");
        book.setAuthor("Machiavelli");

        books.add(book);

        return books;
    }

    @FXML
    void goToBookManagement(ActionEvent event) {

    }

    @FXML
    void goToBooks(ActionEvent event) {

    }

    @FXML
    void goToCirculation(ActionEvent event) {

    }

    @FXML
    void goToMemberManagement(ActionEvent event) {

    }

    @FXML
    void goToMembers(ActionEvent event) {

    }

    @FXML
    void goToProfile(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/Profile.fxml","Profile",event);
    }

    @FXML
    void goToStatistics(ActionEvent event) {

    }

    @FXML
    void logOut(ActionEvent event) throws IOException {

        if(Alerts.showConfirmation("Are you sure to logout ?")){
            sceneChanger.changeScene("/view/fxml/LandingPage.fxml","Landing Page",event);
        }
    }


}
