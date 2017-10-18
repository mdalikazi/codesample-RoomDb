package com.alikazi.cc_airtasker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alikazi.cc_airtasker.conf.AppConf;
import com.alikazi.cc_airtasker.models.Feed;

import java.util.ArrayList;

/**
 * Created by kazi_ on 10/18/2017.
 */

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String LOG_TAG = AppConf.LOG_TAG_CC_AIRTASKER;

    private static final int VIEW_TYPE_ITEM = 0;

    private Context mContext;
    private ArrayList<Feed> mFeedList;

    public FeedAdapter(Context context) {
        mContext = context;
    }

    public void setFeedList(ArrayList<Feed> feedList) {
        mFeedList = new ArrayList<>();
        mFeedList.clear();
        mFeedList = feedList;
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
        int adapterPostion = holder.getAdapterPosition();
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_ITEM:
                FeedListItemViewHolder listItemViewHolder = (FeedListItemViewHolder) holder;
                Feed feedItem = mFeedList.get(adapterPostion);
//                listItemViewHolder.profilePhotoImageView.setImageResource(feedItem.);
                listItemViewHolder.taskDescriptionTextView.setText(feedItem.getText());
                listItemViewHolder.dateTextView.setText(feedItem.getEvent());
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("")
//                feedItem.getCreated_at()
                break;

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
