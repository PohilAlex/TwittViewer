package com.pohil.twittview.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.pohil.twittview.App;
import com.pohil.twittview.R;
import com.pohil.twittview.api.AuthRequest;
import com.pohil.twittview.api.TweetSearchRequest;
import com.pohil.twittview.model.Tweet;
import com.pohil.twittview.model.TweetResponse;
import com.pohil.twittview.utils.TweetUtils;
import com.pohil.twittview.utils.VolleyErrorHelper;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements OnRefreshListener, LoadMoreListView.OnLoadMoreListener {

    PullToRefreshLayout pullToRefreshLayout;
    LoadMoreListView listView;

    List<Tweet> tweetList = new ArrayList<Tweet>();
    TweetAdapter adapter;
    StatusViewMonitor statusMonitor;
    TweetResponse tweetResponse;
    String searchTag = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        statusMonitor = new StatusViewMonitor(this);
        statusMonitor.emptyStatus();

        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.ptr_layout);
        ActionBarPullToRefresh.from(this)
            .allChildrenArePullable()
            .listener(this)
            .setup(pullToRefreshLayout);

        listView = (LoadMoreListView) findViewById(R.id.tweet_list);
        adapter = new TweetAdapter(this, tweetList);
        listView.setAdapter(adapter);
        listView.setOnLoadMoreListener(this);

        String token = App.getPreferenceManager().getAuthToken();
        if (token == null) {
            App.getNetworkManager().send(createAuthRequest());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem searchViewItem = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                boolean isLoading = TextUtils.isEmpty(searchTag) && newText.length() == 1;
                App.getNetworkManager().cancel(TweetSearchRequest.REQUEST_TAG);
                searchTag = newText;
                if (!TextUtils.isEmpty(newText)) {
                    App.getNetworkManager().send(createTweetRequest(createSearchBuilder()));
                    if (isLoading) {
                        statusMonitor.loadingStatus();
                    }
                } else {
                    statusMonitor.emptyStatus();
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public void onRefreshStarted(View view) {
        App.getNetworkManager().send(createTweetRequest(createSearchBuilder()));
    }

    @Override
    public void onLoadMore() {
        if (tweetResponse.hasNextResults()) {
            App.getNetworkManager().send(createTweetRequest(createSearchNextBuilder()));
        }  else {
            listView.onLoadMoreComplete();
        }
    }

    private TweetSearchRequest.TweetSearchBuilder createSearchBuilder() {
        return TweetSearchRequest.createSearchBuilder()
            .setToken(App.getPreferenceManager().getAuthToken())
            .setHashTag(TweetUtils.createTag(searchTag));
    }

    private TweetSearchRequest.TweetSearchBuilder createSearchNextBuilder() {
        return TweetSearchRequest.createSearchBuilder()
            .setToken(App.getPreferenceManager().getAuthToken())
            .setNextPage(tweetResponse.nextResultsUrl);
    }

    private AuthRequest createAuthRequest() {
        return new AuthRequest(new Response.Listener<String>() {
            @Override
            public void onResponse(String token) {
                Log.d("TEST", token);
                App.getPreferenceManager().setAuthToken(token);
                if (!TextUtils.isEmpty(searchTag)) {
                    App.getNetworkManager().send(createTweetRequest(createSearchBuilder()));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String msg = VolleyErrorHelper.getMessage(volleyError, MainActivity.this);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                Log.d("TEST", volleyError.getMessage());
                statusMonitor.customStatus(msg);
            }
        });
    }

    private TweetSearchRequest createTweetRequest(final TweetSearchRequest.TweetSearchBuilder builder) {
        if (TextUtils.isEmpty(builder.getToken())) {
            return null;
        }
        return new TweetSearchRequest(builder, new Response.Listener<TweetResponse>() {
            @Override
            public void onResponse(TweetResponse tweetResponse) {
                for (Tweet tweet : tweetResponse.tweetList) {
                    Log.d("TEST", tweet.text);
                }
                if (!builder.isNextPageSearch()) {
                    tweetList.clear();
                }
                tweetList.addAll(tweetResponse.tweetList);
                adapter.notifyDataSetChanged();
                MainActivity.this.tweetResponse = tweetResponse;
                if (builder.isNextPageSearch()) {
                    listView.onLoadMoreComplete();
                } else {
                    pullToRefreshLayout.setRefreshComplete();
                }
                statusMonitor.loadedStatus();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (VolleyErrorHelper.isAuthProblem(volleyError)) {
                    App.getPreferenceManager().setAuthToken("");
                    App.getNetworkManager().send(createAuthRequest());
                } else {
                    String msg = VolleyErrorHelper.getMessage(volleyError, MainActivity.this);
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    Log.d("TEST", msg);
                    statusMonitor.customStatus(msg);
                }
            }
        });
    }


}
