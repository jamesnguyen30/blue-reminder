package com.example.jamesnguyen.taskcycle.notification_builder;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.example.jamesnguyen.taskcycle.R;
import com.example.jamesnguyen.taskcycle.activities.MainActivity;

/**
 * Created by jamesnguyen on 3/26/18.
 */

//TODO add channel to notification for Android O
public class NotificationBuilder {

    public static NotificationCompat.Builder buildNotification(Context context,
                                                               String channelId,
                                                               String contentTitle,
                                                               String contentDescription){

        Intent intent = createNewIntent(context, MainActivity.START_DEFAULT_FRAGMENT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, channelId)
                .setContentTitle(contentTitle)
                .setContentText(contentDescription)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_check_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.app_icon))
                .setAutoCancel(true)
                .setOnlyAlertOnce(true);
        return builder;
    }

    private static Intent createNewIntent(Context context, int fragmentCode){
        Intent intent = MainActivity.createIntent(context, fragmentCode);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }


}
