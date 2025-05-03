package eus.ehu.template.ui;

import eus.ehu.template.businessLogic.BlFacadeImplementation;
import eus.ehu.template.domain.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ResourceBundle;

import static eus.ehu.template.ui.MainController.*;
import static eus.ehu.template.ui.MainUI.getBundle;

public class SteamAddAccountController {

    private MainController mainController;

        public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

        @FXML
        public Label AccountNameLabel;

        @FXML
        private TextField Accountnamefield;
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
        image04.setVisible(false);;
        id.textProperty().addListener((observable, oldValue, newValue) -> updateImageView(newValue));
        actualizarTextos();
    }

    /**
     * Actualiza los textos de la interfaz según el idioma seleccionado
     */
    public void actualizarTextos() {
        ResourceBundle bundle = getBundle();
        confirm.setText(bundle.getString("steamid_continue_button"));
        back.setText(bundle.getString("steamid_back_button"));
        clickableLabel2.setText(bundle.getString("steamid_click_more_info"));
        AccountNameLabel.setText(bundle.getString("steamid_account_name"));
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
            AddAccountStage.hide();
            MainUI.stage.show();
            image03.setVisible(false);
            image04.setVisible(false);
        }

    @FXML
    void confirm(ActionEvent ignoredEvent) {
        String steamId = id.getText();
        String Accountname = Accountnamefield.getText();
        if (isValidSteamId(steamId)){
                EnterSteamIDinDatabase(steamId, Accountname);
                SteamIDController.identifier = steamId;
        }else{
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Invalid Steam ID");
            alert.setHeaderText(null);
            alert.setContentText("The Steam ID must be a 17-digit number starting with 76561198.");
            alert.showAndWait();
        }
    }
    private boolean isValidSteamId(String steamId) {
        return steamId.length() == 17 && (steamId.startsWith("76561198")||steamId.startsWith("76561199")) && steamId.matches("\\d+");
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


    private void EnterSteamIDinDatabase(String steamId, String Accountname) {
        BlFacadeImplementation bl = BlFacadeImplementation.getInstance();
        User user = getLoggedUser();
        if(user!=null){
            if( bl.userExists(user.getUserName()) ){
                if(!(bl.exitSteamID(user.getUserName(),steamId))) {
                    image03.setVisible(false);
                    image04.setVisible(false);
                    mainController.setSteamID(steamId);
                    AddAccountStage.hide();
                    MainUI.stage.show();
                    bl.vinculateSteamID(user.getUserName(), steamId, Accountname);
                    //Update the ComboBox with the new Steam ID
                    mainController.ChangeSteamID();
                    mainController.setSteamIDSet(true);
                    id.clear();
                }else{
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                    alert.setTitle("Steam ID already linked");
                    alert.setHeaderText(null);
                    alert.setContentText("This Steam ID is already linked to your account.");
                    alert.showAndWait();
                }
            }else{
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("User not found");
                alert.setHeaderText(null);
                alert.setContentText("User not found in the database.");
                alert.showAndWait();
            }
        }else{
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("User not logged in");
            alert.setHeaderText(null);
            alert.setContentText("Please log in to vinculate your Steam ID.");
            alert.showAndWait();
        }
    }
}
