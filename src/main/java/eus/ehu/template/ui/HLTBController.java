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
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static eus.ehu.template.ui.MainController.*;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.ResourceBundle;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class HLTBController {

    @FXML
    private Text Loading2;
    @FXML
    private Text Loading1;
    @FXML
    private Label steamStore;
    @FXML
    private Label yourTime;
    @FXML
    private Label hltbTime;
    @FXML
    private Label gameTitle;
    @FXML
    private Button refresh_button;
    @FXML
    private Label HLTB;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private final BlFacadeImplementation bl = BlFacadeImplementation.getInstance();
    @FXML
    private Label gameCount;

    @FXML
    private Label totalPlaytime;

    @FXML
    private Label completionRate;

    @FXML
    private ListView<Game> game_list;

    @FXML
    private Button login;

    @FXML
    private Button back_bttn;

    @FXML
    private Button sortByNameBtn;
    @FXML
    private Button sortByNumtimeBtn;

    @FXML
    private Button sortByPlaytimeBtn;

    @FXML
    private Button sortByCompletionBtn;


    private ObservableList<Game> games;

    @FXML
    private VBox loadingScreen;

    //bundle for i18n
    private ResourceBundle bundle;

    @FXML
    void back(ActionEvent ignoredEvent) {
        howLongToBeatStage.hide();
        MainUI.stage.show();
    }

    @FXML
    void log_in(ActionEvent ignoredEvent) {

    }

    public void updateUITexts() {
        bundle = MainUI.getBundle();
        HLTB.setText(bundle.getString("hltb.HLTB"));
        refresh_button.setText(bundle.getString("hltb.refresh"));
        gameTitle.setText(bundle.getString("hltb.gametitle"));
        hltbTime.setText(bundle.getString("hltb.hltbtime"));
        yourTime.setText(bundle.getString("hltb.yourtime"));
        steamStore.setText(bundle.getString("hltb.steamstore"));
        Loading1.setText(bundle.getString("hltb.loading1"));
        Loading2.setText(bundle.getString("hltb.loading2"));
        sortByCompletionBtn.setText(bundle.getString("hltb.sortbycompletion"));
        sortByNameBtn.setText(bundle.getString("hltb.sortbyname"));
        sortByPlaytimeBtn.setText(bundle.getString("hltb.sortbyplaytime"));
        sortByNumtimeBtn.setText(bundle.getString("hltb.sortbynumtime"));
    }

    @FXML
    public void initialize() {
        //Language setup
        bundle = MainUI.getBundle();
        // CellFactory divide la info de cada juego en columnas
        game_list.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Game game, boolean empty) {
                super.updateItem(game, empty);

                if (empty || game == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Crear un HBox para organizar las columnas
                    HBox hBox = new HBox();
                    Label nameLabel = new Label(game.getName());
                    Label hltbLabel = new Label(game.getHowlongtobeat());
                    Label playtimeLabel = new Label(game.getPlaytime());

                    // Create a clickable hyperlink for the Steam Store URL
                    Hyperlink linkLabel = new Hyperlink(game.getLink());
                   linkLabel.setTextFill(Color.BLUE);
                    linkLabel.setUnderline(true);
                    linkLabel.setFont(Font.font("System", 12));
                    linkLabel.setOnAction(event -> openURL(game.getLink().substring(1)));

                    // Width fijo para cada variable
                    nameLabel.setPrefWidth(250);
                    hltbLabel.setPrefWidth(80);
                    playtimeLabel.setPrefWidth(70);
                    linkLabel.setPrefWidth(340);

                    hBox.getChildren().addAll(nameLabel, hltbLabel, playtimeLabel, linkLabel);
                    setGraphic(hBox);
                }
            }
        });

        // Initialize sorting button handlers
        sortByNameBtn.setOnAction(event -> sortGamesByName());
        sortByPlaytimeBtn.setOnAction(event -> sortGamesByPlaytime());
        sortByCompletionBtn.setOnAction(event -> sortGamesByCompletion());
        sortByNumtimeBtn.setOnAction(event -> sortGamesByNumtime());

    }

    // Method to open URLs in the default browser
    private void openURL(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            // Show error dialog if the URL can't be opened
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("general.Alert1Title"));
            alert.setHeaderText(bundle.getString("general.Alert1Header"));
            alert.setContentText(bundle.getString("general.Alert12") + url + bundle.getString("general.Alert12") + e.getMessage());
            alert.showAndWait();
        }
    }


    // Change the user's Steam ID
    private void changeUserSteamID() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(bundle.getString("hltb.dialogTitle"));
        dialog.setHeaderText(bundle.getString("hltb.dialogheader"));
        dialog.setContentText(bundle.getString("hltb.dialogcontent"));

        dialog.showAndWait().ifPresent(id -> {
            if (!id.isEmpty()) {
                // Update the Steam ID in MainController
                mainController.setSteamID(id);

                // Reload game data with new ID
                cargaLaVaina();
            }
        });
    }

    // Sort games by name (alphabetically)
    private void sortGamesByName() {
        FXCollections.sort(games, Comparator.comparing(Game::getName, String.CASE_INSENSITIVE_ORDER));
        game_list.scrollTo(0);
    }

    // Sort games by playtime (descending)
    private void sortGamesByPlaytime() {
        FXCollections.sort(games, Comparator.comparing(Game::getPlaytimeInt).reversed());
        game_list.scrollTo(0);
    }
    private void sortGamesByNumtime() {
        FXCollections.sort(games, Comparator.comparing(Game::getNumMinutes).reversed());
        game_list.scrollTo(0);
    }

    // Sort games by completion rate (comparing played time vs HLTB time)
    private void sortGamesByCompletion() {
        FXCollections.sort(games, (game1, game2) -> {
            double completion1 = calculateCompletionPercentage(game1);
            double completion2 = calculateCompletionPercentage(game2);
            return Double.compare(completion2, completion1); // Descending order
        });
        game_list.scrollTo(0);
    }

    // Calculate completion percentage for a single game
    private double calculateCompletionPercentage(Game game) {
        int playedMinutes = game.getPlaytimeInt();
        int hltbMinutes = getHLTBMinutes(game);

        // If there's no HLTB data available, don't include in calculation
        if (hltbMinutes <= 0) {
            return 0.0;
        }

        // Calculate completion percentage, capped at 100%
        double percentage = (playedMinutes * 100.0 / hltbMinutes);
        return Math.min(percentage, 100.0); // Cap at 100%
    }

    // Parse HLTB time string to minutes
    private int getHLTBMinutes(Game game) {
        String hltbTime = game.getHowlongtobeat().trim();
        int hltbMinutes = 0;

        // Check if time string is empty or '--'
        if (hltbTime.isEmpty() || hltbTime.equals("--")) {
            return 0;
        }

        try {
            if (hltbTime.contains("h") || hltbTime.contains("m")) {
                // Handle "10h 30m" format
                if (hltbTime.contains("h")) {
                    int hourIndex = hltbTime.indexOf("h");
                    String hourPart = hltbTime.substring(0, hourIndex).trim();
                    try {
                        hltbMinutes += Integer.parseInt(hourPart) * 60;
                    } catch (NumberFormatException e) {
                        // Handle parsing error
                    }
                }

                if (hltbTime.contains("m")) {
                    int minuteIndex = hltbTime.indexOf("m");
                    int startIndex = hltbTime.contains("h") ? hltbTime.indexOf("h") + 1 : 0;
                    String minutePart = hltbTime.substring(startIndex, minuteIndex).trim();
                    try {
                        hltbMinutes += Integer.parseInt(minutePart);
                    } catch (NumberFormatException e) {
                        // Handle parsing error
                    }
                }
            } else {
                // Try to parse as just a number (assume hours)
                hltbMinutes = (int) (Double.parseDouble(hltbTime) * 60);
            }
        } catch (NumberFormatException e) {
            // If parsing fails, return 0
            return 0;
        }

        return hltbMinutes;
    }

    public void meteJogos() {
        games = FXCollections.observableArrayList();

        // Actualizar la lista inicial en el hilo de la aplicación JavaFX
        Platform.runLater(() -> {
            games.addAll(bl.getGames());
            game_list.setItems(games);
            updateGameCount();
            updateTotalPlaytime();
            updateCompletionRate();
        });

        // Listener para actualizar el conteo de juegos y estadísticas
        games.addListener((ListChangeListener<Game>) change ->
                Platform.runLater(() -> {
                    updateGameCount();
                    updateTotalPlaytime();
                    updateCompletionRate();
                })
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
        Platform.runLater(() -> {
            gameCount.setText(bundle.getString("hltb.gamecount") + games.size());
        });
    }

    private void updateTotalPlaytime() {
        Platform.runLater(() -> {
            int totalMinutes = games.stream()
                    .mapToInt(Game::getPlaytimeInt)
                    .sum();

            // Convert total minutes to hours (round to 1 decimal place)
            double totalHours = Math.round(totalMinutes / 60.0 * 10) / 10.0;
            totalPlaytime.setText(bundle.getString("hltb.playtime") + totalHours + bundle.getString("hltb.hours"));
        });
    }

    private void updateCompletionRate() {
        Platform.runLater(() -> {
            // Count games that have valid HLTB data for calculation
            long gamesWithHLTBData = games.stream()
                    .filter(game -> getHLTBMinutes(game) > 0)
                    .count();

            if (gamesWithHLTBData == 0) {
                completionRate.setText(bundle.getString("hltb.completedRate") + 0 + "%");
                return;
            }

            // Count games where playtime >= HLTB time (capped at 100%)
            long completedGames = games.stream()
                    .filter(game -> {
                        int hltbMinutes = getHLTBMinutes(game);
                        return hltbMinutes > 0 && game.getPlaytimeInt() >= hltbMinutes;
                    })
                    .count();

            int percentage = (int) Math.round((completedGames * 100.0) / gamesWithHLTBData);
            completionRate.setText(bundle.getString("hltb.completedRate") + percentage + "%");
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
            System.err.println(bundle.getString("hltb.noloading") + loadDataTask.getException().getMessage());
        });
        new Thread(loadDataTask).start();
    }
}