package com.pohil.twittview.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Tweet {
    @SerializedName("text")
    private String text;

    @SerializedName("user")
    private User user;

    @SerializedName("entities")
    private Entity entity;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserPictureUrl() {
        return user.userPictureUrl;
    }

    public void setUserPictureUrl(String userPictureUrl) {
        user.userPictureUrl = userPictureUrl;
    }

    public String getUserName() {
        return user.userName;
    }

    public void setUserName(String userName) {
        user.userName = userName;
    }

    class User {
        @SerializedName("profile_image_url")
        private String userPictureUrl;

        @SerializedName("name")
        private String userName;
    }

    class Entity {
        @SerializedName("urls")
        List<TweetUrl> urlList;
    }
}
