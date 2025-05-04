package eus.ehu.template.ui;

import eus.ehu.template.businessLogic.BlFacadeImplementation;
import eus.ehu.template.domain.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import javafx.animation.FadeTransition;

import java.util.ResourceBundle;

import static eus.ehu.template.ui.MainController.*;


public class LogInController {

    @FXML
    private Label AllRights;
    @FXML
    private Label NotAccount;
    @FXML
    private Label PasswordLabel;
    @FXML
    private Label SignInCont;
    @FXML
    private Label WelcomeBack;
    @FXML
    private Label LoginLabel;
    @FXML
    private PasswordField password;

    private MainController mainController;

    //bundle for i18n
    private ResourceBundle bundle;

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

    public void updateUITexts() {
        bundle = MainUI.getBundle();

        LoginLabel.setText(bundle.getString("login.loginM"));
        WelcomeBack.setText(bundle.getString("login.welcomeBack"));
        SignInCont.setText(bundle.getString("login.signInCont"));
        username.setPromptText(bundle.getString("login.username"));
        PasswordLabel.setText(bundle.getString("login.PasswordLabel"));
        login.setText(bundle.getString("login.loginM"));
        password.setPromptText(bundle.getString("login.password"));
        NotAccount.setText(bundle.getString("login.NotAccount"));
        signup.setText(bundle.getString("login.signup"));
        AllRights.setText(bundle.getString("general.allRightsReserved"));
    }

    @FXML
    public void initialize() {
        // Set initial focus on username field
        username.requestFocus();

        // Apply initial styling
        login.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: #ffffff; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(46,125,50,0.5), 10, 0, 0, 3);");
        signup.setStyle("-fx-background-color: #1976d2; -fx-text-fill: #ffffff; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(25,118,210,0.5), 10, 0, 0, 3);");
        back.setStyle("-fx-background-color: transparent; -fx-border-color: white; -fx-border-radius: 50%; -fx-padding: 5;");

        // Setup password field sync
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

        // Add enter key handlers
        username.setOnAction(event -> password.requestFocus());
        password.setOnAction(event -> login(new ActionEvent()));
        passwordTextField.setOnAction(event -> login(new ActionEvent()));
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
        String pass = password.isVisible() ? password.getText() : passwordTextField.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, bundle.getString("general.Alert1Title"), null, bundle.getString("general.Alert1"));
            return;
        }

        BlFacadeImplementation bl = BlFacadeImplementation.getInstance();
        User loggedInUser = bl.login(user, pass);

        if (loggedInUser != null) {
            showAlert(Alert.AlertType.INFORMATION, bundle.getString("general.Alert2Title"), null, bundle.getString("general.Alert2"));

            // Apply animation to login button
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), login);
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.7);
            fadeTransition.setCycleCount(3);
            fadeTransition.setAutoReverse(true);
            fadeTransition.play();

            // Notify MainController about login state
            mainController.setLoggedIn(true);
            mainController.setLoggedUser(loggedInUser);
            mainController.LogInSteamIDs();
            mainController.updateLoginStatus("Welcome back, " + user, "green");

            // Close the login window after a short delay
            fadeTransition.setOnFinished(e -> {
                loginStage.hide();
                MainUI.stage.show();
            });
        } else {
            showAlert(Alert.AlertType.ERROR, bundle.getString("general.Alert1Title"), null, bundle.getString("general.Alert3"));
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
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
        back.setScaleX(1.1);
        back.setScaleY(1.1
        );
    }

    public void handleMouseExit2(MouseEvent ignoredMouseEvent) {
        back.setCursor(Cursor.DEFAULT);
        back.setScaleX(1.0);
        back.setScaleY(1.0);
    }

    public void togglePasswordVisibility(MouseEvent ignoredMouseEvent) {
        Visible(password, passwordTextField, hbox1, hbox2);
    }

    public void loginenter(MouseEvent ignoredMouseEvent) {
        login.setStyle("-fx-background-color: #1b5e20; -fx-text-fill: #ffffff; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(46,125,50,0.7), 12, 0, 0, 4);");
        login.setCursor(Cursor.HAND);
    }

    public void loginexit(MouseEvent ignoredMouseEvent) {
        login.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: #ffffff; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(46,125,50,0.5), 10, 0, 0, 3);");
        login.setCursor(Cursor.DEFAULT);
    }

    public void signupenter(MouseEvent ignoredMouseEvent) {
        signup.setCursor(Cursor.HAND);
        signup.setStyle("-fx-background-color: #1565c0; -fx-text-fill: #ffffff; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(25,118,210,0.7), 12, 0, 0, 4);");
    }

    public void signupexit(MouseEvent ignoredMouseEvent) {
        signup.setCursor(Cursor.DEFAULT);
        signup.setStyle("-fx-background-color: #1976d2; -fx-text-fill: #ffffff; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(25,118,210,0.5), 10, 0, 0, 3);");
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