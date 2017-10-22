package com.alikazi.cc_airtasker.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.alikazi.cc_airtasker.db.type_converters.DateConverter;

/**
 * Created by alikazi on 21/10/17.
 */
/*@Entity(foreignKeys = {
        @ForeignKey(entity = ProfileEntity.class,
                parentColumns = "id",
                childColumns = "task_id",
                onDelete = ForeignKey.CASCADE),

        @ForeignKey(entity = ProfileEntity.class,
                parentColumns = "id",
                childColumns = "profile_id",
                onDelete = ForeignKey.CASCADE)})*/
@Entity
@TypeConverters(DateConverter.class)
public class FeedEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "task_id")
    public int task_id;
    @ColumnInfo(name = "profile_id")
    public int profile_id;
    public String text;
    public String created_at;
    public String event;

}
