package com.pohil.twittview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.pohil.twittview.api.AuthRequest;
import com.pohil.twittview.api.TweetRequest;
import com.pohil.twittview.manager.NetworkManager;
import com.pohil.twittview.model.Tweet;
import com.pohil.twittview.model.TweetResponse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    List<Tweet> tweetList = new ArrayList<Tweet>();
    TweetAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ListView listView = (ListView) findViewById(R.id.tweet_list);
        adapter = new TweetAdapter(this, tweetList);
        listView.setAdapter(adapter);

        String token = App.getPreferenceManager().getAuthToken();
        if (token != null) {
            App.getNetworkManager().send(createTweetRequest(token));
        } else {
            App.getNetworkManager().send(createAuthRequest());
        }
    }

    private AuthRequest createAuthRequest() {
        return new AuthRequest(new Response.Listener<String>() {
            @Override
            public void onResponse(String token) {
                Toast.makeText(MainActivity.this, token, Toast.LENGTH_LONG).show();
                Log.d("TEST", token);
                App.getPreferenceManager().setAuthToken(token);
                App.getNetworkManager().send(createTweetRequest(token));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
                Log.d("TEST", volleyError.getMessage());
            }
        });
    }

    private TweetRequest createTweetRequest(String token) {
        return new TweetRequest("android", token, new Response.Listener<TweetResponse>() {
            @Override
            public void onResponse(TweetResponse tweetResponse) {
                Toast.makeText(MainActivity.this, "Total " + tweetResponse.count, Toast.LENGTH_LONG).show();
                for (Tweet tweet : tweetResponse.tweetList) {
                    Log.d("TEST", tweet.text);
                }
                tweetList.addAll(tweetResponse.tweetList);
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
                Log.d("TEST", "Error");
            }
        });
    }

    class TweetAdapter extends ArrayAdapter<Tweet> {

        LayoutInflater inflater;

        public TweetAdapter(Context context, List<Tweet> objects) {
            super(context, 0, objects);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View layout = convertView;
            ViewHolder holder;
            if (layout == null) {
                layout = inflater.inflate(R.layout.tweet_item, parent, false);
                holder = new ViewHolder(layout);
                layout.setTag(holder);
            } else {
                holder = (ViewHolder) layout.getTag();
            }
            Tweet tweet = getItem(position);
            holder.tweetText.setText(tweet.text);
            holder.networkImageView.setImageUrl(tweet.userPictureUrl, App.getNetworkManager().getImageLoader());
            return layout;
        }

        class ViewHolder {
            TextView tweetText;
            NetworkImageView networkImageView;

            public ViewHolder(View layout) {
                tweetText = (TextView) layout.findViewById(R.id.tweet_text);
                networkImageView = (NetworkImageView) layout.findViewById(R.id.tweet_author_icon);
            }
        }


    }
}
