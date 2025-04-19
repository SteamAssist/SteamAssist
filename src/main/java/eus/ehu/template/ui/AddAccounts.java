package eus.ehu.template.ui;

import eus.ehu.template.domain.Account;
import eus.ehu.template.businessLogic.BlFacadeImplementation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AddAccounts {

    @FXML
    private TextField accountId;

    @FXML
    private TextField accountName;

    @FXML
    private Button add_bttn;

    @FXML
    void Add_Account(ActionEvent event) {
        String id = accountId.getText();
        String name = accountName.getText();

        if (id.length() == 17) {
            BlFacadeImplementation.getInstance().createAccount(LogIn_SignIn_Controller.currentUser, id, name);
            accountId.clear();
            accountName.clear();
            MainController.addAccountsStage.hide();
            MainUI.stage.show();
        } else {
            System.out.println("Invalid ID");
        }
    }

    @FXML
    void back(ActionEvent event) {
        MainController.addAccountsStage.hide();
        MainUI.stage.show();
    }
}
