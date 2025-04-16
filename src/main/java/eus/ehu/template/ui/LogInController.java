package eus.ehu.template.ui;

import eus.ehu.template.businessLogic.BlFacadeImplementation;
import eus.ehu.template.domain.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import static eus.ehu.template.ui.MainController.*;


public class LogInController {
    @FXML
    private PasswordField password;

    private MainController mainController;

    @FXML
    private TextField username;

    @FXML
    private TextField passwordTextField;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {

    }

    @FXML
    public void togglePasswordVisibility(ActionEvent ignoredEvent) {
        Visible(password, passwordTextField);
    }

    static void Visible(PasswordField password, TextField passwordTextField) {
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
    void login(ActionEvent ignoredEvent) {
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Success");
            alert.showAndWait();

            // Notify MainController about login state
            mainController.setLoggedIn(true);
            mainController.updateLoginStatus("Welcome back, "+user, "green");

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
    void back(ActionEvent ignoredEvent) {
        loginStage.hide();
        MainUI.stage.show();
    }

    @FXML
    void signup(ActionEvent ignoredEvent) {
        loginStage.hide();
        RegisterStage.show();
    }

}