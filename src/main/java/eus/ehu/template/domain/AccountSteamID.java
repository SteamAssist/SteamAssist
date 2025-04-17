package eus.ehu.template.domain;

import jakarta.persistence.*;

@IdClass(AccountSteamIDkey.class)
@Entity
@Table(name = "AccountSteamIDs")
public class AccountSteamID {

    //This should be @ManyToOne because is a foreign key
    @Id
    @Column(name = "userName", nullable = false)
    private String userName;

    @Id
    @Column(name = "SteamID", nullable = false)
    private String steamID;

    public AccountSteamID() {}

    public AccountSteamID(String userName, String steamID) {
        this.userName = userName;
        this.steamID = steamID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSteamID() {
        return steamID;
    }

    public void setSteamID(String steamID) {
        this.steamID = steamID;
    }

    @Override
    public String toString() {
        return "VinculateSteamID{" +
                "userName=" + userName +
                ", steamID='" + steamID + '\'' +
                '}';
    }
}