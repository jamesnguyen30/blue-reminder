package com.example.jamesnguyen.taskcycle.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jamesnguyen.taskcycle.R;
import com.example.jamesnguyen.taskcycle.room.ItemEntity;
import com.example.jamesnguyen.taskcycle.utils.DateTimeToStringUtil;

public class ItemEditFragment extends Fragment {

    public static final String TAG ="ItemEditDiaglogFragment";
    public static final String ITEM_ARGS = "item_data_args";
    public static final String POSITION_ARGS = "position_args";

    TextView mTitle;
    TextView mDate;
    TextView mLocation;
    ItemEntity item;
    int position;
    FloatingActionButton fab;
    boolean isChanged;

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
////        return super.onCreateDialog(savedInstanceState);
////
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//
//        builder.setView(inflater.inflate(R.layout.edit_item_fragment, null))
//                .setTitle("Detail")
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//        return builder.create();
//    }


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
        mLocation = view.findViewById(R.id.item_location_edit);

        mTitle.setText(item.getTitle().toString());
        mDate.setText(DateTimeToStringUtil.itemEntityToString(item));
        mLocation.setText("2413 Diamond Oaks, Garland, TX, 75044");

        fab = (FloatingActionButton)container.getRootView().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fab.setVisibility(View.VISIBLE);
        testChange();
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
}
