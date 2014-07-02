package com.pohil.twittview.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nhaarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;
import com.pohil.twittview.App;
import com.pohil.twittview.R;
import com.pohil.twittview.api.AuthRequest;
import com.pohil.twittview.api.TweetSearchRequest;
import com.pohil.twittview.manager.ErrorHandler;
import com.pohil.twittview.model.Tweet;
import com.pohil.twittview.model.TweetResponse;
import com.pohil.twittview.utils.TweetUtils;
import com.pohil.twittview.utils.VolleyErrorHelper;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class TweetSearchFragment extends Fragment implements OnRefreshListener, LoadMoreListView.OnLoadMoreListener, ErrorHandler.ErrorObserver{

    private static final String TAG = TweetSearchFragment.class.getSimpleName();

    PullToRefreshLayout pullToRefreshLayout;
    LoadMoreListView listView;
    SearchView searchView;

    List<Tweet> tweetList = new ArrayList<Tweet>();
    AlphaInAnimationAdapter adapter;
    StatusViewMonitor statusMonitor;
    TweetResponse tweetResponse;
    String searchTag = "";

    public TweetSearchFragment() {
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tweet_search, container, false);
    }

    @Override
    public void onViewCreated(View layout, Bundle savedInstanceState) {
        pullToRefreshLayout = (PullToRefreshLayout) layout.findViewById(R.id.ptr_layout);
        listView = (LoadMoreListView) layout.findViewById(R.id.tweet_list);
        statusMonitor = new StatusViewMonitor(layout);
        statusMonitor.emptyStatus();

        ActionBarPullToRefresh.from(getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(pullToRefreshLayout);
        initListView();

        String token = App.getPreferenceManager().getAuthToken();
        if (TextUtils.isEmpty(token)) {
            App.getNetworkManager().send(createAuthRequest());
        }  else {
            Log.d(TAG, token);
        }
    }

    private void initListView() {
        TweetAdapter tweetAdapter = new TweetAdapter(getActivity(), tweetList);
        tweetAdapter.setOnTagClickListener(new TweetAdapter.OnTagClickListener() {
            @Override
            public void onTagClicked(String tag) {
                statusMonitor.loadingStatus();
                listView.setSelectionAfterHeaderView();
                searchView.setQuery(TweetUtils.parseTag(tag), true);
            }
        });
        tweetAdapter.setOnUrlClickListener(new TweetAdapter.OnUrlClickListener() {
            @Override
            public void onUrlClicked(String url) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
        adapter = new AlphaInAnimationAdapter(tweetAdapter);
        adapter.setAbsListView(listView);
        listView.setAdapter(adapter);
        listView.setOnLoadMoreListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search, menu);

        MenuItem searchViewItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchViewItem.getActionView();
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        App.getErrorHandler().addObserver(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        App.getErrorHandler().removeObserver(this);
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

    @Override
    public void onError(VolleyError volleyError, Class request) {
        if (request != AuthRequest.class && VolleyErrorHelper.isAuthProblem(volleyError)) {
            App.getPreferenceManager().setAuthToken("");
            App.getNetworkManager().send(createAuthRequest());
        } else {
            String msg = VolleyErrorHelper.getMessage(volleyError, getActivity());
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            Log.d(TAG, msg);
            statusMonitor.customStatus(msg);
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
                .setNextPage(tweetResponse.getNextResultsUrl());
    }

    private AuthRequest createAuthRequest() {
        return new AuthRequest(new Response.Listener<String>() {
            @Override
            public void onResponse(String token) {
                Log.d(TAG, token);
                App.getPreferenceManager().setAuthToken(token);
                if (!TextUtils.isEmpty(searchTag)) {
                    App.getNetworkManager().send(createTweetRequest(createSearchBuilder()));
                }
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
                TweetSearchFragment.this.tweetResponse = tweetResponse;
                if (!builder.isNextPageSearch()) {
                    tweetList.clear();
                }
                tweetList.addAll(tweetResponse.getTweetList());
                adapter.notifyDataSetChanged();
                if (builder.isNextPageSearch()) {
                    listView.onLoadMoreComplete();
                } else {
                    pullToRefreshLayout.setRefreshComplete();
                    adapter.reset();
                }
                if (tweetList.size() > 0) {
                    statusMonitor.loadedStatus();
                } else {
                    statusMonitor.nothingStatus();
                }
            }
        });
    }
}
