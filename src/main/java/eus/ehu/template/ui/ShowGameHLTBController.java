package eus.ehu.template.ui;

import com.google.gson.Gson;
import eus.ehu.template.configuration.Utils;
import eus.ehu.template.domain.GameData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

import java.io.IOException;

import static eus.ehu.template.ui.MainController.*;

public class ShowGameHLTBController {

    @FXML
    private Label allLength;

    @FXML
    private Label completeLength;

    @FXML
    private TextArea description;

    @FXML
    private ImageView image;

    @FXML
    private Label mainLength;

    @FXML
    private Label mainSidesLength;

    @FXML
    private Label price;

    @FXML
    private Label title;

    @FXML
    void back(ActionEvent event) {
        howLongToBeatStage.show();
        ShowGameHLTBStage.hide();

    }

    public void loadGameData(String gameId) throws IOException {
    }

}
