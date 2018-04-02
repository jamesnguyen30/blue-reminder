package com.example.jamesnguyen.taskcycle.utils;

import com.example.jamesnguyen.taskcycle.room.ItemEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeToStringUtil {
    private static final String HAS_DATE_WITH_TIME = "EEE, M.d.yyyy h:mm a";
    private static final String HAS_DATE = "EEE, M.d.yyyy";
    private static final String HAS_TIME = "h:mm a";
    private static final String NO_DATE ="";

    public static String itemEntityToString(ItemEntity item){
        String pattern = NO_DATE;
        SimpleDateFormat dateFormate = new SimpleDateFormat(HAS_DATE);
        String result;
        Calendar calendar = Calendar.getInstance();
        if(item.isHasDate()){
            pattern = HAS_DATE;
            if(item.isHasTime()){
                pattern = HAS_DATE_WITH_TIME;

            }
        }
        if(item.isHasTime()){
            pattern = HAS_TIME;
            if(item.isHasDate()){
                pattern = HAS_DATE_WITH_TIME;
            }
        }

        dateFormate.applyPattern(pattern);
        String today = "Today at ";
        if(pattern==HAS_DATE_WITH_TIME || pattern==HAS_DATE){
            today = "";
        } else if(pattern == NO_DATE){
            today = "Today";
        }
        calendar.setTimeInMillis(item.getDate());
        result = today + dateFormate.format(calendar.getTime());
        return result;
    }
}
