package com.medipal;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.parse.ParseQuery;
import com.parse.ParseUser;


public class PatientEnterSymptoms extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_enter_symptoms);

        //create button and listener for submit survey
        Button mSubmitSurvey = (Button) findViewById(R.id.submit_survey_button);
        mSubmitSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSubmitSurvey();
            }
        });
    }

    private void attemptSubmitSurvey() {



        ParseUser.getCurrentUser().put("Symptoms", getValuesFromSurveyFields() + ";" + ParseUser.getCurrentUser().getString("Symptoms"));
        ParseUser.getCurrentUser().saveInBackground();


        Toast.makeText(getApplicationContext(),
                getString(R.string.survey_submitted),
                Toast.LENGTH_SHORT).show();
        finish();
    }


    private String getValuesFromSurveyFields() {
        String values = "";

        //get integer values of sliders
        int i = -1;
        double[] symptoms = new double[9];
        symptoms[++i] = ((SeekBar) findViewById(R.id.pain_bar)).getProgress();
        symptoms[++i] = ((SeekBar) findViewById(R.id.fatigue_bar)).getProgress();
        symptoms[++i] = ((SeekBar) findViewById(R.id.drowsiness_bar)).getProgress();
        symptoms[++i] = ((SeekBar) findViewById(R.id.nausea_bar)).getProgress();
        symptoms[++i] = ((SeekBar) findViewById(R.id.appetite_bar)).getProgress();
        symptoms[++i] = ((SeekBar) findViewById(R.id.breath_bar)).getProgress();
        symptoms[++i] = ((SeekBar) findViewById(R.id.depression_bar)).getProgress();
        symptoms[++i] = ((SeekBar) findViewById(R.id.anxiety_bar)).getProgress();
        symptoms[++i] = 100 - ((SeekBar) findViewById(R.id.well_being_bar)).getProgress();

        //concatenate values into a string
        for(i=0;i<symptoms.length;i++) {
            symptoms[i] = Math.round(symptoms[i] / 10); //convert from double 0-100 to 0-10
            values += (int) symptoms[i] + ",";
        }

        return values;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_patient_enter_symptoms, menu);
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
}
