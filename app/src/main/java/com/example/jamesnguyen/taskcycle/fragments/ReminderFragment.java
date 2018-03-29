package com.example.jamesnguyen.taskcycle.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jamesnguyen.taskcycle.activities.MainActivity;
import com.example.jamesnguyen.taskcycle.R;
import com.example.jamesnguyen.taskcycle.mock_data.ReminderDatabaseMock;
import com.example.jamesnguyen.taskcycle.recycler_view.ReminderAdapter;

/**
 * Created by jamesnguyen on 3/16/18.
 */

public class ReminderFragment extends Fragment {
    public static final String TAG = "ReminderFragmentTag";
    public static final String DATABASE_REF_ARGUMENT = "db_ref_argument";
    RecyclerView mRecyclerView;
    ReminderAdapter mReminderAdapter;

    //TODO database reference


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the view
        View view = inflater.inflate(R.layout.reminder_list_fragment, container,  false);
        mRecyclerView = view.findViewById(R.id.reminder_list_fragment);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setReminderAdapter();
        return view;
    }

    public void setReminderAdapter(){
        //start the database adapter
        //set up the database
        //this is a bad approach if you want your fragment to be
        //used independently from MainActivity
        //for this approach, this fragment only works
        //for MainActivity,
        //DELETE THIS COMMENT WHEN THERE'S A BETTER SOLUTION
        ReminderDatabaseMock db = ((MainActivity)getActivity()).getDatabase();
        mReminderAdapter = new ReminderAdapter(getActivity(),db);
        mRecyclerView.setAdapter(mReminderAdapter);
    }

    public void updateDatabase(){
        mReminderAdapter.notifyDataSetChanged();
    }

    public static ReminderFragment newInstance(){
        return new ReminderFragment();
    }

}
