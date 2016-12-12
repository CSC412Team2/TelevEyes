
package edu.ecu.csc412.televeyes.json.thetvdb;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Errors {

    @SerializedName("invalidFilters")
    @Expose
    public List<String> invalidFilters = null;
    @SerializedName("invalidLanguage")
    @Expose
    public String invalidLanguage;
    @SerializedName("invalidQueryParams")
    @Expose
    public List<String> invalidQueryParams = null;

}
