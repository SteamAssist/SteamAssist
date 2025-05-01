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
import javafx.scene.image.ImageView;
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
    @FXML
    private Button login;
    @FXML
    private Button signup;
    @FXML
    private ImageView eye;
    @FXML
    private ImageView eye_closed;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        password.textProperty().addListener((observable, oldValue, newValue) -> {
            if (passwordTextField.isVisible()) {
                passwordTextField.setText(newValue);
            }
        });

        passwordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (password.isVisible()) {
                password.setText(newValue);
            }
        });
    }

   static void Visible(PasswordField password, TextField passwordTextField, HBox hbox1, HBox hbox2) {
        if (password.isVisible()) {
            passwordTextField.setText(password.getText());
            password.setVisible(false);
            passwordTextField.setVisible(true);
            hbox1.setVisible(true);
            hbox2.setVisible(false);
            passwordTextField.requestFocus();
            passwordTextField.positionCaret(passwordTextField.getText().length());

        } else {
            password.setText(passwordTextField.getText());
            passwordTextField.setVisible(false);
            password.setVisible(true);
            hbox1.setVisible(false);
            hbox2.setVisible(true);
            password.requestFocus();
            password.positionCaret(passwordTextField.getText().length());
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
            mainController.setLoggedUser(loggedInUser);
            mainController.LogInSteamIDs();
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

    public void handleMouseEnter2(MouseEvent ignoredMouseEvent) {
       back.setCursor(Cursor.HAND);
    }

    public void handleMouseExit2(MouseEvent ignoredMouseEvent) {
       back.setCursor(Cursor.DEFAULT);
    }

    public void togglePasswordVisibility(MouseEvent ignoredMouseEvent) {
        Visible(password, passwordTextField, hbox1, hbox2);
    }

    public void loginenter(MouseEvent ignoredMouseEvent) {
        login.setStyle("-fx-background-color: #1b5e20  ;");
        login.setCursor(Cursor.HAND);
    }
    public void loginexit(MouseEvent ignoredMouseEvent) {
        login.setStyle("-fx-background-color: #2e7d32 ;");
        login.setCursor(Cursor.DEFAULT);
    }
    public void signupenter(MouseEvent ignoredMouseEvent) {
        signup.setCursor(Cursor.HAND);
        signup.setStyle("-fx-background-color: #1565c0;-fx-text-fill:#ffffff ");
    }
    public void signupexit(MouseEvent ignoredMouseEvent) {
        signup.setCursor(Cursor.DEFAULT);
        signup.setStyle("-fx-background-color: #1976d2;-fx-text-fill:#ffffff ");
    }



    public void eyeenter(MouseEvent ignoredMouseEvent) {
        eye.setCursor(Cursor.HAND);
        eye_closed.setCursor(Cursor.HAND);

    }
    public void eyeexit(MouseEvent ignoredMouseEvent) {
        eye.setCursor(Cursor.DEFAULT);
        eye_closed.setCursor(Cursor.DEFAULT);
    }

}