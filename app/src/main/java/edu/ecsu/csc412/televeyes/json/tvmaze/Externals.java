package edu.ecsu.csc412.televeyes.json.tvmaze;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Externals {

    @SerializedName("tvrage")
    @Expose
    public String tvrage;
    @SerializedName("thetvdb")
    @Expose
    public String thetvdb;
    @SerializedName("imdb")
    @Expose
    public String imdb;

}
