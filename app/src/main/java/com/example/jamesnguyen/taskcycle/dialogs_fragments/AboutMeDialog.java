package com.example.jamesnguyen.taskcycle.dialogs_fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.example.jamesnguyen.taskcycle.R;

public class AboutMeDialog extends DialogFragment {

    public static final String TAG = "AboutMeDialog";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.about_me_dialog_layout, null);
        builder.setView(view)
                .setTitle("")
                .setPositiveButton("I Will", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
        return builder.create();

    }
}
