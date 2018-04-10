package com.example.jamesnguyen.taskcycle.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import java.util.Calendar;
import java.util.List;


@Database(
        entities = {ItemEntity.class},
        version = ItemDatabase.VERSION,
        exportSchema = false
)
public abstract class ItemDatabase extends RoomDatabase {
    private static ItemDatabase INSTANCE;
    public static final int VERSION = 3;
    public abstract ItemDao getItemDao();

    //TODO fix this design, this class should not hold List<ItemEntity> instance
    List<ItemEntity> items;

    public static ItemDatabase getInstance(Context context){
        if(INSTANCE==null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    ItemDatabase.class, "item-database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public List<ItemEntity> getItems() {
        return items;
    }

    public static void destroyInstance(){
        if(INSTANCE!=null){
            INSTANCE=null;
        }
    }

    public List<ItemEntity> queryAllItems(){
        items = getItemDao().getAll();
        return items;
    }

    public void insertNewItem(ItemEntity[] items){
        getItemDao().insert(items);
        return;
    }

    public void deleteItem(ItemEntity[] items){
        getItemDao().delete(items);
        return;
    }

    public void updateItem(ItemEntity[] items){
        getItemDao().update(items);
        return;
    }

    public List<ItemEntity> queryTodayItems(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        long from = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        long to = calendar.getTimeInMillis();
        items = getItemDao().getItemsBetweenDates(from, to);
        return items;
    }

    public List<ItemEntity> queryThisWeekItems(){
        Calendar calendar = Calendar.getInstance();
        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        long from = calendar.getTimeInMillis();

        calendar.set(Calendar.DAY_OF_MONTH, currentDayOfMonth + 7);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        long to = calendar.getTimeInMillis();
        items = getItemDao().getItemsBetweenDates(from, to);
        return items;

    }

    public List<ItemEntity> queryItemsByPriority(int priority){
        items = getItemDao().getItemsByPriority(priority);
        return items;
    }

//    public List<ItemEntity> queryThisWeekItems(){
//
//    }

    public int getLoadedItemsCount(){
        if(items!=null)
            return items.size();
        else
            return 0;
    }
}
