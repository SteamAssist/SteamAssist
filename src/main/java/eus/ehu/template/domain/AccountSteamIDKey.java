package eus.ehu.template.domain;

public class AccountSteamIDKey {

    public String userName;
    public String steamId;

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


}
