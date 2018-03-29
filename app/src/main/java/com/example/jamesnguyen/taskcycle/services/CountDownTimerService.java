package com.example.jamesnguyen.taskcycle.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.jamesnguyen.taskcycle.activities.MainActivity;
import com.example.jamesnguyen.taskcycle.broadcast_receivers.CountDownTimerReceiver;
import com.example.jamesnguyen.taskcycle.notification_builder.NotificationBuilder;
import com.example.jamesnguyen.taskcycle.utils.TimeUnitUtil;

/**
 * Created by jamesnguyen on 3/26/18.
 */

//TODO This Service runs on Main Thread, fix it
//TODO Add stop condition for the service
public class CountDownTimerService extends Service {

    //intent action
    public static final String SERVICE_ACTION = "count_down_time_service_action";

    public static final String MILLIS_UNTIL_FINISHED_EXTRA = "millis_until_finished";

    //private static final String SERVICE_NAME = "CountDownTimerService";
    public static final String MILLIS_EXTRA_TAG = "RunningWorkCycleServiceSecond";
    private static final String CHANNEL_ID = "RunningWorkCycleServiceChannelId";

    private static final int ON_GOING_NOTIFICATION_ID = 1;

    CountDownTimer timer;
    Intent intentToSend;

    NotificationCompat.Builder notificationBuilder;
    NotificationManagerCompat notificationManager;

    long millisUntilFinished;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = NotificationManagerCompat.from(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //TODO : get this from SharedPreference instead
        millisUntilFinished = intent.getLongExtra(MILLIS_EXTRA_TAG, -1);

        notificationBuilder = NotificationBuilder.buildNotification(
                this,
                CHANNEL_ID,
                TimeUnitUtil.parseTime(millisUntilFinished),
                "Work in progress ... "
        );

        startForeground(ON_GOING_NOTIFICATION_ID, notificationBuilder.build());

        if (millisUntilFinished !=-1) {
            startCountDownTimer(millisUntilFinished);
       }
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        Log.d("CountDownTimerService", "Service Stopped");
        broadcastMillisUntilFinished();
        super.onDestroy();
    }

    public static Intent createIntent(Context context, long millsUntilFinished) {
        Intent intent = new Intent(context, CountDownTimerService.class);
        intent.putExtra(MILLIS_EXTRA_TAG, millsUntilFinished);
        return intent;
    }

    public void setMillisUntilFinished(long millisUntilFinished){
        this.millisUntilFinished = millisUntilFinished;
    }

    private void startCountDownTimer(long timeInMillis){
        timer = new CountDownTimer(timeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                setMillisUntilFinished(millisUntilFinished);
                notificationBuilder.setContentTitle(TimeUnitUtil.parseTime(millisUntilFinished));
                notificationManager.notify(ON_GOING_NOTIFICATION_ID, notificationBuilder.build());
                setMillisUntilFinished(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                //Log.d("CountDownTimerService", "Time's Up");
                notificationBuilder.setContentTitle("Done");
                notificationManager.notify(ON_GOING_NOTIFICATION_ID, notificationBuilder.build());
                //TODO Ring the phone to notify it's done
            }
        };
        timer.start();
    }

    private void broadcastMillisUntilFinished(){
        intentToSend = new Intent();
        intentToSend.setAction(SERVICE_ACTION);
        intentToSend.putExtra(MILLIS_UNTIL_FINISHED_EXTRA, millisUntilFinished);
        sendBroadcast(intentToSend);
    }

    public static IntentFilter getIntentFilter(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(SERVICE_ACTION);
        return filter;
    }

}