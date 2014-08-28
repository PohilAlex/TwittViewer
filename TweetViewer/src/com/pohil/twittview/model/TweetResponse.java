package com.pohil.twittview.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TweetResponse {

    @SerializedName("statuses")
    private List<Tweet> tweetList;

    @SerializedName("search_metadata")
    private MetaData metaData;

    public boolean hasNextResults() {
        return metaData.nextResultsUrl != null;
    }

    public List<Tweet> getTweetList() {
        return tweetList;
    }

    public void setTweetList(List<Tweet> tweetList) {
        this.tweetList = tweetList;
    }

    public int getCount() {
        return metaData.count;
    }

    public void setCount(int count) {
        this.metaData.count = count;
    }

    public String getNextResultsUrl() {
        return metaData.nextResultsUrl;
    }

    public void setNextResultsUrl(String nextResultsUrl) {
        this.metaData.nextResultsUrl = nextResultsUrl;
    }


    class MetaData {
        @SerializedName("count")
        private int count;

        @SerializedName("next_results")
        private String nextResultsUrl;
    }

}
