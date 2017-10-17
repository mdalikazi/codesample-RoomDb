package com.alikazi.cc_airtasker.network;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.alikazi.cc_airtasker.conf.AppConf;
import com.alikazi.cc_airtasker.conf.NetConstants;
import com.alikazi.cc_airtasker.models.Feed;

import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by alikazi on 17/10/17.
 */

public class NetworkProcessor {

    public static final String LOG_TAG = AppConf.LOG_TAG_CC_AIRTASKER;

    private Context mContext;
    private Uri.Builder mUriBuilder;
    private BufferedInputStream mBufferedInputStream;
    private InputStreamReader mInputStreamReader;
    private ConnectionHelper mConnectionHelper;

    private FeedRequestCallbacksListener mFeedRequestCallbacksListener;

    public NetworkProcessor(Context context, FeedRequestCallbacksListener feedRequestCallbacksListener) {
        mContext = context;
        mFeedRequestCallbacksListener = feedRequestCallbacksListener;
        mConnectionHelper = new ConnectionHelper();
    }

    private void getFeed() {
        new AsyncTask<Void, Void, Feed>() {

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
            protected Feed doInBackground(Void... voids) {
                return null;
            }

            @Override
            protected void onPostExecute(Feed feed) {
                super.onPostExecute(feed);
            }

        };
    }

    public interface FeedRequestCallbacksListener {
        void onFeedRequestSuccess(ArrayList<Feed> feeds);

        void onFeedRequestFailure(String errorMessage);
    }
}
