
package edu.ecu.csc412.televeyes.json.thetvdb;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageContainer {
    @SerializedName("data")
    @Expose
    public List<Image> data = null;
    @SerializedName("errors")
    @Expose
    public Errors errors;
}
