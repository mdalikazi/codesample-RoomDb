package com.alikazi.cc_airtasker.main;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import com.alikazi.cc_airtasker.R;
import com.alikazi.cc_airtasker.conf.AppConf;
import com.alikazi.cc_airtasker.conf.NetConstants;
import com.alikazi.cc_airtasker.db.AppDatabase;
import com.alikazi.cc_airtasker.db.DbHelper;
import com.alikazi.cc_airtasker.db.FakeDataDb;
import com.alikazi.cc_airtasker.db.dao.FeedDao;
import com.alikazi.cc_airtasker.db.entities.FeedEntity;
import com.alikazi.cc_airtasker.db.entities.FeedWithTaskAndProfile;
import com.alikazi.cc_airtasker.network.NetworkProcessor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Locale;

@SuppressWarnings("DanglingJavadoc")
public class MainActivity extends AppCompatActivity
        implements NetworkProcessor.FeedRequestListener,
        NetworkProcessor.TasksRequestListener,
        NetworkProcessor.ProfileRequestListener,
        FakeDataDb.FakeDbCallbacksListener {

    private static final String LOG_TAG = AppConf.LOG_TAG_CC_AIRTASKER;

    private static final boolean SIMULATE_SLOW_INTERNET = true;

    private static final int SNACKBAR_FEED = 0;
    private static final int SNACKBAR_TASKS = 1;
    private static final int SNACKBAR_PROFILE = 2;
    private static final int SNACKBAR_DONE = 3;
    private static final int SNACKBAR_REQUEST_ERROR = 4;
    private static final int SNACKBAR_NO_INTERNET = 5;

    // Logic
    private LinkedHashSet<Integer> mTaskIds;
    private LinkedHashSet<Integer> mProfileIds;
    private FeedAdapter mFeedAdapter;
    private NetworkProcessor mNetworkProcessor;
    private AppDatabase mDbInstance;

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

        // Create DB in memory
        mDbInstance = AppDatabase.getDatabaseInstance(this, true);
        DbHelper.clearDbOnInit(mDbInstance);

        mNetworkProcessor = new NetworkProcessor(this, mDbInstance,
                this,this, this);

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
        showHideEmptyListMessage(true);
//        FakeDataDb.initDbFakeDataAsync(mDbInstance, this);
    }

    private void initUi() {
        mFab = findViewById(R.id.main_fab);
        mRecyclerView = findViewById(R.id.main_recycler_view);
        mProgressBar = findViewById(R.id.main_progress_bar);
        mEmptyListTextView = findViewById(R.id.main_empty_list_message);
        mSwipeRefreshLayout = findViewById(R.id.main_swipe_refresh_layout);
    }

    @Override
    public void onFakeDbCreationComplete() {
        fetchFakeDbData();
    }

    private void fetchFakeDbData() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ArrayList<FeedWithTaskAndProfile> feedWithTasksAndProfiles =
                        (ArrayList<FeedWithTaskAndProfile>) mDbInstance.feedModel().loadFeedWithTaskAndProfile();
                populateAdapter(feedWithTasksAndProfiles);

                /*for (FeedWithTaskAndProfile feedEntity : feedWithTasksAndProfiles) {
                    Log.d(LOG_TAG, "feedEntity.id: " + feedEntity.feed.id);
                    Log.d(LOG_TAG, "feedEntity.task_id: " + feedEntity.feed.task_id);
                    Log.d(LOG_TAG, "feedEntity.profile_id: " + feedEntity.feed.profile_id);
                    Log.d(LOG_TAG, "feedEntity.event: " + feedEntity.feed.event);
                    Log.d(LOG_TAG, "feedEntity.created_at: " + feedEntity.feed.created_at);
                    Log.d(LOG_TAG, "feedEntity.text: " + feedEntity.feed.text);
                    Log.d(LOG_TAG, "-----------------------------------------------------");
                }*/
            }
        });
    }

    private boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager != null &&
                connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
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

    private void processSnackbar(int id) {
        if (mSnackbar == null) {
            mSnackbar = Snackbar.make(mSwipeRefreshLayout, "", Snackbar.LENGTH_INDEFINITE);
        }
        switch (id) {
            case SNACKBAR_FEED:
                mSnackbar.setText(R.string.snackbar_message_getting_feed);
                mSnackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                break;
            case SNACKBAR_TASKS:
                mSnackbar.setText(R.string.snackbar_message_processing_tasks);
                mSnackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                break;
            case SNACKBAR_PROFILE:
                mSnackbar.setText(R.string.snackbar_message_processing_profiles);
                mSnackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                break;
            case SNACKBAR_DONE:
                mSnackbar.setText(R.string.snackbar_message_done);
                mSnackbar.setDuration(Snackbar.LENGTH_SHORT);
                break;
            case SNACKBAR_REQUEST_ERROR:
                mSnackbar.setText(R.string.snackbar_message_feed_response_error);
                mSnackbar.setDuration(Snackbar.LENGTH_LONG);
                break;
            case SNACKBAR_NO_INTERNET:
                mSnackbar.setText(R.string.snackbar_message_no_internet);
                mSnackbar.setDuration(Snackbar.LENGTH_LONG);
            default:
                mSnackbar.dismiss();
        }
        mSnackbar.show();
    }

    private void causeDelay() {
        if (SIMULATE_SLOW_INTERNET) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Log.d(LOG_TAG, "Exception with causeDelay: " + e.toString());
            }
        }
    }

    /**
     * Scroll listener to show and hide FAB appropriately
     */
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
        // First we get all the feed
        if (mNetworkProcessor != null && isInternetConnected()) {
            showHideProgressBar(showProgressBar);
            showHideSwipeRefreshing(!showProgressBar);
            showHideEmptyListMessage(false);
            processSnackbar(SNACKBAR_FEED);
            mNetworkProcessor.getFeed();
        } else {
            processSnackbar(SNACKBAR_NO_INTERNET);
        }
    }

    @Override
    public void onFeedRequestSuccess() {
        Log.i(LOG_TAG, "onFeedRequestSuccess");
        causeDelay();
        // Now we use task_id and profile_id to create task and profile requests
        ArrayList<FeedEntity> feed = (ArrayList<FeedEntity>) mDbInstance.feedModel().loadAllFeed();
        processTaskAndProfileIds(feed);
    }

    @Override
    public void onFeedRequestFailure() {
        Log.i(LOG_TAG, "onFeedRequestFailure");
        processDefaultRequestFailure();
    }

    private void processTaskAndProfileIds(ArrayList<FeedEntity> feed) {
        /**
         * We use HashSets to avoid duplicate ids. In this case I have noticed that
         * there are repeat Tasks and Profiles associated with feed. Probably because
         * it is test data. There is no point making repeat API calls.
         * This might not be the case IRL.
         */
        mTaskIds = new LinkedHashSet<>();
        mProfileIds = new LinkedHashSet<>();
        mTaskIds.clear();
        mProfileIds.clear();
        for (FeedEntity feedItem : feed) {
            mTaskIds.add(feedItem.task_id);
            mProfileIds.add(feedItem.profile_id);
        }

        // First we request Tasks
        requestTasksFromServer(mTaskIds);
    }

    private void requestTasksFromServer(LinkedHashSet<Integer> taskIds) {
        Log.i(LOG_TAG, "requestFeedFromServer");
        if (mNetworkProcessor != null) {
            processSnackbar(SNACKBAR_TASKS);
            mNetworkProcessor.getTasks(taskIds);
        }
    }

    @Override
    public void onTasksRequestSuccess() {
        Log.i(LOG_TAG, "onTasksRequestSuccess");
        causeDelay();
        // Then we request profiles
        requestProfilesFromServer(mProfileIds);
    }

    @Override
    public void onTasksRequestFailure() {
        Log.i(LOG_TAG, "onTasksRequestFailure");
        processDefaultRequestFailure();
    }

    private void requestProfilesFromServer(LinkedHashSet<Integer> profileIds) {
        Log.i(LOG_TAG, "requestProfilesFromServer");
        if (mNetworkProcessor != null) {
            processSnackbar(SNACKBAR_PROFILE);
            mNetworkProcessor.getProfiles(profileIds);
        }
    }

    @Override
    public void onProfilesRequestsSuccess() {
        Log.i(LOG_TAG, "onProfilesRequestsSuccess");
        causeDelay();
        showHideProgressBar(false);
        showHideSwipeRefreshing(false);
        showHideEmptyListMessage(false);
        processSnackbar(SNACKBAR_DONE);
        // We have all data in our DB now. Process it from DB.
        processFeedWithTasksAndProfiles();
    }

    @Override
    public void onProfilesRequestFailure() {
        Log.i(LOG_TAG, "onProfilesRequestFailure");
        processDefaultRequestFailure();
    }

    /**
     * Default network request failure management
     */
    private void processDefaultRequestFailure() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mEmptyListTextView.setText(R.string.main_empty_list_message_error);
                showHideProgressBar(false);
                showHideSwipeRefreshing(false);
                showHideEmptyListMessage(true);
                processSnackbar(SNACKBAR_REQUEST_ERROR);
            }
        });
    }

    private void populateAdapter(ArrayList<FeedWithTaskAndProfile> feed) {
        mFeedAdapter.setFeedList(feed);
    }

    /**
     * Makes all the necessary conversions on feed, task and profile before populating adapter
     */
    private void processFeedWithTasksAndProfiles() {
        /**
         * IRL, I would expect to first fetch all feed, tasks and profiles from their own endpoints,
         * store everything in DB and load all everything from the DB via an SQL query
         * that appropriately 'JOIN's each feed with Task and Profile on task_id and profile_id
         * {@link FeedDao#loadFeedWithTaskAndProfile()} does just that.
         */
        ArrayList<FeedWithTaskAndProfile> feedWithTasksAndProfiles =
                (ArrayList<FeedWithTaskAndProfile>) mDbInstance.feedModel().loadFeedWithTaskAndProfile();

        /**
         * FeedWithTaskAndProfile has a @Relation on Task and Profile entities which returns a list of each.
         * For our case, we know that each feed can have only 1 task and 1 profile associated.
         * So even though a list is returned, we will process only <list>.get(0) for task and profile.
         */

        /**
         * Now we will fix the text on Feed, miniUrl -> fullUrl, ISO date -> Java date
         * There might be a way to setup TypeConverters to do these conversions automatically
         * in a lifecycler aware manner.
         *
         * I prefer doing this outside of adapter and keep the adapter as simple as possible.
         */

        for (FeedWithTaskAndProfile feedWithTaskAndProfile : feedWithTasksAndProfiles) {
            // Convert ISO date to Java date
            try {
                SimpleDateFormat isoDateFormat = new SimpleDateFormat(AppConf.DATE_FORMAT_ISO, Locale.US);
                feedWithTaskAndProfile.feed.createdAtJavaDate = isoDateFormat.parse(feedWithTaskAndProfile.feed.created_at);
            } catch (Exception e) {
                Log.d(LOG_TAG, "Exception parsing iso date: " + e.toString());
            }

            // Replace {task_name} and {profile_name} with data from task and profile objects
            String feedText = feedWithTaskAndProfile.feed.text;
            feedText = feedText.replace(NetConstants.JSON_KEY_TASK_NAME,
                    feedWithTaskAndProfile.tasks.get(0).name);
            feedText = feedText.replace(NetConstants.JSON_KEY_PROFILE_NAME,
                    feedWithTaskAndProfile.profiles.get(0).first_name);
            feedWithTaskAndProfile.feed.fixedText = feedText;

            // Convert mini url of profile photo to full url
            Uri.Builder uriBuilder = new Uri.Builder()
                    .scheme(NetConstants.SCHEME_HTTPS)
                    .authority(NetConstants.STAGE_AIRTASKER)
                    .appendPath(NetConstants.ANDROID_CODE_TEST);
            feedWithTaskAndProfile.profiles.get(0).avatarFullUrl = uriBuilder.build().toString() +
                            feedWithTaskAndProfile.profiles.get(0).avatar_mini_url;
        }

        populateAdapter(feedWithTasksAndProfiles);
    }
}
