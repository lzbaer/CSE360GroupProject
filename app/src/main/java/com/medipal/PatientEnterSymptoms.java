package com.medipal;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;


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
        ParseUser.getCurrentUser().put("urgency",calculateUrgencyScore());
        ParseUser.getCurrentUser().put("predictedIllness", calculateSimilarity());

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

    private int calculateUrgencyScore()
    {
        int urgency = 0;
        String allRecords = ParseUser.getCurrentUser().getString("Symptoms");
        String[] records = allRecords.split(",;");
        String currentRecord = records[0];
        Log.e("Current Record: ", currentRecord);

        if (records.length >= 16) {
            double[] pastSymptoms = new double[9]; //sum of all symptoms for past record
            int numPastRecords = records.length - 1; //num of past records
            double[][] allPastRecords = new double[numPastRecords][9]; //2d array of all past records

            for (int i = 1; i < numPastRecords; i++) {
                String pastRecord = records[i];
                String[] symptoms = pastRecord.split(",");
                double[] symptomsNum = new double[9]; //past record at index
                for (int j = 0; j < 9; j++) {
                    Log.e("Inside loop",symptoms[j]);
                    symptomsNum[j] = Double.parseDouble(symptoms[j]);
                    allPastRecords[i][j] = symptomsNum[j]; //add past record at index to 2d array
                    pastSymptoms[j] += symptomsNum[j];

                }
            }
            //compute mean for past record symptoms
            double[] meanPast = pastSymptoms;
            double[] variancePast = new double[9];
            double[][] varianceValues = allPastRecords;
            for (int i = 0; i < meanPast.length; i++) {
                meanPast[i] = meanPast[i] / numPastRecords;
            }

            for (int i = 0; i < numPastRecords; i++) {
                for (int j = 0; j < 9; j++) {
                    varianceValues[i][j] = Math.pow((allPastRecords[i][j] - meanPast[j]), 2);
                    variancePast[j] += varianceValues[i][j];
                }
            }

            double[] stanDevPast = variancePast;
            for (int i = 0; i < 9; i++) {
                variancePast[i] = variancePast[i] / (numPastRecords - 1);
                stanDevPast[i] = Math.pow(variancePast[i], 1 / 2);
            }

            //variance and means for each symptom for all past records done calculating


            //get current record values
            String[] cRecord;
            double[] currentSymptoms = new double[9];
            cRecord = currentRecord.split(",");
            for (int i = 0; i < 9; i++) {
                currentSymptoms[i] = Double.parseDouble(cRecord[i]);
            }

            ////compare standard deviation to each symptom in current
            double[] stanFromMean = new double[9];
            for (int i = 0; i < 9; i++) {
                //find how many standard deviations above mean
                stanFromMean[i] = currentSymptoms[i] - meanPast[i];
                if (stanFromMean[i] >= (2 * stanDevPast[i])) {
                    urgency = 2;
                } else if (stanFromMean[i] >= (stanDevPast[i]))
                    urgency = 1;
            }
        }

        return urgency;
    }

    private int calculateSimilarity()
    {

        int indexIllness = 0;

        String currentPRecords = ParseUser.getCurrentUser().getString("Symptoms");
        String[] pRecords = currentPRecords.split(",;");
        if (pRecords.length >=16)
        {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
            query.whereNotEqualTo("isDoctor", true);
            query.selectKeys(Arrays.asList("Symptoms", "CurrentIllness"));

            List<ParseObject> results = null;
            try {
                results = query.find();
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }


            int numTerminalIllnesses = 21;
            String[] organizedSymptoms = new String[numTerminalIllnesses];
            String[][] s_allRecords = new String[numTerminalIllnesses][9];
            double[][] allRecords = new double[numTerminalIllnesses][9];

            ParseObject patient = null;
            ParseUser tempUser = null;
            ParseUser currentUser = ParseUser.getCurrentUser();
            //get strings from query

            for (int i = 0; i < results.size(); i++)
            {
                patient = results.get(i);
                tempUser = (ParseUser)patient;
                int index =  results.get(i).getInt("CurrentIllness");
                String newEntry = tempUser.getString("Symptoms");
                organizedSymptoms[index] = newEntry + organizedSymptoms[index];
            }

            //fill allRecords
            String[][]s_symptomsInRec;
            for (int i = 1; i < numTerminalIllnesses; i++) {
                if (organizedSymptoms[i] == null)
                {
                    organizedSymptoms[i] = "0,0,0,0,0,0,0,0,0,;";
                }
                //records is all records of one symptom
                String[] records = organizedSymptoms[i].split(",;");

                s_symptomsInRec = new String[records.length-1][9];
                Log.e("Record Length", ""+records.length);

                for (int j = 0; j < records.length-1; j++) {
                    //symptomsInRec is a 2D array with num of patient recs as one dimension and symptoms on another
                    Log.e("Records:", records[j]);

                    s_symptomsInRec[j] = records[j].split(",");
                    for (int k = 0; k < 9; k++) {
                        //Log.e("Each index patient records", s_symptomsInRec[j][k]);
                    }

                }


                //convert all strings to double values
                double[][] symptomsInRec = new double[records.length-1][9];
                for (int r = 0; r < records.length-1; r++)
                {
                    for (int c = 0; c < 9; c++)
                    {
                        symptomsInRec[r][c] = Double.parseDouble(s_symptomsInRec[r][c]);
                    }

                }

                for (int c = 0; c < 9; c++)
                {
                    double total = 0;
                    for (int r = 0; r < records.length-1; r++)
                    {
                        total += symptomsInRec[r][c];
                        allRecords[i][c] = total/(records.length-1);
                    }
                }

            }
            //get current record values

            String currentRecord = pRecords[0];

            String[] cRecord;
            double[] currentSymptoms = new double[9];
            cRecord =  currentRecord.split(",");
            for (int i = 0; i < 9; i++)
            {
                currentSymptoms[i] = Double.parseDouble(cRecord[i]);
                Log.e("Current Symptoms", cRecord[i]);
            }

            double[][] diffArray = new double [numTerminalIllnesses][9];
            double[] totalDiff = new double[numTerminalIllnesses];
            for (int i = 0; i < numTerminalIllnesses; i++)
            {
                double totalDiffperIndex = 0;
                for (int j = 0; j < 9; j++)
                {
                    diffArray[i][j] = Math.abs(currentSymptoms[j] - allRecords[i][j]);
                    totalDiffperIndex += diffArray[i][j];
                }
                totalDiff[i] = totalDiffperIndex;
                Log.e("index",""+i);
                //Log.e("Total difference at index", "" + totalDiff[i]);

            }

            //search through totalDiff to find minimum difference
            indexIllness = 0;
            for (int i = 0; i < numTerminalIllnesses; i++)
            {
                if(totalDiff[i] < totalDiff[indexIllness])
                {
                    indexIllness = i;
                }
            }
        }
        Log.e("finaloutput", "" + indexIllness);
        return indexIllness;
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
