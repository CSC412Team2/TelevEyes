package edu.ecsu.csc412.televeyes.json.tvmaze;

/**
 * Created by joshu on 10/9/2016.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ShowContainer {
    @Expose
    @SerializedName("score")
    public double score;

    @Expose
    @SerializedName("show")
    public Show show;
}
