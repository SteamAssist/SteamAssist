package eus.ehu.template.ui;

import com.google.gson.Gson;
import eus.ehu.template.configuration.Utils;
import eus.ehu.template.domain.GameData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ResourceBundle;

import static eus.ehu.template.ui.MainController.*;

public class ShowGameHLTBController {

    @FXML
    private Button BTGL;
    @FXML
    private Button AddFavorites;
    @FXML
    private Label allStyles;
    @FXML
    private Label Completionist;
    @FXML
    private Label mainsides;
    @FXML
    private Label gameDetails;
    @FXML
    private Label hltbTitle;
    @FXML
    private Label mainStory;

    MainController maincontroller;

    //bundle for i18n
    private ResourceBundle bundle;

    public void updateUITexts() {
        bundle = MainUI.getBundle();
        gameDetails.setText(bundle.getString("hltb2.gameDetails"));
        hltbTitle.setText(bundle.getString("hltb2.title"));
        mainStory.setText(bundle.getString("hltb2.mainStory"));
        mainsides.setText(bundle.getString("hltb2.mainPlusSides"));
        Completionist.setText(bundle.getString("hltb2.completionist"));
        allStyles.setText(bundle.getString("hltb2.allStyles"));
        BTGL.setText(bundle.getString("hltb2.BTGL"));
        AddFavorites.setText(bundle.getString("hltb2.addFavorites"));
    }

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
