package com.example.jamesnguyen.taskcycle.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.jamesnguyen.taskcycle.R;
import com.example.jamesnguyen.taskcycle.fragments.GreetingFragmentIntro1;

public class GreetingActivity extends AppCompatActivity {

    private final int NUM_PAGE = 5;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting);
        mPager = (ViewPager)findViewById(R.id.greeting_view_pager);
        mPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                default:
                case 0:
                    return new GreetingFragmentIntro1();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGE;
        }
    }

}
