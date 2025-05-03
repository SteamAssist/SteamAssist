package eus.ehu.template.ui;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import javafx.stage.Stage;

import java.awt.Desktop;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.*;
import kong.unirest.Unirest;

import static eus.ehu.template.ui.MainController.*;

public class RecommendedController {

    @FXML
    private ListView<String> game_list;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button back;

    @FXML
    private Button filter_bttn;

    @FXML
    private Label gameNameLabel;

    @FXML
    private Label matchingTagsLabel;

    private MainController mainController;

    // Maps each game name to its appid for opening the Steam page later
    private final Map<String, Integer> gameNameToAppId = new HashMap<>();

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        // Custom cell with two columns: game name and matching tag count
        game_list.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String[] parts = item.split("\\(matching tags: ");
                    String gameName = parts[0].trim();
                    String tags = parts.length > 1 ? parts[1].replace(")", "").trim() : "";

                    HBox hBox = new HBox(10);
                    Label nameLabel = new Label(gameName);
                    Label tagsLabel = new Label(tags);

                    nameLabel.setPrefWidth(300);
                    tagsLabel.setPrefWidth(200);

                    hBox.getChildren().addAll(nameLabel, tagsLabel);
                    setGraphic(hBox);
                }
            }
        });

        // Open Steam page on click
        game_list.setOnMouseClicked(event -> {
            String selected = game_list.getSelectionModel().getSelectedItem();
            if (selected != null && gameNameToAppId.containsKey(selected.split("\\(matching")[0].trim())) {
                int appid = gameNameToAppId.get(selected.split("\\(matching")[0].trim());
                try {
                    Desktop.getDesktop().browse(new URI("https://store.steampowered.com/app/" + appid));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Inicializar los textos con el idioma actual
        actualizarTextos();
    }

    @FXML
    public void filter(ActionEvent event) {
        Set<Integer> userGames = new HashSet<>();
        Set<String> userTags = new HashSet<>();
        String id;

        if (mainController == null || !mainController.isSteamIDSet()) {
            id = SteamIDController.getIdNotLogged();
        } else {
            id = mainController.getSteamID();
        }

        progressBar.setVisible(true);
        progressBar.setProgress(0);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    String steamAPIkey = mainController.getSteamAPIKey();
                    String url = "https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key="
                            + steamAPIkey + "&steamid=" + id + "&format=json";

                    JsonObject response = JsonParser.parseString(Unirest.get(url).asString().getBody()).getAsJsonObject();
                    JsonArray games = response.getAsJsonObject("response").getAsJsonArray("games");

                    int totalSteps = games.size() + 100;
                    int step = 0;

                    for (JsonElement game : games) {
                        int appid = game.getAsJsonObject().get("appid").getAsInt();
                        userGames.add(appid);

                        String raw = Unirest.get("https://steamspy.com/api.php?request=appdetails&appid=" + appid).asString().getBody();
                        JsonObject gameInfo = JsonParser.parseString(raw).getAsJsonObject();

                        if (gameInfo.has("tags") && gameInfo.get("tags").isJsonObject()) {
                            JsonObject tags = gameInfo.getAsJsonObject("tags");
                            userTags.addAll(tags.keySet());
                        }

                        step++;
                        updateProgress(step, totalSteps);
                        Thread.sleep(500);
                    }

                    String top100Raw = Unirest.get("https://steamspy.com/api.php?request=top100in2weeks").asString().getBody();
                    JsonObject top100 = JsonParser.parseString(top100Raw).getAsJsonObject();

                    List<GameMatch> matches = new ArrayList<>();

                    for (Map.Entry<String, JsonElement> entry : top100.entrySet()) {
                        JsonObject game = entry.getValue().getAsJsonObject();
                        int appid = game.get("appid").getAsInt();

                        if (userGames.contains(appid)) continue;

                        String detailRaw = Unirest.get("https://steamspy.com/api.php?request=appdetails&appid=" + appid).asString().getBody();
                        JsonObject detailed = JsonParser.parseString(detailRaw).getAsJsonObject();

                        if (detailed.has("tags") && detailed.get("tags").isJsonObject()) {
                            JsonObject tags = detailed.getAsJsonObject("tags");
                            Set<String> gameTags = tags.keySet();

                            int commonTags = (int) gameTags.stream().filter(userTags::contains).count();

                            if (commonTags > 0) {
                                matches.add(new GameMatch(detailed.get("name").getAsString(), commonTags, appid));
                            }
                        }

                        step++;
                        updateProgress(step, totalSteps);
                        Thread.sleep(500);
                    }

                    matches.sort((a, b) -> Integer.compare(b.commonTags, a.commonTags));

                    Platform.runLater(() -> {
                        game_list.getItems().clear();
                        ResourceBundle bundle = MainUI.getBundle();
                        gameNameToAppId.clear();
                        if (matches.isEmpty()) {
                            game_list.getItems().add(bundle.getString("recommended_no_recommendations"));
                        } else {
                            for (GameMatch match : matches.stream().limit(10).collect(Collectors.toList())) {
                                String display = match.name + " (matching tags: " + match.commonTags + ")";
                                game_list.getItems().add(display);
                                gameNameToAppId.put(match.name, match.appid);
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> {
                        game_list.getItems().clear();
                        ResourceBundle bundle = MainUI.getBundle();
                        game_list.getItems().add(bundle.getString("recommended_error_fetching"));
                    });
                }
                return null;
            }

            @Override
            protected void succeeded() {
                progressBar.setVisible(false);
            }

            @Override
            protected void failed() {
                progressBar.setVisible(false);
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }

    public void actualizarTextos() {
        ResourceBundle bundle = MainUI.getBundle();

        // Actualizar los textos de la interfaz
        filter_bttn.setText(bundle.getString("recommended_filter_button"));
        gameNameLabel.setText(bundle.getString("recommended_game_name"));
        matchingTagsLabel.setText(bundle.getString("recommended_matching_tags"));

        // Actualizar mensajes en la lista si hay alguno
        if (!game_list.getItems().isEmpty() && game_list.getItems().size() == 1) {
            String currentMessage = game_list.getItems().get(0);
            if (currentMessage.equals("No game recommendations found.") ||
                currentMessage.equals("Ez da joko gomendiorik aurkitu.")) {
                game_list.getItems().set(0, bundle.getString("recommended_no_recommendations"));
            } else if (currentMessage.equals("Error fetching recommendations. Please try again.") ||
                       currentMessage.equals("Errorea gomendioak eskuratzean. Mesedez, saiatu berriro.")) {
                game_list.getItems().set(0, bundle.getString("recommended_error_fetching"));
            }
        }
    }

    // Helper class
    static class GameMatch {
        String name;
        int commonTags;
        int appid;

        GameMatch(String name, int commonTags, int appid) {
            this.name = name;
            this.commonTags = commonTags;
            this.appid = appid;
        }
    }

    @FXML
    void back(ActionEvent ignoredEvent) {
        RecommendStage.hide();
        MainUI.stage.show();
    }

    public void handleMouseEnter2(MouseEvent ignoredMouseEvent) {
        back.setCursor(Cursor.HAND);
    }

    public void handleMouseExit2(MouseEvent ignoredMouseEvent) {
        back.setCursor(Cursor.DEFAULT);
    }
}
