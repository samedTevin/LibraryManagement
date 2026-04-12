package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import model.Book;


public class CardController {

    @FXML
    private Label authorName;

    @FXML
    private Label bookName;

    @FXML
    private HBox box;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookId;


    public void setData(Book book) {
        String path = book.getImageSrc();
        try {
            if (path != null && !path.isEmpty()) {
                if (path.startsWith("/view/assets")) {
                    bookImage.setImage(new Image(getClass().getResourceAsStream(path)));
                } else {
                    java.io.File file = new java.io.File(path);
                    if (file.exists()) {
                        bookImage.setImage(new Image(file.toURI().toString()));
                    } else {
                        bookImage.setImage(new Image(getClass().getResourceAsStream("/view/assets/placeholder_book.png")));
                    }
                }
            } else {
                bookImage.setImage(new Image(getClass().getResourceAsStream("/view/assets/placeholder_book.png")));
            }
        } catch (Exception e) {
            try {
                bookImage.setImage(new Image(getClass().getResourceAsStream("/view/assets/placeholder_book.png")));
            } catch (Exception ex) {}
        }
        bookId.setText("#" + book.getId());
        bookName.setText(book.getTitle());
        authorName.setText(book.getAuthor());
    }

}
