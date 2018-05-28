package com.alikazi.codesample_roomdb.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.alikazi.codesample_roomdb.db.dao.FeedDao;
import com.alikazi.codesample_roomdb.db.dao.ProfileDao;
import com.alikazi.codesample_roomdb.db.dao.TaskDao;
import com.alikazi.codesample_roomdb.db.entities.FeedEntity;
import com.alikazi.codesample_roomdb.db.entities.ProfileEntity;
import com.alikazi.codesample_roomdb.db.entities.TaskEntity;
import com.alikazi.codesample_roomdb.db.type_converters.DateConverter;
import com.alikazi.codesample_roomdb.db.type_converters.MiniUrlConverter;

/**
 * Created by kazi_ on 10/22/2017.
 */

@Database(entities = {FeedEntity.class, TaskEntity.class, ProfileEntity.class}, version = 1)
@TypeConverters({DateConverter.class, MiniUrlConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "appdatabase.db";
    private static AppDatabase DATABASE_INSTANCE;

    public abstract FeedDao feedModel();
    public abstract TaskDao taskModel();
    public abstract ProfileDao profileModel();

    /**
     *
     * @param context
     * @param inMemory true -> in memory, false -> on disk
     * @return
     */
    public static AppDatabase getDatabaseInstance(Context context, boolean inMemory) {
        if (DATABASE_INSTANCE == null) {
            RoomDatabase.Builder<AppDatabase> databaseBuilder;

            if (inMemory) {
                databaseBuilder =
                        Room.inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class)
                                .allowMainThreadQueries();
            } else {
                databaseBuilder =
                        Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME);
            }

            DATABASE_INSTANCE = databaseBuilder.build();
        }

        return DATABASE_INSTANCE;
    }

    public static void destroyDatabaseInstance() {
        DATABASE_INSTANCE = null;
    }
}
