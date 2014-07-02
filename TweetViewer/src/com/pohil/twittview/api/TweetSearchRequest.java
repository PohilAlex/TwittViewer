package com.pohil.twittview.api;

import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.pohil.twittview.model.TweetResponse;
import com.pohil.twittview.parser.TweetParser;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TweetSearchRequest extends JsonRequest<TweetResponse> {

    public static final String REQUEST_TAG = "TweetSearchRequest";
    private static final String SEARCH_URL = "https://api.twitter.com/1.1/search/tweets.json";

    String token;

    public TweetSearchRequest(TweetSearchBuilder builder, Response.Listener<TweetResponse> listener) {
        super(Method.GET, buildSearchUrl(builder), null, listener, new BaseErrorListener(TweetSearchRequest.class));
        this.token = builder.token;
        this.setTag(REQUEST_TAG);
    }

    private static String buildSearchUrl(TweetSearchBuilder builder) {
        if (builder.nextPage != null) {
            return SEARCH_URL + builder.nextPage;
        } else {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("q", builder.hashTag));
            String paramString = URLEncodedUtils.format(params, "utf-8");
            return SEARCH_URL + "?" + paramString;
        }
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

    public static TweetSearchBuilder createSearchBuilder() {
        return new TweetSearchBuilder();
    }

    public static class TweetSearchBuilder {
        String token;
        String hashTag;
        String nextPage;

        public TweetSearchBuilder setToken(String token) {
            this.token = token;
            return this;
        }

        public String getToken() {
            return token;
        }

        public TweetSearchBuilder setHashTag(String hashTag) {
            this.hashTag = hashTag;
            return this;
        }

        public TweetSearchBuilder setNextPage(String nextPage) {
            this.nextPage = nextPage;
            return this;
        }

        public boolean isNextPageSearch() {
            return nextPage != null;
        }
    }

}
