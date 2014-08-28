package com.pohil.twittview.model;


import com.google.gson.annotations.SerializedName;

public class TweetUrl {
    @SerializedName("url")
    private String url;

    @SerializedName("expanded_url")
    String expandedUrl;

    @SerializedName("display_url")
    String displayUrl;

    public String getUrl() {
        return url;
    }

    public String getExpandedUrl() {
        return expandedUrl;
    }

    public String getDisplayUrl() {
        return displayUrl;
    }
}
