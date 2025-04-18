package eus.ehu.template.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private Button add_acounts_btn;



    @FXML
    private Button login_bttn;

    @FXML
    private Label li_confirm;

    public String id;

    private boolean isLoggedIn = false;

    public static Stage addAccountsStage;
    public AddAccounts addAccountsController;
    public static Stage loginStage;
    public LogIn_SignIn_Controller loginController;
    public static Stage howLongToBeatStage;
    public static HLTBController howLongToBeatController;
    public static Stage idStage;
    public SteamIDController steamIDController;


    private Parent loginUI;
    private Parent idUI;
    private Parent hltbUI;
    private Parent addAccountsUI;



    @FXML
    public void initialize() {

        FXMLLoader loginCon = new FXMLLoader(getClass().getResource("login.fxml"));
        FXMLLoader idCon = new FXMLLoader(getClass().getResource("steam_id.fxml"));
        FXMLLoader hltbCon = new FXMLLoader(getClass().getResource("howlongtobeat.fxml"));
        FXMLLoader addCon = new FXMLLoader(getClass().getResource("add_accounts.fxml"));

        try {
            loginUI = loginCon.load();
            idUI = idCon.load();
            hltbUI = hltbCon.load();
            addAccountsUI = addCon.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        loginController = loginCon.getController();
        steamIDController = idCon.getController();
        howLongToBeatController = hltbCon.getController();
        addAccountsController = addCon.getController();

        loginStage = new Stage();
        howLongToBeatStage = new Stage();
        idStage = new Stage();
        addAccountsStage = new Stage();


        loginStage.setScene(new Scene(loginUI));
        howLongToBeatStage.setScene(new Scene(hltbUI));
        idStage.setScene(new Scene(idUI));
        addAccountsStage.setScene(new Scene(addAccountsUI));

        loginStage.hide();
        howLongToBeatStage.hide();
        idStage.hide();
        addAccountsStage.hide();


        updateUI();

    }



    public void updateLoginStatus(String message, String color) {
        li_confirm.setText(message);
        li_confirm.setStyle("-fx-text-fill: " + color + ";");
    }

    @FXML
    void handleLoginButton(ActionEvent event) {
        if (isLoggedIn) {
            // Handle logout
            setLoggedIn(false);
            li_confirm.setText("");
        } else {
            showLogin(event);
        }
    }

    private void showLogin(ActionEvent event) {
        loginStage.show();
        MainUI.stage.hide();
        howLongToBeatStage.hide();
        idStage.hide();
        loginController.setMainController(this);
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
        updateUI();
    }

    private void updateUI() {
        add_acounts_btn.setVisible(isLoggedIn);
        login_bttn.setText(isLoggedIn ? "Log out" : "Log in/Sign up");
    }

    @FXML
    public void howlongtobeat(ActionEvent event) {
        if(isLoggedIn) {
            //Will get into steam and get the ID
        }
        idStage.show();
        howLongToBeatStage.hide();
        MainUI.stage.hide();
        loginStage.hide();

    }

    @FXML
    void recommend_games(ActionEvent event) {
        // Implement the logic for the Recommend Games button
    }

    @FXML
    void add_acounts(ActionEvent event) {
        MainUI.stage.hide();
        addAccountsStage.show();

    }
}