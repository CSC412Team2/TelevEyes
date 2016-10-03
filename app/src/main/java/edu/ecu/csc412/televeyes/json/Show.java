
package edu.ecu.csc412.televeyes.json;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Show {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("language")
    @Expose
    public String language;
    @SerializedName("genres")
    @Expose
    public List<Object> genres = new ArrayList<Object>();
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("runtime")
    @Expose
    public Integer runtime;
    @SerializedName("premiered")
    @Expose
    public String premiered;
    @SerializedName("schedule")
    @Expose
    public Schedule schedule;
    @SerializedName("rating")
    @Expose
    public Rating rating;
    @SerializedName("weight")
    @Expose
    public Integer weight;
    @SerializedName("network")
    @Expose
    public Network network;
    @SerializedName("webChannel")
    @Expose
    public Object webChannel;
    @SerializedName("externals")
    @Expose
    public Externals externals;
    @SerializedName("image")
    @Expose
    public Image image;
    @SerializedName("summary")
    @Expose
    public String summary;
    @SerializedName("updated")
    @Expose
    public Integer updated;
    @SerializedName("_links")
    @Expose
    public Links links;

}
