package com.example.jamesnguyen.taskcycle.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jamesnguyen.taskcycle.R;
import com.example.jamesnguyen.taskcycle.smart_date_detector.MatchedPosition;
import com.example.jamesnguyen.taskcycle.smart_date_detector.SmartDateDetector;

import java.util.Calendar;
import java.util.List;

/**
 * Created by jamesnguyen on 3/17/18.
 */

public class NewItemFragment extends Fragment {

    public static final String TAG = "NewItemFragmentTag";
    public interface OnNewItemCreated{
        //TODO pass an Item object here,
        // pass a mock object for now
        //void onNewItemCreated(String itemName);
        void onNewItemCreated(String title, Calendar calendar, boolean hasDate, boolean hasTime);
    }
    SpannableStringBuilder spanBuilder;
    FloatingActionButton fab;
    FloatingActionButton workCycleButton;
    EditText mEditText;
    OnNewItemCreated mCallback;
    SmartDateDetector dateDetector = new SmartDateDetector();

    int previousLength;

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
        dateDetector = new SmartDateDetector();
        spanBuilder = new SpannableStringBuilder();


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

        //add text listener
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousLength = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                dateDetector.setOriginalText(s.toString());
                if(dateDetector.findMatches()){
                    buildSpannables(s, dateDetector.getMatchedPositions());
                }

            }
        });

        //set listener when the keyboard is clicked DONE

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch(actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        mCallback.onNewItemCreated(
                                removeDateFromString(v.getText().toString()
                                        , dateDetector.getMatchedPositions()),
                                dateDetector.convertToCalendar(),
                                dateDetector.isHasDate(),
                                dateDetector.isHasTime()
                        );
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

    private void buildSpannable(Editable e, int start, int end){

        e.setSpan(new StyleSpan(Typeface.BOLD),start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        e.setSpan(new ForegroundColorSpan( getResources().getColor(R.color.white)),start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        e.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.orange)),start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void buildSpannables(Editable e, List<MatchedPosition> matchedPositions){
        for(MatchedPosition pos : matchedPositions){
            buildSpannable(e, pos.getStartPos(), pos.getEndPos());
        }

    }

    private String removeDateFromString(String original, List<MatchedPosition> matchedPositions){
        StringBuilder builder = new StringBuilder(original);
        int offset = 0;
        for(MatchedPosition pos : matchedPositions){
            builder.replace(pos.getStartPos() - offset, pos.getEndPos() - offset, "");
            offset = pos.getEndPos() - pos.getStartPos();
        }
        return builder.toString();
    }
}
