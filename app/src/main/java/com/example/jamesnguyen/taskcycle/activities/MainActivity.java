package com.example.jamesnguyen.taskcycle.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.jamesnguyen.taskcycle.R;
import com.example.jamesnguyen.taskcycle.broadcast_receivers.CountDownTimerReceiver;
import com.example.jamesnguyen.taskcycle.fragments.NewItemFragment;
import com.example.jamesnguyen.taskcycle.fragments.ReminderFragment;
import com.example.jamesnguyen.taskcycle.fragments.SettingFragment;
import com.example.jamesnguyen.taskcycle.fragments.WorkCycleFragment;
import com.example.jamesnguyen.taskcycle.mock_data.ReminderDatabaseMock;
import com.example.jamesnguyen.taskcycle.mock_data.ReminderMock;
import com.example.jamesnguyen.taskcycle.services.CountDownTimerService;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements
        NewItemFragment.OnNewItemCreated, WorkCycleFragment.OnTimerRunning {

    FloatingActionButton fab;
    FloatingActionButton workCycleButton;
    //mock database
    ReminderDatabaseMock database;

    private static final int ADD_FLAG = 0;
    private static final int REPLACE_FLAG = 1;

    private static final String FRAGMENT_CODE_EXTRA = "fragment_code_extra";
    public static final int START_DEFAULT_FRAGMENT = 0;
    public static final int START_WORK_CYCLE_FRAGMENT = 1;
    public static final int START_NEW_ITEM_FRAGMENT = 2;
    public static final int START_SETTING_FRAGMNENT = 3;
    private Intent countDownTimterServiceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int fragmentCode = getIntent().getIntExtra(FRAGMENT_CODE_EXTRA, 0);

        database = new ReminderDatabaseMock();
        database.populateMockDatbase();

        fab = findViewById(R.id.fab);
        workCycleButton = findViewById(R.id.work_cycle_button);

        //inflate the fragment

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fragment fragment = ReminderFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_activity_container, fragment, ReminderFragment.TAG)
                .commit();

        if(fragmentCode!=0){
            startFragmentWithBackStack(fragmentCode, ADD_FLAG, null );
        }


        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startFragmentWithBackStack(START_NEW_ITEM_FRAGMENT, ADD_FLAG, null );
            }
        });

        workCycleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO stop CountDownTimerService and get the current millisUntilFinished
                //TODO and send argument to the fragment as well
                //Bundle args = WorkCycleFragment.createMillisUntilFinishedArgument(1000);

                //long millisUntilFinished = mReciever.getMillisUntilFinished();
                //Bundle args = WorkCycleFragment.createMillisUntilFinishedArgument(millisUntilFinished);
                startFragmentWithBackStack(START_WORK_CYCLE_FRAGMENT, REPLACE_FLAG, null);
                if(countDownTimterServiceIntent!=null) {
                    stopService(countDownTimterServiceIntent);
                    countDownTimterServiceIntent = null;
                }
            }
        });
    }

    public ReminderDatabaseMock getDatabase(){
        return database;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startFragmentWithBackStack(START_SETTING_FRAGMNENT, REPLACE_FLAG, null );
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void testEncapsulation(ReminderMock e){
        database.addNewReminder(e);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //save the database reference,
        // for sqlite database implementation,
        //we don't need database object to hold the data
        //but for mock database which needs to be persisted
        // through screen conf changes
        //TODO Start count down timer service here
        //TODO Load millisUntilFinished and pass to count down services

    }


    @Override
    protected void onDestroy() {
       // unregisterTimeReciever();
        super.onDestroy();

    }


    //Callback get called when NewItemFragment done entering the item creation
    @Override
    public void onNewItemCreated(String itemName, Calendar calendar, boolean hasDate, boolean hasTime) {
        //Log.d(getLocalClassName(), itemName);
        ReminderMock newItem = new ReminderMock(itemName, calendar, hasDate, hasTime);
        testEncapsulation(newItem);
        //tell the reminder fragment to update its dataset
        ReminderFragment fragment = (ReminderFragment)getSupportFragmentManager()
                .findFragmentByTag(ReminderFragment.TAG);
        fragment.updateDatabase();
    }

    @Override
    public void onRunning(long millisUntilFinished) {
        //TODO start count down service here
        startCountDownService(millisUntilFinished);
    }

    public static Intent createIntent(Context context, int fragmentCode){
        Intent intent = new Intent(context, MainActivity.class);
        intent .putExtra(FRAGMENT_CODE_EXTRA, fragmentCode);
        return intent;
    }

    private void startFragmentWithBackStack(int fragmentCode, int stackCode, Bundle argument){
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment;
        String tag;

        switch(fragmentCode){
            default:
                fragment = ReminderFragment.newInstance();
                tag = ReminderFragment.TAG;
                break;
            case 1:
                fragment = WorkCycleFragment.newInstance();
                tag = WorkCycleFragment.TAG;
                break;
            case 2:
                fragment = NewItemFragment.newInstance();
                tag = NewItemFragment.TAG;
                break;
            case 3:
                fragment = SettingFragment.newInstance();
                tag = SettingFragment.TAG;
                break;
        }

        if(argument!=null){
            fragment.setArguments(argument);
        }
        switch(stackCode){
            default: // default back stack, ReminderFragment is the final element in the stack
                fm.beginTransaction()
                        .add(R.id.main_activity_container, fragment, tag)
                        .addToBackStack(ReminderFragment.TAG)
                        .commit();
                break;
            case 1:
                fm.beginTransaction()
                        .replace(R.id.main_activity_container, fragment, tag)
                        .addToBackStack(ReminderFragment.TAG)
                        .commit();
                break;
        }
    }

    public void startCountDownService(long millisUntilFinished){
        countDownTimterServiceIntent =
                CountDownTimerService.createIntent(this,millisUntilFinished);
        startService(countDownTimterServiceIntent);
    }

}
