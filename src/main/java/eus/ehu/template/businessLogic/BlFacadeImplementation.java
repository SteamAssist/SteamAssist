package eus.ehu.template.businessLogic;

import eus.ehu.template.configuration.Config;
import eus.ehu.template.dataAccess.DataAccess;
import eus.ehu.template.domain.User;

import java.util.Calendar;


/**
 * Implements the business logic as a web service.
 */
public class BlFacadeImplementation implements BlFacade {

    DataAccess dbManager;
    Config config = Config.getInstance();

    private static BlFacadeImplementation bl = new BlFacadeImplementation();

    public static BlFacadeImplementation getInstance() {
        return bl;
    }

    private BlFacadeImplementation() {
        System.out.println("Creating BlFacadeImplementation instance");
        boolean initialize = config.getDataBaseOpenMode().equals("initialize");
        dbManager = new DataAccess();
        if (initialize)
            dbManager.initializeDB();
    }
    public User login(String username, String password, String role) {
        dbManager.open();
        User user = dbManager.login(username, password, role);
        System.out.println("User logged in: " + user);
        dbManager.close();
        return user;
    }
    public void open() {
        dbManager.open();
    }

    public void close() {
        dbManager.close();
    }
}
