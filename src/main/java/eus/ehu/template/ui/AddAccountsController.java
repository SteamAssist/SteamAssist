package eus.ehu.template.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class AddAccountsController {

        @FXML
        private TextField account_id;

        @FXML
        private TextField account_name;

        @FXML
        void add_account(ActionEvent event) {
            String id = account_id.getText();
            String name = account_name.getText();

            if (id.isEmpty() || name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("All fields are mandatory!");
                alert.showAndWait();
            } else {
                // Add the account to the database
                // BlFacadeImplementation.getInstance().addAccount(id, name);
                System.out.println("Account added: " + id + ", " + name);
            }
        }

}
