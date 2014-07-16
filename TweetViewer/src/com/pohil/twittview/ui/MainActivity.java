package com.pohil.twittview.ui;

import android.app.Activity;
import android.os.Bundle;
import com.pohil.twittview.R;

public class MainActivity extends Activity  {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        buildContentFragment();
    }

    private void buildContentFragment() {
        TweetSearchFragment fragment = (TweetSearchFragment) getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            getFragmentManager().beginTransaction().add(R.id.container, new TweetSearchFragment()).commit();
        }
    }

}
