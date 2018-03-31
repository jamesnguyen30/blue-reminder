package com.example.jamesnguyen.taskcycle.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

@Database(
        entities = {ItemEntity.class},
        version = ItemDatabase.VERSION,
        exportSchema = false
)
public abstract class ItemDatabase extends RoomDatabase {
    private static ItemDatabase INSTANCE;
    public static final int VERSION = 1;
    public abstract ItemDao getItemDao();
    //private final String format = "EEE, M.d.yyyy h:mm a";

    public static ItemDatabase getInstance(Context context){
        if(INSTANCE==null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    ItemDatabase.class, "item-database")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance(){
        if(INSTANCE!=null){
            INSTANCE=null;
        }
    }

    public List<ItemEntity> getAllItems(){
        return getItemDao().getAll();
    }

    public List<ItemEntity> getTodayItems(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        long from = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        long to = calendar.getTimeInMillis();
        return getItemDao().getItemsBetweenDates(from, to);
    }

    public int getAllItemsCount(){
        return getItemDao().size();
    }
}
