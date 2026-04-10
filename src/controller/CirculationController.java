package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Loan;
import model.Member;
import model.Book;
import repository.LoanRepository;
import repository.MemberRepository;
import repository.BookRepository;
import util.Alerts;
import util.SceneChanger;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class CirculationController implements Initializable {

    private final SceneChanger sceneChanger = new SceneChanger();
    private final LoanRepository loanRepository = new LoanRepository();

    @FXML private TextField searchField;
    
    @FXML private TableView<Loan> loansTable;
    @FXML private TableColumn<Loan, Integer> colLoanId;
    @FXML private TableColumn<Loan, Integer> colMemberId;
    @FXML private TableColumn<Loan, String> colMemberName;
    @FXML private TableColumn<Loan, Integer> colBookId;
    @FXML private TableColumn<Loan, String> colBookTitle;
    @FXML private TableColumn<Loan, LocalDate> colBorrowDate;

    @FXML private TextField memberIdField;
    @FXML private TextField bookIdField;
    
    @FXML private ListView<String> memberRefList;
    @FXML private ListView<String> bookRefList;

    private final MemberRepository memberRepository = new MemberRepository();
    private final BookRepository bookRepository = new BookRepository();
    private ObservableList<Loan> masterData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colLoanId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMemberId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colMemberName.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colBookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colBorrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));

        loadLoans();
        loadReferenceLists();
        setupListListeners();
    }

    private void loadReferenceLists() {
        // Members
        List<Member> members = memberRepository.getAll();
        ObservableList<String> memberNames = FXCollections.observableArrayList();
        for (Member m : members) {
            memberNames.add(m.getId() + " - " + m.getFirstName() + " " + m.getLastName());
        }
        memberRefList.setItems(memberNames);

        // Books
        List<Book> books = bookRepository.getAll();
        bookRefList.getItems().clear();
        for (Book b : books) {
            bookRefList.getItems().add(b.getId() + " - " + b.getTitle() + " (Stock: " + b.getCount() + ")");
        }
    }

    private void setupListListeners() {
        memberRefList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selected = memberRefList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    memberIdField.setText(selected.split(" - ")[0]);
                }
            }
        });

        bookRefList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selected = bookRefList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    bookIdField.setText(selected.split(" - ")[0]);
                }
            }
        });
    }

    private void loadLoans() {
        List<Loan> activeLoans = loanRepository.getActiveLoans();
        masterData = FXCollections.observableArrayList(activeLoans);
        loansTable.setItems(masterData);
    }

    @FXML
    void handleSearch(ActionEvent event) {
        String query = searchField.getText();
        if (query == null || query.trim().isEmpty()) {
            loansTable.setItems(masterData);
            return;
        }
        
        ObservableList<Loan> filteredData = FXCollections.observableArrayList();
        String lowerCaseFilter = query.toLowerCase();
        
        for (Loan loan : masterData) {
            boolean matchesName = loan.getMemberName() != null && loan.getMemberName().toLowerCase().contains(lowerCaseFilter);
            boolean matchesTitle = loan.getBookTitle() != null && loan.getBookTitle().toLowerCase().contains(lowerCaseFilter);
            boolean matchesIds = String.valueOf(loan.getMemberId()).equals(lowerCaseFilter) || String.valueOf(loan.getBookId()).equals(lowerCaseFilter);
            
            if (matchesName || matchesTitle || matchesIds) {
                filteredData.add(loan);
            }
        }
        loansTable.setItems(filteredData);
    }

    @FXML
    void handleClearSearch(ActionEvent event) {
        searchField.clear();
        loansTable.setItems(masterData);
    }

    @FXML
    void handleBorrow(ActionEvent event) {
        String mIdStr = memberIdField.getText();
        String bIdStr = bookIdField.getText();

        if (mIdStr.trim().isEmpty() || bIdStr.trim().isEmpty()) {
            Alerts.showError("Member ID and Book ID cannot be empty.");
            return;
        }

        try {
            int mId = Integer.parseInt(mIdStr);
            int bId = Integer.parseInt(bIdStr);

            Member member = memberRepository.getById(mId);
            if (member == null) {
                Alerts.showError("Member not found. Please enter a valid Member ID.");
                return;
            }

            Book book = bookRepository.getById(bId);
            if (book == null) {
                Alerts.showError("Book not found.");
                return;
            }

            if (book.getCount() <= 0) {
                Alerts.showWarning("Out of Stock: " + book.getTitle());
                return;
            }

            boolean success = loanRepository.borrowBook(mId, bId);
            if (success) {
                Alerts.showInformation("Book borrowed successfully!");
                memberIdField.clear();
                bookIdField.clear();
                loadLoans();
                loadReferenceLists();
            } else {
                Alerts.showError("Failed to borrow book. Check if the Member ID is correct.");
            }
        } catch (NumberFormatException e) {
            Alerts.showError("IDs must be numeric.");
        }
    }

    @FXML
    void handleReturn(ActionEvent event) {
        Loan selectedLoan = loansTable.getSelectionModel().getSelectedItem();
        if (selectedLoan == null) {
            Alerts.showError("Please select a borrowed book from the table to return.");
            return;
        }

        if (Alerts.showConfirmation("Are you sure you want to process the return for this book?")) {
            boolean success = loanRepository.returnBook(selectedLoan.getId(), selectedLoan.getBookId());
            if (success) {
                Alerts.showInformation("Book returned successfully!");
                loadLoans();
                loadReferenceLists();
            } else {
                Alerts.showError("Failed to return the book.");
            }
        }
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/Menu.fxml", "Menu", event);
    }
}
