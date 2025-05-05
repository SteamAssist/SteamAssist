package eus.ehu.template.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainUI extends Application {

    private static final Logger logger = LogManager.getLogger("UI");

    public static Stage stage;

    //Bundle for i18n
    private static ResourceBundle bundle;

    @Override
    public void start(Stage stage) throws IOException {

        logger.info("Starting UI");
        //i18n setup
        ChangeLanguage("en");

        FXMLLoader fxmlLoader = new FXMLLoader(MainUI.class.getResource("main-ui.fxml"),bundle);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle(bundle.getString("main.title"));
        stage.show();
        MainUI.stage = stage;
    }
    public static void main(String[] args) throws IOException {
        try {
            // automatically run app.js
            ProcessBuilder pbApp = new ProcessBuilder("node", "src/main/java/eus/ehu/template/howlongtobeat_scraper/app/app.js");
            pbApp.inheritIO();
            pbApp.start();

            // automatically run game.js
            ProcessBuilder pbGame = new ProcessBuilder("node", "src/main/java/eus/ehu/template/howlongtobeat_scraper/app/game.js");
            pbGame.inheritIO();
            pbGame.start();
        } catch (Exception e) {
            System.err.println("Failed to start Node.js scripts: " + e.getMessage());
        }
        launch();
    }
    public static ResourceBundle getBundle() {
        return bundle;
    }
    public static void ChangeLanguage(String language) {
        Locale locale = Locale.forLanguageTag(language);
        bundle = ResourceBundle.getBundle("eus.ehu.i18n.Message", locale);
        logger.info("Language set to: {}", language);
    }
}