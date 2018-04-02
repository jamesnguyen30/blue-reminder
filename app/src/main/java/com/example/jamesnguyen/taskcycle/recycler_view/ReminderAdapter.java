package com.example.jamesnguyen.taskcycle.recycler_view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jamesnguyen.taskcycle.R;
import com.example.jamesnguyen.taskcycle.dialogs_fragments.OpenLocationDialog;
import com.example.jamesnguyen.taskcycle.room.ItemEntity;

import java.util.List;

/**
 * Created by jamesnguyen on 3/16/18.
 */

public class ReminderAdapter extends RecyclerView.Adapter<ReminderViewHolder> {

    public interface ReminderAdapterDbOperations {
        void deleteItem(ItemEntity items);
        void updateItem(ItemEntity items);
    }

    //hold reference to the ItemDatabase
    //this is a mock ItemDatabase
    //ReminderDatabaseMock ItemDatabase;
    Context context;
    List<ItemEntity> items;
    int count;
    ReminderAdapterDbOperations mCallback;

    public ReminderAdapter(Context context) {
        this.context = context;
        try{
            mCallback = ((ReminderAdapterDbOperations)context);
        } catch (ClassCastException e){
            e.printStackTrace();
        }
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
        notifyDataSetChanged();
    }

    public void onDeleteItemAt(int position){
        ItemEntity item = items.remove(position);
        count = items.size();
        mCallback.deleteItem(item);
        notifyDataSetChanged();
    }

    public void onUpdateItemAt(ItemEntity item, int position){
        items.set(position, item);
        mCallback.updateItem(item);
        notifyItemChanged(position);
    }

    public void onOpenLocationDialog(int position){
        //TODO start an edit dialog
        //TODO Update item implementation
        OpenLocationDialog dialog = OpenLocationDialog.newInstance();
        dialog.show(((AppCompatActivity)context).getSupportFragmentManager(), OpenLocationDialog.TAG);
        //revert the item back to its position
        notifyItemChanged(position);
    }
//
//    @Override
//    public void onItemMove(int from, int to) {
//        //TODO Implement when item move up or down
//    }
//
//    @Override
//    public void onItemRemoved(int position) {
//        //TODO Implment when item removed
//    }
}
