package eus.ehu.template.domain;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import eus.ehu.template.domain.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class SteamKeyId implements Serializable {

    public Long id;
    @ManyToOne
    @JoinColumn(name = "user_user_name")
    public User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SteamKeyId() {
    }

    public SteamKeyId(User user, Long id) {
        this.user = user;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SteamKeyId that = (SteamKeyId) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user);
    }
}