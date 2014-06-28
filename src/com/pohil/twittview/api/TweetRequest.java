package com.pohil.twittview.api;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.pohil.twittview.model.TweetResponse;
import com.pohil.twittview.parser.TweetParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TweetRequest extends JsonRequest<TweetResponse> {

    private static final String SEARCH_URL = "https://api.twitter.com/1.1/search/tweets.json";

    String token;

    public TweetRequest(String searchTag, String token, Response.Listener<TweetResponse> listener, Response.ErrorListener errorListener) {
        super(Method.GET, buildSearchUrl(searchTag), null, listener, errorListener);
        this.token = token;
    }

    private static String buildSearchUrl(String searchTag) {
        return SEARCH_URL + "?q=" + searchTag;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + token);
        return headers;
    }

    @Override
    protected Response<TweetResponse> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            TweetParser parser = new TweetParser();
            return Response.success(parser.parse(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }
}
