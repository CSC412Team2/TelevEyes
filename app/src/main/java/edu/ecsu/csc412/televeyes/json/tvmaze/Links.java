package edu.ecsu.csc412.televeyes.json.tvmaze;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {

    @SerializedName("self")
    @Expose
    public Self self;
    @SerializedName("previousepisode")
    @Expose
    public Previousepisode previousepisode;

}
