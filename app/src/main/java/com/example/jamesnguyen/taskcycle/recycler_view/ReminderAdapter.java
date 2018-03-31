package com.example.jamesnguyen.taskcycle.recycler_view;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jamesnguyen.taskcycle.R;
import com.example.jamesnguyen.taskcycle.room.ItemDatabase;
import com.example.jamesnguyen.taskcycle.room.ItemEntity;

import java.util.List;

/**
 * Created by jamesnguyen on 3/16/18.
 */

public class ReminderAdapter extends RecyclerView.Adapter<ReminderViewHolder>{

    //hold reference to the ItemDatabase
    //this is a mock ItemDatabase
    //ReminderDatabaseMock ItemDatabase;
    Context context;
    List<ItemEntity> items;
    int count;

    public ReminderAdapter(Context context) {
        this.context = context;
        count = 0;
        items = null;
    }

    @Override
    public ReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.reminder_item, parent, false);
        return new ReminderViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(ReminderViewHolder holder, int position) {
        //holder.bindView(database.getReminderAt(position));
        //ItemEntity item = db.queryTodayItems().get(position);
        holder.bindView(items.get(position));
    }

    @Override
    public int getItemCount() {
        return count;
        //return database.getDbSize();
    }

    public void updateItemList(List<ItemEntity> items){
        this.items = items;
        count = items.size();
    }
}
