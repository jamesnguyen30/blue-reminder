package com.example.jamesnguyen.taskcycle.dialogs_fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class UpdateGooglePlayServiceDialog extends DialogFragment {

    public static final String TAG = "UpdateGooglePlayService";
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("You must update your Google Play Service to use map features of the app")
                .setTitle("Update your Google Play Service")
                .setPositiveButton("I Will", null);
        return builder.create();
    }
}
