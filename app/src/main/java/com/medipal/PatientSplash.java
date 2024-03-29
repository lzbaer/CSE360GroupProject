package com.medipal;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.parse.*;

/**
 * patient landing
 * may start edit info or submit survey
 * created by Patrick
 */

public class PatientSplash extends ActionBarActivity {

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_splash);

        //get userId from parent activity
        Intent intent = this.getIntent();
        userId = intent.getStringExtra(userId);

        //set title bar
        String firstName = ParseUser.getCurrentUser().getString("First_Name");

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Welcome, " + firstName + "!");

        //create button and listener for send new survey
        Button mSendSurvey = (Button) findViewById(R.id.send_survey_button);
        mSendSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSendSurvey();
            }
        });

        //create button and listener for send new survey
        Button mUpdateInfo = (Button) findViewById(R.id.update_info_button);
        mUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptUpdateInfo();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_patient_splash, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void attemptSendSurvey()
    {
        Intent intent = new Intent(this, PatientEnterSymptoms.class);
        startActivity(intent);
    }

    private void attemptUpdateInfo() {
        Intent intent = new Intent(this, PatientEditInfoPage.class);
        startActivity(intent);
    }

}
