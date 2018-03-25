package com.example.jamesnguyen.taskcycle.setting_utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.jamesnguyen.taskcycle.R;

/**
 * Created by jamesnguyen on 3/24/18.
 */
//this class retrieve shared preference file of the activity
    // to perform save and load data from the file
public class CycleSettingsUtil {

    private final String SP_WORK_LENGTH_KEY = "sp_work_length_key";
    private final String SP_SHORT_BREAK_LENGTH_KEY = "sp_work_length_key";
    private final String SP_LONG_BREAK_LENGTH_KEY = "sp_short_break_length_key";

    private final String SP_FILE_NAME = "sp_cycle_settings";

    private final int DEFAULT_WORK_TIME = 2700; // 45 mins
    private final int DEFAULT_SHORT_BREAK_TIME = 900; // 15 mins
    private final int DEFAULT_LONG_BREAK_TIME = 3600; // 1 hour

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    Context context;


    public CycleSettingsUtil(Context context) {
        this.context = context;
        sp = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public CycleSettingsUtil(String fileName, Context context){
        this.context = context;
        sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor= sp.edit();
    }

    public void saveWorkTime(int timeInSecond){
        editor.putInt(context.getResources().getString(R.string.sp_work_length_key)
                , timeInSecond);
        editor.commit();
    }

    public void saveShortBreakTime(int timeInSecond){
        editor.putInt(context.getResources().getString(R.string.sp_short_break_key),
                timeInSecond);
        editor.commit();
    }

    public void saveLongBreakTime(int timeInSecond){
        editor.putInt(context.getResources().getString(R.string.sp_long_break_key), timeInSecond);
        editor.commit();
    }

    public int getWorkTime(){
        return sp.getInt(context.getResources().getString(R.string.sp_work_length_key), DEFAULT_WORK_TIME);
    }

    public int getShortBreakTime(){
        return sp.getInt(context.getResources().getString(R.string.sp_short_break_key),DEFAULT_SHORT_BREAK_TIME);
    }

    public int getLongBreakTime(){
        return sp.getInt(context.getResources().getString(R.string.sp_long_break_key),DEFAULT_LONG_BREAK_TIME);
    }


}
