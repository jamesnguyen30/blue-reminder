package com.example.jamesnguyen.taskcycle.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

/**
 * Created by jamesnguyen on 3/24/18.
 */

public class CountDownTimerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Reciever", "onReceive");

    }
}
