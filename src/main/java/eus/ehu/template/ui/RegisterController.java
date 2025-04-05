package eus.ehu.template.ui;

import eus.ehu.template.businessLogic.BlFacadeImplementation;
import eus.ehu.template.domain.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.awt.*;

import static eus.ehu.template.ui.MainController.*;


public class RegisterController {
    @FXML
    private PasswordField password;

    private MainController mainController;

    @FXML
    private TextField username;

    @FXML
    private Label clickableLabel;
    @FXML
    private Label match;
    @FXML
    private TextField passwordTextField;

    @FXML
    private Button signup;
    @FXML
    private Button login;


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {

    }

    @FXML
    public void togglePasswordVisibility(ActionEvent event) {
        if (password.isVisible()) {
            passwordTextField.setText(password.getText());
            password.setVisible(false);
            passwordTextField.setVisible(true);
        } else {
            password.setText(passwordTextField.getText());
            passwordTextField.setVisible(false);
            password.setVisible(true);
        }
    }

    @FXML
    public void handleLabelClick(MouseEvent event) {
        String url = clickableLabel.getText();
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMouseEnter(MouseEvent event) {
        clickableLabel.setStyle("-fx-text-fill: blue; -fx-underline: true;");
        clickableLabel.setCursor(Cursor.HAND);
    }

    @FXML
    public void handleMouseExit(MouseEvent event) {
        clickableLabel.setStyle("-fx-text-fill: black; -fx-underline: false;");
        clickableLabel.setCursor(Cursor.DEFAULT);
    }

    @FXML
    void login(ActionEvent event) {
        // Change to login window
        RegisterStage.hide();
        loginStage.show();
    }

    @FXML
    void back(ActionEvent event) {
        RegisterStage.hide();
        MainUI.stage.show();
    }

    @FXML
    void signup(ActionEvent event) {
        String user = username.getText();
        String pass = password.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("All fields are mandatory!");
            alert.showAndWait();
            return;
        }

        BlFacadeImplementation bl = BlFacadeImplementation.getInstance();

        if (!bl.userExists(user)) {
            bl.signUp(user, pass);

            User loggedInUser = bl.login(user, pass);

            if (loggedInUser != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Signup successful");
                alert.showAndWait();
                // Notify MainController about login state
                mainController.setLoggedIn(true);
                mainController.updateLoginStatus("Login successful", "green");
                // Close the register window
                RegisterStage.hide();
                MainUI.stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Signup failed. Please try again");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("User already exists");
            alert.showAndWait();
        }
    }

}
