package com.alikazi.cc_airtasker.db;

import com.alikazi.cc_airtasker.conf.AppConf;
import com.alikazi.cc_airtasker.db.entities.FeedEntity;
import com.alikazi.cc_airtasker.db.entities.ProfileEntity;
import com.alikazi.cc_airtasker.db.entities.TaskEntity;

/**
 * Created by alikazi on 23/10/17.
 */

public class DbHelper {

    private static final String LOG_TAG = AppConf.LOG_TAG_CC_AIRTASKER;

    public static void initDbAsync(final AppDatabase db) {

    }

    public static void clearDbOnInit(AppDatabase db) {
        db.feedModel().deleteAllFeed();
        db.taskModel().deleteAllTasks();
        db.profileModel().deleteAllProfiles();
    }

    public static void addFeed(AppDatabase db, TaskEntity taskEntity, ProfileEntity profileEntity,
                                String createdAt, String event, String text) {
        FeedEntity feedEntity = new FeedEntity();
        feedEntity.task_id = taskEntity.id;
        feedEntity.profile_id = profileEntity.id;
        feedEntity.created_at = createdAt;
        feedEntity.event = event;
        feedEntity.text = text;
        db.feedModel().insertFeed(feedEntity);
    }

    public static TaskEntity addTask(AppDatabase db, int id, int posterId, int workerId, String name,
                                      String description, String state) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.id = id;
        taskEntity.poster_id = posterId;
        taskEntity.worker_id = workerId;
        taskEntity.name = name;
        taskEntity.description = description;
        taskEntity.state = state;
        db.taskModel().insertTask(taskEntity);
        return taskEntity;
    }

    public static ProfileEntity addProfile(AppDatabase db, int id, String firstName,
                                            String miniUrl, int rating) {
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.id = id;
        profileEntity.first_name = firstName;
        profileEntity.avatar_mini_url = miniUrl;
        profileEntity.rating = rating;
        db.profileModel().insertProfile(profileEntity);
        return profileEntity;
    }

    public interface DatabaseInitializerCallbacks {
        void onClearDbComplete();
    }
}
