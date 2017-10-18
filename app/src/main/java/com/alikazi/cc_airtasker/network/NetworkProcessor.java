package com.alikazi.cc_airtasker.network;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.alikazi.cc_airtasker.R;
import com.alikazi.cc_airtasker.conf.AppConf;
import com.alikazi.cc_airtasker.conf.NetConstants;
import com.alikazi.cc_airtasker.models.Feed;
import com.alikazi.cc_airtasker.models.Profile;
import com.alikazi.cc_airtasker.models.Task;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by alikazi on 17/10/17.
 */

public class NetworkProcessor {

    public static final String LOG_TAG = AppConf.LOG_TAG_CC_AIRTASKER;

    private Context mContext;
    private Uri.Builder mUriBuilder;
    private BufferedInputStream mBufferedInputStream;
    private InputStreamReader mInputStreamReader;
    private FeedRequestListener mFeedRequestCallbacksListener;
    private TasksRequestListener mTasksRequestListener;
    private ProfileRequestListener mProfileRequestListener;

    public NetworkProcessor(Context context, FeedRequestListener feedRequestCallbacksListener,
                            TasksRequestListener tasksRequestListener, ProfileRequestListener profileRequestListener) {
        mContext = context;
        mFeedRequestCallbacksListener = feedRequestCallbacksListener;
        mTasksRequestListener = tasksRequestListener;
        mProfileRequestListener = profileRequestListener;
    }

    public void getFeed() {
        new AsyncTask<Void, Void, ArrayList<Feed>>() {
            @Override
            protected void onPreExecute() {
                Log.i(LOG_TAG, "getFeed onPreExecute");
                mUriBuilder = new Uri.Builder()
                        .scheme(NetConstants.SCHEME_HTTPS)
                        .authority(NetConstants.STAGE_AIRTASKER)
                        .appendPath(NetConstants.ANDROID_CODE_TEST)
                        .appendPath(NetConstants.QUERY_FEED);
            }

            @Override
            protected ArrayList<Feed> doInBackground(Void... voids) {
                Log.i(LOG_TAG, "getFeed doInBackground");
                ArrayList<Feed> feeds = new ArrayList<>();
                ConnectionHelper connectionHelper = new ConnectionHelper();
                try {
                    URL url = new URL(mUriBuilder.build().toString());
                    HttpsURLConnection httpsURLConnection = connectionHelper.get(url);
                    if (httpsURLConnection != null && httpsURLConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                        mBufferedInputStream = new BufferedInputStream(httpsURLConnection.getInputStream());
                        mInputStreamReader = new InputStreamReader(mBufferedInputStream);
                        Gson gson = new Gson();
                        Feed[] feed = gson.fromJson(mInputStreamReader, Feed[].class);
                        feeds = new ArrayList<>(Arrays.asList(feed));
                        mInputStreamReader.close();
                        mBufferedInputStream.close();
                        httpsURLConnection.disconnect();
                    }
                } catch (Exception e) {
                    Log.d(LOG_TAG, "Exception with Feeds doInBackground: " + e.toString());
                    if (mFeedRequestCallbacksListener != null) {
                        mFeedRequestCallbacksListener.onFeedRequestFailure(mContext.getString(R.string.feed_response_error));
                    }
                }

                return feeds;
            }

            @Override
            protected void onPostExecute(ArrayList<Feed> feed) {
                Log.i(LOG_TAG, "getFeed onPostExecute");
                if (mFeedRequestCallbacksListener != null && feed != null) {
                    mFeedRequestCallbacksListener.onFeedRequestSuccess(feed);
                } else {
                    mFeedRequestCallbacksListener.onFeedRequestFailure(mContext.getString(R.string.feed_response_error));
                }
            }
        }.execute();
    }

    public void getTasks(final ArrayList<Integer> taskIds) {
        final ArrayList<URL> tasksUrlList = new ArrayList<>();
        new AsyncTask<Void, Void, ArrayList<Task>>() {
            @Override
            protected void onPreExecute() {
                Log.i(LOG_TAG, "getTasks onPreExecute");
                for (int taskId : taskIds) {
                    String taskPath = mContext.getString(R.string.id_json, taskId);
                    Uri.Builder uriBuilder = new Uri.Builder()
                            .scheme(NetConstants.SCHEME_HTTPS)
                            .authority(NetConstants.STAGE_AIRTASKER)
                            .appendPath(NetConstants.ANDROID_CODE_TEST)
                            .appendPath(NetConstants.QUERY_TASK)
                            .appendPath(taskPath);
                    try {
                        URL taskUrl = new URL(uriBuilder.build().toString());
                        tasksUrlList.add(taskUrl);
                    } catch (Exception e) {
                        Log.d(LOG_TAG, "Exception building task URL: " + e.toString());
                        if (mTasksRequestListener != null) {
                            mTasksRequestListener.onTasksRequestFailure(mContext.getString(R.string.feed_response_error));
                        }
                    }
                }
            }

            @Override
            protected ArrayList<Task> doInBackground(Void... voids) {
                Log.i(LOG_TAG, "getTasks doInBackground");
                ArrayList<Task> tasks = new ArrayList<>();
                ConnectionHelper connectionHelper = new ConnectionHelper();
                try {
                    for (URL taskUrl : tasksUrlList) {
                        HttpsURLConnection httpsURLConnection = connectionHelper.get(taskUrl);
                        if (httpsURLConnection != null && httpsURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            mBufferedInputStream = new BufferedInputStream(httpsURLConnection.getInputStream());
                            mInputStreamReader = new InputStreamReader(mBufferedInputStream);
                            Gson gson = new Gson();
                            Task task = gson.fromJson(mInputStreamReader, Task.class);
                            tasks.add(task);
                        }
                    }
                    mBufferedInputStream.close();
                    mInputStreamReader.close();
                } catch (Exception e) {
                    Log.d(LOG_TAG, "Exception with Tasks doInBackground: " + e.toString());
                    if (mTasksRequestListener != null) {
                        mTasksRequestListener.onTasksRequestFailure(mContext.getString(R.string.feed_response_error));
                    }
                }
                return tasks;
            }

            @Override
            protected void onPostExecute(ArrayList<Task> tasks) {
                Log.i(LOG_TAG, "getTasks onPostExecute");
                if (mTasksRequestListener != null && tasks != null) {
                    mTasksRequestListener.onTasksRequestSuccess(tasks);
                } else {
                    mTasksRequestListener.onTasksRequestFailure(mContext.getString(R.string.feed_response_error));
                }
            }
        }.execute();

    }

    public void getProfiles(final ArrayList<Integer> profileIds) {
        final ArrayList<URL> profilesUrlList = new ArrayList<>();
        new AsyncTask<Void, Void, ArrayList<Profile>>() {
            @Override
            protected void onPreExecute() {
                Log.i(LOG_TAG, "getProfiles onPreExecute");
                for (int profileId : profileIds) {
                    String profilePath = mContext.getString(R.string.id_json, profileId);
                    Uri.Builder uriBuilder = new Uri.Builder()
                            .scheme(NetConstants.SCHEME_HTTPS)
                            .authority(NetConstants.STAGE_AIRTASKER)
                            .appendPath(NetConstants.ANDROID_CODE_TEST)
                            .appendPath(NetConstants.QUERY_PROFILE)
                            .appendPath(profilePath);
                    try {
                        URL profileUrl = new URL(uriBuilder.build().toString());
                        profilesUrlList.add(profileUrl);
                    } catch (Exception e) {
                        Log.d(LOG_TAG, "Exception building profile URL: " + e.toString());
                        if (mProfileRequestListener != null) {
                            mProfileRequestListener.onProfilesRequestFailure(mContext.getString(R.string.feed_response_error));
                        }
                    }
                }
            }

            @Override
            protected ArrayList<Profile> doInBackground(Void... voids) {
                Log.i(LOG_TAG, "getProfiles doInBackground");
                ArrayList<Profile> profiles = new ArrayList<>();
                ConnectionHelper connectionHelper = new ConnectionHelper();
                try {
                    for (URL profileUrl : profilesUrlList) {
                        HttpsURLConnection httpsURLConnection = connectionHelper.get(profileUrl);
                        if (httpsURLConnection != null && httpsURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            mBufferedInputStream = new BufferedInputStream(httpsURLConnection.getInputStream());
                            mInputStreamReader = new InputStreamReader(mBufferedInputStream);
                            Gson gson = new Gson();
                            Profile profile = gson.fromJson(mInputStreamReader, Profile.class);
                            profiles.add(profile);
                        }
                    }
                    mBufferedInputStream.close();
                    mInputStreamReader.close();
                } catch (Exception e) {
                    Log.d(LOG_TAG, "Exception with getProfiles doInBackground: " + e.toString());
                    if (mProfileRequestListener != null) {
                        mProfileRequestListener.onProfilesRequestFailure(mContext.getString(R.string.feed_response_error));
                    }
                }
                return profiles;
            }

            @Override
            protected void onPostExecute(ArrayList<Profile> profiles) {
                Log.i(LOG_TAG, "getProfiles onPostExecute");
                if (mProfileRequestListener != null && profiles != null) {
                    mProfileRequestListener.onProfilesRequestsSuccess(profiles);
                } else {
                    mProfileRequestListener.onProfilesRequestFailure(mContext.getString(R.string.feed_response_error));
                }
            }
        }.execute();
    }

    public interface FeedRequestListener {
        void onFeedRequestSuccess(ArrayList<Feed> feeds);

        void onFeedRequestFailure(String errorMessage);
    }

    public interface TasksRequestListener {
        void onTasksRequestSuccess(ArrayList<Task> tasks);

        void onTasksRequestFailure(String errorMessage);
    }

    public interface ProfileRequestListener {
        void onProfilesRequestsSuccess(ArrayList<Profile> profiles);

        void onProfilesRequestFailure(String errorMessage);
    }
}
