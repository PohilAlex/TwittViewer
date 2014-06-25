package com.pohil.twittview;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.pohil.twittview.api.AuthRequest;
import org.json.JSONObject;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        AuthRequest request = new AuthRequest(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Toast.makeText(MainActivity.this, jsonObject.toString(), Toast.LENGTH_LONG).show();
                Log.d("TEST", jsonObject.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
                Log.d("TEST", volleyError.getMessage());
            }
        });
        App.requestQueue.add(request);
    }
}
