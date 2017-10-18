package com.alikazi.cc_airtasker.network;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.alikazi.cc_airtasker.R;
import com.alikazi.cc_airtasker.conf.AppConf;
import com.alikazi.cc_airtasker.conf.NetConstants;
import com.alikazi.cc_airtasker.models.Feed;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.InputStreamReader;
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

    public NetworkProcessor(Context context, FeedRequestListener feedRequestCallbacksListener) {
        mContext = context;
        mFeedRequestCallbacksListener = feedRequestCallbacksListener;
    }

    public void getFeed() {
        new AsyncTask<Void, Void, ArrayList<Feed>>() {
            @Override
            protected void onPreExecute() {
                Log.i(LOG_TAG, "onPreExecute");
                mUriBuilder = new Uri.Builder()
                        .scheme(NetConstants.SCHEME_HTTPS)
                        .authority(NetConstants.STAGE_AIRTASKER)
                        .appendPath(NetConstants.ANDROID_CODE_TEST)
                        .appendPath(NetConstants.QUERY_FEED);
            }

            @Override
            protected ArrayList<Feed> doInBackground(Void... voids) {
                Log.i(LOG_TAG, "doInBackground");
                ArrayList<Feed> feeds = new ArrayList<>();
                ConnectionHelper connectionHelper = new ConnectionHelper();
                try {
                    URL url = new URL(mUriBuilder.build().toString());
                    HttpsURLConnection httpsURLConnection = connectionHelper.get(url);
                    if (httpsURLConnection != null &&
                            httpsURLConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
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
                    Log.d(LOG_TAG, "Exception with doInBackground: " + e.toString());
                }

                return feeds;
            }

            @Override
            protected void onPostExecute(ArrayList<Feed> feed) {
                Log.i(LOG_TAG, "onPostExecute");
                if (mFeedRequestCallbacksListener != null && feed != null) {
                    mFeedRequestCallbacksListener.onFeedRequestSuccess(feed);
                } else {
                    mFeedRequestCallbacksListener.onFeedRequestFailure(mContext.getString(R.string.feed_response_error));
                }
            }
        }.execute();
    }

    public void getTasks() {

    }

    public void getProfiles() {

    }

    public interface FeedRequestListener {
        void onFeedRequestSuccess(ArrayList<Feed> feeds);

        void onFeedRequestFailure(String errorMessage);
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
