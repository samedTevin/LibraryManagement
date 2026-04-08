package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Book;
import repository.BookRepository;
import util.Alerts;
import util.SceneChanger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.ResourceBundle;

public class BooksManagementController implements Initializable {

    private final SceneChanger sceneChanger = new SceneChanger();
    private final BookRepository bookRepository = new BookRepository();

    @FXML private TextField searchField;
    @FXML private TableView<Book> booksTable;
    @FXML private TableColumn<Book, Integer> colId;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, Integer> colCount;
    @FXML private TableColumn<Book, Integer> colRating;

    @FXML private ImageView bookImagePreview;
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField countField;
    @FXML private TextField ratingField;

    private String currentImagePath = "/view/assets/placeholder_book.png";
    private Book selectedBook = null;
    private ObservableList<Book> bookList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        loadBooks();

        // Listen for selection changes
        booksTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            }
        });
    }

    private void loadBooks() {
        List<Book> list = bookRepository.getAll();
        bookList = FXCollections.observableArrayList(list);
        booksTable.setItems(bookList);
    }

    private void populateForm(Book book) {
        this.selectedBook = book;
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        countField.setText(String.valueOf(book.getCount()));
        ratingField.setText(String.valueOf(book.getRating()));
        
        this.currentImagePath = book.getImageSrc();
        if (currentImagePath != null && !currentImagePath.isEmpty()) {
            try {
                if (currentImagePath.startsWith("/view/assets")) {
                    bookImagePreview.setImage(new Image(getClass().getResourceAsStream(currentImagePath)));
                } else {
                    File file = new File(currentImagePath);
                    if (file.exists()) {
                        bookImagePreview.setImage(new Image(file.toURI().toString()));
                    }
                }
            } catch (Exception e) {
                // Ignore image load error
            }
        }
    }

    @FXML
    void handleSearch(ActionEvent event) {
        String query = searchField.getText();
        if (query == null || query.trim().isEmpty()) {
            loadBooks();
            return;
        }
        List<Book> list = bookRepository.search(query);
        booksTable.setItems(FXCollections.observableArrayList(list));
    }

    @FXML
    void handleClearSearch(ActionEvent event) {
        searchField.clear();
        loadBooks();
    }

    @FXML
    void chooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                // Let's copy the file to src/view/assets directory to make it portable
                String projectDir = System.getProperty("user.dir");
                File assetsDir = new File(projectDir, "src/view/assets");
                if (!assetsDir.exists()) assetsDir.mkdirs();

                File destFile = new File(assetsDir, selectedFile.getName());
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                currentImagePath = "/view/assets/" + selectedFile.getName();
                bookImagePreview.setImage(new Image(destFile.toURI().toString()));
            } catch (IOException e) {
                Alerts.showError("Failed to save image: " + e.getMessage());
            }
        }
    }

    @FXML
    void handleAdd(ActionEvent event) {
        if (!validateInput()) return;

        Book book = new Book();
        book.setTitle(titleField.getText());
        book.setAuthor(authorField.getText());
        book.setCount(Integer.parseInt(countField.getText()));
        book.setRating(Integer.parseInt(ratingField.getText()));
        book.setImageSrc(currentImagePath);

        if (bookRepository.add(book)) {
            Alerts.showInformation("Book added successfully.");
            loadBooks();
            clearForm();
        } else {
            Alerts.showError("Failed to add book to the database. Please check your database connection and schema.");
        }
    }

    @FXML
    void handleUpdate(ActionEvent event) {
        if (selectedBook == null) {
            Alerts.showError("Please select a book from the table to update.");
            return;
        }
        if (!validateInput()) return;

        selectedBook.setTitle(titleField.getText());
        selectedBook.setAuthor(authorField.getText());
        selectedBook.setCount(Integer.parseInt(countField.getText()));
        selectedBook.setRating(Integer.parseInt(ratingField.getText()));
        selectedBook.setImageSrc(currentImagePath);

        if (bookRepository.update(selectedBook)) {
            Alerts.showInformation("Book updated successfully.");
            loadBooks();
            booksTable.refresh();
        } else {
            Alerts.showError("Failed to update book in the database.");
        }
    }

    @FXML
    void handleDelete(ActionEvent event) {
        if (selectedBook == null) {
            Alerts.showError("Please select a book from the table to delete.");
            return;
        }

        if (Alerts.showConfirmation("Are you sure you want to delete this book?")) {
            boolean success = bookRepository.delete(selectedBook.getId());
            if (success) {
                Alerts.showInformation("Book deleted successfully.");
                loadBooks();
                clearForm();
            } else {
                Alerts.showError("Failed to delete book.");
            }
        }
    }

    @FXML
    void clearForm() {
        this.selectedBook = null;
        titleField.clear();
        authorField.clear();
        countField.clear();
        ratingField.clear();
        this.currentImagePath = "/view/assets/placeholder_book.png";
        try {
            bookImagePreview.setImage(new Image(getClass().getResourceAsStream(currentImagePath)));
        } catch(Exception e) {}
        booksTable.getSelectionModel().clearSelection();
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/Menu.fxml", "Menu", event);
    }

    private boolean validateInput() {
        if (titleField.getText().trim().isEmpty()) {
            Alerts.showError("Title cannot be empty.");
            return false;
        }
        if (authorField.getText().trim().isEmpty()) {
            Alerts.showError("Author cannot be empty.");
            return false;
        }
        try {
            int count = Integer.parseInt(countField.getText());
            if (count < 0) {
                Alerts.showError("Count cannot be negative.");
                return false;
            }
        } catch (NumberFormatException e) {
            Alerts.showError("Count must be a valid number.");
            return false;
        }
        
        try {
            int rating = Integer.parseInt(ratingField.getText());
            if (rating < 1 || rating > 5) {
                Alerts.showError("Rating must be between 1 and 5.");
                return false;
            }
        } catch (NumberFormatException e) {
            Alerts.showError("Rating must be a valid number (1-5).");
            return false;
        }
        
        return true;
    }
}
