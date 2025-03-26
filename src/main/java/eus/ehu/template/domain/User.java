package eus.ehu.template.domain;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.LinkedList;

import java.sql.ResultSet;
import java.sql.SQLException;

@Entity
@Table(name = "users")
public class User {


    @Id
    @Column(unique = true, nullable = false)
    private String userName;


    @Column(nullable = false)
    private String password;


    @Column(nullable = false)
    private String role;

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
}
