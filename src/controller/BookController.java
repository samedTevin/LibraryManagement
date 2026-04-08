package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import model.Book;

public class BookController {
    @FXML
    private Label authorName;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookName;
    
    @FXML
    private Label bookCount;

    @FXML
    private Label bookId;

    @FXML
    private HBox ratingHBox;

    public void setData(Book book) {
        String path = book.getImageSrc();
        try {
            if (path != null && !path.isEmpty()) {
                if (path.startsWith("/view/assets")) {
                    bookImage.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream(path)));
                } else {
                    java.io.File file = new java.io.File(path);
                    if (file.exists()) {
                        bookImage.setImage(new javafx.scene.image.Image(file.toURI().toString()));
                    }
                }
            } else {
                bookImage.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream("/view/assets/placeholder_book.png")));
            }
        } catch (Exception e) {
            try {
                bookImage.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream("/view/assets/placeholder_book.png")));
            } catch (Exception ex) {
                // Ignore if even fallback fails
            }
        }
        bookId.setText("#" + book.getId());
        bookName.setText(book.getTitle());
        authorName.setText(book.getAuthor());
        bookCount.setText("Available Stock: " + book.getCount());
        
        // Update Stars
        int rating = book.getRating();
        int i = 0;
        for (Node node : ratingHBox.getChildren()) {
            if (node instanceof ImageView) {
                ImageView star = (ImageView) node;
                if (i < rating) {
                    star.setImage(new Image(getClass().getResourceAsStream("/view/assets/star_gold.png")));
                } else {
                    star.setImage(new Image(getClass().getResourceAsStream("/view/assets/star_grey.png")));
                }
                i++;
            }
        }
    }
}
