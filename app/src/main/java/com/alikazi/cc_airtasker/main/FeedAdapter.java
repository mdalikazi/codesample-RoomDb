package com.alikazi.cc_airtasker.main;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.alikazi.cc_airtasker.R;
import com.alikazi.cc_airtasker.conf.AppConf;
import com.alikazi.cc_airtasker.db.entities.FeedWithTaskAndProfile;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by kazi_ on 10/18/2017.
 */

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String LOG_TAG = AppConf.LOG_TAG_CC_AIRTASKER;

    private static final int VIEW_TYPE_ITEM = 0;

    private Activity mActivityContext;
    private boolean mAnimate;
    private ArrayList<FeedWithTaskAndProfile> mFeedList;

    public FeedAdapter(Activity activityContext) {
        mActivityContext = activityContext;
    }

    public void setFeedList(ArrayList<FeedWithTaskAndProfile> feedList) {
        Log.i(LOG_TAG, "setFeedList");
        mAnimate = true;
        mFeedList = new ArrayList<>();
        mFeedList.clear();
        mFeedList = feedList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                view = LayoutInflater.from(mActivityContext).inflate(R.layout.list_item_feed, parent, false);
                return new FeedListItemViewHolder(view);
            default:
                throw new RuntimeException("There are invalid view types in FeedAdapter!");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        animateList(holder.itemView);
        int adapterPostion = holder.getAdapterPosition();
        // Such a switch is useful to have different view types in the adapter.
        // In our case we have only one view type
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_ITEM:
                FeedListItemViewHolder listItemViewHolder = (FeedListItemViewHolder) holder;
                FeedWithTaskAndProfile feedItem = mFeedList.get(adapterPostion);

                final String photoUrl = feedItem.profiles.get(0).avatarFullUrl;
                final String taskName = feedItem.tasks.get(0).name;
                final String description = feedItem.tasks.get(0).description;
                final boolean assigned = feedItem.tasks.get(0).state.contentEquals("assigned");

                Glide.with(mActivityContext)
                        .load(photoUrl)
                        .transition(new DrawableTransitionOptions().crossFade())
                        .apply(new RequestOptions().placeholder(R.mipmap.ic_person_black_24dp))
                        .into(listItemViewHolder.profilePhotoImageView);

                listItemViewHolder.taskNameTextView.setText(taskName);
                listItemViewHolder.feedTypeTextView.setText(feedItem.feed.event);

                SimpleDateFormat dayTimeFormat = new SimpleDateFormat(AppConf.DATE_FORMAT_DAY_TIME, Locale.US);
                String dayTimeDateString = dayTimeFormat.format(feedItem.feed.createdAtJavaDate);
                listItemViewHolder.dateTextView.setText(dayTimeDateString);

                listItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final DetailsDialogFragment detailsDialogFragment =
                                DetailsDialogFragment.newInstance(taskName, photoUrl, description, assigned);
                        detailsDialogFragment.show(mActivityContext.getFragmentManager(), "detailDialog");
                    }
                });

                break;
                //TODO FIX MAIN THREAD ISSUE
                //TODO ADD SPLASHSCREENS
        }
    }

    @Override
    public int getItemCount() {
        if (mFeedList != null) {
            return mFeedList.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mFeedList != null) {
            if (mFeedList.get(position) instanceof FeedWithTaskAndProfile) {
                return VIEW_TYPE_ITEM;
            }
        }

        throw new RuntimeException("There are invalid view types in FeedAdapter!");
    }

    private void animateList(View view) {
        if (!mAnimate) {
            return;
        }
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 1000, 0);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        translateAnimation.setDuration(1000);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Animate only once at the start
                mAnimate = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(translateAnimation);
    }

    private class FeedListItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView profilePhotoImageView;
        private TextView taskNameTextView;
        private TextView dateTextView;
        private TextView feedTypeTextView;

        public FeedListItemViewHolder(View itemView) {
            super(itemView);
            profilePhotoImageView = itemView.findViewById(R.id.list_item_feed_profile_photo);
            taskNameTextView = itemView.findViewById(R.id.list_item_feed_task_name);
            dateTextView = itemView.findViewById(R.id.list_item_feed_date);
            feedTypeTextView = itemView.findViewById(R.id.list_item_feed_type);
        }
    }
}
