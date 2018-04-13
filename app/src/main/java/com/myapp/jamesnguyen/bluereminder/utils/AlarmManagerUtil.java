package com.myapp.jamesnguyen.bluereminder.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.myapp.jamesnguyen.bluereminder.broadcast_receivers.AlarmBroadcastReceiver;
import com.myapp.jamesnguyen.bluereminder.room.ItemEntity;

import java.util.Calendar;

public class AlarmManagerUtil {
    private static final long WEEKLY_INTERVAL = 604800000;
    private static final int DEFAULT_HOUR = 10;
    private static final int DEFAULT_MINUTE= 0;

    //If you app is deleted or re-run, the alarms will be gone
    //PASSED TEST
    public static boolean addAlarm(Context context, ItemEntity item){
        //set alarm only when there's an alarm and its time is not smaller than current time
        Calendar c = Calendar.getInstance();

        if(c.getTimeInMillis() < item.getDate()){
            c.setTimeInMillis(item.getDate());
            Intent intent = AlarmBroadcastReceiver.createIntent(context, item.getTitle());
            c.setTimeInMillis(item.getDate());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    item.getId(),
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            if(item.isHasDate() && item.isHasTime()){
                AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),pendingIntent);

            }
            return true;
        } else {
            return false;
        }
    }
    //OK
    public static void addDailyAlarm(Context context, ItemEntity item){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(item.getDate());

        Intent intent = AlarmBroadcastReceiver.createIntent(context, item.getTitle());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                item.getId(),
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP
                , c.getTimeInMillis()
                , AlarmManager.INTERVAL_DAY ,pendingIntent);
    }


    public static void removeAlarm(Context context, ItemEntity item){
        if(item.isHasAlarm()) {
            Intent intent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, item.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
            item.setHasDate(false);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
        }
    }

    public static boolean isItmeDateLargerThanCurrent(ItemEntity item){
        Calendar c = Calendar.getInstance();
        return item.getDate() > c.getTimeInMillis();
    }
}


//            if(item.isHasTime() && item.isHasDate()){
//                c.setTimeInMillis(item.getDate());
//            } else if(item.isHasDate()){
//                c.setTimeInMillis(item.getDate());
//                c.set(Calendar.HOUR_OF_DAY,DEFAULT_HOUR);
//                c.set(Calendar.MINUTE,DEFAULT_MINUTE);
//            } else if(item.isHasTime()){
//                Calendar temp = Calendar.getInstance();
//                temp.setTimeInMillis(item.getDate());
//                c.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
//                c.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
//            }

//
//
//    public static void addWeeklyAlarm(Context context, ItemEntity item){
//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(item.getDate());
//
//        Intent intent = AlarmBroadcastReceiver.createIntent(context, item.getTitle());
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
//                item.getId(),
//                intent,
//                PendingIntent.FLAG_CANCEL_CURRENT);
//
//        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, item.getDate(), WEEKLY_INTERVAL ,pendingIntent);
//    }