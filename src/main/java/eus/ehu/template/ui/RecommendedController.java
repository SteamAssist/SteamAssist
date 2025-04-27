package eus.ehu.template.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

public class RecommendedController {

    // ListView to display the list of recommended games
    @FXML
    private ListView<String> game_list;

    // Button to apply filters to the list
    @FXML
    private Button filter_bttn;

    // Button to log out of the application
    @FXML
    private Button logout_bttn;

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

        // Fetch user's owned games and recommend new ones
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
                String raw = Unirest.get(steamSpyUrl).asString().getBody();
                JsonObject gameInfo = JsonParser.parseString(raw).getAsJsonObject();

                if (gameInfo.has("tags")) {
                    JsonObject tags = gameInfo.getAsJsonObject("tags");
                    userTags.addAll(tags.keySet());
                }

                Thread.sleep(500); // Avoid hitting API rate limits
            }

            // Fetch top100in2weeks from SteamSpy
            String top100Url = "https://steamspy.com/api.php?request=top100in2weeks";
            JsonObject top100 = JsonParser.parseString(Unirest.get(top100Url).asString().getBody()).getAsJsonObject();

            // Compare and rank games by tag matching
            List<GameMatch> matches = new ArrayList<>();

            for (Map.Entry<String, JsonElement> entry : top100.entrySet()) {
                JsonObject game = entry.getValue().getAsJsonObject();
                int appid = game.get("appid").getAsInt();
                if (userGames.contains(appid)) continue; // Skip owned games

                JsonObject tags = game.getAsJsonObject("tags");
                if (tags != null) {
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

            // Sort by most tags in common
            matches.sort((a, b) -> Integer.compare(b.commonTags, a.commonTags));

            // Display top 10 recommendations in the ListView
            game_list.getItems().clear();
            matches.stream().limit(10).forEach(match ->
                    game_list.getItems().add(match.name + " (matching tags: " + match.commonTags + ")"));

            // **Open the new window automatically**
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




}