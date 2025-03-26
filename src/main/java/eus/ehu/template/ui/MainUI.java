package eus.ehu.template.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class MainUI extends Application {

    private static final Logger logger = LogManager.getLogger("LoginUI");

    public static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        logger.info("Starting LoginUI");
        FXMLLoader fxmlLoader = new FXMLLoader(MainUI.class.getResource("main-ui.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("SteamAssist-Main");
        stage.show();
        this.stage = stage;
    }
    public static void main(String[] args) {
        launch();
    }
}