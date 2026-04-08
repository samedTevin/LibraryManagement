package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import model.Book;

import java.util.Objects;

public class CardController {

    @FXML
    private Label authorName;

    @FXML
    private Label bookName;

    @FXML
    private HBox box;

    @FXML
    private ImageView bookImage;


    public void setData(Book book) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(book.getImageSrc())));
        bookImage.setImage(image);

        bookName.setText(book.getTitle());

        authorName.setText(book.getAuthor());
    }

}
