package eus.ehu.template.ui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import static eus.ehu.template.ui.MainController.*;

public class SteamIDController {


        private static String identifier;

        @FXML
        private TextField id;

        @FXML
        private Label clickableLabel;
        @FXML
        private Label clickableLabel2;


    @FXML
    public void handleLabelClick(MouseEvent ignoredEvent) {
        String url = clickableLabel.getText();
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleLabelClick2(MouseEvent ignoredEvent) {
        String url = clickableLabel.getText();
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleMouseEnter(MouseEvent ignoredEvent) {
        clickableLabel.setStyle("-fx-text-fill: blue; -fx-underline: true;");
        clickableLabel.setCursor(Cursor.HAND);
        clickableLabel2.setStyle("-fx-text-fill: red; ");
        clickableLabel2.setCursor(Cursor.HAND);
    }

    @FXML
    public void handleMouseExit(MouseEvent ignoredEvent) {
        clickableLabel.setStyle("-fx-text-fill: black; -fx-underline: false;");
        clickableLabel.setCursor(Cursor.DEFAULT);
        clickableLabel2.setStyle("-fx-text-fill: black; -fx-underline: false;");
        clickableLabel2.setCursor(Cursor.DEFAULT);

    }
        @FXML
        void back(ActionEvent ignoredEvent) {
            idStage.hide();
            MainUI.stage.show();
        }

    @FXML
    void confirm(ActionEvent ignoredEvent) {
        String steamId = id.getText();
        if (steamId.length() == 17 && steamId.startsWith("76561198") && steamId.matches("\\d+")) {
            identifier = steamId;
            login();
            howLongToBeatController.meteJogos();
            id.clear();
        } else {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Invalid Steam ID");
            alert.setHeaderText(null);
            alert.setContentText("The Steam ID must be a 17-digit number starting with 76561198.");
            alert.showAndWait();
        }
    }

        public static String getId() {
            return identifier;
        }

    public void login(){
            idStage.hide();
            howLongToBeatStage.show();
    }
}
