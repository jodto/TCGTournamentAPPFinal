package com.factory.rimon.tcgtournamentapp.BE;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rimon on 4/29/2016.
 */
public class BETournament implements Serializable{

    String id;
    String title;
    String date;
    String location;
    String edition;
    String rel;
    String format;
    String entryTime;
    String startTime;
    String price;
    String info;
    ArrayList<BEPlayer> players;
    String mongoId;

    public BETournament(String id
            , String title
            , String location
            , String date
            , String format
            , String edition
            , String rel
            , String price
            , String entryTime
            , String startTime
            , String info
            , ArrayList<BEPlayer> players
            , String mongoId)
    {
        this.id = id;
        this.title = title;
        this.date = date;
        this.location = location;
        this.format = format;
        this.edition = edition;
        this.rel = rel;
        this.location = format;
        this.entryTime = entryTime;
        this.startTime = startTime;
        this.price = price;
        this.info = info;
        this.players = players;
        this.mongoId = mongoId;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getEdition() {
        return edition;
    }

    public String getRel() {
        return rel;
    }

    public String getFormat() {
        return format;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getPrice() {
        return price;
    }

    public String getInfo() {
        return info;
    }

    public ArrayList<BEPlayer> getPlayers() {
        return players;
    }

    public String getMongoId() {
        return mongoId;
    }

}
