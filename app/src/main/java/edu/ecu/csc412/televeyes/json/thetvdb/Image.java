
package edu.ecu.csc412.televeyes.json.thetvdb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("keyType")
    @Expose
    public String keyType;
    @SerializedName("subKey")
    @Expose
    public String subKey;
    @SerializedName("fileName")
    @Expose
    public String fileName;
    @SerializedName("languageId")
    @Expose
    public Integer languageId;
    @SerializedName("resolution")
    @Expose
    public String resolution;
    @SerializedName("ratingsInfo")
    @Expose
    public RatingsInfo ratingsInfo;
    @SerializedName("thumbnail")
    @Expose
    public String thumbnail;
}
