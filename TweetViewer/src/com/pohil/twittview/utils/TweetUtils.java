package com.pohil.twittview.utils;

public class TweetUtils {

    public static String createTag(String request) {
        if (!request.startsWith("#")) {
            return "#" + request;
        }  else {
            return request;
        }
    }

    public static String parseTag(String tag) {
        if (tag.startsWith("#")) {
            return tag.substring(1);
        }  else {
            return tag;
        }
    }

}
