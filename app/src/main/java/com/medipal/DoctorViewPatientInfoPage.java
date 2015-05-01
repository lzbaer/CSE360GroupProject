package com.medipal;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DoctorViewPatientInfoPage extends ActionBarActivity {

    private String objectId;

    //UI elements
    private TextView mFullNameView;
    private Spinner mConditionSpinner;
    private Spinner mPotentialSpinner;
    private TextView mCurrentSymptoms;
    private ListView mPatientRecordsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_patient_info_page);

        //get userId from parent activity
        Intent intent = this.getIntent();
        objectId = intent.getStringExtra("objectId");
        objectId = intent.getStringExtra("objectId");

        //set up account information
        mFullNameView = (TextView) findViewById(R.id.full_name);
        mConditionSpinner = (Spinner) findViewById(R.id.condition_spinner);
        mPotentialSpinner = (Spinner) findViewById(R.id.potential_spinner);
        mCurrentSymptoms = (TextView) findViewById(R.id.current_symptoms);
        mPatientRecordsListView = (ListView) findViewById(R.id.patient_records);

        //build some GUI
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.conditions_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mConditionSpinner.setAdapter(adapter);
        mPotentialSpinner.setAdapter(adapter);

        //get the patient object from objectid
        ParseQuery query = ParseQuery.getQuery("_User");
        query.whereEqualTo("objectId", objectId);
        List<ParseObject> results = null;
        try {
            results = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //store patient
        ParseObject patient = results.get(0);

        //get array of symptom arrays
        if (patient.getString("Symptoms").equals("")) {
            // DO NOTHING
        }
        else {
            ArrayList<String[]> patientRecords = getPatientRecords(patient.getString("Symptoms"));
            PatientRecordsAdapter patientRecordsAdapter = new PatientRecordsAdapter(this,patientRecords);
            mPatientRecordsListView.setAdapter(patientRecordsAdapter);
        }

        /*String str = "";
        for(int i=0;i<15;i++){
            String[] dummyData=new String[9];
            for(int j=0;j<9;j++){
                dummyData[j] = ""+(Math.round((Math.random()*10)));
                str+=dummyData[j]+", ";
            }
            str+="\n ";
            patientRecords.add(dummyData);
        }
        Log.i("Dummy Array",str);*/

        //build a records list adapter


        //replace placeholder text with user information
        String firstName = patient.getString("First_Name") + " "; //add space between names
        String lastName = patient.getString("Last_Name");
        int conditionPosition = patient.getInt("CurrentIllness");
        int potentialConditionPosition = patient.getInt("predictedIllness");
        int urgency = patient.getInt("urgency");
        final String[] urgencyStrings = {"Stable","Problematic", "Significantly Problematic" };
        String currentSymptoms = "Current Symptoms: \n" + urgencyStrings[urgency];

        //set fields
        mFullNameView.setText(firstName + lastName);
        mConditionSpinner.setSelection(conditionPosition);
        mPotentialSpinner.setSelection(potentialConditionPosition);
        mCurrentSymptoms.setText(currentSymptoms);

    }

    private ArrayList<String[]> getPatientRecords(String symptoms) {
        ArrayList<String[]> listOfSubmissions = new ArrayList<String[]>();
        String[] indivSubmissions = symptoms.split(",;");

        for (int i = 0; i < indivSubmissions.length; i++) {
            listOfSubmissions.add(indivSubmissions[i].split(","));
        }

        return listOfSubmissions;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_doctor_view_patient_info_page, menu);
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

    public void giveUpFocus(View view ){
        view.clearFocus();
    }


}
