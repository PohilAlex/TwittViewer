package com.pohil.twittview.parser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alex on 26.06.2014.
 */
public class TokenParser implements Parser<String>{

    @Override
    public String parse(String data) {
        String token = null;
        try {
            JSONObject json = new JSONObject(data);
            token = json.getString("access_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return token;

    }
}
