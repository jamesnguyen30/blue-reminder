package com.example.jamesnguyen.taskcycle.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jamesnguyen.taskcycle.activities.MainActivity;
import com.example.jamesnguyen.taskcycle.R;
import com.example.jamesnguyen.taskcycle.item_touch_helper.ItemTouchHelperCallback;
import com.example.jamesnguyen.taskcycle.recycler_view.ReminderAdapter;
import com.example.jamesnguyen.taskcycle.room.ItemDatabase;
import com.example.jamesnguyen.taskcycle.room.ItemEntity;

import java.util.List;

/**
 * Created by jamesnguyen on 3/16/18.
 */

public class ReminderFragment extends Fragment {

    public static final String TAG = "ReminderFragmentTag";
    public static final String DATABASE_REF_ARGUMENT = "db_ref_argument";
    RecyclerView mRecyclerView;
    ReminderAdapter mReminderAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReminderAdapter = new ReminderAdapter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reminder_list_fragment, container,  false);
        mRecyclerView = view.findViewById(R.id.reminder_list_fragment);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //TODO Turn on loading icon here
        setReminderAdapter();
        setItemTouchHelper();
        return view;
    }

    public void setReminderAdapter(){
        mRecyclerView.setAdapter(mReminderAdapter);
    }

    public void setItemTouchHelper(){
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(getContext(), mReminderAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
    }
    public void turnOnLoadingIcon(){
        //TODO turn on loading icon
    }

    public void turnOffLoadingIcon(){
        //TODO Turn off loading icon
    }

    public void updateDatabase(List<ItemEntity> items){
        turnOffLoadingIcon();
        mReminderAdapter.updateItemList(items);
        //mReminderAdapter.notifyDataSetChanged();
    }

    public static ReminderFragment newInstance(){
        return new ReminderFragment();
    }

}
