package com.myapp.jamesnguyen.bluereminder.utils;

import java.util.concurrent.TimeUnit;

/**
 * Created by jamesnguyen on 3/26/18.
 */

public class TimeUnitUtil {
    private static final String FORMAT_HOUR_MINUTE_SECOND = "%02d:%02d:%02d";
    private static final String FORMAT_MINUTE_SECOND = "%02d:%02d";

    public static String parseTime(long milliseconds) {
        if((milliseconds)>=3600000){
            return String.format(FORMAT_HOUR_MINUTE_SECOND,
                    TimeUnit.MILLISECONDS.toHours(milliseconds),
                    TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(
                            TimeUnit.MILLISECONDS.toHours(milliseconds)),
                    TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
        } else{
            return String.format(FORMAT_MINUTE_SECOND,
                    TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(
                            TimeUnit.MILLISECONDS.toHours(milliseconds)),
                    TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
        }
    }

    public static String parseTime(int milliseconds){
        return parseTime((long)milliseconds);
    }
}
