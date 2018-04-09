package com.example.jamesnguyen.taskcycle.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.jamesnguyen.taskcycle.R;
import com.example.jamesnguyen.taskcycle.dialogs_fragments.AboutMeDialog;

public class AboutMeActivity extends AppCompatActivity {

    TextView mAboutMe;
    private static final String myEmail = "james.nguyen.dev.30@gmail.com";
    private static final String subject = "Hi James, Let's get in touch ... ";
    private static final String myFbLink = "https://www.facebook.com/nguyenthecoder";
    private static final String myFbId = "100012596523430";

    FloatingActionButton fabEmail;
    FloatingActionButton fabFacebook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        Toolbar toolbar = findViewById(R.id.toolbar_about_me);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(getParent());
                return;
            }
        });
        setSupportActionBar(toolbar);

        fabEmail = findViewById(R.id.fab_email);
        fabEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                composeEmailToMe();
            }
        });

        fabFacebook = findViewById(R.id.fab_facebook);
        fabFacebook.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openFacebookToMyProfile();
            }
        });



        mAboutMe = findViewById(R.id.about_me);
        mAboutMe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new AboutMeDialog();
                dialogFragment.show(getSupportFragmentManager(), AboutMeDialog.TAG);

            }
        });
    }

    public void composeEmailToMe(){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {myEmail});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);

        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }
    }

    public void openFacebookToMyProfile(){
        Intent intent;
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(myFbLink));

        startActivity(intent);
    }

}
