package com.example.jamesnguyen.taskcycle.utils;

import android.util.Log;

import com.example.jamesnguyen.taskcycle.room.ItemEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateTimeToStringUtil {
    private static final String HAS_DATE_WITH_TIME = "EEE, M.d.yyyy h:mm a";
    private static final String HAS_DATE = "EEE, M.d.yyyy";
    private static final String HAS_TIME_WITH_DAY_OF_WEEK = "EEEE, hh:mm a";
    private static final String HAS_TIME = "hh:mm a";
    private static final String NO_DATE ="";

    public static String itemEntityToString(ItemEntity item){
        String pattern;
        SimpleDateFormat dateFormat = new SimpleDateFormat(HAS_DATE);

        String result;
        String additionalPattern ="";
        Calendar currentTime = GregorianCalendar.getInstance();
        Calendar itemDate = GregorianCalendar.getInstance();
        itemDate.setTimeInMillis(item.getDate());

        pattern = HAS_DATE_WITH_TIME;
        if(item.isHasTime() || item.isHasDate()) {
            if ((currentTime.get(Calendar.YEAR) - itemDate.get(Calendar.YEAR)) == 0) {
                int diff = itemDate.get(Calendar.DAY_OF_YEAR) - currentTime.get(Calendar.DAY_OF_YEAR);
                pattern = HAS_TIME;
                if (diff == 0) {
                    additionalPattern = "Today, ";
                } else if (diff == 1) {
                    additionalPattern = "Tomorrow, ";
                } else if (diff >= 2 && diff < 7) {
                    pattern = HAS_TIME_WITH_DAY_OF_WEEK;
                } else {
                    pattern = HAS_DATE_WITH_TIME;
                }
            }
        } else {
            pattern = NO_DATE;
        }

        dateFormat.applyPattern(pattern);
        result = additionalPattern + dateFormat.format(itemDate.getTime());

        return result;
    }

    public static String getTimeToString(ItemEntity itemEntity){
        SimpleDateFormat dateFormate = new SimpleDateFormat(HAS_TIME);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(itemEntity.getDate());
        return dateFormate.format(calendar.getTime());
    }

    public static String getDateToString(ItemEntity itemEntity){
        SimpleDateFormat dateFormate = new SimpleDateFormat("EEE, MMM d, yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(itemEntity.getDate());
        return dateFormate.format(calendar.getTime());
    }


}
