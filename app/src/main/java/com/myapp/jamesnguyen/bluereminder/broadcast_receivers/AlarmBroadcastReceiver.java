package com.myapp.jamesnguyen.bluereminder.broadcast_receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.myapp.jamesnguyen.bluereminder.activities.MainActivity;
import com.myapp.jamesnguyen.bluereminder.notification_builder.NotificationBuilder;

/**
 * Created by jamesnguyen on 3/24/18.
 */

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    private static final String TITLE_EXTRA = "title_extra";
    private static final String POSITION_EXTRA = "position_extra";
    //public static final int ALARM_REQUEST_CODE = 10;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Reciever", "onReceive");

        String title = intent.getStringExtra(TITLE_EXTRA);
        //build notification
        Notification notification = NotificationBuilder.buildNotification(context,
                "TaskCycleReminder",
                "Reminder: ",
                title).build();

        notification.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification);

//        NotificationManager manager = (NotificationManager)
    }

    public static Intent createIntent(Context context, String title){
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra(TITLE_EXTRA, title);
        return intent;
    }
}
