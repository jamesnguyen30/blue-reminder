package com.example.jamesnguyen.taskcycle;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.jamesnguyen.taskcycle.fragments.NewItemFragment;
import com.example.jamesnguyen.taskcycle.fragments.ReminderFragment;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //get the floating add button

        fab = (FloatingActionButton)findViewById(R.id.fab);

        //inflate the fragment

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.main_activity_container);

        if(fragment==null){
            //create ReminderFragment by default
            fragment = createReminderFragment();
            fm.beginTransaction()
                    .add(R.id.main_activity_container, fragment)
                    .commit();
        }

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //start new item fragment
                Log.d("MainActivyt","Clicked");
                NewItemFragment newFragment = NewItemFragment.newInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.main_activity_container, newFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    public ReminderFragment createReminderFragment(){
        return new ReminderFragment();
    };

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
}
