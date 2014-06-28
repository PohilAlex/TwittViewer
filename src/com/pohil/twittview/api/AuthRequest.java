package com.pohil.twittview.api;

import android.util.Base64;
import com.android.volley.*;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.pohil.twittview.parser.Parser;
import com.pohil.twittview.parser.TokenParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class AuthRequest extends JsonRequest<String> {

    private static final String AUTH_URL = "https://api.twitter.com/oauth2/token";
    private static final String AUTH_BODY = "grant_type=client_credentials";
    private static final String API_KEY = "5j5Wi5zqUdrLJpotkCafWi3Ic";
    private static final String API_SECRET = "C23EoRKcH9Y5dCXfTO9rnKpSWU7RfCWZY2Q1qU9DbK5ifPHu7m";

    TokenParser parser;

    public AuthRequest(Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, AUTH_URL, AUTH_BODY, listener, errorListener);
        parser = new TokenParser();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Basic " + createBasicCredentials(API_KEY, API_SECRET));
        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        return headers;
    }

    private String createBasicCredentials(String username, String password) {
        String credentials = username + ":" + password;
        return Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(parser.parse(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

}
