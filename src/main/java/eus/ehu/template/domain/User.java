package eus.ehu.template.domain;


import jakarta.persistence.*;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eus.ehu.template.domain.Account;


@Entity
@Table(name = "users")
public class User {


    @Id
    @Column(unique = true, nullable = false)
    private String userName;


    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts = new ArrayList<>();

    private boolean accountAdded = false;

    private Long usingId;


    // Default constructor
    public User() {
    }


    // Constructor with fields
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    public User(ResultSet resultSet) throws SQLException {
        this.userName = resultSet.getString("username");
        this.password = resultSet.getString("password");
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

    public void addAccount(Long id) {
        Account account = new Account(this, id);
        accounts.add(account);
        if(!accountAdded) accountAdded = true;
    }
}
