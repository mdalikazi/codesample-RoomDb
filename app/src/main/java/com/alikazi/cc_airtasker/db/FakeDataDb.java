package com.alikazi.cc_airtasker.db;

import android.os.AsyncTask;

import com.alikazi.cc_airtasker.conf.AppConf;
import com.alikazi.cc_airtasker.db.entities.FeedEntity;
import com.alikazi.cc_airtasker.db.entities.ProfileEntity;
import com.alikazi.cc_airtasker.db.entities.TaskEntity;

/**
 * Created by kazi_ on 10/22/2017.
 */

public class FakeDataDb {

    private static final String LOG_TAG = AppConf.LOG_TAG_CC_AIRTASKER;

    public static void initDbAsync(final AppDatabase db, FakeDbCallbacksListener fakeDbCallbacksListener) {
        PopulateDbAsync populateDbAsync = new PopulateDbAsync(db, fakeDbCallbacksListener);
        populateDbAsync.execute();
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;
        private FakeDbCallbacksListener mFakeDbCallbacksListener;

        public PopulateDbAsync(AppDatabase db, FakeDbCallbacksListener fakeDbCallbacksListener) {
            mDb = db;
            mFakeDbCallbacksListener = fakeDbCallbacksListener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            populateDbWithFakeData(mDb);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (mFakeDbCallbacksListener != null) {
                mFakeDbCallbacksListener.onFakeDbCreationComplete();
            }
        }
    }

    private static void populateDbWithFakeData(AppDatabase db) {
        db.feedModel().deleteAllFeed();
        db.taskModel().deleteAllTasks();
        db.profileModel().deleteAllProfiles();

        TaskEntity task1 = addFakeTask(db, 1, 1, 1, "Task 1", "Do task 1", "Assigned");
        TaskEntity task2 = addFakeTask(db, 2, 1, 1, "Task 2", "Do task 2", "Completed");
        TaskEntity task3 = addFakeTask(db, 3, 1, 1, "Task 3", "Do task 3", "Denied");
        TaskEntity task4 = addFakeTask(db, 4, 1, 1, "Task 4", "Do task 4", "Finished");

        ProfileEntity profileEntity1 = addFakeProfile(db, 1, "John Steep", "/img/4.jpg", 3);
        ProfileEntity profileEntity2 = addFakeProfile(db, 2, "Mahnsja Herret", "/img/2.jpg", 1);
        ProfileEntity profileEntity3 = addFakeProfile(db, 3, "Uelsessy Lasma", "/img/1.jpg", 5);

        addFakeFeed(db, task1, profileEntity1, "2017-10-22T14:30:00+11:00",
                "post", "Ali Kazi posted a fake feed with id 1");
        addFakeFeed(db, task2, profileEntity3, "2017-10-22T15:30:00+11:00",
                "post", "Ali Kazi posted a fake feed with id 2");
        addFakeFeed(db, task3, profileEntity3, "2017-10-22T17:30:00+11:00",
                "post", "Ali Kazi posted a fake feed with id 4");
        addFakeFeed(db, task1, profileEntity2, "2017-10-22T19:30:00+11:00",
                "post", "Ali Kazi posted a fake feed with id 6");
        addFakeFeed(db, task3, profileEntity1, "2017-10-22T17:30:00+11:00",
                "post", "Ali Kazi posted a fake feed with id 4");
        addFakeFeed(db, task4, profileEntity2, "2017-10-22T16:30:00+11:00",
                "post", "Ali Kazi posted a fake feed with id 3");
        addFakeFeed(db, task4, profileEntity1, "2017-10-22T18:30:00+11:00",
                "post", "Ali Kazi posted a fake feed with id 5");

    }

    private static void addFakeFeed(AppDatabase db, TaskEntity taskEntity, ProfileEntity profileEntity,
                                    String createdAt, String event, String text) {
        FeedEntity feedEntity = new FeedEntity();
        feedEntity.task_id = taskEntity.id;
        feedEntity.profile_id = profileEntity.id;
        feedEntity.created_at = createdAt;
        feedEntity.event = event;
        feedEntity.text = text;
        db.feedModel().insertFeed(feedEntity);
    }

    private static TaskEntity addFakeTask(AppDatabase db, int id, int posterId, int workerId, String name,
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

    private static ProfileEntity addFakeProfile(AppDatabase db, int id, String firstName,
                                                String miniUrl, int rating) {
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.id = id;
        profileEntity.first_name = firstName;
        profileEntity.avatar_mini_url = miniUrl;
        profileEntity.rating = rating;
        db.profileModel().insertProfile(profileEntity);
        return profileEntity;
    }

    public interface FakeDbCallbacksListener {
        void onFakeDbCreationComplete();
    }
}
