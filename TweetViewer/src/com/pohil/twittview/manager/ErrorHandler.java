package com.pohil.twittview.manager;


import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

public class ErrorHandler {

    private List<ErrorObserver> observers = new ArrayList<ErrorObserver>();

    public void addObserver(ErrorObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ErrorObserver observer) {
        observers.remove(observer);
    }

    public void handleError(VolleyError error, Class request) {
     for (ErrorObserver observer : observers) {
         observer.onError(error, request);
     }
    }

    public interface ErrorObserver {
        void onError(VolleyError error, Class request);
    }
}
