package eus.ehu.template.domain;

import com.google.gson.annotations.SerializedName;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
public class Game {

    String Name;
    @SerializedName("HLTB Time")
    String howlongtobeat;
    String Playtime;
    @EmbeddedId
    GameId id;
    String Link;
    String HLTBLink;
    int numMinutes;

    public Game(String name, String hltb, String playtime, String link, String hltblink, int minutes) {
        this.Name = name;
        this.howlongtobeat = hltb;
        this.Playtime = playtime;
        this.Link = link;
        this.HLTBLink = hltblink;
        this.numMinutes = minutes;

        id = new GameId(name, link);
    }
    public Game(String name){
        this.Name = name;
        this.howlongtobeat="";
        this.Link="";
        this.HLTBLink="";
        this.Playtime="";
        this.numMinutes=0;
    }
    public Game() {

    }

    public Game(String name, String hltb) {
    this.Name=name;
    this.howlongtobeat=hltb;
        this.Link="";
        this.HLTBLink="";
        this.Playtime="";
        this.numMinutes=0;
    }
    public Game(String name, String hltb, String playtime) {
        this.Name=name;
        this.howlongtobeat=hltb;
        this.Playtime=playtime;
        this.Link="";
        this.HLTBLink="";
        this.numMinutes=0;
    }

    public String getName() {
        return Name;
    }
    public String getHowlongtobeat() {
        return howlongtobeat;
    }
    public String getPlaytime() {
        return Playtime;
    }
    //create a getPlaytimeInt
    //this is the usual form of the string: "2h 30m"
    //but it can also be "2h" or "30m"
    //or "--"
    public int getPlaytimeInt() {
        if (Playtime.equals("--")) return 0;
        String[] parts = Playtime.split(" ");
        int hours = 0;
        int minutes = 0;
        for (String part : parts) {
            if (part.endsWith("h")) {
                hours = Integer.parseInt(part.substring(0, part.length() - 1));
            } else if (part.endsWith("m")) {
                minutes = Integer.parseInt(part.substring(0, part.length() - 1));
            }
        }
        return hours * 60 + minutes;
    }

    public String getLink() { return Link; }
    public String getHLTBLink() { return HLTBLink; }
    public int getNumMinutes() {return numMinutes;}


    @Override
    public String toString() {
        return String.format("%s - How long to beat? %sh - Playtime: %sh - link %s - hltblink %s - Number of minutes %d" , Name, howlongtobeat, Playtime, Link, HLTBLink, numMinutes);
    }
}