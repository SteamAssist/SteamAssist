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

public class SteamAddAccountController {

    @FXML
    private Label APIkey;
    @FXML
    private Label SteamID;
    @FXML
    private Label Accountname;
    @FXML
    private Label AddSteamAccout;

    private MainController mainController;

        public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

        @FXML
        private TextField Accountnamefield;
        @FXML
        private TextField id;
        @FXML
        private TextField apikey;
        @FXML
        private Button confirm;
        @FXML
        private Button back;

    //bundle for i18n
    private ResourceBundle bundle;


    @FXML
        private Label clickableLabel;
        @FXML
        private Label clickableLabel2;


        @FXML
        private ImageView image03;

        @FXML
        private ImageView image04;

    public void updateUITexts() {
        bundle = MainUI.getBundle();
        AddSteamAccout.setText(bundle.getString("addAccount.addSteamAccout"));
        clickableLabel.setText(bundle.getString("addAccount.clickableLabel"));
        clickableLabel2.setText(bundle.getString("addAccount.clickableLabel2"));
        confirm.setText(bundle.getString("addAccount.confirm"));
        Accountname.setText(bundle.getString("addAccount.Accountname"));
        SteamID.setText(bundle.getString("addAccount.SteamID"));
        APIkey.setText(bundle.getString("addAccount.APIkey"));
    }

    @FXML
    public void initialize() {
        //Language setup
        bundle = MainUI.getBundle();
        image03.setVisible(false);
        image04.setVisible(false);
        id.textProperty().addListener((observable, oldValue, newValue) -> updateImageView(newValue));
    }
      @FXML
    public void handleLabelClick(MouseEvent ignoredEvent) {
        String url = "https://steamid.io/"; // Provide a default URL for finding Steam ID
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLabelClick2(MouseEvent ignoredEvent) {
        String url = "https://help.steampowered.com/en/faqs/view/2816-BE67-5B69-0FEC"; // URL for more Steam ID information
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
    }
    @FXML
    public void handleMouseEnter4(MouseEvent ignoredEvent) {
        clickableLabel2.setStyle("-fx-text-fill: blue; -fx-underline: true;");
        clickableLabel2.setCursor(Cursor.HAND);
    }
    @FXML
    public void handleMouseEnter2(MouseEvent ignoredEvent) {
        confirm.setCursor(Cursor.HAND);
        confirm.setScaleX(1.1);
        confirm.setScaleY(1.1);
    }
    @FXML
    public void handleMouseExit2(MouseEvent ignoredEvent) {
        confirm.setCursor(Cursor.DEFAULT);
        confirm.setScaleX(1.0);
        confirm.setScaleY(1.0);
    }
    @FXML
    public void handleMouseEnter3(MouseEvent ignoredEvent) {
        back.setCursor(Cursor.HAND);
        back.setScaleX(1.1);
        back.setScaleY(1.1);
    }
    @FXML
    public void handleMouseExit3(MouseEvent ignoredEvent) {
        back.setCursor(Cursor.DEFAULT);
        back.setScaleX(1.0);
        back.setScaleY(1.0);
    }

    @FXML
    public void handleMouseExit(MouseEvent ignoredEvent) {
        clickableLabel.setStyle("-fx-text-fill: #66c0f4; -fx-underline: true;");
        clickableLabel.setCursor(Cursor.DEFAULT);
    }
    @FXML
    public void handleMouseExit4(MouseEvent ignoredEvent) {
        clickableLabel2.setStyle("-fx-text-fill: #66c0f4; -fx-underline: true;");
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
        String apikeyText = apikey.getText();
        if (isValidSteamId(steamId)){
                EnterSteamIDinDatabase(steamId, Accountname, apikeyText);
                SteamIDController.identifier = steamId;
        }else{
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("general.Alert1Title"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("general.Alert8"));
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


    private void EnterSteamIDinDatabase(String steamId, String Accountname, String apikeyText) {
        BlFacadeImplementation bl = BlFacadeImplementation.getInstance();
        User user = mainController.getLoggedUser();
        if(user!=null){
            if( bl.userExists(user.getUserName()) ){
                if(!(bl.exitSteamID(user.getUserName(),steamId))) {
                    image03.setVisible(false);
                    image04.setVisible(false);
                    mainController.setSteamID(steamId);
                    if(apikeyText!=null && !apikeyText.equals("")){
                        mainController.setSteamAPIkey(apikeyText);
                    }
                    AddAccountStage.hide();
                    MainUI.stage.show();
                    bl.vinculateSteamID(user.getUserName(), steamId, Accountname, apikeyText);
                    //Update the ComboBox with the new Steam ID
                    mainController.ChangeSteamID();
                    mainController.setSteamIDSet(true);
                    id.clear();
                }else{
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                    alert.setTitle(bundle.getString("general.Alert1Title"));
                    alert.setHeaderText(null);
                    alert.setContentText(bundle.getString("general.Alert9"));
                    alert.showAndWait();
                }
            }else{
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle(bundle.getString("general.Alert1Title") );
                alert.setHeaderText(null);
                alert.setContentText(bundle.getString("general.Alert10"));
                alert.showAndWait();
            }
        }else{
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("general.Alert1Title"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("general.Alert11"));
            alert.showAndWait();
        }
    }

}
