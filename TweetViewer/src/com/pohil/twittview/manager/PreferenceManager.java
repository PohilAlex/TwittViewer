package com.pohil.twittview.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by alex on 27.06.14.
 */
public class PreferenceManager {

    private static final String AUTH_TOKEN_KEY = "AUTH_TOKEN_KEY";

    SharedPreferences pref;

    public void init(Context context) {
        pref = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setAuthToken(String token) {
        pref.edit().putString(AUTH_TOKEN_KEY, token).commit();
    }

    public String getAuthToken() {
        return pref.getString(AUTH_TOKEN_KEY, null);
    }
}
