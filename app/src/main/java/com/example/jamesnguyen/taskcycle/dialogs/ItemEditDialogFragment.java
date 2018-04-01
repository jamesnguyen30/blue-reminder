package com.example.jamesnguyen.taskcycle.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jamesnguyen.taskcycle.R;

public class ItemEditDialogFragment extends DialogFragment {

    public static final String TAG ="ItemEditDiaglogFragment";
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
//
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.edit_item_dialog, null))
                .setTitle("Reminder")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    public static ItemEditDialogFragment newInstance(){
        return new ItemEditDialogFragment();
    }
}
