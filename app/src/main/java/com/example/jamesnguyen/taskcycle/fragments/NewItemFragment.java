package com.example.jamesnguyen.taskcycle.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jamesnguyen.taskcycle.MainActivity;
import com.example.jamesnguyen.taskcycle.R;

/**
 * Created by jamesnguyen on 3/17/18.
 */

public class NewItemFragment extends Fragment {

    public static final String TAG = "NewItemFragmentTag";
    public interface OnNewItemCreated{
        //TODO pass an Item object here,
        // pass a mock object for now
        void onNewItemCreated(String itemName);
    }
    FloatingActionButton fab;
    EditText mEditText;
    OnNewItemCreated mCallback;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            //attach the mCallBack to the host activity
            mCallback = (OnNewItemCreated) getActivity();
        } catch(ClassCastException e){
            throw new ClassCastException(getActivity().getPackageName()
                    + " must implements the OnNewItemCreated interface");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_reminder, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        fab = (FloatingActionButton)container.getRootView().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        mEditText =(EditText)view.findViewById(R.id.new_reminder_input);

        //show keyboard
        InputMethodManager ipm =  (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        ipm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        mEditText.requestFocus();

        //set listener when the keyboard is clicked DONE
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch(actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        //TODO add new item with the information
                        //Log.d("NewFragment", v.getText().toString());
                        mCallback.onNewItemCreated(v.getText().toString());
                        //remove this fragment from Support Fragment Manager
                        getActivity().getSupportFragmentManager().popBackStack();

                        return false;
                    default:
                        return false;
                }
            }
        });
        return view;
    }

    public static NewItemFragment newInstance(){
        return new NewItemFragment();
    }

    @Override
    public void onPause() {
        super.onPause();
        fab.setVisibility(View.VISIBLE);
    }
}
