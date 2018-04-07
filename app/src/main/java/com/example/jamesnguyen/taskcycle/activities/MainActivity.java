package com.example.jamesnguyen.taskcycle.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.jamesnguyen.taskcycle.R;
import com.example.jamesnguyen.taskcycle.fragments.ItemEditFragment;
import com.example.jamesnguyen.taskcycle.fragments.NewItemFragment;
import com.example.jamesnguyen.taskcycle.fragments.ReminderFragment;
import com.example.jamesnguyen.taskcycle.fragments.SettingFragment;
import com.example.jamesnguyen.taskcycle.recycler_view.ReminderAdapter;
import com.example.jamesnguyen.taskcycle.room.ItemDatabase;
import com.example.jamesnguyen.taskcycle.room.ItemEntity;
import com.example.jamesnguyen.taskcycle.utils.AlarmManagerUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements
        NewItemFragment.OnNewItemCreated, ReminderAdapter.ReminderAdapterDbOperations{

    FloatingActionButton fab;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    //mock ItemDatabase
//    ReminderDatabaseMock database;
    ItemDatabase database;
    public static final int ADD_FLAG = 0;
    public static final int REPLACE_FLAG = 1;

    private static final String FRAGMENT_CODE_EXTRA = "fragment_code_extra";
    public static final int START_DEFAULT_FRAGMENT = 0;
    public static final int START_NEW_ITEM_FRAGMENT = 1;
    public static final int START_SETTING_FRAGMENT = 2;
    public static final int START_EDIT_FRAGMENT = 3;

    public static final int NEW_FRAGMET_NO_ANIMATION = 0;
    public static final int NEW_FRAGMENT_ENTER_FROM_LEFT = 1;
    public static final int NEW_FRAGMENT_ENTER_FROM_RIGHT = 2;
    public static final int NEW_FRAGMENT_ENTER_FROM_BOTTOM = 3;

    LoadItemsTask asyncTask;
    int loadMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = ItemDatabase.getInstance(this);
        fab = findViewById(R.id.fab);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigation_view);

        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();
                        switch(id){
                            default:
                            case R.id.all_items:
                                mDrawerLayout.closeDrawer(Gravity.LEFT);
                                return true;
                        }
                    }
                }
        );


        loadMode = LoadItemsTask.LOAD_ALL_ITEMS;

        //populateDb();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ReminderFragment.TAG);
        if(fragment==null) {
            fragment = ReminderFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_activity_container, fragment, ReminderFragment.TAG)
                    .commit();

            loadByMode(loadMode);
        }

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startFragmentWithBackStack(START_NEW_ITEM_FRAGMENT, ADD_FLAG, null, 0);
            }
        });
        turnOnFabButton(fab);

        registerNotificationChannel();

    }

    public ItemDatabase getDatabase(){
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

        switch (id){
            case R.id.action_settings:
                startFragmentWithBackStack(START_SETTING_FRAGMENT, REPLACE_FLAG, null, 0);
                return true;
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkGooglePlayServices();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ItemDatabase.destroyInstance();
    }

    @Override
    public void onNewItemCreated(ItemEntity newItem) {
        ItemEntity item = newItem;//AlarmManagerUtil.addWeeklyAlarm(this, item);
        insertItems(item);
        loadByMode(loadMode);

    }

    public static Intent createIntent(Context context, int fragmentCode){
        Intent intent = new Intent(context, MainActivity.class);
        intent .putExtra(FRAGMENT_CODE_EXTRA, fragmentCode);
        return intent;
    }

    public void startFragmentWithBackStack(int fragmentCode, int stackCode, Bundle argument, int animationFlag){
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
            case 3:
                fragment = ItemEditFragment.newInstance();
                tag = ItemEditFragment.TAG;
                fragment.setTargetFragment(fm.findFragmentByTag(ReminderFragment.TAG), 0);
                break;
        }

        if(argument!=null){
            fragment.setArguments(argument);
        }

        FragmentTransaction ft = fm.beginTransaction();
        switch (animationFlag){
            default:
            case NEW_FRAGMET_NO_ANIMATION:
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                break;
            case NEW_FRAGMENT_ENTER_FROM_BOTTOM:
                ft.setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.pop_enter_from_left,
                        R.anim.pop_exit_to_right);
                break;

        }

        switch(stackCode){
            default: // default back stack, ReminderFragment is the final element in the stack
                ft.add(R.id.main_activity_container, fragment, tag)
                        .addToBackStack(ReminderFragment.TAG)
                        .commit();
                break;
            case 1:
                ft.replace(R.id.main_activity_container, fragment, tag)
                        .addToBackStack(ReminderFragment.TAG)
                        .commit();
                break;
        }
    }

    public void setLoadMode(int loadMode){
        this.loadMode = loadMode;
    }

    public void loadByMode(int loadMode){
        asyncTask = new LoadItemsTask(loadMode, true);
        asyncTask.execute();
    }

    public void loadAllItems(){
        asyncTask = new LoadItemsTask(LoadItemsTask.LOAD_ALL_ITEMS, true);
        asyncTask.execute();
    }

    public void loadTodayItems(){
        asyncTask = new LoadItemsTask(LoadItemsTask.LOAD_TODAY_ITEMS, true);
        asyncTask.execute();
    }

    public void insertItems(ItemEntity item){
        asyncTask = new LoadItemsTask(LoadItemsTask.SAVE_ITEM, false);
        asyncTask.execute(item);
    }

    @Override
    public void deleteItem(ItemEntity item) {
        asyncTask = new LoadItemsTask(LoadItemsTask.DELETE_ITEM, false);
        asyncTask.execute(item);
        //if item has alarm, removes it
        if(item.isHasAlarm()){
            AlarmManagerUtil.removeAlarm(this, item);
        }
    }

    @Override
    public void updateItem(ItemEntity item) {
        asyncTask = new LoadItemsTask(LoadItemsTask.UPDATE_ITEM, false);
        asyncTask.execute(item);
    }

    public void populateDb(){
        int count = 10;
        ItemEntity[] items = new ItemEntity[count];
        ItemEntity item;
        for(int i =0;i<10;i++){
            item = new ItemEntity("Item #" + Integer.toString(i)
                    , Calendar.getInstance().getTimeInMillis()
                    , true, true,false,
                    "","");
            items[i] = item;
        }
        asyncTask = new LoadItemsTask(LoadItemsTask.SAVE_ITEM, false);
        asyncTask.execute(items);
    }

    private class LoadItemsTask extends AsyncTask<ItemEntity, Void, Void> {
        public final static int LOAD_ALL_ITEMS = 0;
        public final static int LOAD_TODAY_ITEMS = 1;
        public final static int SAVE_ITEM = 2;
        public final static int DELETE_ITEM = 3;
        public final static int UPDATE_ITEM = 4;

        boolean updateReminderFragment;
        int flag;

        public LoadItemsTask(int flag, boolean updateReminderFragment) {
            this.updateReminderFragment = updateReminderFragment;
            this.flag = flag;
        }

        @Override
        protected Void doInBackground(ItemEntity... itemEntities) {
            //load datbase to item list
            switch(flag){
                default:
                case 0:
                    database.queryAllItems();
                    break;
                case 1:
                    database.queryTodayItems();
                    break;
                case 2 :
                    database.insertNewItem(itemEntities);
                    break;
                case 3:
                    database.deleteItem(itemEntities);
                    break;
                case 4:
                    database.updateItem(itemEntities);
                    break;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //turn on loading icon
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //update recyclerView fragment
            if(updateReminderFragment){
                ReminderFragment fragment = (ReminderFragment)getSupportFragmentManager()
                        .findFragmentByTag(ReminderFragment.TAG);
                fragment.updateDatabase(database.getItems());
            }
        }
    }

    private boolean checkGooglePlayServices() {
        final int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        //Log.i("MainActivity", GoogleApiAvailability.getInstance().getErrorString(status));
        return status == ConnectionResult.SUCCESS;
    }

    private void registerNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Task Cycle";
            String description = "Reminder Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel mChannel = new NotificationChannel("TaskCycleReminder", name, importance);
            mChannel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) getSystemService(
                    NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);
        }
    }

    public void turnOnFabButton(FloatingActionButton fab){
        Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        fab.startAnimation(scaleUp);
        fab.setVisibility(View.VISIBLE);
    }

    public void turnOffFabButton(FloatingActionButton fab){
        Animation scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        fab.startAnimation(scaleDown);
        fab.setVisibility(View.INVISIBLE);
    }

}
