package com.alikazi.cc_airtasker.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by kazi_ on 10/22/2017.
 */

@Dao
public interface TaskDao {

    @Query("SELECT * FROM TaskEntity")
    List<TaskEntity> loadAllTasks();

    @Query("SELECT * FROM TaskEntity WHERE id = :id")
    TaskEntity loadTaskById(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTask(TaskEntity task);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(TaskEntity task);

    @Query("DELETE FROM TaskEntity")
    void deleteAllTasks();
}
