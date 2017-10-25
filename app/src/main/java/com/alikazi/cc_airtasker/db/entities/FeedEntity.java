package com.alikazi.cc_airtasker.db.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.alikazi.cc_airtasker.db.type_converters.DateConverter;

import java.util.Date;

/**
 * Created by alikazi on 21/10/17.
 */

// Not the best idea for our case
/*@Entity(foreignKeys = {
        @ForeignKey(entity = ProfileEntity.class,
                parentColumns = "id",
                childColumns = "task_id",
                onDelete = ForeignKey.CASCADE),

        @ForeignKey(entity = ProfileEntity.class,
                parentColumns = "id",
                childColumns = "profile_id",
                onDelete = ForeignKey.CASCADE)})
*/
@Entity
public class FeedEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int task_id;
    public int profile_id;
    public String text;
    public @TypeConverters(DateConverter.class) String created_at; // DateConverter can't help
    public String event;

    @Ignore
    public Date createdAtJavaDate;
    @Ignore
    public String fixedText;
}
