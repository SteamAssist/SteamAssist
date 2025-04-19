package eus.ehu.template.businessLogic;

import eus.ehu.template.configuration.Config;
import eus.ehu.template.dataAccess.DataAccess;
import eus.ehu.template.domain.User;
import eus.ehu.template.domain.Game;
import javafx.collections.ObservableList;

import java.util.ArrayList;
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

    private void ensureOpen() {
        if (!dbManager.isOpen()) {
            dbManager.open();
        }
    }

    public List<Game> getGames() {
        ensureOpen();
            List<Game> games = dbManager.getAllGames();
            dbManager.close();
            return games;


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
        ensureOpen();
        boolean exists = dbManager.userExists(username);
        dbManager.close();
        return exists;
    }

    @Override
    public User login(String username, String password) {
        ensureOpen();
        User user = dbManager.login(username, password);
        System.out.println("User logged in: " + user);
        dbManager.close();
        return user;
    }

    public void signUp(String username, String password) {
        ensureOpen();
        dbManager.signUp(username, password);
        System.out.println("User signed up: " + username);
        dbManager.close();
    }

    @Override
    public void storeGame(String name, String hltb, String playtime, String link, String hltblink, int minutes) {
        ensureOpen();
        dbManager.storeGame(name, hltb, playtime, link, hltblink, minutes);
        dbManager.close();
    }

    @Override
    public void storeGameDirectly(Game game) {
        ensureOpen();
        dbManager.storeGameDirectly(game);
        dbManager.close();
    }

    @Override
    public void deleteGameByName(String name) {
        ensureOpen();
        dbManager.deleteGameByName(name);
        dbManager.close();
    }

    @Override
    public void deleteGame(Game game) {
        ensureOpen();
        dbManager.deleteGame(game);
        dbManager.close();
    }

    @Override
    public void vinculateSteamID(String username, String steamID, String Accountname) {
        ensureOpen();
        dbManager.vinculateSteamID(username, steamID, Accountname);
        dbManager.close();
    }

    @Override
    public boolean exitSteamID(String username, String steamID){
        ensureOpen();
        boolean result= dbManager.exitSteamID(username, steamID);
        dbManager.close();
        return result;
    }

    @Override
    public ArrayList<String> getAccountname(String name){
        ensureOpen();
        ArrayList<String> steamIDs = dbManager.getSteamAccounts(name);
        dbManager.close();
        return steamIDs;
    };

    @Override
    public ArrayList<String> getSteamID(String name) {
        ensureOpen();
        ArrayList<String> steamIDs = dbManager.getSteamId(name);
        dbManager.close();
        return steamIDs;
    }
}
