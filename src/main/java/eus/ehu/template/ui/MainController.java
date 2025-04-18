package eus.ehu.template.ui;

import eus.ehu.template.businessLogic.BlFacade;
import eus.ehu.template.businessLogic.BlFacadeImplementation;
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
import java.util.ArrayList;

public class MainController {

    @FXML
    private Button add_acounts_btn;

    @FXML
    private Button login_bttn;

    @FXML
    private Label li_confirm;

    private boolean SteamIDSet = false;
    private String SteamID;
    private boolean isLoggedIn = false;
    private ArrayList<String> AccountsNames = new ArrayList<>();
    private static int steamIDIndex = 0;

    public static Stage RegisterStage;
    public RegisterController registerController;
    public static Stage loginStage;
    public LogInController loginController;
    public static Stage howLongToBeatStage;
    public static HLTBController howLongToBeatController;
    public static Stage idStage;
    public SteamIDController steamIDController;
    public static Stage AddAccountStage;
    public SteamAddAccountController steamAddAccountController;

    private static User loggedUser=null;
    private Parent RegisterUI;
    private Parent loginUI;
    private Parent idUI;
    private Parent hltbUI;
    private Parent steamAddUI;

    @FXML
    private Label IDLabel;


    @FXML
    public void initialize() {

        FXMLLoader loginCon = new FXMLLoader(getClass().getResource("login.fxml"));
        FXMLLoader idCon = new FXMLLoader(getClass().getResource("steam_id.fxml"));
        FXMLLoader hltbCon = new FXMLLoader(getClass().getResource("howlongtobeat.fxml"));
        FXMLLoader registerCon = new FXMLLoader(getClass().getResource("register.fxml"));
        FXMLLoader steamAddCon = new FXMLLoader(getClass().getResource("steam_addAccount.fxml"));

        try {
            loginUI = loginCon.load();
            idUI = idCon.load();
            hltbUI = hltbCon.load();
            RegisterUI = registerCon.load();
            steamAddUI = steamAddCon.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        loginController = loginCon.getController();
        steamIDController = idCon.getController();
        howLongToBeatController = hltbCon.getController();
        registerController = registerCon.getController();
        steamAddAccountController = steamAddCon.getController();

        loginStage = new Stage();
        howLongToBeatStage = new Stage();
        idStage = new Stage();
        RegisterStage = new Stage();
        AddAccountStage = new Stage();

        loginStage.setScene(new Scene(loginUI));
        howLongToBeatStage.setScene(new Scene(hltbUI));
        idStage.setScene(new Scene(idUI));
        RegisterStage.setScene(new Scene(RegisterUI));
        AddAccountStage.setScene(new Scene(steamAddUI));

        loginStage.hide();
        howLongToBeatStage.hide();
        idStage.hide();
        RegisterStage.hide();
        AddAccountStage.hide();

        steamIDController.setMainController(this);
        steamAddAccountController.setMainController(this);
        howLongToBeatController.setMainController(this);

        loginStage.setTitle("Login");
        RegisterStage.setTitle("Register");
        ChangeSteamID();

        updateUI();
        this.setSteamIDSet(false);
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

    public String getSteamID() {
        return SteamID;
    }

    public void setSteamID(String steamID) {
        this.SteamID = steamID;
    }

    @FXML
    public void howlongtobeat(ActionEvent ignoredEvent) {
        //Reset ID if you are not logged in
        if(!isLoggedIn) {
            SteamIDController.setIdentifier(null);
        }
        // Check if the user is logged in before showing the HLTB stage, the id is in the SteamIDController
        if(SteamID != null) {
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
    void addAccounts(ActionEvent event) {
        AddAccountStage.show();
        AddAccountStage.setTitle("Add Steam Account");
        steamAddAccountController.setMainController(this);
        MainUI.stage.hide();
    }

    public static void setLoggedUser(User user) {
        loggedUser = user;
    }

    public static User getLoggedUser() {
        return loggedUser;
    }

    @FXML
    void Next(ActionEvent event) {
        if(steamIDIndex + 1 < AccountsNames.size()) {
            steamIDIndex++;
            ChangeSteamID();
        }
    }

    @FXML
    void Previous(ActionEvent event) {
        if(steamIDIndex > 0) {
            steamIDIndex--;
            ChangeSteamID();
        }
    }

    public void ChangeSteamID() {
        BlFacade bl = BlFacadeImplementation.getInstance();
        User user = getLoggedUser();
        if (user == null) {
            IDLabel.setText("No Steam IDs found.");
            return;
        }
        AccountsNames = bl.getAccountname(user.getUserName());
        if (AccountsNames != null && !AccountsNames.isEmpty()) {
            if (steamIDIndex < AccountsNames.size() && steamIDIndex >= 0) {
                String steamID = AccountsNames.get(steamIDIndex);
                IDLabel.setText(steamID);
                steamIDIndex++;
            } else {
                while(steamIDIndex >= AccountsNames.size() || steamIDIndex < 0) {
                    if(steamIDIndex >= AccountsNames.size()) {
                        steamIDIndex--;
                    } else {
                        steamIDIndex++;
                    }
                }
                ChangeSteamID();
            }
        } else {
            IDLabel.setText("No Steam IDs found.");
        }
    }
    public void setSteamIDSet(boolean steamIDSet) {
        SteamIDSet = steamIDSet;
    }
    public boolean isSteamIDSet() {return SteamIDSet;}

}