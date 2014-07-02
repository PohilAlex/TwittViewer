package com.pohil.twittview.model;

import java.util.List;

public class TweetResponse {

    private List<Tweet> tweetList;
    private int count;
    private String nextResultsUrl;

    public boolean hasNextResults() {
        return nextResultsUrl != null;
    }

    public List<Tweet> getTweetList() {
        return tweetList;
    }

    public void setTweetList(List<Tweet> tweetList) {
        this.tweetList = tweetList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNextResultsUrl() {
        return nextResultsUrl;
    }

    public void setNextResultsUrl(String nextResultsUrl) {
        this.nextResultsUrl = nextResultsUrl;
    }
}
