package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Book;

public class BookController {
    @FXML
    private Label authorName;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookName;

    public void setData(Book book) {
        Image image = new Image(getClass().getResourceAsStream("/view/assets/test.png"));
        bookImage.setImage(image);
        bookName.setText(book.getTitle());
        authorName.setText(book.getAuthor());
    }
}
