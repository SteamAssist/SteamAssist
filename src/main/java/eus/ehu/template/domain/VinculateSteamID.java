package eus.ehu.template.domain;

import jakarta.persistence.*;

import java.sql.ResultSet  ;
import java.sql.SQLException;

@IdClass(VinculateSteamIDKey.class)
@Entity
@Table(name = "VinculateSteamIDs")
public class VinculateSteamID {
    @Id
    @ManyToOne
    @JoinColumn(name = "userName", referencedColumnName = "userName", nullable = false)
    private User userName;

    @Id
    @Column(nullable = false)
    private String SteamID;


    public VinculateSteamID() {}

    public VinculateSteamID(User userName, String password) {
        this.userName = userName;
        this.SteamID = password;
    }


    public User getUserName() {return userName; }

    public String getID() {return SteamID; }

    public void setID(String ID) {this.SteamID = ID; }

    public VinculateSteamID(ResultSet resultSet) throws SQLException {
        this.userName = new User(resultSet);
        this.SteamID = resultSet.getString("SteamID");
    }
    public String toString() {
        return "VinculateSteamID{" +
                "userName=" + userName +
                ", SteamID='" + SteamID + '\'' +
                '}';
    }

}
