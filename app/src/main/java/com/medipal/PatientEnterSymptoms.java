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

/**
 * class calculates urgency, similarity, allows submission of
 * symptoms
 * created by Lisa
 */
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

    //private function to calculate urgency of submitted survey
    //returns urgency rating
    private int calculateUrgencyScore()
    {
        //initialize urgency
        int urgency = 0;
        //get current user's string of submissions and parse to separate records
        String allRecords = ParseUser.getCurrentUser().getString("Symptoms");
        String[] records = allRecords.split(",;");
        //record being submitted stored in index 0
        String currentRecord = records[0];
        Log.e("Current Record: ", currentRecord);

        //check if number of patient records is greater than or equal to 16 in order to calculate urgency
        //purpose is to have a general idea of patient's usual wellbeing before attempting to determine urgency
        if (records.length >= 16) {
            //create variables
            double[] pastSymptoms = new double[9]; //sum of all symptoms for past record
            int numPastRecords = records.length - 1; //num of past records
            double[][] allPastRecords = new double[numPastRecords][9]; //2d array of all past records

            //loop through records to get ratings of each symptom
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
            //find average for each symptom
            for (int i = 0; i < meanPast.length; i++) {
                meanPast[i] = meanPast[i] / numPastRecords;
            }

            //determine variance for each symptom
            for (int i = 0; i < numPastRecords; i++) {
                for (int j = 0; j < 9; j++) {
                    varianceValues[i][j] = Math.pow((allPastRecords[i][j] - meanPast[j]), 2);
                    variancePast[j] += varianceValues[i][j];
                }
            }

            //calculate standard deviation for each symptom using variance array
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

            //compare standard deviation to each symptom in current
            double[] stanFromMean = new double[9];
            //traverse through current symptoms to compare ratings to the mean
            for (int i = 0; i < 9; i++) {
                //find how many standard deviations above mean
                stanFromMean[i] = currentSymptoms[i] - meanPast[i];
                //determine urgency based on how many standard deviations above the mean of past symptoms

                //if difference of symptom from mean is at least 2 standard deviations above mean
                //urgency = 2
                if (stanFromMean[i] >= (2 * stanDevPast[i])) {
                    urgency = 2;
                //if difference of symptom from mean is at least 1 standard deviation and less than 2
                // standard deviations above mean
                //urgency =1
                } else if (stanFromMean[i] >= (stanDevPast[i]))
                    urgency = 1;
            }
        }
        //return urgency value
        return urgency;
    }

    //private method to calculate similarity of record to other diseases
    //returns index of disease that is most similar

    /*finds average of all symptoms for every illness, then compares current record to
    determine which illness the current record is most similar to
     */
    private int calculateSimilarity()
    {

        int indexIllness = 0;

        //create array of strings to hold symptom ratings of current survey
        String currentPRecords = ParseUser.getCurrentUser().getString("Symptoms");
        String[] pRecords = currentPRecords.split(",;");
        //checks if patient has submitted at least 15 previous records in order to understand
        //general trends of patient symptoms before attempting to data mine
        if (pRecords.length >=16)
        {
            //implement parse query to get all patients
            ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
            query.whereNotEqualTo("isDoctor", true);
            //retrieves categories "symptoms" and "currentIllness" for each patient in query
            query.selectKeys(Arrays.asList("Symptoms", "CurrentIllness"));

            //create list of parse objects to hold information
            List<ParseObject> results = null;
            try {
                results = query.find();
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }

            //create variables
            int numTerminalIllnesses = 21;
            String[] organizedSymptoms = new String[numTerminalIllnesses];
            String[][] s_allRecords = new String[numTerminalIllnesses][9];
            double[][] allRecords = new double[numTerminalIllnesses][9];

            //variables needs to get variables from parse query
            ParseObject patient = null;
            ParseUser tempUser = null;
            ParseUser currentUser = ParseUser.getCurrentUser();
            //get strings from query

            //loop through list of results to collect query information
            for (int i = 0; i < results.size(); i++)
            {
                patient = results.get(i);
                tempUser = (ParseUser)patient;
                //get current illness of patient to use as index for an array
                //that will hold all the patient records
                int index =  results.get(i).getInt("CurrentIllness");
                String newEntry = tempUser.getString("Symptoms");
                organizedSymptoms[index] = newEntry + organizedSymptoms[index];
            }

            //fill allRecords
            String[][]s_symptomsInRec;
            for (int i = 1; i < numTerminalIllnesses; i++) {
                //if no patient has an illness, set symptom ratings for that illness to 0
                if (organizedSymptoms[i] == null)
                {
                    organizedSymptoms[i] = "0,0,0,0,0,0,0,0,0,;";
                }
                //records is all records of one symptom
                String[] records = organizedSymptoms[i].split(",;");

                s_symptomsInRec = new String[records.length-1][9];
                Log.e("Record Length", ""+records.length);

                //split individual record strings to obtain symptom ratings
                for (int j = 0; j < records.length-1; j++) {
                    //symptomsInRec is a 2D array with num of patient recs as one dimension and symptoms on another
                    Log.e("Records:", records[j]);

                    s_symptomsInRec[j] = records[j].split(",");

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

                //get average for every symptom, for every illness
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
            //convert current record values to doubles
            for (int i = 0; i < 9; i++)
            {
                currentSymptoms[i] = Double.parseDouble(cRecord[i]);
                Log.e("Current Symptoms", cRecord[i]);
            }

            double[][] diffArray = new double [numTerminalIllnesses][9];
            double[] totalDiff = new double[numTerminalIllnesses];
            //find difference of each symptom to the mean of each symptom for every illness
            for (int i = 0; i < numTerminalIllnesses; i++)
            {
                double totalDiffperIndex = 0;
                for (int j = 0; j < 9; j++)
                {
                    //determine absolute difference and add to running total
                    diffArray[i][j] = Math.abs(currentSymptoms[j] - allRecords[i][j]);
                    totalDiffperIndex += diffArray[i][j];
                }
                //save difference totals to an array
                totalDiff[i] = totalDiffperIndex;
                Log.e("index",""+i);
                //Log.e("Total difference at index", "" + totalDiff[i]);

            }

            //search through totalDiff to find minimum difference, tracks index of minimum
            indexIllness = 0;
            //find minimum difference total
            for (int i = 0; i < numTerminalIllnesses; i++)
            {
                if(totalDiff[i] < totalDiff[indexIllness])
                {
                    indexIllness = i;
                }
            }
        }
        Log.e("finaloutput", "" + indexIllness);
        //return index of illness where difference of symptoms is minimum
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
