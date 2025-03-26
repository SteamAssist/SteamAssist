// GameId.java
package eus.ehu.template.domain;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GameId implements Serializable {
    private String name;
    private String link;

    public GameId() {}

    public GameId(String name, String link) {
        this.name = name;
        this.link = link;
    }

    // Getters and setters

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameId gameId = (GameId) o;
        return Objects.equals(name, gameId.name) && Objects.equals(link, gameId.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, link);
    }
}