package com.myapp.jamesnguyen.bluereminder.dialogs_fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import com.myapp.jamesnguyen.bluereminder.room.ItemEntity;

import java.util.Calendar;

public class DatePickerDialogFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public static final String TAG = "DatePickerDialogFragment";
    public static final String DATE_ARGS = "date_args";
    public static final String KEY_ARGS = "key_agrs";
    public static final int REQUEST_CODE = 1;
    public static final String CALENDAR_EXTRA = "calendar_extra";


    Calendar calendar;
    String extraKey;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        //Calendar calendar = null;
        if(args!=null){
            calendar = (Calendar) args.getSerializable(DATE_ARGS);
            extraKey = args.getString(KEY_ARGS);
        } else {
            calendar = Calendar.getInstance();
        }
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public static DatePickerDialogFragment newInstance(ItemEntity item){
        Calendar date = Calendar.getInstance();
        DatePickerDialogFragment fragment = new DatePickerDialogFragment();

        if(item!=null){
            date.setTimeInMillis(item.getDate());
        }
        Bundle args = new Bundle();
        args.putSerializable(DATE_ARGS, date);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Intent intent = new Intent();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        intent.putExtra(CALENDAR_EXTRA, calendar);
        getTargetFragment().onActivityResult(REQUEST_CODE,
                Activity.RESULT_OK, intent);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        getTargetFragment().onActivityResult(REQUEST_CODE,
                Activity.RESULT_CANCELED, null);
    }
}
