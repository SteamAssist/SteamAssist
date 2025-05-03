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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;

import static eus.ehu.template.ui.MainController.*;

public class SteamIDController {


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
    @FXML
    public void initialize() {
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
            alert.setTitle(MainUI.getBundle().getString("steamid_invalid_id"));
            alert.setHeaderText(null);
            alert.setContentText(MainUI.getBundle().getString("steamid_invalid_format"));
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

    /**
     * Actualiza los textos de la interfaz con el idioma actual
     */
    public void actualizarTextos() {
        ResourceBundle bundle = MainUI.getBundle();

        // Actualizar textos de botones
        back.setText(bundle.getString("steamid_back_button"));
        confirm.setText(bundle.getString("steamid_continue_button"));

        // Actualizar textos de labels
        Label titleLabel = (Label) ((HBox) back.getParent()).getChildren().get(2);
        titleLabel.setText(bundle.getString("steamid_title"));

        Label dontKnowLabel = (Label) ((AnchorPane) back.getParent().getParent()).getChildren().stream()
                .filter(node -> node instanceof Label && ((Label) node).getText() != null &&
                        ((Label) node).getText().contains("Don't know what is"))
                .findFirst().orElse(null);
        if (dontKnowLabel != null) {
            dontKnowLabel.setText(bundle.getString("steamid_dont_know"));
        }

        Label clickToCopyLabel = (Label) ((AnchorPane) back.getParent().getParent()).getChildren().stream()
                .filter(node -> node instanceof Label && ((Label) node).getText() != null &&
                        ((Label) node).getText().contains("Click the number"))
                .findFirst().orElse(null);
        if (clickToCopyLabel != null) {
            clickToCopyLabel.setText(bundle.getString("steamid_click_to_copy"));
        }

        Label whereToGetLabel = (Label) ((AnchorPane) back.getParent().getParent()).getChildren().stream()
                .filter(node -> node instanceof Label && ((Label) node).getText() != null &&
                        ((Label) node).getText().contains("Don't know where to get"))
                .findFirst().orElse(null);
        if (whereToGetLabel != null) {
            whereToGetLabel.setText(bundle.getString("steamid_where_to_get"));
        }

        Label stillDontKnowLabel = (Label) ((AnchorPane) back.getParent().getParent()).getChildren().stream()
                .filter(node -> node instanceof Label && ((Label) node).getText() != null &&
                        ((Label) node).getText().contains("Still don't know"))
                .findFirst().orElse(null);
        if (stillDontKnowLabel != null) {
            stillDontKnowLabel.setText(bundle.getString("steamid_still_dont_know"));
        }

        clickableLabel2.setText(bundle.getString("steamid_click_more_info"));

        Label insertIdLabel = (Label) ((AnchorPane) back.getParent().getParent()).getChildren().stream()
                .filter(node -> node instanceof Label && ((Label) node).getText() != null &&
                        ((Label) node).getText().contains("Please insert"))
                .findFirst().orElse(null);
        if (insertIdLabel != null) {
            insertIdLabel.setText(bundle.getString("steamid_insert_id"));
        }
    }
}
