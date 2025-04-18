package eus.ehu.template.ui;

import eus.ehu.template.businessLogic.BlFacadeImplementation;
import eus.ehu.template.domain.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import static eus.ehu.template.ui.MainController.*;


public class LogInController {
    @FXML
    private PasswordField password;

    private MainController mainController;

    @FXML
    private TextField username;

    @FXML
    private TextField passwordTextField;
    @FXML
    private Button back;
    @FXML
    private HBox hbox1;
    @FXML
    private HBox hbox2;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {

    }

    @FXML
    public void togglePasswordVisibility(ActionEvent ignoredEvent) {
        Visible(password, passwordTextField, hbox1, hbox2);
    }

    static void Visible(PasswordField password, TextField passwordTextField, HBox hbox1, HBox hbox2) {
        if (password.isVisible()) {
            passwordTextField.setText(password.getText());
            password.setVisible(false);
            passwordTextField.setVisible(true);
            hbox1.setVisible(true);
            hbox2.setVisible(false);

        } else {
            password.setText(passwordTextField.getText());
            passwordTextField.setVisible(false);
            password.setVisible(true);
            hbox1.setVisible(false);
            hbox2.setVisible(true);
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

    public void handleMouseEnter2(MouseEvent mouseEvent) {
        back.setScaleX(1.1);
        back.setScaleY(1.1);
        back.setStyle("-fx-background-color: #60A5FA; -fx-text-fill: #FFFFFF;");
        back.setCursor(Cursor.HAND);
    }

    public void handleMouseExit2(MouseEvent mouseEvent) {
        back.setScaleX(1.0);
        back.setScaleY(1.0);
        back.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: #FFFFFF;");
        back.setCursor(Cursor.DEFAULT);
    }

    public void togglePasswordVisibilityphoto(MouseEvent mouseEvent) {
        Visible(password, passwordTextField, hbox1, hbox2);

    }
}