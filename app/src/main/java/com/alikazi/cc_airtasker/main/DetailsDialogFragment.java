package com.alikazi.cc_airtasker.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.alikazi.cc_airtasker.R;

/**
 * Created by alikazi on 25/10/17.
 */

public class DetailsDialogFragment extends DialogFragment {

    public DetailsDialogFragment() {
        super();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        builder.setView(R.layout.details_dialog);
        return super.onCreateDialog(savedInstanceState);
    }
}
