package eus.ehu.template.dataAccess;

import eus.ehu.template.configuration.Config;
import eus.ehu.template.domain.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


/**
 * Implements the Data Access utility to the objectDb database
 */
public class DataAccess {

  protected EntityManager db;
  protected EntityManagerFactory emf;

  public DataAccess() {

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
