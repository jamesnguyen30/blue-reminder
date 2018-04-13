package com.myapp.jamesnguyen.bluereminder.dialogs_fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;

import com.myapp.jamesnguyen.bluereminder.R;

public class DonateDialog extends DialogFragment {

    public static final String TAG= "DonateDialog";
    Button oneDollarButton;
    Button threeDollaButton;
    Button fiveDollarButton;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.donate_dialog_layout, null);

        oneDollarButton = view.findViewById(R.id.one_dollar_donation);
        threeDollaButton = view.findViewById(R.id.three_dollar_donation);
        fiveDollarButton = view.findViewById(R.id.five_dollar_donation);
        builder.setTitle("Support Me")
                .setView(view)
                .setPositiveButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

        oneDollarButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        threeDollaButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        fiveDollarButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
        return builder.create();
    }
}
