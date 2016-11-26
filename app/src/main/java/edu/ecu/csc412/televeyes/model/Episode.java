package edu.ecu.csc412.televeyes.model;

/**
 * Created by bi00dsh0t on 11/23/16.
 */

public class Episode {
    private String name;
    private int number;
    private int season;

    public Episode(edu.ecu.csc412.televeyes.json.Episode episode){
        name = episode.name;
        number = episode.number;
        season = episode.season;
    }

    public String getName(){
        return name;
    }

    public int getNumber(){
        return number;
    }

    public int getSeason(){
        return season;
    }
}
