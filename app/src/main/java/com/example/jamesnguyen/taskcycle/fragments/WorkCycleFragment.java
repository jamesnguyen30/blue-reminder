package com.example.jamesnguyen.taskcycle.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jamesnguyen.taskcycle.R;
import com.example.jamesnguyen.taskcycle.controllers.AlarmController;
import com.example.jamesnguyen.taskcycle.services.RunningWorkCycleService;
import com.example.jamesnguyen.taskcycle.utils.CycleSettingsUtil;

/**
 * Created by jamesnguyen on 3/24/18.
 */

public class WorkCycleFragment extends Fragment {

    public static final String TAG = "WorkCycleFragment";
    FloatingActionButton fab;
    FloatingActionButton workCycleButton;
    Button play;
    CycleSettingsUtil cycleSettingsUtil;
    AlarmController alarmController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadCycleSettings(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.work_cycle_fragment, container, false);

        alarmController = new AlarmController();
        fab = (FloatingActionButton)container.getRootView().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        workCycleButton = (FloatingActionButton)container.getRootView().findViewById(R.id.work_cycle_button);
        workCycleButton.setVisibility(View.INVISIBLE);
        play = (Button)view.findViewById(R.id.start_working_button);

        play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //alarmController.startAlarm(getContext(), 10);
                Intent intent = RunningWorkCycleService.createIntent(getActivity(), 3610);
                getContext().startService(intent);
            }
        });
        return view;
    }

    public static WorkCycleFragment getInstance(){
        return new WorkCycleFragment();
    }

    @Override
    public void onPause() {
        super.onPause();
        fab.setVisibility(View.VISIBLE);
        workCycleButton.setVisibility(View.VISIBLE);
    }

    private void loadCycleSettings(Context context){
        cycleSettingsUtil = new CycleSettingsUtil(context);
    }
}
