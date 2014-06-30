package com.pohil.twittview;

import android.app.Application;
import com.pohil.twittview.manager.NetworkManager;
import com.pohil.twittview.manager.PreferenceManager;


public class App extends Application {

    private static PreferenceManager preferenceManager = new PreferenceManager();
    private static NetworkManager networkManager = new NetworkManager();

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
}
