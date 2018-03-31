package com.example.jamesnguyen.taskcycle.recycler_view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.jamesnguyen.taskcycle.R;
import com.example.jamesnguyen.taskcycle.room.ItemEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by jamesnguyen on 3/16/18.
 */

public class ReminderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView reminderTitle;
    TextView reminderDate;
    RadioButton isDoneButton;
    Calendar calendar;

    //Context to start activity
    Context context;

    private final String HAS_DATE_WITH_TIME = "EEE, M.d.yyyy h:mm a";
    private final String HAS_DATE = "EEE, M.d.yyyy";
    private final String HAS_TIME = "h:mm a";
    private final String NO_DATE ="";
//    private String chosenDatePattern;
    SimpleDateFormat dateFormate = new SimpleDateFormat(HAS_DATE);

    public ReminderViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        calendar = Calendar.getInstance();
        reminderTitle = (TextView)itemView.findViewById(R.id.reminder_title);
        reminderDate = (TextView)itemView.findViewById(R.id.reminder_date);
    }

    public void bindView(ItemEntity item){
        reminderTitle.setText(item.getTitle());
        //Log.d("Here", reminderDate.getText().toString());
        String pattern = NO_DATE;

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
        reminderDate.setText(today + dateFormate.format(calendar.getTime()));

    }
    @Override
    public void onClick(View v) {

    }
}