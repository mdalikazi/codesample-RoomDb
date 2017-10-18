package com.alikazi.cc_airtasker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alikazi.cc_airtasker.conf.AppConf;
import com.alikazi.cc_airtasker.models.Feed;
import com.alikazi.cc_airtasker.models.Profile;
import com.alikazi.cc_airtasker.models.Task;
import com.alikazi.cc_airtasker.network.NetworkProcessor;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NetworkProcessor.FeedRequestListener,
        NetworkProcessor.TasksRequestListener,
        NetworkProcessor.ProfileRequestListener {

    public static final String LOG_TAG = AppConf.LOG_TAG_CC_AIRTASKER;

    // Logic
    private NetworkProcessor mNetworkProcessor;

    // UI
    private TextView mTextView;
    private TextView mEmptyListTextView;
    private ProgressBar mProgressBar;
    private FloatingActionButton mFab;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Snackbar mSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();
        mNetworkProcessor = new NetworkProcessor(this, this,this, this);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestFeedFromServer(true);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestFeedFromServer(false);
            }
        });
    }

    private void initUi() {
        mFab = findViewById(R.id.main_fab);
        mTextView = findViewById(R.id.main_text_view);
        mProgressBar = findViewById(R.id.main_progress_bar);
        mEmptyListTextView = findViewById(R.id.main_empty_list_message);
        mSwipeRefreshLayout = findViewById(R.id.main_swipe_refresh_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void showHideEmptyListMessage(boolean show) {
        if (mEmptyListTextView != null) {
            mEmptyListTextView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void showHideProgressBar(boolean show) {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void showHideSwipeRefreshing(boolean show) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(show);
        }
    }

    private void processSnackbar(String message) {
        if (mSnackbar != null && mSnackbar.isShown()) {
            mSnackbar.dismiss();
        }
        mSnackbar = Snackbar.make(mSwipeRefreshLayout, message, Snackbar.LENGTH_LONG);
        mSnackbar.show();
    }

    private void requestFeedFromServer(boolean showProgressBar) {
        Log.i(LOG_TAG, "requestFeedFromServer");
        if (mNetworkProcessor != null) {
            showHideProgressBar(showProgressBar);
            showHideSwipeRefreshing(!showProgressBar);
            showHideEmptyListMessage(false);
            processSnackbar("Getting Feed...");
            mNetworkProcessor.getFeed();
        }
    }

    @Override
    public void onFeedRequestSuccess(ArrayList<Feed> feed) {
        Log.i(LOG_TAG, "onFeedRequestSuccess");
        /*if (mTextView != null) {
            for (Feed feedItem : feed) {
                mTextView.append(feed.getText() + "\n");
            }
        }*/
        ArrayList<Integer> taskIds = new ArrayList<>();
        ArrayList<Integer> profileIds = new ArrayList<>();
        for (Feed feedItem : feed) {
            taskIds.add(feedItem.getTask_id());
            profileIds.add(feedItem.getProfile_id());
        }
        requestTasksFromServer(taskIds);
//        requestProfilesFromServer(profileIds);
    }

    @Override
    public void onFeedRequestFailure(String errorMessage) {
        Log.i(LOG_TAG, "onFeedRequestFailure");
        showHideProgressBar(false);
        showHideSwipeRefreshing(false);
        showHideEmptyListMessage(true);
        processSnackbar(errorMessage);
    }

    private void requestTasksFromServer(ArrayList<Integer> taskIds) {
        Log.i(LOG_TAG, "requestFeedFromServer");
        if (mNetworkProcessor != null) {
            processSnackbar("Processing Tasks...");
            mNetworkProcessor.getTasks(taskIds);
        }
    }

    @Override
    public void onTasksRequestSuccess(ArrayList<Task> tasks) {
        Log.i(LOG_TAG, "onTasksRequestSuccess");
        if (mTextView != null) {
            for (Task task : tasks) {
                mTextView.append(task.getName() + "\n");
            }
        }
    }

    @Override
    public void onTasksRequestFailure(String errorMessage) {
        Log.i(LOG_TAG, "onTasksRequestFailure");
        showHideProgressBar(false);
        showHideSwipeRefreshing(false);
        showHideEmptyListMessage(true);
        processSnackbar(errorMessage);
    }

    private void requestProfilesFromServer(ArrayList<Integer> profileIds) {
        Log.i(LOG_TAG, "requestProfilesFromServer");
        showHideProgressBar(false);
        showHideSwipeRefreshing(false);
        showHideEmptyListMessage(false);
        if (mNetworkProcessor != null) {
            processSnackbar("Processing Profiles...");
            mNetworkProcessor.getTasks(profileIds);
        }
    }

    @Override
    public void onProfilesRequestsSuccess(ArrayList<Profile> profiles) {
        Log.i(LOG_TAG, "onProfilesRequestsSuccess");
    }

    @Override
    public void onProfilesRequestFailure(String errorMessage) {
        Log.i(LOG_TAG, "onProfilesRequestFailure");
        showHideProgressBar(false);
        showHideSwipeRefreshing(false);
        showHideEmptyListMessage(true);
        processSnackbar(errorMessage);
    }
}
