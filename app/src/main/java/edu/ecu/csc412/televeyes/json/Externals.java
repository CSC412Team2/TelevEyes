package edu.ecu.csc412.televeyes.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Externals {

    @SerializedName("tvrage")
    @Expose
    public Object tvrage;
    @SerializedName("thetvdb")
    @Expose
    public Integer thetvdb;
    @SerializedName("imdb")
    @Expose
    public String imdb;

}
