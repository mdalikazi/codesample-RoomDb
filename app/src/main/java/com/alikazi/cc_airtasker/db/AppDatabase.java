package com.alikazi.cc_airtasker.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.alikazi.cc_airtasker.db.dao.FeedDao;
import com.alikazi.cc_airtasker.db.dao.ProfileDao;
import com.alikazi.cc_airtasker.db.dao.TaskDao;
import com.alikazi.cc_airtasker.db.entities.FeedEntity;
import com.alikazi.cc_airtasker.db.entities.ProfileEntity;
import com.alikazi.cc_airtasker.db.entities.TaskEntity;
import com.alikazi.cc_airtasker.db.type_converters.DateConverter;
import com.alikazi.cc_airtasker.db.type_converters.MiniUrlConverter;

/**
 * Created by kazi_ on 10/22/2017.
 */

@Database(entities = {FeedEntity.class, TaskEntity.class, ProfileEntity.class}, version = 1)
@TypeConverters({DateConverter.class, MiniUrlConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase DATABASE_INSTANCE;

    public abstract FeedDao feedModel();
    public abstract TaskDao taskModel();
    public abstract ProfileDao profileModel();

    public static AppDatabase getDatabaseInstance(Context context) {
        if (DATABASE_INSTANCE == null) {
            DATABASE_INSTANCE =
                    Room.inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class)
                            .allowMainThreadQueries()
                            .build();
        }

        return DATABASE_INSTANCE;
    }

    public static void destroyDatabaseInstance() {
        DATABASE_INSTANCE = null;
    }
}
