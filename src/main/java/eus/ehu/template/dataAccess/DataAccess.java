package eus.ehu.template.dataAccess;

import eus.ehu.template.configuration.Config;
import eus.ehu.template.domain.*;
import jakarta.persistence.*;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import eus.ehu.template.domain.Game;
import java.sql.*;
import java.util.List;


/**
 * Implements the Data Access utility to the objectDb database
 */
public class DataAccess {

  protected EntityManager db;
  protected EntityManagerFactory emf;

  public DataAccess() {
    /*
    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure() // configures settings from hibernate.cfg.xml
            .build();
    try {
      emf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    } catch (Exception e) {
      StandardServiceRegistryBuilder.destroy(registry);

    }

    db = emf.createEntityManager();

    System.out.println("DataBase opened");
    */
    this.open();

  }

  public void open() {
    open(false);
  }


  public void open(boolean initializeMode) {

    Config config = Config.getInstance();

    System.out.println("Opening DataAccess instance => isDatabaseLocal: " +
            config.isDataAccessLocal() + " getDatabBaseOpenMode: " + config.getDataBaseOpenMode());

    String fileName = config.getDatabaseName();
    if (initializeMode) {
      fileName = fileName + ";drop";
      System.out.println("Deleting the DataBase");
    }

    if (config.isDataAccessLocal()) {
      // Only create a new EntityManagerFactory if it doesn't exist already
      if (emf == null || !emf.isOpen()) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
          emf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
          // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
          // so destroy it manually.
          StandardServiceRegistryBuilder.destroy(registry);
        }
      }

      // Only create a new EntityManager if it doesn't exist or is closed
      if (db == null || !db.isOpen()) {
        db = emf.createEntityManager();
        System.out.println("DataBase opened");
      }
    }
  }


  public User findUserByUsername(String username) {
    try {
      Connection connection = getConnection();
      String query = "SELECT * FROM users WHERE username = ?";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setString(1, username);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        // Assuming User has a constructor that takes ResultSet
        return new User(resultSet);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
  public boolean saveUser(User user) {
    try {
      Connection connection = getConnection();
      String query = "UPDATE users SET password = ? WHERE username = ?";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setString(1, user.getPassword());
      statement.setString(2, user.getUserName());
      int rowsUpdated = statement.executeUpdate();
      return rowsUpdated > 0;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  private Connection getConnection() {
    // Implement your logic to get a database connection
    Connection connection = null;
    try {
      // Load the JDBC driver
      Class.forName("com.mysql.cj.jdbc.Driver");
      // Establish the connection
      connection = DriverManager.getConnection("jdbc:h2", "your_username", "your_password");
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
    return connection;
  }
  public void reset() {
    db.getTransaction().begin();
  db.createQuery("DELETE FROM User").executeUpdate();
    db.getTransaction().commit();
  }
  public User login(String username, String password) {
    try {
      TypedQuery<User> query = db.createQuery(
              "SELECT u FROM User u WHERE u.userName = :username AND u.password = :password", User.class);
      query.setParameter("username", username);
      query.setParameter("password", password);
      return query.getSingleResult();
    } catch (NoResultException e) {
      System.out.println("User not found");
      return null;
    }
  }

  public void signUp(String username, String password) {
    db.getTransaction().begin();
    User user = new User(username, password);
    db.persist(user);
    db.getTransaction().commit();
  }

  public void storeGame(String name, String hltb, String playtime, String link, String hltblink, int minutes) {
    try {
      db.getTransaction().begin();
      Game game = new Game(name, hltb, playtime, link, hltblink, minutes);
      db.persist(game);
      db.getTransaction().commit();
      System.out.println(game + " has been saved");
      System.out.println("Games in DB: " + getAllGames().size());
    }catch (Exception e){
      System.out.println("Error: " + e.getMessage());
    }
  }

  public void storeGameDirectly (Game game){
    try {
      db.getTransaction().begin();
      db.persist(game);
      db.getTransaction().commit();
      System.out.println(game + " has been saved");
    }catch (Exception e){
      System.out.println("Error: " + e.getMessage());
    }
  }

  public List<Game> getAllGames() {
    TypedQuery<Game> q1 = db.createQuery(
            "SELECT p FROM Game p", Game.class);
    List<Game> games = q1.getResultList();
    return games;
  }

  public void deleteGameByName(String name){
    db.getTransaction().begin();
    Query query = db.createQuery("DELETE FROM Game p WHERE p.Name = :name");
    query.setParameter("name", name);
    int rowsDeleted = query.executeUpdate();
    db.getTransaction().commit();
    System.out.println(rowsDeleted + " game(s) deleted");
  }

  private Game getGameByName(String gameName) {
    TypedQuery<Game> q4 = db.createQuery(
            "SELECT p FROM Game p WHERE p.Name = :name", Game.class);
    q4.setParameter("name", gameName);
    List<Game> games = q4.getResultList();
    if (games.isEmpty())
      return null;
    else
      return games.get(0);
  }

  public void deleteGame(Game p)
  {
    db.getTransaction().begin();
    db.remove(p);
    db.getTransaction().commit();
  }

  public void initializeDB() {
    this.reset();

    try {
      db.getTransaction().begin();

      // Create test users with different roles
      User admin = new User("admin", "admin");
      // Persist all users
      db.persist(admin);

      db.getTransaction().commit();
      System.out.println("The database has been initialized");
    } catch (Exception e) {
      e.printStackTrace();

    }
  }



  private void generateTestingData() {
    // create domain entities and persist them
  }



  public void close() {
    if (db != null && db.isOpen()) {
      db.close();
      System.out.println("EntityManager is closed");
    }
    // Do not close the EntityManagerFactory here to reuse it between operations
  }

    public boolean userExists(String user) {
        try {
            TypedQuery<User> query = db.createQuery(
                    "SELECT u FROM User u WHERE u.userName = :username", User.class);
            query.setParameter("username", user);
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            System.out.println("User not found");
            return false;
        }
    }
}
