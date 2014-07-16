package com.pohil.twittview.ui;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;
import com.pohil.twittview.App;
import com.pohil.twittview.R;
import com.pohil.twittview.model.Segment;
import com.pohil.twittview.model.Tweet;
import com.pohil.twittview.utils.TweetUtils;

import java.util.List;

public class TweetAdapter extends ArrayAdapter<Tweet> {

    public static final String TAG_REGEX = "#\\w+";
    public static final String URL_REGEX = "http[s]?://\\S+";

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
        holder.tweetText.setText(tweet.getText());
        holder.nameText.setText(tweet.getUserName());
        holder.networkImageView.setImageUrl(tweet.getUserPictureUrl(), App.getNetworkManager().getImageLoader());
        holder.networkImageView.setDefaultImageResId(R.drawable.default_icon);
        holder.networkImageView.setErrorImageResId(R.drawable.default_icon);
        setupSpannable(holder.tweetText);
        return layout;
    }

    class ViewHolder {
        TextView tweetText;
        TextView nameText;
        NetworkImageView networkImageView;

        public ViewHolder(View layout) {
            tweetText = (TextView) layout.findViewById(R.id.tweet_text);
            nameText = (TextView) layout.findViewById(R.id.tweet_author_name);
            networkImageView = (NetworkImageView) layout.findViewById(R.id.tweet_author_icon);
        }
    }

    void setupSpannable(TextView textView) {
        String text = textView.getText().toString();
        SpannableString spannable = new SpannableString(text);

        for(Segment segment : TweetUtils.findRegex(text, TAG_REGEX)) {
            //spannable.setSpan(new ForegroundColorSpan(Color.GREEN), segment.start, segment.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ClickableTagSpan(segment.getContent()), segment.getStart(), segment.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        for(Segment segment : TweetUtils.findRegex(text, URL_REGEX)) {
            //spannable.setSpan(new ForegroundColorSpan(Color.BLUE), segment.start, segment.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ClickableUrlSpan(segment.getContent()), segment.getStart(), segment.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(spannable, TextView.BufferType.SPANNABLE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setOnTagClickListener(OnTagClickListener tagClickListener) {
        this.tagClickListener = tagClickListener;
    }

    public void setOnUrlClickListener(OnUrlClickListener urlClickListener) {
        this.urlClickListener = urlClickListener;
    }

    class ClickableTagSpan extends ClickableSpan {

        String tag;

        public ClickableTagSpan(String tag) {
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
