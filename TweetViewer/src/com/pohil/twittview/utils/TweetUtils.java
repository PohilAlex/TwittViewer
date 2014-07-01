package com.pohil.twittview.utils;

public class TweetUtils {

    public static String createTag(String request) {
        if (!request.startsWith("#")) {
            return "#" + request;
        }  else {
            return request;
        }
    }

}
