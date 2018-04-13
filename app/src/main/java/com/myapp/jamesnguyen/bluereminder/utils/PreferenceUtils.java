package com.myapp.jamesnguyen.bluereminder.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.myapp.jamesnguyen.bluereminder.R;

public class PreferenceUtils {
    Context context;
    SharedPreferences sharedPrefs;

    public PreferenceUtils(Context context) {
        this.context = context;
        this.sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean getAutoAlarmPref(){
        return sharedPrefs.getBoolean(context.getResources().getString(R.string.pref_auto_alarm)
                ,false);
    }
}
