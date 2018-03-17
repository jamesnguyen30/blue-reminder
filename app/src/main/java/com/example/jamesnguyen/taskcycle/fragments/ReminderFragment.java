package com.example.jamesnguyen.taskcycle.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jamesnguyen.taskcycle.R;
import com.example.jamesnguyen.taskcycle.mock_data.ReminderDatabaseMock;
import com.example.jamesnguyen.taskcycle.recycler_view.ReminderAdapter;

/**
 * Created by jamesnguyen on 3/16/18.
 */

public class ReminderFragment extends Fragment {
    RecyclerView mRecyclerView;
    ReminderAdapter mReminderAdapter;
    //TODO database reference

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the view
        View view = inflater.inflate(R.layout.reminder_list_fragment, container,  false);
        mRecyclerView = view.findViewById(R.id.reminder_list_fragment);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        //floating button in main activity,
        //Because I want to keep this button through Fragment transition
//
//        FloatingActionButton fab = (FloatingActionButton)container.getRootView().findViewById(R.id.fab);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //start NewReminder fragment
//                Log.d("MainActivity", "clicked");
//            }
//        });


        setReminderAdapter();
        return view;
    }

    public void setReminderAdapter(){
        //start the database adapter
        //set up the database
        mReminderAdapter = new ReminderAdapter(getActivity());
        mRecyclerView.setAdapter(mReminderAdapter);
    }
}
