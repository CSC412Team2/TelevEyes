package edu.ecsu.csc412.televeyes.json.thetvdb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("fanart")
    @Expose
    public Integer fanart;
    @SerializedName("poster")
    @Expose
    public Integer poster;
    @SerializedName("season")
    @Expose
    public Integer season;
    @SerializedName("seasonwide")
    @Expose
    public Integer seasonwide;
    @SerializedName("series")
    @Expose
    public Integer series;
}
