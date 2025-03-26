package eus.ehu.template.businessLogic;

// import eus.ehu.template.domain.Hello;

import eus.ehu.template.domain.User;

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
    User login(String username, String password, String id);

    boolean userExists(String user);
}
