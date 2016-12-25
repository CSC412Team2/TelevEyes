package edu.ecsu.csc412.televeyes.json.thetvdb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageContainer {
    @SerializedName("data")
    @Expose
    public List<Image> data = null;
    @SerializedName("errors")
    @Expose
    public Errors errors;
}
