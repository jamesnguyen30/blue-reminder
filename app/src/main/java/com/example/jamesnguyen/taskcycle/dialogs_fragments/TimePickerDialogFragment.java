package com.example.jamesnguyen.taskcycle.dialogs_fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import com.example.jamesnguyen.taskcycle.fragments.ItemEditFragment;
import com.example.jamesnguyen.taskcycle.room.ItemEntity;

import java.util.Calendar;

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public static final String TAG = "TimePickerDialogFragment";
    public static final String DATE_ARGS = "date_args";
    public static final String HOUR_EXTRA = "hour_extra";
    public static final String MINUTE_EXTRA = "minute_extra";

    Calendar calendar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();

        if (args != null) {
            calendar = (Calendar) args.getSerializable(DATE_ARGS);
        } else {
            calendar = Calendar.getInstance();
        }
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                true);
    }

    public static TimePickerDialogFragment newInstance(ItemEntity item){
        Calendar date = Calendar.getInstance();
        TimePickerDialogFragment fragment = new TimePickerDialogFragment();
        if(item!=null){
            date.setTimeInMillis(item.getDate());
            Bundle args = new Bundle();
            args.putSerializable(DATE_ARGS, date);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Intent intent = new Intent();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        intent.putExtra(ItemEditFragment.CALENDAR_EXTRA, calendar);
        getTargetFragment().onActivityResult(ItemEditFragment.REQUEST_CODE,
                Activity.RESULT_OK, intent);
    }
}
