
package edu.ecu.csc412.televeyes.json;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Schedule {

    @SerializedName("time")
    @Expose
    public String time;
    @SerializedName("days")
    @Expose
    public List<String> days = new ArrayList<String>();

}