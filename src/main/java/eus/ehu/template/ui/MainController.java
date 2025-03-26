package eus.ehu.template.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

    private boolean isLoggedIn = false;

    @FXML
    public void initialize() {
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
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            Scene loginScene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(loginScene);
            stage.setTitle("SteamAssist-Login");
            stage.show();

            // Pass the main controller to the login controller
            LogIn_SignIn_Controller loginController = fxmlLoader.getController();
            loginController.setMainController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    void howlongtobeat(ActionEvent event) {
        // Implement the logic for the HowLongToBeat button
    }

    @FXML
    void recommend_games(ActionEvent event) {
        // Implement the logic for the Recommend Games button
    }

    @FXML
    void add_acounts(ActionEvent event) {
        // Implement the logic for the Add Accounts button
    }
}