package com.example.jamesnguyen.taskcycle.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
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
import com.example.jamesnguyen.taskcycle.broadcast_receivers.AlarmReceiver;
import com.example.jamesnguyen.taskcycle.fragments.NewItemFragment;
import com.example.jamesnguyen.taskcycle.fragments.ReminderFragment;
import com.example.jamesnguyen.taskcycle.fragments.SettingFragment;
import com.example.jamesnguyen.taskcycle.fragments.WorkCycleFragment;
import com.example.jamesnguyen.taskcycle.mock_data.ReminderDatabaseMock;
import com.example.jamesnguyen.taskcycle.mock_data.ReminderMock;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NewItemFragment.OnNewItemCreated {

    FloatingActionButton fab;
    FloatingActionButton workCycleButton;
    //mock database
    ReminderDatabaseMock database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //test broadcast receiver
        //startAlarm(5);
        //Instantiate the database
        database = new ReminderDatabaseMock();
        database.populateMockDatbase();

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //get the floating add button

        fab = findViewById(R.id.fab);
        workCycleButton = findViewById(R.id.work_cycle_button);

        //inflate the fragment

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.main_activity_container);

        if(fragment==null){
            //create ReminderFragment by default
            fragment = createReminderFragment();
            fm.beginTransaction()
                    .add(R.id.main_activity_container, fragment, ReminderFragment.TAG)
                    .commit();
        }

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //start new item fragment

                NewItemFragment newFragment = NewItemFragment.newInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.main_activity_container, newFragment, NewItemFragment.TAG)
                        .addToBackStack(ReminderFragment.TAG)
                        .commit();
            }
        });

        workCycleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                WorkCycleFragment workCycleFragment = WorkCycleFragment.getInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.main_activity_container, workCycleFragment, WorkCycleFragment.TAG)
                        .addToBackStack(ReminderFragment.TAG)
                        .commit();
            }
        });
    }

    public ReminderFragment createReminderFragment(){

        return new ReminderFragment();
    };

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
            //Intent setting = new Intent(this, SettingActivity.class);
            //startActivity(setting);
            SettingFragment newFragment = SettingFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_activity_container, newFragment, SettingFragment.TAG)
                    .addToBackStack(ReminderFragment.TAG)
                    .commit();

            return true;
        }

        return super.onOptionsItemSelected(item);
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

    public void testEncapsulation(ReminderMock e){
        database.addNewReminder(e);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //save the database reference,
        // for sqlite database implementation,
        //we don't need database object to hold the data
        //but for mock database which needs to be persisted
        // through screen conf changes

    }

    public void startAlarm(int seconds){
        long currentTime = System.currentTimeMillis();
        Intent intent =new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, currentTime + seconds*1000, pendingIntent);
    }
}
