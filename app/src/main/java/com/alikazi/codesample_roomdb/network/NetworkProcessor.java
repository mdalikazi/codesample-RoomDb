package com.alikazi.codesample_roomdb.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.alikazi.codesample_roomdb.R;
import com.alikazi.codesample_roomdb.conf.AppConf;
import com.alikazi.codesample_roomdb.conf.NetConstants;
import com.alikazi.codesample_roomdb.db.AppDatabase;
import com.alikazi.codesample_roomdb.db.entities.FeedEntity;
import com.alikazi.codesample_roomdb.db.entities.ProfileEntity;
import com.alikazi.codesample_roomdb.db.entities.TaskEntity;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by alikazi on 17/10/17.
 */

public class NetworkProcessor {

    private static final String LOG_TAG = AppConf.LOG_TAG_ALI_KAZI;

    private Context mContext;
    private AppDatabase mDatabaseInstance;
    private Uri.Builder mUriBuilder;
    private BufferedInputStream mBufferedInputStream;
    private InputStreamReader mInputStreamReader;
    private FeedRequestListener mFeedRequestCallbacksListener;
    private TasksRequestListener mTasksRequestListener;
    private ProfileRequestListener mProfileRequestListener;

    public NetworkProcessor(Context context, AppDatabase databaseInstance, FeedRequestListener feedRequestCallbacksListener,
                            TasksRequestListener tasksRequestListener, ProfileRequestListener profileRequestListener) {
        mContext = context;
        mDatabaseInstance = databaseInstance;
        mFeedRequestCallbacksListener = feedRequestCallbacksListener;
        mTasksRequestListener = tasksRequestListener;
        mProfileRequestListener = profileRequestListener;
    }

    @SuppressLint("StaticFieldLeak")
    public void getFeed() {
        new AsyncTask<Void, Void, Boolean>() {
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
            protected Boolean doInBackground(Void... voids) {
                Log.i(LOG_TAG, "getFeed doInBackground");
                ConnectionHelper connectionHelper = new ConnectionHelper();
                try {
                    URL url = new URL(mUriBuilder.build().toString());
                    HttpsURLConnection httpsURLConnection = connectionHelper.get(url);
                    if (httpsURLConnection != null && httpsURLConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                        mBufferedInputStream = new BufferedInputStream(httpsURLConnection.getInputStream());
                        mInputStreamReader = new InputStreamReader(mBufferedInputStream);
                        Gson gson = new Gson();
                        FeedEntity[] feedsArray = gson.fromJson(mInputStreamReader, FeedEntity[].class);
                        ArrayList<FeedEntity> feedsList = new ArrayList<>(Arrays.asList(feedsArray));
                        for (FeedEntity feed : feedsList) {
                            mDatabaseInstance.feedModel().insertFeed(feed);
                        }
                        if (mInputStreamReader != null) {
                            mInputStreamReader.close();
                        }
                        if (mBufferedInputStream != null) {
                            mBufferedInputStream.close();
                        }
                    }
                } catch (Exception e) {
                    Log.d(LOG_TAG, "Exception with Feeds doInBackground: " + e.toString());
                    if (mFeedRequestCallbacksListener != null) {
                        mFeedRequestCallbacksListener.onFeedRequestFailure();
                    }
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccess) {
                Log.i(LOG_TAG, "getFeed onPostExecute");
                if (mFeedRequestCallbacksListener != null) {
//                    if (feed != null && !feed.isEmpty()) {
                    if (isSuccess) {
                        mFeedRequestCallbacksListener.onFeedRequestSuccess();
                    }
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void getTasks(final LinkedHashSet<Integer> taskIds) {
        final ArrayList<URL> tasksUrlList = new ArrayList<>();
        new AsyncTask<Void, Void, Boolean>() {
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
                            mTasksRequestListener.onTasksRequestFailure();
                        }
                    }
                }
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                Log.i(LOG_TAG, "getTasks doInBackground");
                ConnectionHelper connectionHelper = new ConnectionHelper();
                try {
                    for (URL taskUrl : tasksUrlList) {
                        HttpsURLConnection httpsURLConnection = connectionHelper.get(taskUrl);
                        if (httpsURLConnection != null && httpsURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            mBufferedInputStream = new BufferedInputStream(httpsURLConnection.getInputStream());
                            mInputStreamReader = new InputStreamReader(mBufferedInputStream);
                            Gson gson = new Gson();
                            TaskEntity task = gson.fromJson(mInputStreamReader, TaskEntity.class);
                            mDatabaseInstance.taskModel().insertTask(task);
//                            tasks.add(task);
                        }
                    }
                    if (mInputStreamReader != null) {
                        mInputStreamReader.close();
                    }
                    if (mBufferedInputStream != null) {
                        mBufferedInputStream.close();
                    }
                } catch (Exception e) {
                    Log.d(LOG_TAG, "Exception with Tasks doInBackground: " + e.toString());
                    if (mTasksRequestListener != null) {
                        mTasksRequestListener.onTasksRequestFailure();
                    }
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccess) {
                Log.i(LOG_TAG, "getTasks onPostExecute");
                if (mTasksRequestListener != null) {
//                    if (tasks != null && !tasks.isEmpty()) {
                    if (isSuccess) {
                        mTasksRequestListener.onTasksRequestSuccess();
                    }
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void getProfiles(final LinkedHashSet<Integer> profileIds) {
        final ArrayList<URL> profilesUrlList = new ArrayList<>();
        new AsyncTask<Void, Void, Boolean>() {
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
                            mProfileRequestListener.onProfilesRequestFailure();
                        }
                    }
                }
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                Log.i(LOG_TAG, "getProfiles doInBackground");
//                ArrayList<Profile> profiles = new ArrayList<>();
                ConnectionHelper connectionHelper = new ConnectionHelper();
                try {
                    for (URL profileUrl : profilesUrlList) {
                        HttpsURLConnection httpsURLConnection = connectionHelper.get(profileUrl);
                        if (httpsURLConnection != null && httpsURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            mBufferedInputStream = new BufferedInputStream(httpsURLConnection.getInputStream());
                            mInputStreamReader = new InputStreamReader(mBufferedInputStream);
                            Gson gson = new Gson();
                            ProfileEntity profile = gson.fromJson(mInputStreamReader, ProfileEntity.class);
                            mDatabaseInstance.profileModel().insertProfile(profile);
//                            profiles.add(profile);
                        }
                    }
                    if (mInputStreamReader != null) {
                        mInputStreamReader.close();
                    }
                    if (mBufferedInputStream != null) {
                        mBufferedInputStream.close();
                    }
                } catch (Exception e) {
                    Log.d(LOG_TAG, "Exception with getProfiles doInBackground: " + e.toString());
                    if (mProfileRequestListener != null) {
                        mProfileRequestListener.onProfilesRequestFailure();
                    }
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccess) {
                Log.i(LOG_TAG, "getProfiles onPostExecute");
                if (mProfileRequestListener != null) {
//                    if (profiles != null && !profiles.isEmpty()) {
                    if (isSuccess) {
                        mProfileRequestListener.onProfilesRequestsSuccess();
                    }
                }
            }
        }.execute();
    }

    public interface FeedRequestListener {
        void onFeedRequestSuccess();

        void onFeedRequestFailure();
    }

    public interface TasksRequestListener {
        void onTasksRequestSuccess();

        void onTasksRequestFailure();
    }

    public interface ProfileRequestListener {
        void onProfilesRequestsSuccess();

        void onProfilesRequestFailure();
    }
}
