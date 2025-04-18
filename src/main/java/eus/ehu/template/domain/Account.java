package eus.ehu.template.domain;

import jakarta.persistence.*;
import eus.ehu.template.domain.AccountSteamIDKey;

@Entity
@Table(name = "accounts")
public class Account {

    @EmbeddedId
    private AccountSteamIDKey id;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String steam_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    // Other fields and methods...

    public String getId() {
        return steam_id;
    }

    public void setId(String id) {
        this.steam_id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Account (User user, String steam_id, String name) {
        this.user = user;
        this.steam_id = steam_id;
        this.id = new AccountSteamIDKey(user.getUserName(), steam_id);
        this.name = name;
    }

    // No-argument constructor (required by JPA)
    public Account() {
    }
}