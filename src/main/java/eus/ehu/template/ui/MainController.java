package eus.ehu.template.ui;

import eus.ehu.template.domain.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

import static eus.ehu.template.ui.SteamIDController.getId;

public class MainController {

    @FXML
    private Button add_acounts_btn;



    @FXML
    private Button login_bttn;

    @FXML
    private Label li_confirm;

    private boolean isLoggedIn = false;

    public static Stage RegisterStage;
    public RegisterController registerController;
    public static Stage loginStage;
    public LogInController loginController;
    public static Stage howLongToBeatStage;
    public static HLTBController howLongToBeatController;
    public static Stage idStage;
    public SteamIDController steamIDController;

    private static User loggedUser=null;
    private Parent RegisterUI;
    private Parent loginUI;
    private Parent idUI;
    private Parent hltbUI;



    @FXML
    public void initialize() {

        FXMLLoader loginCon = new FXMLLoader(getClass().getResource("login.fxml"));
        FXMLLoader idCon = new FXMLLoader(getClass().getResource("steam_id.fxml"));
        FXMLLoader hltbCon = new FXMLLoader(getClass().getResource("howlongtobeat.fxml"));
        FXMLLoader registerCon = new FXMLLoader(getClass().getResource("register.fxml"));

        try {
            loginUI = loginCon.load();
            idUI = idCon.load();
            hltbUI = hltbCon.load();
            RegisterUI = registerCon.load();

        } catch (IOException e) {
           System.out.println("error at initialize");
        }

        loginController = loginCon.getController();
        steamIDController = idCon.getController();
        howLongToBeatController = hltbCon.getController();
        registerController = registerCon.getController();

        loginStage = new Stage();
        howLongToBeatStage = new Stage();
        idStage = new Stage();
        RegisterStage = new Stage();

        loginStage.setScene(new Scene(loginUI));
        howLongToBeatStage.setScene(new Scene(hltbUI));
        idStage.setScene(new Scene(idUI));
        RegisterStage.setScene(new Scene(RegisterUI));

        loginStage.hide();
        howLongToBeatStage.hide();
        idStage.hide();
        RegisterStage.hide();

        loginStage.setTitle("Login");
        RegisterStage.setTitle("Register");

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

    private void showLogin(ActionEvent ignoredEvent) {
        loginStage.show();
        MainUI.stage.hide();
        howLongToBeatStage.hide();
        idStage.hide();
        loginController.setMainController(this);
        registerController.setMainController(this);
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
    public void howlongtobeat(ActionEvent ignoredEvent) {
        // Check if the user is logged in before showing the HLTB stage, the id is in the SteamIDController
        if(getId() != null) {
            SteamIDController.startHowLongToBeat(true);
            howLongToBeatStage.show();
            howLongToBeatController.setMainController(this);
            howLongToBeatController.meteJogos();
            idStage.hide();
            MainUI.stage.hide();
            loginStage.hide();
        } else {
            idStage.show();
            howLongToBeatStage.hide();
            MainUI.stage.hide();
            loginStage.hide();
            li_confirm.setText("Please log in to access this feature.");
        }
    }

    @FXML
    void recommend_games(ActionEvent ignoredEvent) {
        // Implement the logic for the Recommend Games button
    }

    @FXML
    void add_acounts(ActionEvent ignoredEvent) {
        // Implement the logic for the Add Accounts button
    }

    @FXML
    void VinculateSteamID(ActionEvent event) {
        idStage.show();
        howLongToBeatStage.hide();
        MainUI.stage.hide();
        loginStage.hide();
    }

    public static void setLoggedUser(User user) {
        loggedUser = user;
    }
    public static User getLoggedUser() {
        return loggedUser;
    }
}