package edu.ecu.csc412.televeyes.json;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Network {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("country")
    @Expose
    public Country country;

}
