package com.alikazi.codesample_roomdb.db.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.alikazi.codesample_roomdb.db.type_converters.MiniUrlConverter;

/**
 * Created by alikazi on 21/10/17.
 */

@Entity
public class ProfileEntity {
    public @PrimaryKey int id;
    public @TypeConverters(MiniUrlConverter.class) String avatar_mini_url; // TypeConverter can't help
    public String first_name;
    public int rating;

    @Ignore
    public String avatarFullUrl;
}
