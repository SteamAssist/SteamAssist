package eus.ehu.template.domain;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.LinkedList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements Iterable<Account>{


    @Id
    @Column(name = "name", unique = true, nullable = false)
    private String userName;


    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Account> accounts;

    // Default constructor
    public User() {
        System.out.println("User is created");
    }


    // Constructor with fields
    public User(String userName, String password) {

        System.out.println("User is created");
        this.userName = userName;
        this.password = password;
        accounts = new ArrayList<>();
    }
    public User(ResultSet resultSet) throws SQLException {
        System.out.println("User is created");
        this.userName = resultSet.getString("username");
        this.password = resultSet.getString("password");
        accounts = new ArrayList<>();
    }

    @Override
    public Iterator<Account> iterator() {
        return accounts.iterator();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void removeAccount(Account account) {
        accounts.remove(account);
    }

    public void nextAccount() {

    }

    public void previousAccount() {

    }

    // Getters and Setters
    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
