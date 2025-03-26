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


public class LogIn_SignIn_Controller {
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
    private PasswordField newPasswordField2;

    @FXML
    private TextField newPasswordTextField2;
    @FXML
    private Button submitNewPasswordButton;
    @FXML
    private Button signup;
    @FXML
    private Button login;

    private static String steamUsername;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {

    }
    @FXML
    public void handleForgotPassword(ActionEvent event) {
        password.setPromptText("Enter your new password");
        passwordTextField.setPromptText("Enter your new password");
        newPasswordField2.setVisible(true);
        submitNewPasswordButton.setVisible(true);
        match.setVisible(true);
        signup.setVisible(false);
        login.setVisible(false);
    }

    @FXML
    public void handleSubmitNewPassword(ActionEvent event) {
        String newPassword1 = password.isVisible() ? password.getText() : passwordTextField.getText();
        String newPassword2 = newPasswordField2.isVisible() ? newPasswordField2.getText() : newPasswordTextField2.getText();

        if (newPassword1.equals(newPassword2)) {
            BlFacadeImplementation bl = BlFacadeImplementation.getInstance();
            boolean success = bl.updatePassword(username.getText(), newPassword1);

            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Password updated successfully");
                alert.showAndWait();

                // Hide the password fields and submit button
                match.setVisible(false);
                signup.setVisible(true);
                login.setVisible(true);
                password.setVisible(true);
                passwordTextField.setVisible(false);
                newPasswordField2.setVisible(false);
                newPasswordTextField2.setVisible(false);
                submitNewPasswordButton.setVisible(false);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to update password. Please try again.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Passwords do not match. Please try again.");
            alert.showAndWait();
        }
    }
    @FXML
    public void togglePasswordVisibility(ActionEvent event) {
        if (password.isVisible() && !passwordTextField.isVisible() && !newPasswordField2.isVisible() && !newPasswordTextField2.isVisible()) {
            passwordTextField.setText(password.getText());
            password.setVisible(false);
            passwordTextField.setVisible(true);
        } else if (!password.isVisible() && passwordTextField.isVisible() && !newPasswordField2.isVisible() && !newPasswordTextField2.isVisible()) {
            password.setText(passwordTextField.getText());
            passwordTextField.setVisible(false);
            password.setVisible(true);
        } else if (password.isVisible() && newPasswordField2.isVisible() && !passwordTextField.isVisible() && !newPasswordTextField2.isVisible()) {
            passwordTextField.setText(password.getText());
            password.setVisible(false);
            passwordTextField.setVisible(true);
        } else if (!password.isVisible() && !newPasswordField2.isVisible() && passwordTextField.isVisible() && newPasswordTextField2.isVisible()) {
            password.setText(passwordTextField.getText());
            passwordTextField.setVisible(false);
            newPasswordField2.setText(newPasswordTextField2.getText());
            newPasswordTextField2.setVisible(false);
            password.setVisible(true);
            newPasswordField2.setVisible(true);
        } else if (newPasswordField2.isVisible() && !newPasswordTextField2.isVisible()) {
            newPasswordTextField2.setText(newPasswordField2.getText());
            newPasswordField2.setVisible(false);
            newPasswordTextField2.setVisible(true);
        } else if (!newPasswordField2.isVisible() && newPasswordTextField2.isVisible()) {
            newPasswordField2.setText(newPasswordTextField2.getText());
            newPasswordTextField2.setVisible(false);
            newPasswordField2.setVisible(true);
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
        User loggedInUser = bl.login(user, pass);

        if (loggedInUser != null) {
            steamUsername = user;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Login successful");
            alert.showAndWait();

            // Notify MainController about login state
            mainController.setLoggedIn(true);
            mainController.updateLoginStatus("Login successful", "green");

            // Close the login window
            loginStage.hide();
            MainUI.stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid credentials. Please try again");
            alert.showAndWait();
        }
    }

    @FXML
    void back(ActionEvent event) {
        loginStage.hide();
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
                steamUsername = user;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Signup successful");
                alert.showAndWait();
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

    @FXML
    void logOut(ActionEvent event) {
        steamUsername = null;
    }
}