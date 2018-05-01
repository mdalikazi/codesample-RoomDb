package com.alikazi.cc_airtasker.db;

import android.os.AsyncTask;

import com.alikazi.cc_airtasker.conf.AppConf;
import com.alikazi.cc_airtasker.db.entities.ProfileEntity;
import com.alikazi.cc_airtasker.db.entities.TaskEntity;

import static com.alikazi.cc_airtasker.db.DbHelper.addFeed;
import static com.alikazi.cc_airtasker.db.DbHelper.addProfile;
import static com.alikazi.cc_airtasker.db.DbHelper.addTask;

/**
 * Created by kazi_ on 10/22/2017.
 */

public class FakeDataDb {

    private static final String LOG_TAG = AppConf.LOG_TAG_ALI_KAZI;

    public static void initDbFakeDataAsync(final AppDatabase db, FakeDbCallbacksListener fakeDbCallbacksListener) {
        PopulateDbFakeDataAsync populateDbAsync = new PopulateDbFakeDataAsync(db, fakeDbCallbacksListener);
        populateDbAsync.execute();
    }

    private static class PopulateDbFakeDataAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;
        private final FakeDbCallbacksListener mFakeDbCallbacksListener;

        public PopulateDbFakeDataAsync(AppDatabase db, FakeDbCallbacksListener fakeDbCallbacksListener) {
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
        DbHelper.clearDbOnInit(db);

        TaskEntity task1 = addTask(db, 1, 1, 1, "Task 1", "Do task 1", "Assigned");
        TaskEntity task2 = addTask(db, 2, 1, 1, "Task 2", "Do task 2", "Completed");
        TaskEntity task3 = addTask(db, 3, 1, 1, "Task 3", "Do task 3", "Denied");
        TaskEntity task4 = addTask(db, 4, 1, 1, "Task 4", "Do task 4", "Finished");

        ProfileEntity profileEntity1 = addProfile(db, 1, "John Steep", "/img/4.jpg", 3);
        ProfileEntity profileEntity2 = addProfile(db, 2, "Mahnsja Herret", "/img/2.jpg", 1);
        ProfileEntity profileEntity3 = addProfile(db, 3, "Uelsessy Lasma", "/img/1.jpg", 5);

        addFeed(db, task1, profileEntity1, "2017-10-22T14:30:00+11:00",
                "post", "Ali Kazi posted a fake feed with id 1");
        addFeed(db, task2, profileEntity3, "2017-10-22T15:30:00+11:00",
                "post", "Ali Kazi posted a fake feed with id 2");
        addFeed(db, task3, profileEntity3, "2017-10-22T17:30:00+11:00",
                "post", "Ali Kazi posted a fake feed with id 4");
        addFeed(db, task1, profileEntity2, "2017-10-22T19:30:00+11:00",
                "post", "Ali Kazi posted a fake feed with id 6");
        addFeed(db, task3, profileEntity1, "2017-10-22T17:30:00+11:00",
                "post", "Ali Kazi posted a fake feed with id 4");
        addFeed(db, task4, profileEntity2, "2017-10-22T16:30:00+11:00",
                "post", "Ali Kazi posted a fake feed with id 3");
        addFeed(db, task4, profileEntity1, "2017-10-22T18:30:00+11:00",
                "post", "Ali Kazi posted a fake feed with id 5");
    }

    public interface FakeDbCallbacksListener {
        void onFakeDbCreationComplete();
    }
}
