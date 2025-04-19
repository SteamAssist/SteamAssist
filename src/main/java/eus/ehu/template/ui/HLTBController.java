package eus.ehu.template.ui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eus.ehu.template.businessLogic.BlFacadeImplementation;
import eus.ehu.template.configuration.Utils;
import eus.ehu.template.domain.Game;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import static eus.ehu.template.ui.MainController.*;
import javafx.scene.control.Button;
import java.io.IOException;
import java.util.Optional;
import javafx.scene.layout.VBox;

public class HLTBController {

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private final BlFacadeImplementation bl = BlFacadeImplementation.getInstance();
    @FXML
    private Label gameCount;

    @FXML
    private ListView<Game> game_list;

    @FXML
    private Button login;

    @FXML
    private Button back_bttn;

       private ObservableList<Game> games;

    @FXML
    private VBox loadingScreen;

    @FXML
    void back(ActionEvent ignoredEvent) {
        howLongToBeatStage.hide();
        MainUI.stage.show();
    }

    @FXML
    void log_in(ActionEvent ignoredEvent) {

    }

    @FXML
    public void initialize() {

    }

    @FXML
    void addDriverUI(ActionEvent ignoredEvent) {
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Add Game");
        nameDialog.setHeaderText("Add a new game");
        nameDialog.setContentText("Game name:");

        Optional<String> nameResult = nameDialog.showAndWait();

        nameResult.ifPresent(name -> {
            TextInputDialog hltbDialog = new TextInputDialog();
            hltbDialog.setTitle("Add Game");
            hltbDialog.setHeaderText("Add a new game");
            hltbDialog.setContentText("How long to beat main story:");

            Optional<String> hltbResult = hltbDialog.showAndWait();

            hltbResult.ifPresent(hltb -> {
                TextInputDialog playtimeDialog = new TextInputDialog();
                playtimeDialog.setTitle("Add Game");
                playtimeDialog.setHeaderText("Add a new game");
                playtimeDialog.setContentText("Playtime:");

                Optional<String> playtimeResult = playtimeDialog.showAndWait();

                playtimeResult.ifPresent(playtime -> {
                    TextInputDialog linkDialog = new TextInputDialog();
                    linkDialog.setTitle("Add Game");
                    linkDialog.setHeaderText("Add a new game");
                    linkDialog.setContentText("Link:");

                    Optional<String> linkResult = linkDialog.showAndWait();

                    linkResult.ifPresent(link -> {
                        TextInputDialog hltblinkDialog = new TextInputDialog();
                        hltblinkDialog.setTitle("Add Game");
                        hltblinkDialog.setHeaderText("Add a new game");
                        hltblinkDialog.setContentText("HLTB Link:");

                        Optional<String> hltblinkResult = hltblinkDialog.showAndWait();

                        hltblinkResult.ifPresent(hltblink -> {
                            TextInputDialog numMinutesDialog = new TextInputDialog();
                            numMinutesDialog.setTitle("Add Game");
                            numMinutesDialog.setHeaderText("Add a new game");
                            numMinutesDialog.setContentText("Number of minutes:");

                            Optional<String> numMinutesResult = numMinutesDialog.showAndWait();

                            numMinutesResult.ifPresent(numMinutesStr -> {
                                int numMinutes = Integer.parseInt(numMinutesStr);
                                Game newGame = new Game(name, hltb, playtime, link, hltblink, numMinutes);
                                bl.storeGame(newGame.getName(), newGame.getHowlongtobeat(), newGame.getPlaytime(), newGame.getLink(), newGame.getHLTBLink(), newGame.getNumMinutes());

                                // Add to the observable list (will automatically update ListView)
                                games.add(newGame);

                                // Optional: Scroll to the new item
                                game_list.scrollTo(newGame);
                            });
                        });
                    });
                });
            });
        });
    }


    public void meteJogos() {
        games = FXCollections.observableArrayList();

        // Actualizar la lista inicial en el hilo de la aplicación JavaFX
        Platform.runLater(() -> {
            games.addAll(bl.getGames());
            game_list.setItems(games);
            updateGameCount();
        });

        // Listener para actualizar el conteo de juegos
        games.addListener((ListChangeListener<Game>) change ->
                Platform.runLater(this::updateGameCount)
        );

        try {
            String link = "http://localhost:3000/?id=";
            if (!mainController.isSteamIDSet()) {
                link += SteamIDController.getIdNotLogged();
            } else {
                link += mainController.getSteamID();
            }

            String json = Utils.query(link);
            JsonParser parser = new JsonParser();
            JsonElement rootElement = parser.parse(json);
            JsonObject rootObject = rootElement.getAsJsonObject();
            JsonArray dataArray = rootObject.getAsJsonArray("data");

            for (JsonElement element : dataArray) {
                JsonObject gameObject = element.getAsJsonObject();
                Game newGame = new Game(
                        gameObject.get("Name").getAsString(),
                        gameObject.get("HLTB Time").getAsString(),
                        gameObject.get("Playtime").getAsString(),
                        gameObject.get("Link").getAsString(),
                        gameObject.get("HLTBLink").getAsString(),
                        gameObject.get("numMinutes").getAsInt()
                );

                bl.storeGame(newGame.getName(), newGame.getHowlongtobeat(), newGame.getPlaytime(), newGame.getLink(), newGame.getHLTBLink(), newGame.getNumMinutes());

                // Agregar el nuevo juego a la lista en el hilo de la aplicación JavaFX
                Platform.runLater(() -> games.add(newGame));
            }

        } catch (IOException e) {
            System.out.println("An unexpected error occurred connecting to the server, try running app.js.");
        }
    }



    private void updateGameCount() {
        Platform.runLater(() -> {;//pa que funcione con la pantalla de carga
            gameCount.setText("Number of games: " + games.size());
        });
    }

    @FXML
    public void cargaLaVaina() {
        loadingScreen.setVisible(true);
        Task<Void> loadDataTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                meteJogos();
                return null;
            }
        };
        loadDataTask.setOnSucceeded(event -> loadingScreen.setVisible(false));
        loadDataTask.setOnFailed(event -> {
            loadingScreen.setVisible(false);
            System.err.println("Could not load games: " + loadDataTask.getException().getMessage());
        });
        new Thread(loadDataTask).start();
    }

}
