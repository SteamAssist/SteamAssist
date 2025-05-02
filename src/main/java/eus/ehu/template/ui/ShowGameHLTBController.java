package eus.ehu.template.ui;

import com.google.gson.Gson;
import eus.ehu.template.configuration.Utils;
import eus.ehu.template.domain.GameData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

import static eus.ehu.template.ui.MainController.*;

public class ShowGameHLTBController {

    MainController maincontroller;

    public void setMainController(MainController maincontroller) {
        this.maincontroller = maincontroller;
    }

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
        String json = Utils.query("http://localhost:3001/game?id=" + gameId);
        Gson gson = new Gson();
        GameData gameData = gson.fromJson(json, GameData.class);

        // Set text fields
        title.setText(gameData.getData().getTitle());
        description.setText(gameData.getData().getDescription());
        price.setText(gameData.getData().getPrice());

        // Set times
        mainLength.setText(gameData.getData().getTimes().getMainStory());
        mainSidesLength.setText(gameData.getData().getTimes().getMainPlusSides());
        completeLength.setText(gameData.getData().getTimes().getCompletionist());
        allLength.setText(gameData.getData().getTimes().getAllStyles());

        // Load image
        String imageUrl = gameData.getData().getImage().getUrl();
        Image fxImage = new Image(imageUrl);
        image.setImage(fxImage);

    }

}
