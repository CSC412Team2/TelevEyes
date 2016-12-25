package edu.ecsu.csc412.televeyes.json.tvmaze;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Rating {

    @SerializedName("average")
    @Expose
    public double average;

}
