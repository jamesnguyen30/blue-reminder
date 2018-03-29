package com.example.jamesnguyen.taskcycle.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.jamesnguyen.taskcycle.R;
import com.example.jamesnguyen.taskcycle.broadcast_receivers.CountDownTimerReceiver;
import com.example.jamesnguyen.taskcycle.controllers.AlarmController;
import com.example.jamesnguyen.taskcycle.services.CountDownTimerService;
import com.example.jamesnguyen.taskcycle.utils.CycleSettingsUtil;
import com.example.jamesnguyen.taskcycle.utils.TimeUnitUtil;

/**
 * Created by jamesnguyen on 3/24/18.
 */

public class WorkCycleFragment extends Fragment {


    public static String MILLIS_UNTIL_FINISHED_ARG = "millis_until_finished_arg";
    public interface OnTimerRunning {
        void onRunning(long millisUntilFinished);
    }
    public static final String TAG = "WorkCycleFragment";

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Reciever", "onReceive");
            milliUntilFinished = intent.getLongExtra(CountDownTimerService.MILLIS_UNTIL_FINISHED_EXTRA, -1);
            isPaused = false;
            startCountDownTimer(milliUntilFinished);
        }
    };

    FloatingActionButton fab;
    FloatingActionButton workCycleButton;
    Button play;
    CycleSettingsUtil cycleSettingsUtil;
    CountDownTimer timer;
    AlarmController alarmController;
    CycleSettingsUtil settingsUtil;
    TextView remainingTimeTextView;
    OnTimerRunning mCallback;


    boolean isPaused;
    long milliUntilFinished;
    long WORK_TIME;
    long SHORT_BREAK;
    long LONG_BREAK;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback = (OnTimerRunning) getActivity();
        } catch(ClassCastException e){
            throw new ClassCastException(getActivity().getCallingPackage()
                    + " must implement the OnTimerRunning");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("Fragment here", "Fragment");
        super.onCreate(savedInstanceState);
//        mReceiver = new CountDownTimerReceiver();

        cycleSettingsUtil = new CycleSettingsUtil(getActivity());
        settingsUtil = new CycleSettingsUtil(getActivity());
        isPaused = true;

        //TODO Preferences here
        WORK_TIME = settingsUtil.getWorkTime();
        SHORT_BREAK = settingsUtil.getShortBreakTime();
        LONG_BREAK = settingsUtil.getLongBreakTime();

        loadMillisUntilFinished();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.work_cycle_fragment, container, false);

        //recieve the time from arg
        alarmController = new AlarmController();

        remainingTimeTextView = view.findViewById(R.id.remaining_time_text_view);
        fab = (FloatingActionButton)container.getRootView().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        workCycleButton = (FloatingActionButton)container.getRootView().findViewById(R.id.work_cycle_button);
        workCycleButton.setVisibility(View.INVISIBLE);
        updateRemainingTimeTextView(milliUntilFinished);

        play = view.findViewById(R.id.start_working_button);

        play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //alarmController.startAlarm(getContext(), 10);
                //start the activity only once
                //update the remaining time text view
                if(isPaused) {
                    startCountDownTimer(milliUntilFinished);
                } else {
                    //TODO Save milliUntilFinished
                    //stop count down timer
                    stopCountDownTimer();
                }
                isPaused =!isPaused;
                return;
            }
        });

        return view;
    }

    public static WorkCycleFragment newInstance(){
        return new WorkCycleFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = CountDownTimerService.getIntentFilter();
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);

        fab.setVisibility(View.VISIBLE);
        workCycleButton.setVisibility(View.VISIBLE);
        //TODO save millsUntilFinished to Preference
        settingsUtil.saveMillsUntilFinished(milliUntilFinished);
        stopCountDownTimer();
        //TODO call on run interface
        if(!isPaused)
            mCallback.onRunning(milliUntilFinished);
    }

    public void setMilliUntilFinished(long milliUntilFinished){
        this.milliUntilFinished = milliUntilFinished;
    }

    private void loadMillisUntilFinished(){
        //if the notification is clicked
        milliUntilFinished = getActivity().getIntent().getLongExtra(CountDownTimerService.MILLIS_UNTIL_FINISHED_EXTRA, -1);
        if(milliUntilFinished!=-1) {
            isPaused = false;
            return;
        }

        //TODO Load saved preference for milliUntilFinished
        milliUntilFinished = settingsUtil.getMillsUntilFinished();
        if(milliUntilFinished<=0) {
            milliUntilFinished = WORK_TIME*1000;
        }
    }

    private void updateRemainingTimeTextView(long millisUntilFinished){
        remainingTimeTextView.setText(TimeUnitUtil.parseTime(millisUntilFinished));
    }


    private void startCountDownTimer(final long milliseconds){
        if(milliseconds<=0){
            remainingTimeTextView.setText("DONE");
//            settingsUtil.saveMillsUntilFinished(WORK_TIME*1000);
        }
        timer = new CountDownTimer(milliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateRemainingTimeTextView(millisUntilFinished);
                setMilliUntilFinished(millisUntilFinished);
             }

            @Override
            public void onFinish() {
                //TODO Ring the phone to notify it's done

                remainingTimeTextView.setText("DONE");
                //Reset the millisUntilFinished
                setMilliUntilFinished(WORK_TIME*1000);
            }
        };
        timer.start();
    }

    private void stopCountDownTimer(){
        if(timer!=null)
            timer.cancel();
    }

}

