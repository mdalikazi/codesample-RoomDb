package com.alikazi.codesample_roomdb.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.alikazi.codesample_roomdb.db.entities.ProfileEntity;
import com.alikazi.codesample_roomdb.db.type_converters.MiniUrlConverter;

import java.util.List;

/**
 * Created by kazi_ on 10/22/2017.
 */

@Dao
@TypeConverters(MiniUrlConverter.class)
public interface ProfileDao {

    @Query("SELECT * FROM ProfileEntity")
    List<ProfileEntity> loadAllProfiles();

    @Query("SELECT * FROM ProfileEntity WHERE id = :id")
    List<ProfileEntity> loadProfileById(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertProfile(ProfileEntity profile);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateProfile(ProfileEntity profile);

    @Query("DELETE FROM ProfileEntity")
    void deleteAllProfiles();
}
