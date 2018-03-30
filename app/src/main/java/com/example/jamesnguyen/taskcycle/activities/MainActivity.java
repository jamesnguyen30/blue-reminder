package com.example.jamesnguyen.taskcycle.activities;

import android.content.Context;
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
import com.example.jamesnguyen.taskcycle.fragments.NewItemFragment;
import com.example.jamesnguyen.taskcycle.fragments.ReminderFragment;
import com.example.jamesnguyen.taskcycle.fragments.SettingFragment;
import com.example.jamesnguyen.taskcycle.mock_data.ReminderDatabaseMock;
import com.example.jamesnguyen.taskcycle.mock_data.ReminderMock;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements
        NewItemFragment.OnNewItemCreated{

    FloatingActionButton fab;
    //mock database
    ReminderDatabaseMock database;

    private static final int ADD_FLAG = 0;
    private static final int REPLACE_FLAG = 1;

    private static final String FRAGMENT_CODE_EXTRA = "fragment_code_extra";
    public static final int START_DEFAULT_FRAGMENT = 0;
    public static final int START_NEW_ITEM_FRAGMENT = 1;
    public static final int START_SETTING_FRAGMNENT = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new ReminderDatabaseMock();
        database.populateMockDatbase();

        fab = findViewById(R.id.fab);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ReminderFragment.TAG);
        if(fragment==null){
            fragment = ReminderFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_activity_container, fragment, ReminderFragment.TAG)
                    .commit();
        }
//        int fragmentCode = getIntent().getIntExtra(FRAGMENT_CODE_EXTRA, 0);
//
//        if(fragmentCode!=0){
//            startFragmentWithBackStack(fragmentCode, ADD_FLAG, null );
//        }

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startFragmentWithBackStack(START_NEW_ITEM_FRAGMENT, ADD_FLAG, null );
            }
        });
    }

    public ReminderDatabaseMock getDatabase(){
        return database;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNewItemCreated(String itemName, Calendar calendar, boolean hasDate, boolean hasTime) {
        ReminderMock newItem = new ReminderMock(itemName, calendar, hasDate, hasTime);
        testEncapsulation(newItem);

        ReminderFragment fragment = (ReminderFragment)getSupportFragmentManager()
                .findFragmentByTag(ReminderFragment.TAG);
        fragment.updateDatabase();
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
                fragment = NewItemFragment.newInstance();
                tag = NewItemFragment.TAG;
                break;
            case 2:
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

}
