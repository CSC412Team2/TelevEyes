package edu.ecu.csc412.televeyes.model;

/**
 * Created by bi00dsh0t on 11/23/16.
 */

public class Season {
    private int number;

    public Season(edu.ecu.csc412.televeyes.json.tvmaze.Season season) {
        number = season.getNumber();
    }

    public int getNumber() {
        return number;
    }
}
