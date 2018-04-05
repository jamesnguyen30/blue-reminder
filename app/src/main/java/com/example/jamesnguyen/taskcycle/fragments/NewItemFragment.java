package com.example.jamesnguyen.taskcycle.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityManagerCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jamesnguyen.taskcycle.R;
import com.example.jamesnguyen.taskcycle.dialogs_fragments.DatePickerDialogFragment;
import com.example.jamesnguyen.taskcycle.dialogs_fragments.TimePickerDialogFragment;
import com.example.jamesnguyen.taskcycle.room.ItemEntity;
import com.example.jamesnguyen.taskcycle.smart_date_detector.MatchedPosition;
import com.example.jamesnguyen.taskcycle.smart_date_detector.SmartDateDetector;
import com.example.jamesnguyen.taskcycle.utils.AlarmManagerUtil;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.Calendar;
import java.util.List;

/**
 * Created by jamesnguyen on 3/17/18.
 */

public class NewItemFragment extends Fragment {

    public static final String TAG = "NewItemFragmentTag";
    private static final int PLACE_PICKER_REQUEST_CODE = 4;

    public interface OnNewItemCreated{
        //TODO pass an Item object here,
        void onNewItemCreated(ItemEntity newItem);
    }
    SpannableStringBuilder spanBuilder;
    FloatingActionButton fab;
    EditText mEditText;
    OnNewItemCreated mCallback;
    SmartDateDetector dateDetector;
    ImageView mBackground;

    int previousLength;
    InputMethodManager ipm;

    ImageButton mDatePicker;
    ImageButton mTimePicker;
    ImageButton mPlacePicker;
    ImageButton mColorPicker;
    ImageButton mSetAlarm;

    String mTitle;
    Calendar mCalendar;
    boolean mHasDate;
    boolean mHasTime;
    boolean mHasAlarm;
    String mPlaceName;
    String mReadableAddress;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            //attach the mCallBack to the host activity
            mCallback = (OnNewItemCreated) getActivity();
        } catch(ClassCastException e){
            throw new ClassCastException(getActivity().getPackageName()
                    + " must implements the OnNewItemCreated interface");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateDetector = new SmartDateDetector();
        dateDetector = new SmartDateDetector();
        spanBuilder = new SpannableStringBuilder();
        mCalendar = null;
        mPlaceName = "";
        mReadableAddress ="";
        mHasDate = false;
        mHasTime = false;
        mHasAlarm = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_reminder_fragment, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        fab = container.getRootView().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        mEditText = view.findViewById(R.id.new_reminder_input);
        mBackground = view.findViewById(R.id.new_reminder_framgnet_background);

        mDatePicker = view.findViewById(R.id.date_picker_button);
        mTimePicker = view.findViewById(R.id.time_picker_button);
        mPlacePicker = view.findViewById(R.id.place_picker_button);
        mColorPicker = view.findViewById(R.id.color_picker_button);
        mSetAlarm = view.findViewById(R.id.set_alarm_button);

        //show keyboard
        ipm =  (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        mEditText.requestFocus();

        //add text listener
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousLength = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                dateDetector.setOriginalText(s.toString());
                if(dateDetector.findMatches()){
                    buildSpannables(s, dateDetector.getMatchedPositions());
                }
            }
        });

        //set listener when the keyboard is clicked DONE

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch(actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        if(mCalendar==null){
                            mCalendar = dateDetector.convertToCalendar();
                        }
                        //Test
                        ItemEntity item = new ItemEntity(
                                removeDateFromString(v.getText().toString(),
                                        dateDetector.getMatchedPositions()),
                                mCalendar.getTimeInMillis(),
                                mHasDate || dateDetector.isHasDate(),
                                mHasTime || dateDetector.isHasTime(),
                                mHasAlarm,
                                mPlaceName,
                                mReadableAddress);
                        //set alarm here if needed

                        mCallback.onNewItemCreated(item);

                        if(mHasAlarm){
                            //set alarm
                            AlarmManagerUtil.addAlarm(getContext(), item);
                        }

                        getActivity().getSupportFragmentManager().popBackStack();
                        return false;
                    default:
                        return false;
                }
            }
        });

        mBackground.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        mDatePicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!mHasDate){
                    ipm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    DatePickerDialogFragment dialogFragment = DatePickerDialogFragment.newInstance(null);
                    dialogFragment.setTargetFragment(getActivity().getSupportFragmentManager().findFragmentByTag(TAG),
                            DatePickerDialogFragment.REQUEST_CODE);
                    dialogFragment.show(getActivity().getSupportFragmentManager(), DatePickerDialogFragment.TAG);
                } else {
                    mDatePicker.setImageDrawable(
                            getActivity().getResources()
                            .getDrawable(R.drawable.ic_date_range_dark_24dp)
                    );
                }
            }
        });

        mTimePicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!mHasTime){
                    ipm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    TimePickerDialogFragment dialogFragment = TimePickerDialogFragment.newInstance(null);
                    dialogFragment.setTargetFragment(getActivity().getSupportFragmentManager().findFragmentByTag(TAG),
                            TimePickerDialogFragment.REQUEST_CODE);
                    dialogFragment.show(getActivity().getSupportFragmentManager(), DatePickerDialogFragment.TAG);
                } else {
                    mTimePicker.setImageDrawable(
                            getActivity().getResources()
                            .getDrawable(R.drawable.ic_access_time_dark_24dp)
                    );
                }
            }
        });


        mPlacePicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                if(mPlaceName.equals("")){
                    //TODO start at a current location
                    try {
                        startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST_CODE);
                    } catch(GooglePlayServicesRepairableException e){
                        e.printStackTrace();
                    } catch(GooglePlayServicesNotAvailableException e){
                        e.printStackTrace();
                    }
                } else {
                    mPlaceName = "";
                    mReadableAddress="";
                    mPlacePicker.setImageDrawable(
                            getActivity().getResources()
                            .getDrawable(R.drawable.ic_location_on_dark_24dp)
                    );
                }

            }
        });

        mSetAlarm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mHasAlarm==false){
                    dateDetector.convertToCalendar();
                    if((mHasDate || dateDetector.isHasDate()) &&
                            (mHasTime || dateDetector.isHasTime())){
                        //mHasAlarm = true;
                        mSetAlarm.setImageDrawable(
                                getActivity().getResources()
                                        .getDrawable(R.drawable.ic_alarm_red_24dp)
                        );
                        mHasAlarm = true;
                    } else {
                        Toast.makeText(getContext(),"Can't set alarm without Date or Time",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mSetAlarm.setImageDrawable(
                            getActivity().getResources()
                                    .getDrawable(R.drawable.ic_alarm_black_24dp)
                    );
                    mHasAlarm = false;
                }
            }
        });



        return view;
    }

    public static NewItemFragment newInstance(){
        return new NewItemFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        ipm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void onPause() {
        super.onPause();
        ipm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fab.setVisibility(View.VISIBLE);
    }

    private void buildSpannable(Editable e, int start, int end){

        e.setSpan(new StyleSpan(Typeface.BOLD),start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        e.setSpan(new ForegroundColorSpan( getResources().getColor(R.color.white)),start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        e.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.orange)),start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void buildSpannables(Editable e, List<MatchedPosition> matchedPositions){
        for(MatchedPosition pos : matchedPositions){
            buildSpannable(e, pos.getStartPos(), pos.getEndPos());
        }
    }

    private String removeDateFromString(String original, List<MatchedPosition> matchedPositions){
        StringBuilder builder = new StringBuilder(original);
        int offset = 0;
        for(MatchedPosition pos : matchedPositions){
            builder.replace(pos.getStartPos() - offset, pos.getEndPos() - offset, "");
            offset = pos.getEndPos() - pos.getStartPos();
        }
        return builder.toString();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode==PLACE_PICKER_REQUEST_CODE){
                //TODO Handle place data here
                Place place = PlacePicker.getPlace(getContext(), data);
                mPlaceName = place.getName().toString();
                mReadableAddress = place.getAddress().toString();
                //TODO update the database with place name and it's coords
                Log.d("ItemEditFragment", place.getName().toString() + " at " + place.getAddress());
                mPlacePicker.setImageDrawable(
                        getActivity().getResources()
                        .getDrawable(R.drawable.ic_location_on_red_24dp)
                );

            } else if (requestCode==DatePickerDialogFragment.REQUEST_CODE
                    || requestCode== TimePickerDialogFragment.REQUEST_CODE ){
                mCalendar = (Calendar)data.getSerializableExtra(DatePickerDialogFragment.CALENDAR_EXTRA);

                if(requestCode==DatePickerDialogFragment.REQUEST_CODE){
                    mHasDate = true;
                    mDatePicker.setImageDrawable(
                            getActivity().getResources()
                            .getDrawable(R.drawable.ic_date_range_red_24dp)
                    );
                } else if(requestCode==TimePickerDialogFragment.REQUEST_CODE){
                    mHasTime = true;
                    mTimePicker.setImageDrawable(
                            getActivity().getResources()
                            .getDrawable(R.drawable.ic_access_time_red_24dp)
                    );
                }
            }
        }
    }
}
