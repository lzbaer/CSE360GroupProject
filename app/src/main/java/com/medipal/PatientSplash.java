package com.medipal;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


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
        String firstName="";
        firstName = getFirstNameByUserId(userId);
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

    private String getFirstNameByUserId(String userId) {
        String firstName="Patient";
            //TODO get the first name from the database
                firstName = "Patrick"; //replace with logic
        return firstName;
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
            //log out activity
            this.finish();//try activityname.finish instead of this
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void attemptSendSurvey()
    {
        Intent intent = new Intent(this, PatientEnterSymptoms.class);
        intent.putExtra("userId",userId);
        startActivity(intent);
    }

    private void attemptUpdateInfo() {
        Intent intent = new Intent(this, PatientEditInfoPage.class);
        intent.putExtra("userId",userId);
        startActivity(intent);
    }

}
