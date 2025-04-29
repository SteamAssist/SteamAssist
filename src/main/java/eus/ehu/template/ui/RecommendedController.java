package eus.ehu.template.ui;

import eus.ehu.template.domain.Game;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kong.unirest.Unirest;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.*;

import static eus.ehu.template.ui.MainController.RecommendStage;
import static eus.ehu.template.ui.MainController.loginStage;

public class RecommendedController {

    // ListView to display the list of recommended games
    @FXML
    private ListView<String> game_list;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button back;

    // Reference to the MainController
    private MainController mainController;

    // Setter to inject the MainController instance
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    // Method to handle the filter button click event


    @FXML
    public void initialize() {
        // Set up custom cell factory to format game name and matching tags
        game_list.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Split game name and tags from the display string
                    String[] parts = item.split("\\(matching tags: ");
                    String gameName = parts[0].trim();
                    String tags = parts.length > 1 ? parts[1].replace(")", "").trim() : "";

                    // Layout
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
            protected Void call() throws Exception {
                try {
                    String url = "https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key="
                            + "FE22AD6FF3F54621F5BE4652A491CEFF" + "&steamid=" + id + "&format=json";
                    JsonObject response = JsonParser.parseString(Unirest.get(url).asString().getBody()).getAsJsonObject();
                    JsonArray games = response.getAsJsonObject("response").getAsJsonArray("games");

                    int totalSteps = games.size() + 100;
                    int step = 0;

                    for (JsonElement game : games) {
                        int appid = game.getAsJsonObject().get("appid").getAsInt();
                        userGames.add(appid);

                        String steamSpyUrl = "https://steamspy.com/api.php?request=appdetails&appid=" + appid;
                        String raw = Unirest.get(steamSpyUrl).asString().getBody();
                        JsonObject gameInfo = JsonParser.parseString(raw).getAsJsonObject();

                        if (gameInfo.has("tags") && gameInfo.get("tags").isJsonObject()) {
                            JsonObject tags = gameInfo.getAsJsonObject("tags");
                            userTags.addAll(tags.keySet());
                        }

                        step++;
                        updateProgress(step, totalSteps);
                        Thread.sleep(500);
                    }

                    String top100Url = "https://steamspy.com/api.php?request=top100in2weeks";
                    JsonObject top100 = JsonParser.parseString(Unirest.get(top100Url).asString().getBody()).getAsJsonObject();

                    List<GameMatch> matches = new ArrayList<>();

                    for (Map.Entry<String, JsonElement> entry : top100.entrySet()) {
                        JsonObject game = entry.getValue().getAsJsonObject();
                        int appid = game.get("appid").getAsInt();

                        if (userGames.contains(appid)) continue;

                        String detailUrl = "https://steamspy.com/api.php?request=appdetails&appid=" + appid;
                        String detailRaw = Unirest.get(detailUrl).asString().getBody();
                        JsonObject detailed = JsonParser.parseString(detailRaw).getAsJsonObject();

                        if (detailed.has("tags") && detailed.get("tags").isJsonObject()) {
                            JsonObject tags = detailed.getAsJsonObject("tags");
                            Set<String> gameTags = tags.keySet();

                            int commonTags = 0;
                            for (String tag : gameTags) {
                                if (userTags.contains(tag)) {
                                    commonTags++;
                                }
                            }

                            if (commonTags > 0) {
                                matches.add(new GameMatch(detailed.get("name").getAsString(), commonTags));
                            }
                        }

                        step++;
                        updateProgress(step, totalSteps);
                        Thread.sleep(500);
                    }

                    matches.sort((a, b) -> Integer.compare(b.commonTags, a.commonTags));

                    Platform.runLater(() -> {
                        game_list.getItems().clear();
                        if (matches.isEmpty()) {
                            game_list.getItems().add("No game recommendations found.");
                        } else {
                            matches.stream().limit(10).forEach(match ->
                                    game_list.getItems().add(match.name + " (matching tags: " + match.commonTags + ")"));
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> {
                        game_list.getItems().clear();
                        game_list.getItems().add("Error fetching recommendations. Please try again.");
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


    // Helper class
    static class GameMatch {
        String name;
        int commonTags;

        GameMatch(String name, int commonTags) {
            this.name = name;
            this.commonTags = commonTags;
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