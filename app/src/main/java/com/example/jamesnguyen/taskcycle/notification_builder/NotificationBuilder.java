package com.example.jamesnguyen.taskcycle.notification_builder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.jamesnguyen.taskcycle.R;

/**
 * Created by jamesnguyen on 3/26/18.
 */

public class NotificationBuilder {

    public static NotificationCompat.Builder buildNotification(Context context,
                                          String channelId,
                                          String contentTitle,
                                          String contentDescription,
                                          PendingIntent pendingIntent){
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, channelId)
                .setContentTitle(contentTitle)
                .setContentText(contentDescription)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_timer_white_24dp)
                .setOnlyAlertOnce(true);
        return builder;

    }
}
