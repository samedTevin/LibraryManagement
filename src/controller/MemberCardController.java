package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import model.Member;

public class MemberCardController {

    @FXML private ImageView memberImage;
    @FXML private Label memberName;
    @FXML private Label memberEmail;
    @FXML private Label memberId;

    public void setData(Member member) {
        String path = member.getImagePath();
        try {
            if (path != null && !path.isEmpty()) {
                if (path.startsWith("/view/assets")) {
                    memberImage.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream(path)));
                } else {
                    java.io.File file = new java.io.File(path);
                    if (file.exists()) {
                        memberImage.setImage(new javafx.scene.image.Image(file.toURI().toString()));
                    }
                }
            } else {
                memberImage.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream("/view/assets/placeholder_member.png")));
            }
        } catch (Exception e) {}
        
        memberId.setText("#" + member.getId());
        memberName.setText(member.getFirstName() + " " + member.getLastName());
        memberEmail.setText(member.getEmail());
    }
}
