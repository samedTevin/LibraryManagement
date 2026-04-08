package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Book;
import repository.BookRepository;
import util.SceneChanger;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;

public class BrowseBooksController implements Initializable {

    private final SceneChanger sceneChanger = new SceneChanger();
    private final BookRepository bookRepository = new BookRepository();

    @FXML private TextField searchField;
    @FXML private Label resultsCountLabel;
    @FXML private FlowPane booksFlowPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadBooks(bookRepository.getAll());
    }

    private void loadBooks(List<Book> books) {
        resultsCountLabel.setText("Found " + books.size() + " books");
        booksFlowPane.getChildren().clear();
        for (Book book : books) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/Book.fxml"));
                VBox bookCard = loader.load();
                BookController controller = loader.getController();
                controller.setData(book);
                booksFlowPane.getChildren().add(bookCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleSearch(ActionEvent event) {
        String query = searchField.getText();
        if (query == null || query.trim().isEmpty()) {
            loadBooks(bookRepository.getAll());
            return;
        }
        List<Book> list = bookRepository.search(query);
        loadBooks(list);
    }

    @FXML
    void handleClearSearch(ActionEvent event) {
        searchField.clear();
        loadBooks(bookRepository.getAll());
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/Menu.fxml", "Menu", event);
    }
}
