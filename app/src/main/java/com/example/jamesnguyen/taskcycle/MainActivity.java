package com.example.jamesnguyen.taskcycle;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.jamesnguyen.taskcycle.fragments.NewItemFragment;
import com.example.jamesnguyen.taskcycle.fragments.ReminderFragment;
import com.example.jamesnguyen.taskcycle.mock_data.ReminderDatabaseMock;
import com.example.jamesnguyen.taskcycle.mock_data.ReminderMock;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NewItemFragment.OnNewItemCreated {

    FloatingActionButton fab;
    FloatingActionButton startTask;
    //mock database
    ReminderDatabaseMock database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instantiate the database
        database = new ReminderDatabaseMock();
        database.populateMockDatbase();

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //get the floating add button

        fab = findViewById(R.id.fab);
        startTask = findViewById(R.id.start_task);

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

        startTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

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
}
