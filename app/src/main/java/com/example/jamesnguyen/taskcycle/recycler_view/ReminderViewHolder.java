package com.example.jamesnguyen.taskcycle.recycler_view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.jamesnguyen.taskcycle.R;
import com.example.jamesnguyen.taskcycle.mock_data.ReminderMock;

import java.text.SimpleDateFormat;

/**
 * Created by jamesnguyen on 3/16/18.
 */

public class ReminderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView reminderTitle;
    TextView reminderDate;
    RadioButton isDoneButton;

    //Context to start activity
    Context context;
    SimpleDateFormat dateFormate = new SimpleDateFormat("EEE, M.d.yyyy hh:mm");

    public ReminderViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        reminderTitle = (TextView)itemView.findViewById(R.id.reminder_title);
        reminderDate = (TextView)itemView.findViewById(R.id.reminder_date);
    }

    public void bindView(ReminderMock data){
        reminderTitle.setText(data.getTitle());
        reminderDate.setText(dateFormate.format(data.getCalendar().getTime()));
    }
    @Override
    public void onClick(View v) {

    }
}
