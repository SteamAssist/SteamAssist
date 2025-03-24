package eus.ehu.template.ui;

import eus.ehu.template.businessLogic.BlFacadeImplementation;
import eus.ehu.template.domain.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.h2.tools.Server;

public class MainController {

    @FXML
    private PasswordField password;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private TextField username;
    private BlFacadeImplementation bl;

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("Teacher", "Student", "Administrative");
        roleComboBox.setPromptText("Select Role");
        bl = BlFacadeImplementation.getInstance();
    }

    @FXML
    void login(ActionEvent event) {
        String user = username.getText();
        String pass = password.getText();
        String role = roleComboBox.getValue();

        if (user.isEmpty() || pass.isEmpty() || role == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("All fields are mandatory!");
            alert.showAndWait();
            return;
        }

        BlFacadeImplementation bl = BlFacadeImplementation.getInstance();
       // bl.open();
        User loggedInUser = bl.login(user, pass, role);
       // bl.close();
        if (loggedInUser != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Login successful");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid credentials. Please try again");
            alert.showAndWait();
        }
    }
}