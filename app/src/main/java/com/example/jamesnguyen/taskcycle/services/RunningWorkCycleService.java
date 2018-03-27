package com.example.jamesnguyen.taskcycle.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.jamesnguyen.taskcycle.notification_builder.NotificationBuilder;
import com.example.jamesnguyen.taskcycle.utils.TimeUnitUtil;

/**
 * Created by jamesnguyen on 3/26/18.
 */

public class RunningWorkCycleService extends Service {
    private static final String SERVICE_NAME = "RunningWorkCycleService";
    private static final String SECONDS_EXTRA_TAG = "RunningWorkCycleServiceSecond";
    private static final String CHANNEL_ID = "RunningWorkCycleServiceChannelId";
    private static final int ON_GOING_NOTIFICATION_ID = 1;
    PendingIntent pendingIntent;
    CountDownTimer timer;
    NotificationCompat.Builder notificationBuilder;
    NotificationManagerCompat notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = NotificationManagerCompat.from(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int seconds = intent.getIntExtra(SECONDS_EXTRA_TAG, 0);
        notificationBuilder = NotificationBuilder.buildNotification(
                this,
                CHANNEL_ID,
                TimeUnitUtil.parseTime(seconds*1000),
                "Work in progress",
                pendingIntent
        );
        pendingIntent = PendingIntent.getActivity(this,
                0, intent, 0);

        startForeground(ON_GOING_NOTIFICATION_ID, notificationBuilder.build());

        if (seconds > 0) {
            timer = new CountDownTimer(seconds * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //Log.d("RunningWorkCycleService", "Remaining " + Long.toString(millisUntilFinished / 1000));
                    notificationBuilder.setContentTitle(TimeUnitUtil.parseTime(millisUntilFinished));
                    //Log.d("HEre", Long.toString(millisUntilFinished));
                    notificationManager.notify(ON_GOING_NOTIFICATION_ID, notificationBuilder.build());
                }

                @Override
                public void onFinish() {
                    //Log.d("RunningWorkCycleService", "Time's Up");
                    notificationBuilder.setContentTitle("Done");
                    notificationManager.notify(ON_GOING_NOTIFICATION_ID, notificationBuilder.build());
                }
            };
            timer.start();

       }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        Log.d("RunningWorkCycleService", "Service Stopped");
        super.onDestroy();

    }

    public static Intent createIntent(Context context, int seconds) {
        Intent intent = new Intent(context, RunningWorkCycleService.class);
        intent.putExtra(SECONDS_EXTRA_TAG, seconds);
        return intent;
    }
}