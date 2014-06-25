package com.pohil.twittview;

import android.app.Application;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class App extends Application {

    public static RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(this);
    }
}
