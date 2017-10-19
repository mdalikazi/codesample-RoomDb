package com.alikazi.cc_airtasker;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alikazi.cc_airtasker.conf.AppConf;
import com.alikazi.cc_airtasker.conf.NetConstants;
import com.alikazi.cc_airtasker.models.Feed;
import com.alikazi.cc_airtasker.models.Profile;
import com.alikazi.cc_airtasker.models.Task;
import com.alikazi.cc_airtasker.network.NetworkProcessor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NetworkProcessor.FeedRequestListener,
        NetworkProcessor.TasksRequestListener,
        NetworkProcessor.ProfileRequestListener {

    public static final String LOG_TAG = AppConf.LOG_TAG_CC_AIRTASKER;

    // Logic
    private ArrayList<Feed> mFeed;
    private ArrayList<Task> mTasks;
    private ArrayList<Profile> mProfiles;
    private ArrayList<Integer> mTaskIds;
    private ArrayList<Integer> mProfileIds;
    private FeedAdapter mFeedAdapter;
    private NetworkProcessor mNetworkProcessor;

    // UI
    private RecyclerView mRecyclerView;
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mFeedAdapter = new FeedAdapter(this);
        mRecyclerView.setAdapter(mFeedAdapter);
        setupRecyclerScrollListener();
    }

    private void initUi() {
        mFab = findViewById(R.id.main_fab);
        mRecyclerView = findViewById(R.id.main_recycler_view);
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
        if (message == null) {
            return;
        }
        if (!message.isEmpty()) {
            if (mSnackbar == null) {
                mSnackbar = Snackbar.make(mSwipeRefreshLayout, "", Snackbar.LENGTH_INDEFINITE);
            }
            mSnackbar.setText(message);
            mSnackbar.show();
        } else {
            mSnackbar.setDuration(Snackbar.LENGTH_LONG);
        }
    }

    private void setupRecyclerScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && mFab.isShown()) {
                    mFab.hide();
                } else {
                    mFab.show();
                }
            }
        });
    }

    private void requestFeedFromServer(boolean showProgressBar) {
        Log.i(LOG_TAG, "requestFeedFromServer");
        if (mNetworkProcessor != null) {
            showHideProgressBar(showProgressBar);
            showHideSwipeRefreshing(!showProgressBar);
            showHideEmptyListMessage(false);
            processSnackbar(getString(R.string.snackar_message_getting_feed));
            mNetworkProcessor.getFeed();
        }
    }

    @Override
    public void onFeedRequestSuccess(ArrayList<Feed> feed) {
        Log.i(LOG_TAG, "onFeedRequestSuccess");
        mFeed = new ArrayList<>();
        mFeed = feed;
        processTaskAndProfileIds();
    }

    @Override
    public void onFeedRequestFailure(String errorMessage) {
        Log.i(LOG_TAG, "onFeedRequestFailure");
        showHideProgressBar(false);
        showHideSwipeRefreshing(false);
        showHideEmptyListMessage(true);
        processSnackbar(errorMessage);
    }

    private void processTaskAndProfileIds() {
        mTaskIds = new ArrayList<>();
        mProfileIds = new ArrayList<>();
        mTaskIds.clear();
        mProfileIds.clear();
        for (Feed feedItem : mFeed) {
            mTaskIds.add(feedItem.getTask_id());
            mProfileIds.add(feedItem.getProfile_id());
        }

        requestTasksFromServer(mTaskIds);
    }

    private void requestTasksFromServer(ArrayList<Integer> taskIds) {
        Log.i(LOG_TAG, "requestFeedFromServer");
        if (mNetworkProcessor != null) {
            processSnackbar(getString(R.string.snackbar_message_processing_tasks));
            mNetworkProcessor.getTasks(taskIds);
        }
    }

    @Override
    public void onTasksRequestSuccess(ArrayList<Task> tasks) {
        Log.i(LOG_TAG, "onTasksRequestSuccess");
        mTasks = new ArrayList<>();
        mTasks = tasks;
        requestProfilesFromServer(mProfileIds);
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
        if (mNetworkProcessor != null) {
            processSnackbar(getString(R.string.snackbar_message_processing_profiles));
            mNetworkProcessor.getProfiles(profileIds);
        }
    }

    @Override
    public void onProfilesRequestsSuccess(ArrayList<Profile> profiles) {
        Log.i(LOG_TAG, "onProfilesRequestsSuccess");
        showHideProgressBar(false);
        showHideSwipeRefreshing(false);
        showHideEmptyListMessage(false);
        processSnackbar("");
        mProfiles = new ArrayList<>();
        mProfiles = profiles;
        processFeedWithTasksAndProfiles();
        mFeedAdapter.setFeedList(mFeed);
    }

    @Override
    public void onProfilesRequestFailure(String errorMessage) {
        Log.i(LOG_TAG, "onProfilesRequestFailure");
        showHideProgressBar(false);
        showHideSwipeRefreshing(false);
        showHideEmptyListMessage(true);
        processSnackbar(errorMessage);
    }

    private void processFeedWithTasksAndProfiles() {
        for (int i = 0; i < mFeed.size(); i++) {
            Feed feed = mFeed.get(i);
            Task task = mTasks.get(i);
            Profile profile = mProfiles.get(i);
            if (feed.getTask_id() == task.getId() && feed.getProfile_id() == profile.getId()) {

                // Replace {task_name} and {profile_name} with data from task and profile
                String feedText = feed.getText();
                feedText = feedText.replace(NetConstants.JSON_KEY_TASK_NAME, task.getName());
                feedText = feedText.replace(NetConstants.JSON_KEY_PROFILE_NAME, profile.getFirst_name());
                feed.setProcessedText(feedText);

                // Set transient task and profile objects on feed
                feed.setTask(task);
                feed.setProfile(profile);
            }

            // Convert mini url of profile photo to full url
            Uri.Builder uriBuilder = new Uri.Builder()
                    .scheme(NetConstants.SCHEME_HTTPS)
                    .authority(NetConstants.STAGE_AIRTASKER)
                    .appendPath(NetConstants.ANDROID_CODE_TEST);
            String imageUrl = uriBuilder.build().toString() + feed.getProfile().getAvatar_mini_url();
            feed.getProfile().setAvatarFullUrl(imageUrl);

            // Convert ISO date to Java date
            try {
                SimpleDateFormat isoDateFormat = new SimpleDateFormat(AppConf.DATE_FORMAT_ISO, Locale.US);
                Date javaDate = isoDateFormat.parse(feed.getCreated_at());
                feed.setCreatedAtJavaDate(javaDate);
            } catch (Exception e) {
                Log.d(LOG_TAG, "Exception parsing iso date: " + e.toString());
            }
        }
    }
}
