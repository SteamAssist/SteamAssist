package eus.ehu.template.domain;

import java.util.Objects;

public class AccountSteamIDKey {

    public String userName;
    public String steamId;

    // No-argument constructor required by Hibernate
    public AccountSteamIDKey() {
    }

    public AccountSteamIDKey(String userName, String steamId) {
        this.userName = userName;
        this.steamId = steamId;
    }

    public int compareTo(AccountSteamIDKey other) {
        int userNameComparison = this.userName.compareTo(other.userName);
        if (userNameComparison != 0) {
            return userNameComparison;
        }
        return this.steamId.compareTo(other.steamId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountSteamIDKey that = (AccountSteamIDKey) o;
        return Objects.equals(userName, that.userName) && Objects.equals(steamId, that.steamId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, steamId);
    }
}
