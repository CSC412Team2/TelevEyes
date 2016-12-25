package edu.ecsu.csc412.televeyes.json.thetvdb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RatingsInfo {

    @SerializedName("average")
    @Expose
    public double average;
    @SerializedName("count")
    @Expose
    public Integer count;

}
