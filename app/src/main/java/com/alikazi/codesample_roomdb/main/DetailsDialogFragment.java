package com.alikazi.codesample_roomdb.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alikazi.codesample_roomdb.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by alikazi on 25/10/17.
 */

public class DetailsDialogFragment extends DialogFragment {

    private static final String BUNDLE_EXTRA_TITLE = "BUNDLE_EXTRA_TITLE";
    private static final String BUNDLE_EXTRA_PROFILE_PHOTO = "BUNDLE_EXTRA_PROFILE_PHOTO";
    private static final String BUNDLE_EXTRA_DESCRIPTION = "BUNDLE_EXTRA_DESCRIPTION";
    private static final String BUNDLE_EXTRA_ASSIGNED = "BUNDLE_EXTRA_ASSIGNED";
    private static final String BUNDLE_EXTRA_RATING = "BUNDLE_EXTRA_RATING";

    public static DetailsDialogFragment newInstance(String taskName, String photoUrl, String description, int rating, boolean assigned) {
        DetailsDialogFragment detailsDialogFragment = new DetailsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_EXTRA_TITLE, taskName);
        bundle.putString(BUNDLE_EXTRA_PROFILE_PHOTO, photoUrl);
        bundle.putString(BUNDLE_EXTRA_DESCRIPTION, description);
        bundle.putInt(BUNDLE_EXTRA_RATING, rating);
        bundle.putBoolean(BUNDLE_EXTRA_ASSIGNED, assigned);
        detailsDialogFragment.setArguments(bundle);
        return detailsDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View detailView = LayoutInflater.from(getActivity()).inflate(R.layout.details_dialog, null);
        TextView title = detailView.findViewById(R.id.details_dialog_title);
        ImageView profilePhoto = detailView.findViewById(R.id.details_dialog_profile_photo);
        TextView description = detailView.findViewById(R.id.details_dialog_description);
        GridView ratingGridView = detailView.findViewById(R.id.details_dialog_rating_grid_view);

        title.setText(getArguments().getString(BUNDLE_EXTRA_TITLE));
        description.setText(getArguments().getString(BUNDLE_EXTRA_DESCRIPTION));

        final boolean assigned = getArguments().getBoolean(BUNDLE_EXTRA_ASSIGNED);
        if (assigned) {
            title.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.color_task_assigned));
        } else {
            title.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.color_task_open));
        }

        Glide.with(getActivity())
                .asBitmap()
                .apply(new RequestOptions().placeholder(R.mipmap.ic_person_black_24dp))
                .transition(new BitmapTransitionOptions().crossFade())
                .load(getArguments().getString(BUNDLE_EXTRA_PROFILE_PHOTO))
                .into(profilePhoto);

        RatingAdapter ratingAdapter = new RatingAdapter(getActivity(), getArguments().getInt(BUNDLE_EXTRA_RATING));
        ratingGridView.setAdapter(ratingAdapter);

        builder.setView(detailView);

        builder.setPositiveButton(assigned ?
                        getString(R.string.details_dialog_action_ok) : getString(R.string.details_dialog_action_accept),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (assigned) {
                            // IRL, it would trigger task accept logic
                            dialogInterface.dismiss();
                        } else {
                            dialogInterface.dismiss();
                        }
                    }
                });

        builder.setNegativeButton(getString(R.string.details_dialog_action_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        return builder.create();
    }
}
