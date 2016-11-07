package edu.ecu.csc412.televeyes.model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import edu.ecu.csc412.televeyes.json.Schedule;
import edu.ecu.csc412.televeyes.json.Series;
import edu.ecu.csc412.televeyes.json.ShowContainer;

/**
 * Created by bi00dsh0t on 10/31/16.
 */

public class Show {
    private int id;
    private String name;
    private String summary;
    private Schedule schedule;
    private int numberOfEpisodes;
    private double rating;
    private List<String> genres;

    //TODO add suppport for multiple images
    private String image;

    private Show(){}

    public Show(int id, String name, String summary, edu.ecu.csc412.televeyes.json.Schedule schedule, int episodes, double rating, String image, List<String> genres){
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.schedule = new Schedule(schedule);
        this.numberOfEpisodes = episodes;
        this.rating = rating;
        this.image = image;
        this.genres = genres;
    }

    public Show(edu.ecu.csc412.televeyes.json.Show show){
        this.id = show.id;
        this.name = show.name;
        this.summary = show.summary;
        this.schedule = new Schedule(show.schedule);
        this.rating = show.rating.average;
        if(show.image.medium != null) {
            this.image = show.image.medium;
        } else {
            this.image = show.image.original;
        }
    }

    public Show(ShowContainer container){
        this.id = container.show.id;
        this.name = container.show.name;
        this.summary = container.show.summary;
        this.schedule = new Schedule(container.show.schedule);
        this.numberOfEpisodes = -1;
        this.rating = container.score;
        this.genres = container.show.genres;

        if(container.show.image.medium != null) {
            this.image = container.show.image.medium;
        } else {
            this.image = container.show.image.original;
        }
    }

    public Show(Series series){
        this.id = series.show.id;
        this.name = series.show.name;
        this.summary = series.summary;
        this.schedule = new Schedule(series.show.schedule);
        this.rating = series.show.rating.average;

        if(series.show.image != null)
        if(series.show.image.medium != null) {
            this.image = series.show.image.medium;
        } else {
            this.image = series.show.image.original;
        }
    }

    public List<String> getGenres(){
        return genres;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getSummary(){
        return summary;
    }

    public Schedule getSchedule(){
        return schedule;
    }

    public int getNumberOfEpisodes(){
        return numberOfEpisodes;
    }

    public double getRating(){
        return rating;
    }

    public String getImage(){
        return image;
    }

    public class Episode{
        private String name;
        private String summary;
        private double rating;
        private long airDate;

        public String getName() {
            return name;
        }

        public String getSummary() {
            return summary;
        }

        public double getRating() {
            return rating;
        }

        public long getAirDate() {
            return airDate;
        }
    }

    public class Schedule{
        private String showTime;
        private List<String> days;

        private Schedule(){}

        public Schedule(String showTime, List<String> days){
            this.showTime = showTime;
            this.days = days;
        }

        public Schedule(edu.ecu.csc412.televeyes.json.Schedule schedule){
            this.showTime = schedule.time;
            this.days = schedule.days;
        }

        public String getShowTime() {
            return showTime;
        }

        public List<String> getDays() {
            return days;
        }
    }
}
