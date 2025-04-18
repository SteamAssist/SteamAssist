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

    @Column(name= "CountName", nullable = false)
    private String countName;

    public AccountSteamID() {}

    public AccountSteamID(String userName, String steamID, String countName) {
        this.userName = userName;
        this.steamID = steamID;
        this.countName = countName;
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

    public void setCountName(String countName) {this.countName = countName;}

    public String getCountName() {return countName;}


    @Override
    public String toString() {
        return "VinculateSteamID{" +
                "userName=" + userName +
                ", steamID='" + steamID +
                ", countName='" + countName + '\'' +
                '}';
    }
}