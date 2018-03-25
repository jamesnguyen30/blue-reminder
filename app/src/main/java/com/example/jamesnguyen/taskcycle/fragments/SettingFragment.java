package com.example.jamesnguyen.taskcycle.fragments;

import android.os.Bundle;

import android.preference.PreferenceFragment;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.annotation.Nullable;

import com.example.jamesnguyen.taskcycle.R;

/**
 * Created by jamesnguyen on 3/24/18.
 */

public class SettingFragment extends PreferenceFragmentCompat {

    public static final String TAG = "SettingFragment";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        addPreferencesFromResource(R.xml.preferences);
//    }

    public static SettingFragment newInstance(){
        return new SettingFragment();
    }
}
