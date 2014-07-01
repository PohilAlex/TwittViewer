package com.pohil.twittview.utils;

import com.pohil.twittview.R;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

public class VolleyErrorHelper {

    /**
     * Returns appropriate message which is to be displayed to the user
     * against the specified error object.
     *
     * @param error
     * @param context
     * @return
     */
    public static final String ERRORS = "errors";
    public static final String MESSAGE = "message";

    public static String getMessage(Object error, Context context) {
        if (error instanceof TimeoutError) {
            return context.getResources().getString(R.string.generic_server_down);
        } else if (isServerProblem(error)) {
            return handleServerError(error);
        } else if (isNetworkProblem(error)) {
            return context.getResources().getString(R.string.no_internet_msg);
        }
        return context.getResources().getString(R.string.generic_error);
    }

    public static boolean isAuthProblem(Object error) {
        return error instanceof AuthFailureError;
    }

    /**
     * Determines whether the error is related to network
     *
     * @param error
     * @return
     */
    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError) || (error instanceof NoConnectionError);
    }

    /**
     * Determines whether the error is related to server
     *
     * @param error
     * @return
     */
    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }

    /**
     * Handles the server error, tries to determine whether to show a stock message or to
     * show a message retrieved from the server.
     *
     * @param err
     * @return
     */
    private static String handleServerError(Object err) {
        VolleyError error = (VolleyError) err;
        NetworkResponse response = error.networkResponse;
        try {
            JSONObject result = new JSONObject(new String(response.data));
            if (result.has(ERRORS)) {
                JSONArray errors = result.getJSONArray(ERRORS);
                return errors.getJSONObject(0).getString(MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return error.getMessage();
    }

}

