package eus.ehu.template.ui;
import eus.ehu.template.businessLogic.BlFacadeImplementation;
import eus.ehu.template.domain.User;
import eus.ehu.template.domain.AccountSteamID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;


import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;

import static eus.ehu.template.ui.MainController.*;

public class SteamIDController {

    @FXML
    private Label example4;
    @FXML
    private Label insertText;
    @FXML
    private Label example3;
    @FXML
    private Label example2;
    @FXML
    private Label example;
    @FXML
    private Label titleLabel;


        public static String identifier;
    private MainController mainController;

        public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

        @FXML
        private TextField id;
        @FXML
        private Button confirm;
        @FXML
        private Button back;

    //bundle for i18n
    private ResourceBundle bundle;


        @FXML
        private Hyperlink clickableLabel;
        @FXML
        private Label clickableLabel2;
        @FXML
         private Label copy;
        @FXML
        private ImageView image03;

        @FXML
        private ImageView image04;

    public static class CopyableLabel {

        public static void makeLabelCopyable(Label label) {
            // Create a ContextMenu for right-click (optional)
            ContextMenu contextMenu = new ContextMenu();
            MenuItem copyItem = new MenuItem("Copiar");
            copyItem.setOnAction(event -> copyToClipboard(label.getText()));
            contextMenu.getItems().add(copyItem);
            label.setContextMenu(contextMenu);

            // Add left-click event for copying
            label.setOnMouseClicked(event -> {
                if (event.getButton().name().equals("PRIMARY")) { // Left-click
                    copyToClipboard(label.getText());
                }
            });
        }

        private static void copyToClipboard(String text) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(text);
            clipboard.setContent(content);
        }
    }

    public void updateUITexts() {
        bundle = MainUI.getBundle();
        titleLabel.setText(bundle.getString("steamid.title"));
        example.setText(bundle.getString("steamid.example"));
        example2.setText(bundle.getString("steamid.example2"));
        example3.setText(bundle.getString("steamid.example3"));
        clickableLabel2.setText(bundle.getString("steamid.clickableLabel2"));
        insertText.setText(bundle.getString("steamid.insertText"));
        confirm.setText(bundle.getString("steamid.confirm"));
        example4.setText(bundle.getString("steamid.example4"));
    }

    @FXML
    public void initialize() {
        //Language setup
        bundle = MainUI.getBundle();
        image03.setVisible(false);
        image04.setVisible(false);
        id.textProperty().addListener((observable, oldValue, newValue) -> updateImageView(newValue));
        CopyableLabel.makeLabelCopyable(copy);
    }
    @FXML
    public void handleLabelClick(MouseEvent ignoredEvent) {
        try {
            String url = clickableLabel.getText();
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e ) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleMouseEnter(MouseEvent ignoredEvent) {
        clickableLabel2.setCursor(Cursor.HAND);
    }
    @FXML
    public void handleMouseExit(MouseEvent ignoredEvent) {
        clickableLabel2.setCursor(Cursor.DEFAULT);
    }
    @FXML
    public void handleMouseEnter2(MouseEvent ignoredEvent) {
         back.setCursor(Cursor.HAND);
    }
    @FXML
    public void handleMouseExit2(MouseEvent ignoredEvent) {

        back.setCursor(Cursor.DEFAULT);
    }
    @FXML
    public void handleMouseEnter3(MouseEvent ignoredEvent) {
       confirm.setStyle("-fx-background-color: #60A5FA; -fx-text-fill: #FFFFFF;");
        confirm.setCursor(Cursor.HAND);
    }
    @FXML
    public void handleMouseExit3(MouseEvent ignoredEvent) {
        confirm.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: #FFFFFF;");
        confirm.setCursor(Cursor.DEFAULT);
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
        if (isValidSteamId(steamId)){
                image03.setVisible(false);
                image04.setVisible(false);
                identifier = steamId;
                login();
            howLongToBeatController.cargaLaVaina();
        } else {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("general.Alert1Title"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("general.Alert14"));
            alert.showAndWait();
        }
    }
    private boolean isValidSteamId(String steamId) {
        return steamId.length() == 17 && steamId.matches("\\d+");
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

    public static String getIdNotLogged() {
            return identifier;
    }

    public void login(){
            idStage.hide();
            howLongToBeatStage.show();
    }

    public static void setIdentifier(String identifier) {
        SteamIDController.identifier = identifier;
    }
}
