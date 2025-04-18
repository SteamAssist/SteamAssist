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


public class RegisterController {
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

    @FXML
    private Button back1;
    @FXML
    private Button signup;



    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {

    }

    @FXML
    public void togglePasswordVisibility(MouseEvent ignoredEvent) {
       Visible(password, passwordTextField, hbox1, hbox2);
    }
    @FXML
    public void togglePasswordVisibility1(MouseEvent ignoredEvent) {
        Visible(password1, passwordTextField1, hbox3, hbox4);
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
        if(password.getText().equals(password1.getText())) {
            pass = password.getText();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Passwords do not match!");
            alert.showAndWait();
            return;
        }
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
    public void handleMouseEnter2(MouseEvent ignoredMouseEvent) {
         back.setCursor(Cursor.HAND);
    }

    public void handleMouseExit2(MouseEvent ignoredMouseEvent) {
       back.setCursor(Cursor.DEFAULT);
    }


    public void signupenter(MouseEvent ignoredMouseEvent) {
        signup.setStyle("-fx-background-color: #b71c1c; -fx-text-fill: #ffffff");
        signup.setCursor(Cursor.HAND);
    }
    public void signupexit(MouseEvent ignoredMouseEvent) {
        signup.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: #ffffff");
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
        back1.setStyle("-fx-background-color: #1565c0;-fx-text-fill:#ffffff ");

    }

    public void back1exited(MouseEvent mouseEvent) {
        back1.setCursor(Cursor.DEFAULT);
        back1.setStyle("-fx-background-color: #1976d2;-fx-text-fill:#ffffff; ");
    }
}
