package com.alikazi.cc_airtasker.network;

import android.util.Log;

import com.alikazi.cc_airtasker.conf.AppConf;
import com.alikazi.cc_airtasker.conf.NetConstants;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by alikazi on 17/10/17.
 */

public class ConnectionHelper {

    public static final String LOG_TAG = AppConf.LOG_TAG_CC_AIRTASKER;

    private HttpsURLConnection mHttpsURLConnection;

    public ConnectionHelper() {
        super();
    }

    public HttpsURLConnection get(URL url) {
        Log.i(LOG_TAG, "Trying to get: " + url.toString());
        try {
            mHttpsURLConnection = (HttpsURLConnection) url.openConnection();
            mHttpsURLConnection.setDoInput(true);
            mHttpsURLConnection.setDoOutput(false);
            mHttpsURLConnection.setRequestMethod(NetConstants.REQUEST_METHOD_GET);
            mHttpsURLConnection.setConnectTimeout(NetConstants.REQUEST_TIMEOUT);
            mHttpsURLConnection.setReadTimeout(NetConstants.REQUEST_TIMEOUT);
            mHttpsURLConnection.connect();
        } catch (Exception e) {
            Log.d(LOG_TAG, "Exception with get: " + e.toString());
        }

        return mHttpsURLConnection;
    }

    public HttpsURLConnection post(URL url) {
        Log.i(LOG_TAG, "Trying to post: " + url.toString());
        try {
            mHttpsURLConnection = (HttpsURLConnection) url.openConnection();
            mHttpsURLConnection.setDoInput(true);
            mHttpsURLConnection.setDoOutput(true);
            mHttpsURLConnection.setRequestMethod(NetConstants.REQUEST_METHOD_POST);
            mHttpsURLConnection.setConnectTimeout(NetConstants.REQUEST_TIMEOUT);
            mHttpsURLConnection.connect();
        } catch (Exception e) {
            Log.d(LOG_TAG, "Exception with post: " + e.toString());
        }

        return mHttpsURLConnection;
    }
}
