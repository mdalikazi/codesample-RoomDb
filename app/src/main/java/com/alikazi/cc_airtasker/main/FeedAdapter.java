package com.alikazi.cc_airtasker.main;

import android.content.Context;
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
import com.alikazi.cc_airtasker.models.Feed;
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

    private Context mContext;
    private boolean mAnimate;
    private ArrayList<Feed> mFeedList;

    public FeedAdapter(Context context) {
        mContext = context;
    }

    public void setFeedList(ArrayList<Feed> feedList) {
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
                view = LayoutInflater.from(mContext).inflate(R.layout.list_item_feed, parent, false);
                return new FeedListItemViewHolder(view);
            default:
                throw new RuntimeException("There are invalid view types in FeedAdapter!");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        animateList(holder.itemView);
        int adapterPostion = holder.getAdapterPosition();
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_ITEM:
                FeedListItemViewHolder listItemViewHolder = (FeedListItemViewHolder) holder;
                Feed feedItem = mFeedList.get(adapterPostion);

                Glide.with(mContext)
                        .load(feedItem.getProfile().getAvatarFullUrl())
                        .transition(new DrawableTransitionOptions().crossFade())
                        .apply(new RequestOptions().placeholder(R.mipmap.ic_person_black_24dp))
                        .into(listItemViewHolder.profilePhotoImageView);

                listItemViewHolder.taskDescriptionTextView.setText(feedItem.getProcessedText());
                listItemViewHolder.feedTypeTextView.setText(feedItem.getEvent());

                SimpleDateFormat dayTimeFormat = new SimpleDateFormat(AppConf.DATE_FORMAT_DAY_TIME, Locale.US);
                String dayTimeDateString = dayTimeFormat.format(feedItem.getCreatedAtJavaDate());
                listItemViewHolder.dateTextView.setText(dayTimeDateString);
                break;
                //TODO ADD COMMENTS
                //TODO ARCH COMPONENTS
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
            if (mFeedList.get(position) instanceof Feed) {
                return VIEW_TYPE_ITEM;
            }
        }

        throw new RuntimeException("There are invalid view types in FeedAdapter!");
    }

    private void animateList(View view) {
        if (!mAnimate) {
            return;
        }
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 1500, 0);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        translateAnimation.setDuration(1250);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
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
        private TextView taskDescriptionTextView;
        private TextView dateTextView;
        private TextView feedTypeTextView;

        public FeedListItemViewHolder(View itemView) {
            super(itemView);
            profilePhotoImageView = itemView.findViewById(R.id.list_item_feed_profile_photo);
            taskDescriptionTextView = itemView.findViewById(R.id.list_item_feed_task_description);
            dateTextView = itemView.findViewById(R.id.list_item_feed_date);
            feedTypeTextView = itemView.findViewById(R.id.list_item_feed_type);
        }
    }
}
