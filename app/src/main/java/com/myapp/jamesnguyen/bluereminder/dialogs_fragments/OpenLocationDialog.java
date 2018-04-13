package com.myapp.jamesnguyen.bluereminder.dialogs_fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.myapp.jamesnguyen.bluereminder.R;

public class OpenLocationDialog extends DialogFragment {
    public static final String TAG = "OpenLocationDialog";
    private static final String NAVIGATIONAL_QUERY = "google.navigation:q=";
    private static final String PLACE_NAME_ARGS = "place_name";
    private static final String READABLE_ADDRESS_ARGS = "readable_addres";

    TextView mAddress;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        Bundle args = getArguments();
        String placeName = args.getString(PLACE_NAME_ARGS);
        final String readableAddress = args.getString(READABLE_ADDRESS_ARGS);

        View view = inflater.inflate(R.layout.open_location_dialog, null);
        mAddress = view.findViewById(R.id.location_address_text_view);

        if(readableAddress.equals("")){
            mAddress.setText("Location is not added");
        } else mAddress.setText(readableAddress);

        String positiveButtonText;
        if(readableAddress.equals("")){
            positiveButtonText = "Open Map";
        } else {
            positiveButtonText = "Go";
        }
        builder.setView(view)
                .setTitle("Open direction to " + placeName)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO send intent to google map for direction to a place
                        if(!readableAddress.equals("")){
                            startDirectionFromMapApp(readableAddress);
                        } else {
                            startMapOnly();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }


    private void startMapOnly(){
        Intent mapIntent= new Intent(Intent.ACTION_VIEW, null);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private void startDirectionFromMapApp(String readableAddress){
        Uri gmmIntentUri = Uri.parse(NAVIGATIONAL_QUERY + readableAddress);
        Intent mapIntent= new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public static OpenLocationDialog newInstance(String placeName, String readableAddress){
        Bundle args = new Bundle();
        args.putString(PLACE_NAME_ARGS, placeName);
        args.putString(READABLE_ADDRESS_ARGS, readableAddress);
        OpenLocationDialog dialogFragment = new OpenLocationDialog();
        dialogFragment.setArguments(args);
        return dialogFragment;
    }
}
