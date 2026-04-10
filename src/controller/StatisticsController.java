package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import model.Book;
import repository.BookRepository;
import repository.LoanRepository;
import repository.MemberRepository;
import util.SceneChanger;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StatisticsController implements Initializable {

    private final SceneChanger sceneChanger = new SceneChanger();
    
    private final BookRepository bookRepository = new BookRepository();
    private final MemberRepository memberRepository = new MemberRepository();
    private final LoanRepository loanRepository = new LoanRepository();

    @FXML private Label totalBooksLabel;
    @FXML private Label totalMembersLabel;
    @FXML private Label activeLoansLabel;

    @FXML private PieChart inventoryPieChart;
    @FXML private BarChart<String, Integer> stockBarChart;
    @FXML private CategoryAxis xAxis;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadStatistics();
    }

    private void loadStatistics() {
        List<Book> allBooks = bookRepository.getAll();
        int totalBooks = allBooks.size();
        int totalMembers = memberRepository.getAll().size();
        int activeLoans = loanRepository.getActiveLoans().size();

        totalBooksLabel.setText(String.valueOf(totalBooks));
        totalMembersLabel.setText(String.valueOf(totalMembers));
        activeLoansLabel.setText(String.valueOf(activeLoans));

        // Populate Pie Chart (In Stock vs On Loan)
        int totalStock = 0;
        for (Book b : allBooks) totalStock += b.getCount();
        
        PieChart.Data inStockData = new PieChart.Data("In Stock (" + totalStock + ")", totalStock);
        PieChart.Data onLoanData = new PieChart.Data("On Loan (" + activeLoans + ")", activeLoans);
        inventoryPieChart.setData(FXCollections.observableArrayList(inStockData, onLoanData));

        // Populate Bar Chart (Top 10 Books by Stock)
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("Stock Level");
        
        allBooks.stream()
            .sorted((b1, b2) -> Integer.compare(b2.getCount(), b1.getCount()))
            .limit(10)
            .forEach(b -> series.getData().add(new XYChart.Data<>(b.getTitle(), b.getCount())));
            
        stockBarChart.getData().clear();
        stockBarChart.getData().add(series);
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/Menu.fxml", "Menu", event);
    }
}
