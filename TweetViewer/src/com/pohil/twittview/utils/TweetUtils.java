package com.pohil.twittview.utils;

import com.pohil.twittview.model.Segment;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static ArrayList<Segment> findRegex(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        ArrayList<Segment> result = new ArrayList<Segment>();
        while (matcher.find()) {
            Segment segment = new Segment();
            segment.setStart(matcher.start());
            segment.setEnd(matcher.end());
            segment.setContent(matcher.group());
            result.add(segment);
        }
        return result;
    }

}
