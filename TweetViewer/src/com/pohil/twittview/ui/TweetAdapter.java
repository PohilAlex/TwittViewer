package com.pohil.twittview.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;
import com.pohil.twittview.App;
import com.pohil.twittview.R;
import com.pohil.twittview.model.Tweet;

import java.util.List;

public class TweetAdapter extends ArrayAdapter<Tweet> {

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
