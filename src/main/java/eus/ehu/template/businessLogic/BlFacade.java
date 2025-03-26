package eus.ehu.template.businessLogic;

// import eus.ehu.template.domain.Hello;

import eus.ehu.template.domain.Game;
import eus.ehu.template.domain.User;

import java.util.List;

/**
 * Interface that specifies the business logic.
 */
public interface BlFacade  {

    // ===== Define the public interface of the BL

    /**
     * Attempts to log in a user with the given credentials
     * @param username the username
     * @param password the password
     * @return User if login successful, null otherwise
     */
    User login(String username, String password);
    List<Game> getGames();
    void storeGame(String name, String htlb, String playtime, String link, String hltblink, int minutes);
    void storeGameDirectly(Game game);
    void deleteGameByName(String name);
    void deleteGame(Game game);
    boolean userExists(String user);
}
