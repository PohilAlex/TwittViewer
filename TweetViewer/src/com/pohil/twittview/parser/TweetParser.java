package com.pohil.twittview.parser;

import com.pohil.twittview.model.Tweet;
import com.pohil.twittview.model.TweetResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 26.06.14.
 */
public class TweetParser implements Parser<TweetResponse>{

    @Override
    public TweetResponse parse(String data) {
        TweetResponse response = new TweetResponse();
        List<Tweet> tweetList = new ArrayList<Tweet>();
        try {
            JSONObject json = new JSONObject(data);
            JSONObject metadata = json.getJSONObject("search_metadata");
            response.setCount(metadata.getInt("count"));
            if  (metadata.has("next_results")) {
                response.setNextResultsUrl(metadata.getString("next_results"));
            }
            JSONArray statuses = json.getJSONArray("statuses");
            for (int i = 0; i < statuses.length(); i++) {
                JSONObject status = statuses.getJSONObject(i);
                Tweet tweet = new Tweet();
                tweet.setText(status.getString("text"));
                JSONObject user = status.getJSONObject("user");
                tweet.setUserPictureUrl(user.getString("profile_image_url"));
                tweet.setUserName(user.getString("name"));
                tweetList.add(tweet);
            }
            response.setTweetList(tweetList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

}
