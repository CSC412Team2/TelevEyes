package edu.ecu.csc412.televeyes.json.tvmaze;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Country {

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("code")
    @Expose
    public String code;
    @SerializedName("timezone")
    @Expose
    public String timezone;

}
