package com.pohil.twittview.manager;

import android.app.ActivityManager;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.pohil.twittview.utils.BitmapLruCache;

public class NetworkManager {

    RequestQueue requestQueue;
    ImageLoader imageLoader;

    public void init(Context context) {
        requestQueue = Volley.newRequestQueue(context);

        int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        // Use 1/8th of the available memory for this memory cache.
        int cacheSize = 1024 * 1024 * memClass / 8;
        imageLoader = new ImageLoader(requestQueue, new BitmapLruCache(cacheSize));
    }

    public void send(Request request) {
        if (request != null) {
            requestQueue.add(request);
        }
    }

    public void cancel(Object tag) {
        requestQueue.cancelAll(tag);

    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
