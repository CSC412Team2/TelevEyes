package edu.ecsu.csc412.televeyes.model;

import java.util.List;

import edu.ecsu.csc412.televeyes.json.tvmaze.Series;
import edu.ecsu.csc412.televeyes.json.tvmaze.ShowContainer;

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
    private String network;

    private String imdb;
    private String thetvdb;


    //TODO add suppport for multiple images
    private String image;
    private String largeImage;

    private Show() {
    }

    public Show(edu.ecsu.csc412.televeyes.json.tvmaze.Show show) {
        this.id = show.id;
        this.name = show.name;
        this.summary = show.summary;
        this.schedule = new Schedule(show.schedule);
        this.rating = show.rating.average;
        this.genres = show.genres;

        this.imdb = show.externals.imdb;
        this.thetvdb = show.externals.thetvdb;

        if (show.image != null) {
            if (show.image.medium != null) {
                this.image = show.image.medium;
            }
            if (show.image.original != null) {
                this.largeImage = show.image.original;
            }
        }
        this.network = show.network.name;
    }

    public Show(ShowContainer container) {
        this.id = container.show.id;
        this.name = container.show.name;
        this.summary = container.show.summary;
        this.schedule = new Schedule(container.show.schedule);
        this.numberOfEpisodes = -1;
        this.rating = container.score;
        this.genres = container.show.genres;

        this.imdb = container.show.externals.imdb;
        this.thetvdb = container.show.externals.thetvdb;

        if (container.show.image != null) {
            if (container.show.image.medium != null) {
                this.image = container.show.image.medium;
            }
            if (container.show.image.original != null) {
                this.largeImage = container.show.image.original;
            }
        }

        if (container.show.network != null)
            this.network = container.show.network.name;
    }

    public Show(Series series) {
        this.id = series.show.id;
        this.name = series.show.name;
        this.summary = series.summary;
        this.schedule = new Schedule(series.show.schedule);
        this.rating = series.show.rating.average;
        this.genres = series.show.genres;
        this.network = series.show.network.name;

        if (series.show.image != null) {
            if (series.show.image.medium != null) {
                this.image = series.show.image.medium;
            }
            if (series.show.image.original != null) {
                this.largeImage = series.show.image.original;
            }
        }
    }

    public List<String> getGenres() {
        return genres;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public int getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public double getRating() {
        return rating;
    }

    public String getImage() {
        return image;
    }

    public String getLargeImage() {
        return largeImage;
    }

    public String getNetwork() {
        return network;
    }

    public String getImdb() {
        return imdb;
    }

    public String getThetvdb() {
        return thetvdb;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        final Show show = (Show) other;

        if (this.getId() == show.getId()) {
            return true;
        } else {
            return false;
        }
    }

    public class Episode {
        private String name;
        private
        String summary;
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

    public class Schedule {
        private String showTime;
        private List<String> days;

        private Schedule() {
        }

        public Schedule(String showTime, List<String> days) {
            this.showTime = showTime;
            this.days = days;
        }

        public Schedule(edu.ecsu.csc412.televeyes.json.tvmaze.Schedule schedule) {
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
