package com.pohil.twittview.ui;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;
import com.pohil.twittview.App;
import com.pohil.twittview.R;
import com.pohil.twittview.model.Tweet;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TweetAdapter extends ArrayAdapter<Tweet> {

    private static final String TAG_REGEX = "#\\w+";
    private static final String URL_REGEX = "http[s]?://\\S+";

    LayoutInflater inflater;
    OnTagClickListener tagClickListener;
    OnUrlClickListener urlClickListener;

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
        selectTag(holder.tweetText);
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

    void selectTag(TextView textView) {
        String text = textView.getText().toString();
        SpannableString spannable = new SpannableString(text);

        for(Segment segment : findRegex(text, TAG_REGEX)) {
            spannable.setSpan(new ForegroundColorSpan(Color.GREEN), segment.start, segment.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ClickableTagSpan(segment.content), segment.start, segment.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        for(Segment segment : findRegex(text, URL_REGEX)) {
            spannable.setSpan(new ForegroundColorSpan(Color.BLUE), segment.start, segment.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ClickableUrlSpan(segment.content), segment.start, segment.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(spannable, TextView.BufferType.SPANNABLE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static ArrayList<Segment> findRegex(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        ArrayList<Segment> result = new ArrayList<Segment>();
        while (matcher.find()) {
            Segment segment = new Segment();
            segment.start = matcher.start();
            segment.end = matcher.end();
            segment.content = matcher.group();
            result.add(segment);
        }
        return result;
    }

    public void setOnTagClickListener(OnTagClickListener tagClickListener) {
        this.tagClickListener = tagClickListener;
    }

    public void setOnUrlClickListener(OnUrlClickListener urlClickListener) {
        this.urlClickListener = urlClickListener;
    }

    static class Segment {
        int start;
        int end;
        String content;
    }

    class ClickableTagSpan extends ClickableSpan {

        String tag;

        public ClickableTagSpan(String tag) {
            super();
            this.tag = tag;
        }

        @Override
        public void onClick(View widget) {
            if (tagClickListener != null) {
                tagClickListener.onTagClicked(tag);
            }
        }
    }

    class ClickableUrlSpan extends ClickableSpan {

        String url;

        public ClickableUrlSpan(String url) {
            super();
            this.url = url;
        }

        @Override
        public void onClick(View widget) {
            if (urlClickListener != null) {
                urlClickListener.onUrlClicked(url);
            }
        }
    }

    public interface OnTagClickListener {
        void onTagClicked(String tag);
    }

    public interface OnUrlClickListener {
        void onUrlClicked(String url);
    }

}
