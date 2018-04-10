package com.example.jamesnguyen.taskcycle.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jamesnguyen.taskcycle.R;

public class GreetingFragmentIntro1 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.greeting_fragment_intro_1, container, false);

        return viewGroup;
    }
}
