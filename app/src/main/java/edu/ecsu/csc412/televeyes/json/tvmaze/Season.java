package edu.ecsu.csc412.televeyes.json.tvmaze;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Season {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("episodeOrder")
    @Expose
    private Integer episodeOrder;
    @SerializedName("premiereDate")
    @Expose
    private String premiereDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("network")
    @Expose
    private Network network;
    @SerializedName("webChannel")
    @Expose
    private Object webChannel;
    @SerializedName("image")
    @Expose
    private Object image;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("_links")
    @Expose
    private Links links;

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return The number
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @param number The number
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The episodeOrder
     */
    public Integer getEpisodeOrder() {
        return episodeOrder;
    }

    /**
     * @param episodeOrder The episodeOrder
     */
    public void setEpisodeOrder(Integer episodeOrder) {
        this.episodeOrder = episodeOrder;
    }

    /**
     * @return The premiereDate
     */
    public String getPremiereDate() {
        return premiereDate;
    }

    /**
     * @param premiereDate The premiereDate
     */
    public void setPremiereDate(String premiereDate) {
        this.premiereDate = premiereDate;
    }

    /**
     * @return The endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate The endDate
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * @return The network
     */
    public Network getNetwork() {
        return network;
    }

    /**
     * @param network The network
     */
    public void setNetwork(Network network) {
        this.network = network;
    }

    /**
     * @return The webChannel
     */
    public Object getWebChannel() {
        return webChannel;
    }

    /**
     * @param webChannel The webChannel
     */
    public void setWebChannel(Object webChannel) {
        this.webChannel = webChannel;
    }

    /**
     * @return The image
     */
    public Object getImage() {
        return image;
    }

    /**
     * @param image The image
     */
    public void setImage(Object image) {
        this.image = image;
    }

    /**
     * @return The summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary The summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * @return The links
     */
    public Links getLinks() {
        return links;
    }

    /**
     * @param links The _links
     */
    public void setLinks(Links links) {
        this.links = links;
    }

}
