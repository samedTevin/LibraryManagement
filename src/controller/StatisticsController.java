package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import repository.BookRepository;
import repository.LoanRepository;
import repository.MemberRepository;
import util.SceneChanger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StatisticsController implements Initializable {

    private final SceneChanger sceneChanger = new SceneChanger();
    
    private final BookRepository bookRepository = new BookRepository();
    private final MemberRepository memberRepository = new MemberRepository();
    private final LoanRepository loanRepository = new LoanRepository();

    @FXML private Label totalBooksLabel;
    @FXML private Label totalMembersLabel;
    @FXML private Label activeLoansLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadStatistics();
    }

    private void loadStatistics() {
        int totalBooks = bookRepository.getAll().size();
        int totalMembers = memberRepository.getAll().size();
        int activeLoans = loanRepository.getActiveLoans().size();

        totalBooksLabel.setText(String.valueOf(totalBooks));
        totalMembersLabel.setText(String.valueOf(totalMembers));
        activeLoansLabel.setText(String.valueOf(activeLoans));
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/Menu.fxml", "Menu", event);
    }
}
