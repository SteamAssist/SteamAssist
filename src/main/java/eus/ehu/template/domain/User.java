package eus.ehu.template.domain;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.LinkedList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Entity
@Table(name = "users")
public class User {


    @Id
    @Column(unique = true, nullable = false)
    private String userName;


    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts;

    @OneToOne
    @JoinColumn(name = "current_account_id")
    private Account currentAccount;

    public Account getCurrentAccount() {
        return currentAccount;
    }


    // Default constructor
    public User() {
    }


    // Constructor with fields
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.accounts = new ArrayList<>();
    }
    public User(ResultSet resultSet) throws SQLException {
        this.userName = resultSet.getString("username");
        this.password = resultSet.getString("password");
    }

    public void addAccount(Account account) {
        accounts.add(account);
        account.setUser(this);
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
