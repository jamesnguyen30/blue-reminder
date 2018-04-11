package com.example.jamesnguyen.taskcycle.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.jamesnguyen.taskcycle.R;
import com.example.jamesnguyen.taskcycle.activities.MainActivity;

/**
 * Created by jamesnguyen on 3/24/18.
 */

public class SettingFragment extends PreferenceFragmentCompat {

    public static final String TAG = "SettingFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        ((MainActivity)getActivity()).setToolbarTitleWithText("Settings");
        addPreferencesFromResource(R.xml.preferences);

    }

    public static SettingFragment newInstance(){
        return new SettingFragment();
    }
}
