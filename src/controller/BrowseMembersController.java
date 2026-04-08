package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Member;
import repository.MemberRepository;
import util.SceneChanger;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;

public class BrowseMembersController implements Initializable {

    private final SceneChanger sceneChanger = new SceneChanger();
    private final MemberRepository memberRepository = new MemberRepository();

    @FXML private TextField searchField;
    @FXML private Label resultsCountLabel;
    @FXML private FlowPane membersFlowPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadMembers(memberRepository.getAll());
    }

    private void loadMembers(List<Member> members) {
        resultsCountLabel.setText("Found " + members.size() + " members");
        membersFlowPane.getChildren().clear();
        for (Member member : members) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/MemberCard.fxml"));
                VBox card = loader.load();
                MemberCardController controller = loader.getController();
                controller.setData(member);
                membersFlowPane.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleSearch(ActionEvent event) {
        String query = searchField.getText();
        if (query == null || query.trim().isEmpty()) {
            loadMembers(memberRepository.getAll());
            return;
        }
        List<Member> list = memberRepository.search(query);
        loadMembers(list);
    }

    @FXML
    void handleClearSearch(ActionEvent event) {
        searchField.clear();
        loadMembers(memberRepository.getAll());
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        sceneChanger.changeScene("/view/fxml/Menu.fxml", "Menu", event);
    }
}
