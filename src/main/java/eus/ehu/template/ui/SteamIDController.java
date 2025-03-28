package eus.ehu.template.ui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;


import static eus.ehu.template.ui.MainController.*;

public class SteamIDController {


        private static String identifier;

        @FXML
        private TextField id;
        @FXML
        private Button confirm;
        @FXML
        private Button back;


    @FXML
        private Label clickableLabel;
        @FXML
        private Label clickableLabel2;


        @FXML
        private ImageView image03;

        @FXML
        private ImageView image04;

    @FXML
    public void initialize() {
        image03.setVisible(false);
        image04.setVisible(false);
        id.textProperty().addListener((observable, oldValue, newValue) -> updateImageView(newValue));
    }
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
    public void handleMouseEnter2(MouseEvent ignoredEvent) {
        confirm.setStyle("-fx-border-color: #ff0000; -fx-border-width: 2px; -fx-background-color: #007baa; -fx-text-fill: #ffffff;");
        confirm.setScaleX(1.1);
        confirm.setScaleY(1.1);
    }
    @FXML
    public void handleMouseExit2(MouseEvent ignoredEvent) {
        confirm.setStyle("-fx-border-color: transparent; -fx-border-width: 0px; -fx-background-color: #007bff; -fx-text-fill: #ffffff;");
        confirm.setScaleX(1.0);
        confirm.setScaleY(1.0);
    }
    @FXML
    public void handleMouseEnter3(MouseEvent ignoredEvent) {
        back.setStyle("-fx-border-color: #ff0000; -fx-border-width: 2px; -fx-background-color: #007baa; -fx-text-fill: #ffffff;");
        back.setScaleX(1.1);
        back.setScaleY(1.1);
    }
    @FXML
    public void handleMouseExit3(MouseEvent ignoredEvent) {
        back.setStyle("-fx-border-color: transparent; -fx-border-width: 0px; -fx-background-color: #007bff; -fx-text-fill: #ffffff;");
        back.setScaleX(1.0);
        back.setScaleY(1.0);
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
            image03.setVisible(false);
            image04.setVisible(false);
        }

    @FXML
    void confirm(ActionEvent ignoredEvent) {
        String steamId = id.getText();
        if (isValidSteamId(steamId)) {
            image03.setVisible(false);
            image04.setVisible(false);
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
    private boolean isValidSteamId(String steamId) {
        return steamId.length() == 17 && steamId.startsWith("76561198") && steamId.matches("\\d+");
    }
    private void updateImageView(String steamId) {
        if (isValidSteamId(steamId)) {
            image03.setVisible(true);
            image04.setVisible(false);
        } else {
            image03.setVisible(false);
            image04.setVisible(true);
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
