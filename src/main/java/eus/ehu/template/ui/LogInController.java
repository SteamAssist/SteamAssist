package eus.ehu.template.ui;

import eus.ehu.template.businessLogic.BlFacadeImplementation;
import eus.ehu.template.domain.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import javafx.animation.FadeTransition;

import java.util.ResourceBundle;

import static eus.ehu.template.ui.MainController.*;
import static eus.ehu.template.ui.MainUI.getBundle;

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

    @FXML
    private Label headerLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label noAccountLabel;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        // Set initial focus on username field
        username.requestFocus();

        // Apply initial styling
        login.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: #ffffff; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(46,125,50,0.5), 10, 0, 0, 3);");
        signup.setStyle("-fx-background-color: #1976d2; -fx-text-fill: #ffffff; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(25,118,210,0.5), 10, 0, 0, 3);");
        back.setStyle("-fx-background-color: transparent; -fx-border-color: white; -fx-border-radius: 50%; -fx-padding: 5;");
        actualizarTextos();
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
    }

    /**
     * Actualiza todos los textos de la interfaz según el idioma actual
     */
    public void actualizarTextos() {
        ResourceBundle bundle = getBundle();
        // Actualizar textos de la interfaz
        if (headerLabel != null) headerLabel.setText(bundle.getString("login_header"));
        if (login != null) login.setText(bundle.getString("login_button"));
        if (signup != null) signup.setText(bundle.getString("signup_button"));
        if (usernameLabel != null) usernameLabel.setText(bundle.getString("username_label"));
        if (passwordLabel != null) passwordLabel.setText(bundle.getString("password_label"));
        if (noAccountLabel != null) noAccountLabel.setText(bundle.getString("no_account_label"));

        // Actualizar prompts de los campos de texto
        if (username != null) username.setPromptText(bundle.getString("username_label").replace(":", ""));
        if (password != null) password.setPromptText(bundle.getString("password_label").replace(":", ""));
        if (passwordTextField != null) passwordTextField.setPromptText(bundle.getString("password_label").replace(":", ""));

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
        ResourceBundle bundle = getBundle();

        if (user.isEmpty() || pass.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", null, bundle.getString("all_fields_error"));
            return;
        }

        BlFacadeImplementation bl = BlFacadeImplementation.getInstance();
        User loggedInUser = bl.login(user, pass);

        if (loggedInUser != null) {
            showAlert(Alert.AlertType.INFORMATION, "Success", null, bundle.getString("login_success"));

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
            showAlert(Alert.AlertType.ERROR, "Error", null, bundle.getString("invalid_credentials_error"));
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