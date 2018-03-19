package com.example.jamesnguyen.taskcycle.recycler_view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jamesnguyen.taskcycle.R;
import com.example.jamesnguyen.taskcycle.mock_data.ReminderDatabaseMock;

/**
 * Created by jamesnguyen on 3/16/18.
 */

public class ReminderAdapter extends RecyclerView.Adapter<ReminderViewHolder>{

    //hold reference to the database
    //this is a mock database
    ReminderDatabaseMock database;
    Context context;

    public ReminderAdapter(Context context, ReminderDatabaseMock db) {
        this.context = context;
        database = db;
    }

    @Override
    public ReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.reminder_item, parent, false);

        return new ReminderViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(ReminderViewHolder holder, int position) {
        holder.bindView(database.getReminderAt(position));
    }

    @Override
    public int getItemCount() {
        return database.getDbSize();
    }

}
