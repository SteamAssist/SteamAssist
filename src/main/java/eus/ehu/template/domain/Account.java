package eus.ehu.template.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {

    @EmbeddedId
    private SteamKeyId id;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long steam_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Other fields and methods...

    public Long getId() {
        return steam_id;
    }

    public void setId(Long id) {
        this.steam_id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Account (User user, Long steam_id) {
        this.user = user;
        this.steam_id = steam_id;
        this.id = new SteamKeyId(user, steam_id);
    }

    // No-argument constructor (required by JPA)
    public Account() {
    }
}