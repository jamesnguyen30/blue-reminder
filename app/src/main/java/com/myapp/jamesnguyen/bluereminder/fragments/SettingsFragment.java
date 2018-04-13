package com.myapp.jamesnguyen.bluereminder.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.myapp.jamesnguyen.bluereminder.R;
import com.myapp.jamesnguyen.bluereminder.activities.MainActivity;

/**
 * Created by jamesnguyen on 3/24/18.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String TAG = "SettingsFragment";
    private static final String myEmail = "james.nguyen.dev.30@gmail.com";
    private static final String subject = "Blue Reminder Feedback.";
    private static final String myFbLink = "https://www.facebook.com/nguyenthecoder";
    private static final String myFbId = "100012596523430";
    private static final String playStoreLink = "https://play.google.com/store/apps/developer?id=James+Nguyen";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        ((MainActivity)getActivity()).setToolbarTitleWithText("Settings");
                addPreferencesFromResource(R.xml.preferences);

        Preference emailMePref = findPreference("send_me_email");
        emailMePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //Send Email here
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {myEmail});
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);

                if(intent.resolveActivity(getActivity().getPackageManager())!=null){
                    startActivity(intent);
                }
                return true;
            }
        });

        Preference visitStorePref = findPreference("visit_store");
        visitStorePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //Visit store
                try{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market:dev?id=James+Nguyen")));
                } catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(playStoreLink)));
                }
                return true;
            }
        });


    }

    public static SettingsFragment newInstance(){
        return new SettingsFragment();
    }
}
