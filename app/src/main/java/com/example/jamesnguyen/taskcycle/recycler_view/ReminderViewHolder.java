package com.example.jamesnguyen.taskcycle.recycler_view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.jamesnguyen.taskcycle.R;
import com.example.jamesnguyen.taskcycle.activities.MainActivity;
import com.example.jamesnguyen.taskcycle.fragments.ItemEditFragment;
import com.example.jamesnguyen.taskcycle.room.ItemEntity;
import com.example.jamesnguyen.taskcycle.utils.DateTimeToStringUtil;

import java.util.Calendar;

/**
 * Created by jamesnguyen on 3/16/18.
 */

public class ReminderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView reminderTitle;
    TextView reminderDate;
    TextView placeName;
    Calendar calendar;
    ItemEntity item;

    Context context;

    public ReminderViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        calendar = Calendar.getInstance();
        reminderTitle = itemView.findViewById(R.id.reminder_title);
        reminderDate = itemView.findViewById(R.id.reminder_date);
        placeName = itemView.findViewById(R.id.place_name_text_view);
        itemView.setOnClickListener(this);
    }

    public void bindView(ItemEntity item){
        this.item = item;
        reminderTitle.setText(item.getTitle());
        reminderDate.setText(DateTimeToStringUtil.itemEntityToString(item));
        placeName.setText(item.getPlaceName());
    }

    @Override
    public void onClick(View v) {
        Bundle args = ItemEditFragment.creatBundle(item, getAdapterPosition());
        ((MainActivity)context).startFragmentWithBackStack(MainActivity.START_EDIT_FRAGMENT,
                MainActivity.REPLACE_FLAG,
                args);
    }
}