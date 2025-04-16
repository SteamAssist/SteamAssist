package eus.ehu.template.domain;

import java.io.Serializable;
import java.util.Objects;

public class VinculateSteamIDKey implements Serializable {
    private User userName;
    private String ID;

    public VinculateSteamIDKey() {}

    public VinculateSteamIDKey(User userName, String ID) {
        this.userName = userName;
        this.ID = ID;
    }

    // Getters, setters, equals, and hashCode
    public User getUserName() {
        return userName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VinculateSteamIDKey that = (VinculateSteamIDKey) o;
        return Objects.equals(userName, that.userName) && Objects.equals(ID, that.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, ID);
    }
}
