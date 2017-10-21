package com.alikazi.cc_airtasker.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.alikazi.cc_airtasker.db.type_converters.MiniUrlConverter;

/**
 * Created by alikazi on 21/10/17.
 */

@Entity
@TypeConverters(MiniUrlConverter.class)
public class ProfileEntity {
    @PrimaryKey
    private int id;
    private String avatar_mini_url;
    private String first_name;
    private int rating;
}
