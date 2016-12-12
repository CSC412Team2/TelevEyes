
package edu.ecu.csc412.televeyes.json.thetvdb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageSummary {

    @SerializedName("data")
    @Expose
    public Data data;

}
