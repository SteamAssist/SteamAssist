package eus.ehu.template.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class LoginUI extends Application {

    private static final Logger logger = LogManager.getLogger("LoginUI");

    @Override
    public void start(Stage stage) throws IOException {
        logger.info("Starting LoginUI");
        FXMLLoader fxmlLoader = new FXMLLoader(LoginUI.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}