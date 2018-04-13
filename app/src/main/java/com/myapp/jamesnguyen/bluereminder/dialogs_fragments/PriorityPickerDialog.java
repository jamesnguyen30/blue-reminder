package com.myapp.jamesnguyen.bluereminder.dialogs_fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.myapp.jamesnguyen.bluereminder.R;

public class PriorityPickerDialog extends DialogFragment{
    public static final String TAG = "PriorityPickerDialog";
    public static final int REQUEST_CODE = 6;
    public static final String PRIORITY_EXTRA = "priority_extra";

    public static final String[] priority = new String[]{
            "Default",
            "Important",
            "Urgent",
            "Urgent and Important"
    };
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pick Priority")
                .setItems(priority, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra(PRIORITY_EXTRA, which);
                        getTargetFragment().onActivityResult(REQUEST_CODE, Activity.RESULT_OK, intent);
                    }
                });

        return builder.create();
    }
}
