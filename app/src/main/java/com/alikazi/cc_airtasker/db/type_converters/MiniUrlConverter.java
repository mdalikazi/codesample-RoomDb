package com.alikazi.cc_airtasker.db.type_converters;

import android.arch.persistence.room.TypeConverter;
import android.net.Uri;

import com.alikazi.cc_airtasker.conf.NetConstants;

/**
 * Created by alikazi on 21/10/17.
 */

public class MiniUrlConverter {

    @TypeConverter
    public static String convertMiniUrlToFull(String miniUrl) {
        Uri.Builder uriBuilder = new Uri.Builder()
                .scheme(NetConstants.SCHEME_HTTPS)
                .authority(NetConstants.STAGE_AIRTASKER)
                .appendPath(NetConstants.ANDROID_CODE_TEST);

        if (miniUrl != null) {
            return uriBuilder.build().toString() + miniUrl;
        } else {
            return null;
        }
    }
}
