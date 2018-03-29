package com.example.jamesnguyen.taskcycle.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

import com.example.jamesnguyen.taskcycle.services.CountDownTimerService;
import com.example.jamesnguyen.taskcycle.utils.CycleSettingsUtil;

/**
 * Created by jamesnguyen on 3/24/18.
 */

public class CountDownTimerReceiver extends BroadcastReceiver {
    long millisUntilFinished;
    private CycleSettingsUtil settingsUtil;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Reciever", "onReceive");
        millisUntilFinished = intent.getLongExtra(CountDownTimerService.MILLIS_UNTIL_FINISHED_EXTRA, -1);
        //isPaused = false;
        //startCountDownTimer(milliUntilFinished);
    }

    public long getMillisUntilFinished(){
        return millisUntilFinished;
    }
}
