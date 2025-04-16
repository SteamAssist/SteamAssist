package eus.ehu.template.domain;

import jakarta.persistence.*;

@IdClass(AccountSteamIDkey.class)
@Entity
@Table(name = "AccountSteamIDs")
public class AccountSteamID {

    @Id
    @ManyToOne
    @JoinColumn(name = "userName", referencedColumnName = "userName", nullable = false)
    private User userName;

    @Id
    @Column(name = "SteamID", nullable = false)
    private String steamID;

    public AccountSteamID() {}

    public AccountSteamID(User userName, String steamID) {
        this.userName = userName;
        this.steamID = steamID;
    }

    public User getUserName() {
        return userName;
    }

    public void setUserName(User userName) {
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