package com.pohil.twittview;

import android.app.Application;
import com.pohil.twittview.manager.ErrorHandler;
import com.pohil.twittview.manager.NetworkManager;
import com.pohil.twittview.manager.PreferenceManager;


public class App extends Application {

    private static PreferenceManager preferenceManager = new PreferenceManager();
    private static NetworkManager networkManager = new NetworkManager();
    private static ErrorHandler errorHandler = new ErrorHandler();

    @Override
    public void onCreate() {
        super.onCreate();
        preferenceManager.init(this);
        networkManager.init(this);
    }

    public static PreferenceManager getPreferenceManager() {
        return preferenceManager;
    }

    public static NetworkManager getNetworkManager() {
        return networkManager;
    }

    public static ErrorHandler getErrorHandler() {
        return errorHandler;
    }
}
