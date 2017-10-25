package com.alikazi.cc_airtasker.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.alikazi.cc_airtasker.R;

/**
 * Created by kazi_ on 10/25/2017.
 */

public class RatingAdapter extends BaseAdapter {

    private static final int MAX_RATING = 5;

    private Context mContext;
    private int mRating;

    public RatingAdapter(Context context, int rating) {
        mContext = context;
        mRating = rating;
    }

    public void setRating(int rating) {
        mRating = rating;
    }

    @Override
    public int getCount() {
        return mRating != 0 ? mRating : MAX_RATING;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.grid_item_star, viewGroup, false);
        }
        return view;
    }
}
