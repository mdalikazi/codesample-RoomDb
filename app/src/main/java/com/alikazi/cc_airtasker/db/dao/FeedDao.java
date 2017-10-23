package com.alikazi.cc_airtasker.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.alikazi.cc_airtasker.db.entities.FeedEntity;
import com.alikazi.cc_airtasker.db.type_converters.DateConverter;

import java.util.List;

/**
 * Created by alikazi on 21/10/17.
 */

@Dao
@TypeConverters(DateConverter.class)
public interface FeedDao {

    @Query("SELECT * FROM FeedEntity")
    List<FeedEntity> loadAllFeed();

    @Query("SELECT * FROM FeedEntity WHERE id = :id")
    FeedEntity loadFeedById(int id);

    @Query("SELECT * FROM FeedEntity " +
            "INNER JOIN TaskEntity ON TaskEntity.id = FeedEntity.task_id")
    List<FeedEntity> loadFeedByTaskId();

    @Query("SELECT * FROM FeedEntity " +
            "INNER JOIN TaskEntity ON TaskEntity.id = FeedEntity.task_id " +
            "INNER JOIN ProfileEntity ON ProfileEntity.id = FeedEntity.profile_id")
    List<FeedEntity> loadFeedByTaskAndProfileId();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertFeed(FeedEntity feed);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFeed(FeedEntity feed);

    @Query("DELETE FROM FeedEntity")
    void deleteAllFeed();
}
