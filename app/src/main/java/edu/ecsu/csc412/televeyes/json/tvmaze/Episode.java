package edu.ecsu.csc412.televeyes.json.tvmaze;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Episode {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("season")
    @Expose
    public Integer season;
    @SerializedName("number")
    @Expose
    public Integer number;
    @SerializedName("airdate")
    @Expose
    public String airdate;
    @SerializedName("airtime")
    @Expose
    public String airtime;
    @SerializedName("airstamp")
    @Expose
    public String airstamp;
    @SerializedName("runtime")
    @Expose
    public Integer runtime;
    @SerializedName("image")
    @Expose
    public Image image;
    @SerializedName("summary")
    @Expose
    public String summary;
    @SerializedName("_links")
    @Expose
    public Links links;
}
