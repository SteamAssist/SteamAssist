package eus.ehu.template.ui;

import eus.ehu.template.businessLogic.BlFacadeImplementation;
import eus.ehu.template.domain.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import static eus.ehu.template.ui.MainController.*;


public class RegisterController {
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
        LogInController.Visible(password, passwordTextField);
    }



    @FXML
    void login(ActionEvent ignoredEvent) {
        // Change to log in window
        RegisterStage.hide();
        loginStage.show();
    }

    @FXML
    void back(ActionEvent ignoredEvent) {
        RegisterStage.hide();
        MainUI.stage.show();
    }

    @FXML
    void signup(ActionEvent ignoredEvent) {
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
                mainController.updateLoginStatus("Welcome, "+user, "green");
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
