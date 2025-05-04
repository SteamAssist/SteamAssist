package eus.ehu.template.ui;

import eus.ehu.template.businessLogic.BlFacadeImplementation;
import eus.ehu.template.domain.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.util.ResourceBundle;

import static eus.ehu.template.ui.MainController.*;

public class RegisterController {

    @FXML
    private Label confirm;
    @FXML
    private Label AllRights;
    @FXML
    private Label AlreadyHA;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label UsernameLabel;
    @FXML
    private Label Userinfo;
    @FXML
    private Label YourTGC;
    @FXML
    private Label createAccount;

    private MainController mainController;
    @FXML
    private Button back;
    @FXML
    private TextField username;
    @FXML
    private HBox hbox1;
    @FXML
    private HBox hbox2;
    @FXML
    private PasswordField password;
    @FXML
    private TextField passwordTextField;
    @FXML
    private ImageView eye;
    @FXML
    private ImageView eye_closed;

    @FXML
    private HBox hbox3;
    @FXML
    private HBox hbox4;
    @FXML
    private PasswordField password1;
    @FXML
    private TextField passwordTextField1;
    @FXML
    private ImageView eye1;
    @FXML
    private ImageView eye_closed1;

    //bundle for i18n
    private ResourceBundle bundle;

    @FXML
    private Button back1;
    @FXML
    private Button signup;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void updateUITexts() {
        bundle = MainUI.getBundle();
        createAccount.setText(bundle.getString("signup.createAccount"));
        YourTGC.setText(bundle.getString("signup.YourTGC"));
        Userinfo.setText(bundle.getString("signup.Userinfo"));
        UsernameLabel.setText(bundle.getString("signup.UsernameLabel"));
        username.setPromptText(bundle.getString("signup.username"));
        passwordLabel.setText(bundle.getString("signup.passwordLabel"));
        passwordTextField.setPromptText(bundle.getString("signup.password"));
        password.setPromptText(bundle.getString("signup.password"));
        passwordTextField1.setPromptText(bundle.getString("signup.passwordConfirm"));
        password1.setPromptText(bundle.getString("signup.passwordConfirm"));
        signup.setText(bundle.getString("signup.signup"));
        AlreadyHA.setText(bundle.getString("signup.AlreadyHA"));
        back1.setText(bundle.getString("signup.LogInHere"));
        AllRights.setText(bundle.getString("general.allRightsReserved"));
        confirm.setText(bundle.getString("signup.confirm"));
        back.setText(bundle.getString("signup.home"));
    }

    @FXML
    public void initialize() {
        username.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                back1(new ActionEvent());
            }
        });
        password.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                back1(new ActionEvent());
            }
        });
        passwordTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                back1(new ActionEvent());
            }
        });
        password1.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                back1(new ActionEvent());
            }
        });
        passwordTextField1.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                back1(new ActionEvent());
            }
        });

        // Set initial styles
        configureAnimations();
        username.setOnAction(event -> signup(new ActionEvent()));
        password.setOnAction(event -> signup(new ActionEvent()));
        passwordTextField.setOnAction(event -> signup(new ActionEvent()));
        password1.setOnAction(event -> signup(new ActionEvent()));
        passwordTextField1.setOnAction(event -> signup(new ActionEvent()));
    }

    private void configureAnimations() {
        // Add initial hover effects to buttons
        back.setOnMouseEntered(e -> {
            back.setStyle("-fx-background-color: #495057; -fx-text-fill: white; -fx-background-radius: 5;");
            back.setCursor(Cursor.HAND);
        });

        back.setOnMouseExited(e -> {
            back.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-background-radius: 5;");
            back.setCursor(Cursor.DEFAULT);
        });

        // Configuration for text fields focus effect
        username.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                username.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #007bff; -fx-border-width: 2;");
            } else {
                username.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #ced4da; -fx-border-width: 1;");
            }
        });

        // Similar focus effects for password fields
        configureFocusEffect(password);
        configureFocusEffect(password1);
        configureFocusEffect(passwordTextField);
        configureFocusEffect(passwordTextField1);
    }

    private void configureFocusEffect(TextField field) {
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #007bff; -fx-border-width: 2;");
            } else {
                field.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #ced4da; -fx-border-width: 1;");
            }
        });
    }

    @FXML
    public void togglePasswordVisibility(MouseEvent ignoredEvent) {
        togglePasswordVisibility(password, passwordTextField, hbox1, hbox2, eye, eye_closed);
    }

    @FXML
    public void togglePasswordVisibility1(MouseEvent ignoredEvent) {
        togglePasswordVisibility(password1, passwordTextField1, hbox3, hbox4, eye1, eye_closed1);
    }

    private void togglePasswordVisibility(PasswordField password, TextField passwordTextField,
                                          HBox hbox1, HBox hbox2, ImageView eye, ImageView eye_closed) {
        if (password.isVisible()) {
            passwordTextField.setText(password.getText());
            password.setVisible(false);
            hbox2.setVisible(false);

            passwordTextField.setVisible(true);
            hbox1.setVisible(true);

            passwordTextField.requestFocus();
            passwordTextField.positionCaret(passwordTextField.getText().length());
        } else {
            password.setText(passwordTextField.getText());
            passwordTextField.setVisible(false);
            hbox1.setVisible(false);

            password.setVisible(true);
            hbox2.setVisible(true);

            password.requestFocus();
            password.positionCaret(password.getText().length());
        }
    }

    @FXML
    void login(ActionEvent ignoredEvent) {
        RegisterStage.hide();
        loginStage.show();
    }

    @FXML
    void back(ActionEvent ignoredEvent) {
        RegisterStage.hide();
        MainUI.stage.show();
    }

    @FXML
    void back1(ActionEvent ignoredEvent) {
        RegisterStage.hide();
        loginStage.show();
    }

    @FXML
    void signup(ActionEvent ignoredEvent) {
        String user = username.getText();
        String pass;

        if (password.getText().equals("") && passwordTextField.getText().equals("")) {
            showAlert(Alert.AlertType.ERROR, bundle.getString("general.Alert1Title"), null, bundle.getString("general.Alert1"));
            return;
        }

        if (password.isVisible()) {
            pass = password.getText();
        } else {
            pass = passwordTextField.getText();
        }

        String confirmPass;
        if (password1.isVisible()) {
            confirmPass = password1.getText();
        } else {
            confirmPass = passwordTextField1.getText();
        }

        if (!pass.equals(confirmPass)) {
            showAlert(Alert.AlertType.ERROR, bundle.getString("general.ALert1Title"), null, bundle.getString("general.Alert4"));
            shakeControl(password.isVisible() ? password : passwordTextField);
            shakeControl(password1.isVisible() ? password1 : passwordTextField1);
            return;
        }

        if (user.isEmpty() || pass.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, bundle.getString("general.ALert1Title"), null, bundle.getString("general.Alert1"));
            return;
        }

        BlFacadeImplementation bl = BlFacadeImplementation.getInstance();

        if (!bl.userExists(user)) {
            bl.signUp(user, pass);

            User loggedInUser = bl.login(user, pass);

            if (loggedInUser != null) {
                showAlert(Alert.AlertType.INFORMATION, bundle.getString("general.Alert2Title"), null, bundle.getString("general.Alert5"));

                // Set the logged-in user in MainController
                mainController.setLoggedUser(loggedInUser);
                // Notify MainController about login state
                mainController.setLoggedIn(true);
                mainController.LogInSteamIDs();
                mainController.updateLoginStatus("Welcome, " + user, "green");

                // Close the register window immediately
                RegisterStage.hide();
                MainUI.stage.show();
            } else {
                showAlert(Alert.AlertType.ERROR, bundle.getString("general.Alert1Title"), null, bundle.getString("general.Alert6"));
            }
        } else {
            showAlert(Alert.AlertType.ERROR, bundle.getString("general.Alert1Title"), null, bundle.getString("general.Alert7"));
            shakeControl(username);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void shakeControl(javafx.scene.Node control) {
        // Create shake animation for invalid fields
        javafx.animation.TranslateTransition shake = new javafx.animation.TranslateTransition(Duration.millis(50), control);
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.play();

        // Also highlight the control with red border
        String originalStyle = control.getStyle();
        control.setStyle(originalStyle + "; -fx-border-color: #dc3545; -fx-border-width: 2;");

        // Return to original style after animation
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> control.setStyle(originalStyle));
        pause.play();
    }

    public void handleMouseEnter2(MouseEvent ignoredMouseEvent) {
        back.setCursor(Cursor.HAND);
    }

    public void handleMouseExit2(MouseEvent ignoredMouseEvent) {
        back.setCursor(Cursor.DEFAULT);
    }

    public void signupenter(MouseEvent ignoredMouseEvent) {
        signup.setStyle("-fx-background-color: #b71c1c; -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-background-radius: 8;");
        signup.setCursor(Cursor.HAND);
    }

    public void signupexit(MouseEvent ignoredMouseEvent) {
        signup.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-background-radius: 8;");
        signup.setCursor(Cursor.DEFAULT);
    }

    public void eyeenter(MouseEvent ignoredMouseEvent) {
        eye.setCursor(Cursor.HAND);
        eye_closed.setCursor(Cursor.HAND);
    }

    public void eyeexit(MouseEvent ignoredMouseEvent) {
        eye.setCursor(Cursor.DEFAULT);
        eye_closed.setCursor(Cursor.DEFAULT);
    }

    public void back1enter(MouseEvent mouseEvent) {
        back1.setCursor(Cursor.HAND);
        back1.setStyle("-fx-background-color: transparent; -fx-text-fill: #0056b3; -fx-underline: true;");
    }

    public void back1exited(MouseEvent mouseEvent) {
        back1.setCursor(Cursor.DEFAULT);
        back1.setStyle("-fx-background-color: transparent; -fx-text-fill: #007bff; -fx-underline: true;");
    }
}