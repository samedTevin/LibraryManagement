package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.FlowPane;
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
import java.util.Comparator;
import repository.BookRepository;


public class MenuController implements Initializable {



    @FXML
    private HBox cardLayout;
    @FXML
    private FlowPane bookContainer;
    @FXML
    private Label profileName;
    @FXML
    private ImageView profileImage;
    @FXML
    private TextField searchField;
    @FXML
    private Label recentlyAddedLabel;
    @FXML
    private ScrollPane recentlyAddedPane;
    @FXML
    private Separator sectionSeparator;
    @FXML
    private Label recommendedLabel;
    @FXML
    private ScrollPane recommendedScrollPane;

    private List<Book> allBooks;

    private final SceneChanger sceneChanger = new  SceneChanger();
    private final BookRepository bookRepository = new BookRepository();

    private List<Book> recentlyAdded;
    private List<Book> recommendedBooks;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        profileName.setText(Session.getUser().getFirstName() + " " + Session.getUser().getLastName());
        if(Session.getUser().getImagePath() != null && !Session.getUser().getImagePath().isEmpty()){
            try {
                Image image = new Image(new File(Session.getUser().getImagePath()).toURI().toString());
                profileImage.setImage(image);
            } catch (Exception e) {
                profileImage.setImage(new Image(getClass().getResourceAsStream("/view/assets/placeholder_member.png")));
            }
        } else {
            profileImage.setImage(new Image(getClass().getResourceAsStream("/view/assets/placeholder_member.png")));
        }

        // 1. Fetch ALL books once
        allBooks = bookRepository.getAll();
        
        // Add live search listener
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            performSearch(newValue);
        });
        
        loadDefaultView();
    }

    private void loadDefaultView() {
        // Toggle UI
        recentlyAddedLabel.setVisible(true);
        recentlyAddedLabel.setManaged(true);
        recentlyAddedPane.setVisible(true);
        recentlyAddedPane.setManaged(true);
        sectionSeparator.setVisible(true);
        sectionSeparator.setManaged(true);
        recommendedLabel.setText("Recommended for you");

        // 2. Compute "Recently Added" -> highest IDs first (newest addition), capped at 10 items
        List<Book> recentSorted = new ArrayList<>(allBooks);
        recentSorted.sort((b1, b2) -> Integer.compare(b2.getId(), b1.getId()));
        recentlyAdded = new ArrayList<>();
        for (int i = 0; i < Math.min(10, recentSorted.size()); i++) {
            recentlyAdded.add(recentSorted.get(i));
        }

        // 3. Compute "Recommended" -> highest rating first (5 to 1), full catalog
        List<Book> ratingSorted = new ArrayList<>(allBooks);
        ratingSorted.sort((b1, b2) -> {
            int ratingCompare = Integer.compare(b2.getRating(), b1.getRating());
            if (ratingCompare == 0) {
                return b1.getTitle().compareToIgnoreCase(b2.getTitle());
            }
            return ratingCompare;
        });
        recommendedBooks = new ArrayList<>(ratingSorted);

        renderCards(recentlyAdded);
        renderGrid(recommendedBooks);
    }

    private void renderCards(List<Book> books) {
        cardLayout.getChildren().clear();
        try {
            for (Book value : books) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/fxml/Card.fxml"));
                HBox cardBox = fxmlLoader.load();
                CardController cardController = fxmlLoader.getController();
                cardController.setData(value);
                cardLayout.getChildren().add(cardBox);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void renderGrid(List<Book> books) {
        bookContainer.getChildren().clear();
        try {
            for (Book book : books) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/fxml/Book.fxml"));
                VBox bookBox = fxmlLoader.load();
                BookController bookController = fxmlLoader.getController();
                bookController.setData(book);

                bookContainer.getChildren().add(bookBox);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onSearch(ActionEvent event) {
        performSearch(searchField.getText());
    }

    @FXML
    void onSearchIconClicked() {
        performSearch(searchField.getText());
    }

    private void performSearch(String query) {
        if (query == null || query.trim().isEmpty()) {
            loadDefaultView();
            return;
        }

        // Search mode - hide recently added and section separator
        recentlyAddedLabel.setVisible(false);
        recentlyAddedLabel.setManaged(false);
        recentlyAddedPane.setVisible(false);
        recentlyAddedPane.setManaged(false);
        sectionSeparator.setVisible(false);
        sectionSeparator.setManaged(false);
        
        recommendedLabel.setText("Search Results for: " + query);

        String filter = query.toLowerCase();
        List<Book> filtered = new ArrayList<>();
        for (Book b : allBooks) {
            if (b.getTitle().toLowerCase().contains(filter) || b.getAuthor().toLowerCase().contains(filter)) {
                filtered.add(b);
            }
        }

        renderGrid(filtered);
    }

    // Dummy methods eliminated in favor of Dynamic Repositories

    @FXML
    void goToBookManagement(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/Books.fxml", "Book Management", event);
    }

    @FXML
    void goToBooks(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/BrowseBooks.fxml", "Books", event);
    }

    @FXML
    void goToCirculation(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/Circulation.fxml", "Circulation", event);
    }

    @FXML
    void goToMemberManagement(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/Members.fxml", "Member Management", event);
    }

    @FXML
    void goToMembers(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/BrowseMembers.fxml", "Members", event);
    }

    @FXML
    void goToProfile(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/Profile.fxml","Profile",event);
    }

    @FXML
    void goToStatistics(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/Statistics.fxml", "Statistics Dashboard", event);
    }

    @FXML
    void logOut(ActionEvent event) throws IOException {

        if(Alerts.showConfirmation("Are you sure to logout ?")){
            sceneChanger.changeScene("/view/fxml/LandingPage.fxml","Landing Page",event);
        }
    }


}
