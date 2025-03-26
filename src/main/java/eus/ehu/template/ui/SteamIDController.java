package eus.ehu.template.ui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static eus.ehu.template.ui.MainController.*;

public class SteamIDController {

        @FXML
        private Button back_bttn;

        private static String identifier;

        @FXML
        private TextField id;

        @FXML
        void back(ActionEvent event) {
            idStage.hide();
            MainUI.stage.show();
        }

        @FXML
        void confirm(ActionEvent event) {
            if(id.getText().length() == 17){
                identifier = id.getText();
                login();
                howLongToBeatController.meteJogos();
                id.clear();
            }

        }

        public static String getId() {
            return identifier;
        }

    public void login(){
            idStage.hide();
            howLongToBeatStage.show();
    }
}
