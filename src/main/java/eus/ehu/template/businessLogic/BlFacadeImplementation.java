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
    @Override
    public boolean userExists(String username) {
        dbManager.open();
        boolean exists = dbManager.userExists(username);
        dbManager.close();
        return exists;
    }

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
    public boolean updatePassword(String username, String newPassword) {
        boolean success = false;
        dbManager.open();
        if (dbManager.userExists(username)) {
            User user = dbManager.findUserByUsername(username);
            if (user != null) {
                user.setPassword(newPassword); // Assuming User has a setPassword method
                success = dbManager.saveUser(user);
                if (success) {
                    System.out.println("Password updated for user: " + username);
                } else {
                    System.out.println("Failed to update password for user: " + username);
                }
            } else {
                System.out.println("User not found: " + username);
            }
        } else {
            System.out.println("User does not exist: " + username);
        }
        dbManager.close();
        return success;
    }


}