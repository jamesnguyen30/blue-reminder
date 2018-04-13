package com.myapp.jamesnguyen.bluereminder.recycler_view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.myapp.jamesnguyen.bluereminder.R;
import com.myapp.jamesnguyen.bluereminder.dialogs_fragments.OpenLocationDialog;
import com.myapp.jamesnguyen.bluereminder.room.ItemEntity;
import com.myapp.jamesnguyen.bluereminder.utils.AlarmManagerUtil;
import com.shashank.sony.fancytoastlib.FancyToast;

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
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
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
        FancyToast.makeText(context, "Items Loaded", Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
    }

    public void onDeleteItemAt(int position){
        FancyToast.makeText(context, "Marked Done", Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();

        ItemEntity item = items.remove(position);
        count = items.size();
        mCallback.deleteItem(item);
        notifyItemRemoved(position);
    }

    public void onUpdateItemAt(ItemEntity item, int position){
        FancyToast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
        items.set(position, item);
        mCallback.updateItem(item);
        notifyItemChanged(position);
    }

    public void onOpenLocationDialog(int position){
        //TODO start an edit dialog
        //TODO Update item implementation
        OpenLocationDialog dialog = OpenLocationDialog.newInstance(items.get(position).getPlaceName(),
                items.get(position).getReadableAddress());
        dialog.show(((AppCompatActivity)context).getSupportFragmentManager(), OpenLocationDialog.TAG);
        notifyItemChanged(position);
    }
//
//    public ItemEntity getItemWithId(int id){
//        for(ItemEntity item:items){
//            if(item.getId()==id){
//                return item;
//            }
//        }
//        return null;
//    }

}
