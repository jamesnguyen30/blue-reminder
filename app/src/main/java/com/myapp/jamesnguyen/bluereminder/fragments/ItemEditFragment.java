package com.myapp.jamesnguyen.bluereminder.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myapp.jamesnguyen.bluereminder.R;
import com.myapp.jamesnguyen.bluereminder.activities.MainActivity;
import com.myapp.jamesnguyen.bluereminder.dialogs_fragments.DatePickerDialogFragment;
import com.myapp.jamesnguyen.bluereminder.dialogs_fragments.OpenLocationDialog;
import com.myapp.jamesnguyen.bluereminder.dialogs_fragments.PriorityPickerDialog;
import com.myapp.jamesnguyen.bluereminder.dialogs_fragments.TimePickerDialogFragment;
import com.myapp.jamesnguyen.bluereminder.room.ItemEntity;
import com.myapp.jamesnguyen.bluereminder.utils.AlarmManagerUtil;
import com.myapp.jamesnguyen.bluereminder.utils.DateTimeToStringUtil;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Calendar;

public class ItemEditFragment extends Fragment {

    public static final String TAG ="ItemEditDiaglogFragment";
    public static final String ITEM_ARGS = "item_data_args";
    public static final String POSITION_ARGS = "position_args";
    public static int REMOVE_ITEM_REQUEST_CODE = 20;
    //public static int REQUEST_CODE= 1;
    public static int PLACE_PICKER_REQUEST_CODE = 11;

    EditText mTitle;
    TextView mDate;
    TextView mTime;
    TextView mLocation;
    CheckBox mCheckBoxAlarm;
    ItemEntity item;
    ImageView mPriorityIcon;
    TextView mPriorityText;

    ConstraintLayout mDateEdit;
    ConstraintLayout mTimeEdit;
    ConstraintLayout mLocationEdit;
    ConstraintLayout mSetAlarm;
    ConstraintLayout mPriorityEdit;

    int position;
    FloatingActionButton saveEditFab;
    FloatingActionButton directionFab;
    FloatingActionButton markDoneFab;

    boolean isChanged;
    int priority;

    ItemEntity tempItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isChanged = false;
        //load arguement
        Bundle args = getArguments();
        item = args.getParcelable(ITEM_ARGS);

        tempItem = item.deepCopy();

        position = args.getInt(POSITION_ARGS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_item_fragment, container, false);
        mTitle = view.findViewById(R.id.item_title_edit);
        mDate=  view.findViewById(R.id.item_date_textview);
        mTime = view.findViewById(R.id.item_time_textview);
        mLocation = view.findViewById(R.id.item_location_textview);
        mCheckBoxAlarm = view.findViewById(R.id.set_alarm_check_box);
        mDateEdit = view.findViewById(R.id.edit_date);
        mTimeEdit = view.findViewById(R.id.edit_time);
        mLocationEdit = view.findViewById(R.id.edit_location);
        mSetAlarm = view.findViewById(R.id.edit_alarm);
        mPriorityEdit = view.findViewById(R.id.edit_priority);
        mPriorityIcon = view.findViewById(R.id.priority_icon);
        mPriorityText = view.findViewById(R.id.priority_textview);

        saveEditFab = view.findViewById(R.id.save_edit_fab);
        markDoneFab = view.findViewById(R.id.mark_done_fab);
        directionFab = view.findViewById(R.id.show_direction_fab);

        turnOnFabButton(saveEditFab);
        turnOnFabButton(markDoneFab);
        turnOnFabButton(directionFab);

        mTitle.setText(tempItem.getTitle());

        if(tempItem.isHasDate())
            mDate.setText(DateTimeToStringUtil.getDateToString(tempItem));
        else
            mDate.setText("No Date");

        mTime.setText(DateTimeToStringUtil.getTimeToString(tempItem));

        mCheckBoxAlarm.setChecked(tempItem.isHasAlarm());

        if(tempItem.getPlaceName().equals("")){
            mLocation.setText("Location is not added");
        } else mLocation.setText(tempItem.getPlaceName());

        setPriority(tempItem.getPriority());

        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //item.setTitle(s.toString());
                 tempItem.setTitle(s.toString());
                isChanged = true;
            }
        });

        mPriorityEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO Show priority picker dialog
                PriorityPickerDialog dialogFramgnet = new PriorityPickerDialog();
                dialogFramgnet.setTargetFragment(getActivity().getSupportFragmentManager().findFragmentByTag(TAG),
                        PriorityPickerDialog.REQUEST_CODE);
                dialogFramgnet.show(
                        getActivity().getSupportFragmentManager(),
                        PriorityPickerDialog.TAG
                );
            }
        });

        mDateEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO Open date picker dialog
                DatePickerDialogFragment dialogFragment;
                if(tempItem.isHasDate()){
                    dialogFragment = DatePickerDialogFragment.newInstance(tempItem);
                } else{
                    dialogFragment = DatePickerDialogFragment.newInstance(null);
                }
                dialogFragment.setTargetFragment(getActivity().getSupportFragmentManager().findFragmentByTag(TAG),
                        DatePickerDialogFragment.REQUEST_CODE);
                dialogFragment.show(getActivity().getSupportFragmentManager(), DatePickerDialogFragment.TAG);
            }
        });

        mTimeEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO Open time picker dialog
                TimePickerDialogFragment dialogFramgment;
                if(tempItem.isHasTime()){
                    dialogFramgment = TimePickerDialogFragment.newInstance(tempItem);
                } else {
                    dialogFramgment = TimePickerDialogFragment.newInstance(null);
                }
                dialogFramgment.setTargetFragment(getActivity().getSupportFragmentManager().findFragmentByTag(TAG),
                        DatePickerDialogFragment.REQUEST_CODE );
                dialogFramgment.show(getActivity().getSupportFragmentManager(), TimePickerDialogFragment.TAG);
            }
        });

        mLocationEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO Open PlacePickeer
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                //TODO start at a current location
                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST_CODE);
                } catch(GooglePlayServicesRepairableException e){
                    e.printStackTrace();
                } catch(GooglePlayServicesNotAvailableException e){
                    e.printStackTrace();
                }
            }
        });

        mSetAlarm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                isChanged = true;
                if(!mCheckBoxAlarm.isChecked()){
                    if(tempItem.isHasDate()) {
                        if (AlarmManagerUtil.addAlarm(getContext(), tempItem)) {
                            tempItem.setHasAlarm(true);
                            mCheckBoxAlarm.setChecked(true);
                            return;
                        } else {
                            FancyToast.makeText(getContext(), "Overdue!", Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                        }
                    } else {
                        FancyToast.makeText(getContext(), "No Available Date", Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();

                    }
                    tempItem.setHasAlarm(false);
                    mCheckBoxAlarm.setChecked(false);
                } else {
                    tempItem.setHasAlarm(false);
                    mCheckBoxAlarm.setChecked(false);
                    AlarmManagerUtil.removeAlarm(getContext(), tempItem);
                }
            }
        });

        saveEditFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(isChanged){
                    item = tempItem.deepCopy();
                    Intent intent = ReminderFragment.createItent(item, position);
                    Fragment fragment = getTargetFragment();
                    fragment.onActivityResult(ReminderFragment.REQUEST_CODE,
                            Activity.RESULT_OK, intent);
                    //FancyToast.makeText(getContext(), "Saved Changes", Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                }
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        markDoneFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO Delete this item and update the UI
                //((MainActivity) getActivity()).deleteItem(item);
                //int loadMode = ((MainActivity)getActivity()).getLoadMode();
                //((MainActivity)getActivity()).loadByMode(loadMode);
                Intent intent = new Intent();
                intent.putExtra(ReminderFragment.POSITION_EXTRA, position);
                Fragment fragment = getTargetFragment();
                fragment.onActivityResult(ReminderFragment.REQUEST_CODE,
                        REMOVE_ITEM_REQUEST_CODE, intent);
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });

        directionFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO Show directional dialog
                if(!tempItem.getPlaceName().equals("")) {
                    OpenLocationDialog dialog = OpenLocationDialog.newInstance(tempItem.getPlaceName(),
                            tempItem.getReadableAddress());
                    dialog.show(((AppCompatActivity) getContext()).getSupportFragmentManager(), OpenLocationDialog.TAG);
                } else {
                    FancyToast.makeText(getContext(), "Location is not added", Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setToolbarTitleWithText("Reminder Detail");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        turnOffFabButton(saveEditFab);
    }

    public static Bundle creatBundle(ItemEntity item, int position){

        Bundle args = new Bundle();
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
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PLACE_PICKER_REQUEST_CODE) {
                //TODO Handle place data here
                Place place = PlacePicker.getPlace(getContext(), data);
                tempItem.setPlaceName(place.getName().toString());
                tempItem.setReadableAddress(place.getAddress().toString());
                mLocation.setText(place.getName());
                //TODO update the database with place name and it's coords

            } else if (requestCode == DatePickerDialogFragment.REQUEST_CODE
                    || requestCode == TimePickerDialogFragment.REQUEST_CODE) {
                Calendar calendar = (Calendar) data.getSerializableExtra(DatePickerDialogFragment.CALENDAR_EXTRA);
                tempItem.setDate(calendar.getTimeInMillis());
                tempItem.setHasDate(true);
                tempItem.setHasTime(true);
                mDate.setText(DateTimeToStringUtil.getDateToString(tempItem));
                mTime.setText(DateTimeToStringUtil.getTimeToString(tempItem));
                mCheckBoxAlarm.setChecked(false);
            } else if(requestCode == PriorityPickerDialog.REQUEST_CODE){

                priority = data.getIntExtra(PriorityPickerDialog.PRIORITY_EXTRA, 0);
                tempItem.setPriority(priority);
                setPriority(priority);
            }
            isChanged = true;
        }
    }

    private void turnOnFabButton(FloatingActionButton fab){
        Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        fab.startAnimation(scaleUp);
        fab.setVisibility(View.VISIBLE);
    }

    private void turnOffFabButton(FloatingActionButton fab){
        Animation scaleDown = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);
        fab.startAnimation(scaleDown);
        fab.setVisibility(View.INVISIBLE);
    }

    private void setPriority(int priority){

        switch (priority) {
            default:
            case ItemEntity.PRIORITY_DEFAULT:
                mPriorityText.setText("Normal");
                mPriorityIcon.setImageDrawable(
                        getResources().getDrawable(R.drawable.solid_circle_blue)
                );
                break;
            case ItemEntity.PRIORITY_IMPORTANT:
                mPriorityText.setText("Important");
                mPriorityIcon.setImageDrawable(
                        getResources().getDrawable(R.drawable.solid_circle_green)
                );
                break;
            case ItemEntity.PRIORITY_URGENT:
                mPriorityText.setText("Urgent");
                mPriorityIcon.setImageDrawable(
                        getResources().getDrawable(R.drawable.solid_circle_purple)
                );
                break;
            case ItemEntity.PRIORITY_URGENT_AND_IMPORTANT:
                mPriorityText.setText("Urgent and Important");
                mPriorityIcon.setImageDrawable(
                        getResources().getDrawable(R.drawable.solid_circle_red)
                );
                break;
        }
    }

    

}
