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

    private static ResourceBundle bundle;

    public static ResourceBundle getBundle() {
        return bundle;
    }
    @Override
    public void start(Stage stage) throws IOException {

        logger.info("Starting UI");
        //i18n setup
        Locale locale = Locale.forLanguageTag("en");
        bundle = ResourceBundle.getBundle("eus.ehu.i18n.messages", locale);
        FXMLLoader fxmlLoader = new FXMLLoader(MainUI.class.getResource("main-ui.fxml"), bundle);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle(bundle.getString("titlemain"));
        stage.show();
        MainUI.stage = stage;
    }
    public static void main(String[] args) throws IOException {
        try{
        ProcessBuilder pb = new ProcessBuilder("node", "src/main/java/eus/ehu/template/howlongtobeat_scraper/app/app.js");
        pb.inheritIO();
        pb.start();
        }catch (Exception e){
            Exception ea = new Exception("Failed to start node");
        }
        launch();
    }
    public static void cambiarIdioma(String codigoIdioma) {
        Locale locale = Locale.forLanguageTag(codigoIdioma);
        bundle = ResourceBundle.getBundle("eus.ehu.i18n.messages", locale);
        if (stage != null) {
            stage.setTitle(bundle.getString("titlemain"));
        }
    }
    public static Locale getLocaleActual() {
        return bundle.getLocale();
    }
}