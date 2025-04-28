package eus.ehu.template.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import kong.unirest.Unirest;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
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
    private Button back;

    // Reference to the MainController
    private MainController mainController;

    // Setter to inject the MainController instance
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    // Method to handle the filter button click event


    @FXML
    public void filter(ActionEvent event) {
        Set<Integer> userGames = new HashSet<>();
        Set<String> userTags = new HashSet<>();
        String id;

        // Check if Steam ID is set in MainController
        if (mainController == null || !mainController.isSteamIDSet()) {
            id = SteamIDController.getIdNotLogged();
        } else {
            id = mainController.getSteamID();
        }

        // URL to fetch user's owned games
        String url = "https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key="
                + "FE22AD6FF3F54621F5BE4652A491CEFF" + "&steamid=" + id + "&format=json";
        System.out.println(url);

        try {
            JsonObject response = JsonParser.parseString(Unirest.get(url).asString().getBody()).getAsJsonObject();
            JsonArray games = response.getAsJsonObject("response").getAsJsonArray("games");

            for (JsonElement game : games) {
                int appid = game.getAsJsonObject().get("appid").getAsInt();
                userGames.add(appid);

                // Get tags from SteamSpy
                String steamSpyUrl = "https://steamspy.com/api.php?request=appdetails&appid=" + appid;
                System.out.println(steamSpyUrl);
                String raw = Unirest.get(steamSpyUrl).asString().getBody();
                JsonObject gameInfo = JsonParser.parseString(raw).getAsJsonObject();
                System.out.println(gameInfo);

                if (gameInfo.has("tags") && gameInfo.get("tags").isJsonObject()) { // Check if 'tags' is a JsonObject
                    JsonObject tags = gameInfo.getAsJsonObject("tags");
                    userTags.addAll(tags.keySet());
                }

                Thread.sleep(500); // Avoid hitting API rate limits
            }

            // Fetch top 100 games in the last 2 weeks from SteamSpy
            String top100Url = "https://steamspy.com/api.php?request=top100in2weeks";
            JsonObject top100 = JsonParser.parseString(Unirest.get(top100Url).asString().getBody()).getAsJsonObject();

            // Compare and rank games by matching tags
            List<GameMatch> matches = new ArrayList<>();

            for (Map.Entry<String, JsonElement> entry : top100.entrySet()) {
                JsonObject game = entry.getValue().getAsJsonObject();
                int appid = game.get("appid").getAsInt();
                if (userGames.contains(appid)) continue; // Skip owned games

                if (game.has("tags") && game.get("tags").isJsonObject()) {
                    JsonObject tags = game.getAsJsonObject("tags");
                    Set<String> gameTags = tags.keySet();
                    int commonTags = 0;
                    for (String tag : gameTags) {
                        if (userTags.contains(tag)) {
                            commonTags++;
                        }
                    }
                    if (commonTags > 0) {
                        matches.add(new GameMatch(game.get("name").getAsString(), commonTags));
                    }
                }
            }

            // Sort by most common tags
            matches.sort((a, b) -> Integer.compare(b.commonTags, a.commonTags));

            // Display top 10 recommendations in the ListView
            game_list.getItems().clear();
            matches.stream().limit(10).forEach(match ->
                    game_list.getItems().add(match.name + " (matching tags: " + match.commonTags + ")"));

            // Open the new window automatically
            FXMLLoader loader = new FXMLLoader(getClass().getResource("recommended.fxml"));
            Parent recommendedUI = loader.load();

            // Get the controller of RecommendedController
            RecommendedController recommendedController = loader.getController();

            // Inject the MainController instance into RecommendedController
            recommendedController.setMainController(mainController);

            // Create a new window for the Recommended view
            Stage stage = new Stage();
            stage.setScene(new Scene(recommendedUI));
            stage.setTitle("Recommended Games");

            // Show the new window
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            game_list.getItems().add("Error fetching recommendations. Please try again.");
        }
    }


    // Helper class to store game matches
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