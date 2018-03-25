package com.example.jamesnguyen.taskcycle.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

/**
 * Created by jamesnguyen on 3/24/18.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
//        vibrator.vibrate(5000);

        Log.d("AlarmReceiver","Alarm went off");
    }
}
