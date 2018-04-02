package com.example.jamesnguyen.taskcycle.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jamesnguyen.taskcycle.R;
import com.example.jamesnguyen.taskcycle.dialogs_fragments.DatePickerDialogFragment;
import com.example.jamesnguyen.taskcycle.dialogs_fragments.TimePickerDialogFragment;
import com.example.jamesnguyen.taskcycle.room.ItemEntity;
import com.example.jamesnguyen.taskcycle.utils.DateTimeToStringUtil;

import java.util.Calendar;

public class ItemEditFragment extends Fragment {

    public static final String TAG ="ItemEditDiaglogFragment";
    public static final String ITEM_ARGS = "item_data_args";
    public static final String POSITION_ARGS = "position_args";
    public static final String CALENDAR_EXTRA = "calendar_extra";
    public static int REQUEST_CODE= 1;

    EditText mTitle;
    TextView mDate;
    TextView mTime;
    TextView mLocation;
    ItemEntity item;
    int position;
    FloatingActionButton fab;
    boolean isChanged;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isChanged = false;
        //load arguement
        Bundle args = getArguments();
        item = args.getParcelable(ITEM_ARGS);
        position = args.getInt(POSITION_ARGS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_item_fragment, container, false);
        mTitle = view.findViewById(R.id.item_title_edit);
        mDate=  view.findViewById(R.id.item_date_edit);
        mTime = view.findViewById(R.id.item_time_edit);
        mLocation = view.findViewById(R.id.item_location_edit);

        mTitle.setText(item.getTitle().toString());
        mDate.setText(DateTimeToStringUtil.getDateToString(item));
        mTime.setText(DateTimeToStringUtil.getTimeToString(item));
        mLocation.setText("2413 Diamond Oaks, Garland, TX, 75044");

        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                item.setTitle(s.toString());
                isChanged = true;
            }
        });
        mDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO Open date picker dialog
                DatePickerDialogFragment dialogFragment;
                if(item.isHasDate()){
                    dialogFragment = DatePickerDialogFragment.newInstance(item);
                } else{
                    dialogFragment = DatePickerDialogFragment.newInstance(null);
                }
                dialogFragment.setTargetFragment(getActivity().getSupportFragmentManager().findFragmentByTag(TAG), REQUEST_CODE);
                dialogFragment.show(getActivity().getSupportFragmentManager(), DatePickerDialogFragment.TAG);
            }
        });

        mTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO Open time picker dialog
                TimePickerDialogFragment dialogFramgment;
                if(item.isHasTime()){
                    dialogFramgment = TimePickerDialogFragment.newInstance(item);
                } else {
                    dialogFramgment = TimePickerDialogFragment.newInstance(null);
                }
                dialogFramgment.setTargetFragment(getActivity().getSupportFragmentManager().findFragmentByTag(TAG), REQUEST_CODE );
                dialogFramgment.show(getActivity().getSupportFragmentManager(), TimePickerDialogFragment.TAG);
            }
        });

        mLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO Open location picker ( could be dialog or fragment

            }
        });

        fab = container.getRootView().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fab.setVisibility(View.VISIBLE);
        //testChange();
        //if item is edited
        if(isChanged){
            Intent intent = ReminderFragment.createItent(item, position);
            //send data to Reminder Fragmetn
            Fragment fragment = getTargetFragment();
            fragment.onActivityResult(0, Activity.RESULT_OK, intent);
        }

    }

    private void testChange(){
        isChanged = true;
        item.setTitle("Changed the title");
    }

    public static Bundle creatBundle(ItemEntity item, int position){
        Bundle args = new Bundle();
        //args.putSerializable(item);
        args.putParcelable(ITEM_ARGS, item);
        args.putInt(POSITION_ARGS, position);
        return args;
    }
    public static ItemEditFragment newInstance(){
        ItemEditFragment fragment = new ItemEditFragment();
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Calendar calendar = (Calendar)data.getSerializableExtra(CALENDAR_EXTRA);
            item.setDate(calendar.getTimeInMillis());
            mDate.setText(DateTimeToStringUtil.getDateToString(item));
            mTime.setText(DateTimeToStringUtil.getTimeToString(item));
            isChanged = true;
        }
    }
}
