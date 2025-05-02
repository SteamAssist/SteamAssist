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
    public String getLink() { return Link; }
    public String getHLTBLink() { return HLTBLink; }
    public String getGameIdentifier() {
        String[] link = HLTBLink.split("/game/");
        return link[1];
    }
    public int getNumMinutes() {return numMinutes;}


    @Override
    public String toString() {
        return String.format("%s - How long to beat? %sh - Playtime: %sh - link %s - hltblink %s - Number of minutes %d" , Name, howlongtobeat, Playtime, Link, HLTBLink, numMinutes);
    }
}