package eus.ehu.template.domain;

import java.io.Serializable;
import java.util.Objects;

public class AccountSteamIDkey implements Serializable {

    private String userName;
    private String steamID;

    public AccountSteamIDkey() {}

    public AccountSteamIDkey(String userName, String steamID) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountSteamIDkey that = (AccountSteamIDkey) o;
        return Objects.equals(userName, that.userName) &&
                Objects.equals(steamID, that.steamID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, steamID);
    }
}