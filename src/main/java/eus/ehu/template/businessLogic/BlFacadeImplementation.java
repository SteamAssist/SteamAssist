package eus.ehu.template.businessLogic;

import eus.ehu.template.configuration.Config;
import eus.ehu.template.dataAccess.DataAccess;
import eus.ehu.template.domain.User;
import eus.ehu.template.domain.Game;
import javafx.collections.ObservableList;

import java.util.List;


/**
 * Implements the business logic as a web service.
 */
public class BlFacadeImplementation implements BlFacade {

    DataAccess dbManager;
    Config config = Config.getInstance();

    private static final BlFacadeImplementation bl = new BlFacadeImplementation();

    public static BlFacadeImplementation getInstance() {
        return bl;
    }

    public List<Game> getGames() {
        return dbManager.getAllGames();
    }

    private BlFacadeImplementation() {
        System.out.println("Creating BlFacadeImplementation instance");
        boolean initialize = config.getDataBaseOpenMode().equals("initialize");
        dbManager = new DataAccess();
        if (initialize)
            dbManager.initializeDB();
    }
    @Override
    public boolean userExists(String username) {
        dbManager.open();
        boolean exists = dbManager.userExists(username);
        dbManager.close();
        return exists;
    }

    @Override
    public User login(String username, String password) {
        dbManager.open();
        User user = dbManager.login(username, password);
        System.out.println("User logged in: " + user);
        dbManager.close();
        return user;
    }

    public void signUp(String username, String password) {
        dbManager.open();
        dbManager.signUp(username, password);
        System.out.println("User signed up: " + username);
        dbManager.close();
    }

    @Override
    public void storeGame(String name, String hltb, String playtime, String link, String hltblink, int minutes) {
        dbManager.storeGame(name, hltb, playtime, link, hltblink, minutes);
    }

    @Override
    public void storeGameDirectly(Game game) {
        dbManager.storeGameDirectly(game);
    }

    @Override
    public void deleteGameByName(String name) {
        dbManager.deleteGameByName(name);
    }

    @Override
    public void deleteGame(Game game) {
        dbManager.deleteGame(game);
    }

    @Override
    public void vinculateSteamID(String username, String steamID) {
        dbManager.open();
        dbManager.vinculateSteamID(username, steamID);
        dbManager.close();
    }

    @Override
    public boolean exitSteamID(String username, String steamID){
        dbManager.open();
        boolean result= dbManager.exitSteamID(username, steamID);
        dbManager.close();
        return result;
    }

    @Override
    public ObservableList<String> getSteamIDs(String name){
        dbManager.open();
        ObservableList<String> steamIDs = dbManager.getSteamIDs(name);
        dbManager.close();
        return steamIDs;
    };
}
