package com.pohil.twittview.ui;


import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.pohil.twittview.R;

public class StatusViewMonitor {

    ListView listView;
    TextView statusView;
    ProgressBar progressBar;

    public StatusViewMonitor(Activity layout) {
        listView = (ListView) layout.findViewById(R.id.tweet_list);
        statusView = (TextView) layout.findViewById(R.id.status_text);
        progressBar = (ProgressBar) layout.findViewById(R.id.load_progress);
    }

    public void emptyStatus() {
        listView.setVisibility(View.INVISIBLE);
        statusView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        statusView.setText(R.string.empty_text);
    }

    public void loadingStatus() {
        listView.setVisibility(View.INVISIBLE);
        statusView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        statusView.setText(R.string.load_text);
    }

    public void loadedStatus() {
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                listView.setVisibility(View.VISIBLE);
                statusView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        }, 64);

    }

}
