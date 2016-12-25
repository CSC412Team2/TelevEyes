package edu.ecsu.csc412.televeyes.json.tvmaze;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Schedule {

    @SerializedName("time")
    @Expose
    public String time;
    @SerializedName("days")
    @Expose
    public List<String> days = new ArrayList<String>();

}
