package com.pohil.twittview.parser;

import com.pohil.twittview.api.TweetRequest;
import com.pohil.twittview.model.Tweet;
import com.pohil.twittview.model.TweetResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            response.count = Integer.parseInt(metadata.getString("count"));
            response.nextResultsUrl = metadata.getString("next_results");
            JSONArray statuses = json.getJSONArray("statuses");
            for (int i = 0; i < statuses.length(); i++) {
                JSONObject status = statuses.getJSONObject(i);
                Tweet tweet = new Tweet();
                tweet.text = status.getString("text");
                tweet.userPictureUrl = status.getJSONObject("user").getString("profile_image_url");
                tweetList.add(tweet);
            }
            response.tweetList = tweetList;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

}
