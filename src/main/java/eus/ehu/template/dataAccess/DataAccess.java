package eus.ehu.template.dataAccess;

import eus.ehu.template.configuration.Config;
import eus.ehu.template.domain.*;
import jakarta.persistence.*;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

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



  public void reset() {
    db.getTransaction().begin();
  db.createQuery("DELETE FROM User").executeUpdate();
    db.getTransaction().commit();
  }
  public User login(String username, String password, String role) {
    try {
      TypedQuery<User> query = db.createQuery(
              "SELECT u FROM User u WHERE u.userName = :username AND u.password = :password AND u.role = :role", User.class);
      query.setParameter("username", username);
      query.setParameter("password", password);
      query.setParameter("role", role);
      return query.getSingleResult();
    } catch (NoResultException e) {
      System.out.println("User not found");
      return null;
    }
  }

  public void signUp(String username, String password, String role) {
    db.getTransaction().begin();
    User user = new User(username, password, role);
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
    db.getTransaction().begin();
    db.persist(game);
    db.getTransaction().commit();
    System.out.println(game + " has been saved");
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
      User teacher1 = new User("john.smith", "pass123", "Teacher");
      User teacher2 = new User("mary.jones", "pass456", "Teacher");

      User student1 = new User("alex.brown", "student123", "Student");
      User student2 = new User("sarah.wilson", "student456", "Student");
      User student3 = new User("mike.davis", "student789", "Student");

      User admin1 = new User("admin.main", "admin123", "Administrative");
      User admin2 = new User("jane.doe", "admin456", "Administrative");

      // Persist all users
      db.persist(teacher1);
      db.persist(teacher2);
      db.persist(student1);
      db.persist(student2);
      db.persist(student3);
      db.persist(admin1);
      db.persist(admin2);

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

}
