package com.pohil.twittview.api;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.pohil.twittview.App;

public class BaseErrorListener implements Response.ErrorListener {

    private Class request;

    public BaseErrorListener(Class request) {
        this.request = request;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        App.getErrorHandler().handleError(error, request);
    }
}
