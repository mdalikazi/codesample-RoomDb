package com.alikazi.cc_airtasker.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by alikazi on 21/10/17.
 */

@Entity
public class TaskEntity {
    @NonNull
    public @PrimaryKey int id;
    public String name;
    public String description;
    public String state;
    public int poster_id;
    public int worker_id;
}
