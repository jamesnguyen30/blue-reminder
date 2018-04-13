package com.myapp.jamesnguyen.bluereminder.recycler_view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapp.jamesnguyen.bluereminder.R;
import com.myapp.jamesnguyen.bluereminder.activities.MainActivity;
import com.myapp.jamesnguyen.bluereminder.fragments.ItemEditFragment;
import com.myapp.jamesnguyen.bluereminder.room.ItemEntity;
import com.myapp.jamesnguyen.bluereminder.utils.AlarmManagerUtil;
import com.myapp.jamesnguyen.bluereminder.utils.DateTimeToStringUtil;

import java.util.Calendar;

/**
 * Created by jamesnguyen on 3/16/18.
 */

public class ReminderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView reminderTitle;
    TextView reminderDate;
    TextView placeName;
    ImageView alarmIcon;
    TextView overDue;
    Calendar calendar;
    ItemEntity item;
    ImageView straightBar;

    View itemView;
    Context context;

    public ReminderViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        calendar = Calendar.getInstance();
        reminderTitle = itemView.findViewById(R.id.reminder_title);
        reminderDate = itemView.findViewById(R.id.reminder_date);
        placeName = itemView.findViewById(R.id.place_name_text_view);
        alarmIcon = itemView.findViewById(R.id.alarm_icon);
        overDue = itemView.findViewById(R.id.overdue_text);
        straightBar = itemView.findViewById(R.id.color_straight_bar);
        itemView.setOnClickListener(this);
        this.itemView = itemView;

    }

    public void bindView(ItemEntity item){
        this.item = item;
        overDue.setVisibility(View.INVISIBLE);
        alarmIcon.setVisibility(View.INVISIBLE);
        reminderTitle.setText(item.getTitle());
        reminderDate.setText(DateTimeToStringUtil.itemEntityToString(item));
        placeName.setText(item.getPlaceName());
        if(item.isHasAlarm()){
            alarmIcon.setVisibility(View.VISIBLE);
        } else {
            alarmIcon.setVisibility(View.INVISIBLE);
        }

        if (!AlarmManagerUtil.isItmeDateLargerThanCurrent(item) &&(item.isHasTime() || item.isHasDate())) {
            alarmIcon.setVisibility(View.INVISIBLE);
            overDue.setVisibility(View.VISIBLE);
        }
        setPriority(item.getPriority(), itemView);
    }

    @Override
    public void onClick(View v) {
        Bundle args = ItemEditFragment.creatBundle(item, getAdapterPosition());
        ((MainActivity)context).startFragmentWithBackStack(MainActivity.START_EDIT_FRAGMENT,
                MainActivity.REPLACE_FLAG,
                args, MainActivity.NEW_FRAGMENT_ENTER_FROM_BOTTOM);

    }

    private void setPriority(int priority, View itemView){
        switch (priority) {
            default:
            case ItemEntity.PRIORITY_DEFAULT:
                straightBar.setBackgroundColor(
                        context.getResources().getColor(R.color.blue_dark)
                );
                itemView.setBackgroundColor(
                        context.getResources().getColor(R.color.white));
                break;
            case ItemEntity.PRIORITY_IMPORTANT:
                straightBar.setBackgroundColor(
                        context.getResources().getColor(R.color.green_dark)
                );
                itemView.setBackgroundColor(
                        context.getResources().getColor(R.color.green_light));
                break;
            case ItemEntity.PRIORITY_URGENT:
                straightBar.setBackgroundColor(
                        context.getResources().getColor(R.color.purple_dark)
                );
                itemView.setBackgroundColor(
                        context.getResources().getColor(R.color.purple_light));
                break;
            case ItemEntity.PRIORITY_URGENT_AND_IMPORTANT:
                straightBar.setBackgroundColor(
                        context.getResources().getColor(R.color.red_dark)
                );
                itemView.setBackgroundColor(
                        context.getResources().getColor(R.color.red_light));
                break;
        }
    }


//
//    private void showAlarmIcon(){
//        alarmIcon.setVisibility(View.VISIBLE);
//    }
//
//    private void hideAlarmIcon(){
//        alarmIcon.setVisibility(View.INVISIBLE);
//    }
//
//    private void showOverdue(){
//        overDue.setVisibility(View.VISIBLE);
//    }
//
//    private void hideOverdue(){
//        overDue.setVisibility(View.INVISIBLE);
//    }
}