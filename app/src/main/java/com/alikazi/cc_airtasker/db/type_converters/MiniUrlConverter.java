package com.alikazi.cc_airtasker.db.type_converters;

import android.arch.persistence.room.TypeConverter;
import android.net.Uri;

import com.alikazi.cc_airtasker.conf.NetConstants;

import java.net.URL;

/**
 * Created by alikazi on 21/10/17.
 */

public class MiniUrlConverter {

    @TypeConverter
    public static URL fromString(String miniUrl) {
        if (miniUrl == null) {
            return null;
        }

        Uri.Builder uriBuilder = new Uri.Builder()
                .scheme(NetConstants.SCHEME_HTTPS)
                .authority(NetConstants.STAGE_AIRTASKER)
                .appendPath(NetConstants.ANDROID_CODE_TEST);
        try {
            String fullUrl = uriBuilder.build().toString() + miniUrl;
            return new URL(fullUrl);
        } catch (Exception e) {
            return null;
        }
    }

    @TypeConverter
    public static String toString(URL fullUrl) {
        if (fullUrl == null) {
            return null;
        }

        String[] miniUrl = fullUrl.toString().split(NetConstants.ANDROID_CODE_TEST);
        return miniUrl[1];
    }
}
