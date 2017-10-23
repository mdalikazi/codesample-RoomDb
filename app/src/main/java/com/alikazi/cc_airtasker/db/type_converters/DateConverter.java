package com.alikazi.cc_airtasker.db.type_converters;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import com.alikazi.cc_airtasker.conf.AppConf;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alikazi on 21/10/17.
 */

public class DateConverter {

    private static final String LOG_TAG = AppConf.LOG_TAG_CC_AIRTASKER;

    @TypeConverter
    public static Date toJavaDate(String isoDate) {
        SimpleDateFormat isoDateFormat = new SimpleDateFormat(AppConf.DATE_FORMAT_ISO, Locale.getDefault());
        try {
            return isoDateFormat.parse(isoDate);
        } catch (Exception e) {
            Log.d(LOG_TAG, "Exception parsing isoDate: " + e.toString());
            return null;
        }
    }
}
