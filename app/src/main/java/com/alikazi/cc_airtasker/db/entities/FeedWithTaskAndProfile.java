package com.alikazi.cc_airtasker.db.entities;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * Created by kazi_ on 10/24/2017.
 */

public class FeedWithTaskAndProfile {

    @Embedded
    public FeedEntity feed;

    @Relation(parentColumn = "task_id", entityColumn = "id")
    public List<TaskEntity> tasks;

    @Relation(parentColumn = "profile_id", entityColumn = "id")
    public List<ProfileEntity> profiles;
}
