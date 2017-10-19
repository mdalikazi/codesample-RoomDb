package com.alikazi.cc_airtasker;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alikazi.cc_airtasker.conf.AppConf;
import com.alikazi.cc_airtasker.conf.NetConstants;
import com.alikazi.cc_airtasker.models.Feed;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
        Log.i(LOG_TAG, "setFeedList");
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
        int adapterPostion = holder.getAdapterPosition();
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_ITEM:
                FeedListItemViewHolder listItemViewHolder = (FeedListItemViewHolder) holder;
                Feed feedItem = mFeedList.get(adapterPostion);
                Uri.Builder uriBuilder = new Uri.Builder()
                        .scheme(NetConstants.SCHEME_HTTPS)
                        .authority(NetConstants.STAGE_AIRTASKER)
                        .appendPath(NetConstants.ANDROID_CODE_TEST);
                String imageUrl = uriBuilder.build().toString() + feedItem.getProfile().getAvatar_mini_url();
                Glide.with(mContext)
                        .load(imageUrl)
                        .apply(new RequestOptions().placeholder(R.mipmap.ic_person_black_24dp))
                        .into(listItemViewHolder.profilePhotoImageView);
                listItemViewHolder.taskDescriptionTextView.setText(feedItem.getText());
                listItemViewHolder.feedTypeTextView.setText(feedItem.getEvent());

                Date date = new Date();
                try {
                    SimpleDateFormat isoDateFormat = new SimpleDateFormat(AppConf.DATE_FORMAT_ISO, Locale.US);
                    date = isoDateFormat.parse(feedItem.getCreated_at());
                } catch (Exception e) {
                    Log.d(LOG_TAG, "Exception parsing iso date: " + e.toString());
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppConf.DATE_FORMAT_DAY_TIME, Locale.US);
                listItemViewHolder.dateTextView.setText(simpleDateFormat.format(date));
                break;
                //TODO CHECK IF DATE AND IMAGE URL CAN BE PARSED IN MODEL
                //TODO ADD COMMENTS
                //TODO ARCH COMPONENTS
                //TODO HIDE FAB
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
