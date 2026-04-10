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
                    var stream = getClass().getResourceAsStream(path);
                    if (stream != null) {
                        memberImage.setImage(new javafx.scene.image.Image(stream));
                    } else {
                        var fb = getClass().getResourceAsStream("/view/assets/placeholder_member.png");
                        if (fb != null) memberImage.setImage(new javafx.scene.image.Image(fb));
                    }
                } else {
                    java.io.File file = new java.io.File(path);
                    if (file.exists()) {
                        memberImage.setImage(new javafx.scene.image.Image(file.toURI().toString()));
                    } else {
                        var stream = getClass().getResourceAsStream("/view/assets/placeholder_member.png");
                        if (stream != null) memberImage.setImage(new javafx.scene.image.Image(stream));
                    }
                }
            } else {
                var stream = getClass().getResourceAsStream("/view/assets/placeholder_member.png");
                if (stream != null) memberImage.setImage(new javafx.scene.image.Image(stream));
            }
        } catch (Exception e) {}
        
        memberId.setText("#" + member.getId());
        memberName.setText(member.getFirstName() + " " + member.getLastName());
        memberEmail.setText(member.getEmail());
    }
}
