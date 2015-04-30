package com.medipal;

import android.app.ActionBar;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.*;

import com.parse.*;



public class DoctorSplash extends ActionBarActivity {

    //private List<ParseObject> patientsList;
    private ParseObject[] patientsList;
    private ArrayAdapter<CharSequence> spinnerAdapter= null;
    private ArrayAdapter<CharSequence> listAdapter= null;
    private List<CharSequence> alerts = null;
    private List<ParseObject> results = null;
    private List<ParseObject> problematic = null;
    private List<ParseObject> significantlyProblematic = null;
    private ParseQuery query =null;
    private ParseQuery alertQuery =null;

    //UI elements
    private String[] inString = null;
    private TextView mFullNameView;
    private TextView mEmailView;
    private TextView mDoctorIdView;
    private ListView mPatientsListView;
    private Spinner mSpinner =null;
    private Button mStartViewPatientInfoButton=null;
    private RadioGroup mRadioGroup;
    private RadioButton mRecencyRadioButton = null;
    private RadioButton mUrgencyRadioButton = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_splash);

        //set up account information
        mFullNameView = (TextView) findViewById(R.id.full_name);
        mEmailView = (TextView) findViewById(R.id.email);
        mDoctorIdView = (TextView) findViewById(R.id.doctor_id);
        mSpinner = (Spinner) findViewById(R.id.spinner);
        mPatientsListView = (ListView) findViewById(R.id.listView);
        mRadioGroup = (RadioGroup) findViewById(R.id.doctor_splash_radio_group);
        mUrgencyRadioButton = (RadioButton) findViewById(R.id.doctor_splash_radio_button_urgency);
        mRecencyRadioButton = (RadioButton) findViewById(R.id.doctor_splash_radio_button_recency);
        mStartViewPatientInfoButton = (Button) findViewById(R.id.view_patients_button);

        //listeners
        mStartViewPatientInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDoctorPatientsList(v);
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.doctor_splash_radio_button_recency:
                        sortByRecency();
                        updateViews();
                        break;
                    case R.id.doctor_splash_radio_button_urgency:
                        sortByUrgency();
                        updateViews();
                        break;
                    default:
                        break;
                }
            }
        });

        //replace placeholder text with user information
        String firstName = ParseUser.getCurrentUser().getString("First_Name") + " "; //add space between names
        String lastName = ParseUser.getCurrentUser().getString("Last_Name");
        String email = ParseUser.getCurrentUser().getString("username");
        String doctorId = ParseUser.getCurrentUser().getString("DoctorID");

        //set text fields
        mFullNameView.setText(firstName+lastName);
        mEmailView.setText(email);
        mDoctorIdView.setText(doctorId);

        //set title bar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Welcome, Dr. " + lastName + "!");

        getResultsFromQuery();
        updateViews();

    }

    private void getResultsFromQuery() {
        //get patients
        query = ParseQuery.getQuery("_User");
        query.whereEqualTo("DoctorID", ParseUser.getCurrentUser().getString("DoctorID"));
        query.whereNotEqualTo("isDoctor", true);

        try {
            results = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void updateViews() {

        //set spinner resource array
        String[] patientNames = getPatientNames();
        if(patientNames.length==0){
            patientNames= new String[] { "No Patients Found!"};
        }

        //set list view resource array
        String[] alertsTexts = getAlerts();
        if(alertsTexts.length==0){
            alertsTexts= new String[] { "No Alerts Found!"};
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        if(spinnerAdapter!=null){
            spinnerAdapter=null;
            mSpinner.setAdapter(null);
        }
        spinnerAdapter = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,patientNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);

        //create an arrayadapter for list
        listAdapter = new ArrayAdapter<CharSequence>(this,R.layout.text_view,alertsTexts);
        mPatientsListView.setAdapter(listAdapter);
    }

    private void sortByRecency() {
        if(query!=null)
            query.orderByDescending("updatedAt");
    }

    private void sortByUrgency() {
        if(query!=null)
            query.orderByDescending("urgency");
    }

    private String[] getPatientNames() {
        String[] patientNames = null;
        if(results!=null){
            patientNames = new String[results.size()];
            for(int i =0; i<patientNames.length;i++){
                patientNames[i] = results.get(i).getString("First_Name") + " " + results.get(i).getString("Last_Name");
            }
        }
        return patientNames;
    }

    private String[] getAlerts() {

        ArrayList<String> stringlist1 = new ArrayList<String>();

        //get patients
        alertQuery = ParseQuery.getQuery("_User");
        alertQuery.whereEqualTo("DoctorID", ParseUser.getCurrentUser().getString("DoctorID"));
        alertQuery.whereNotEqualTo("isDoctor", true);
        alertQuery.selectKeys(Arrays.asList("urgency"));
        alertQuery.whereEqualTo("urgency",(int)2);

        try {
             significantlyProblematic = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //set spinner
        if(significantlyProblematic!=null) {
            for (int i = 0; i < significantlyProblematic.size(); i++) {
                stringlist1.add(significantlyProblematic.get(i).getString("First_Name") + " " + significantlyProblematic.get(i).getString("Last_Name") + ": Significantly Problematic!");
            }
        }
        //get patients
        alertQuery.whereEqualTo("urgency",(int)1);

        try {
            problematic = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //set spinner
        if(problematic!=null) {
            for (int i = 0; i < problematic.size(); i++) {
                stringlist1.add(problematic.get(i).getString("First_Name") + " " + problematic.get(i).getString("Last_Name") + ": Problematic!");
            }
        }

        String[] returnstr = stringlist1.toArray(new String[stringlist1.size()]);
        return returnstr;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_doctor_splash, menu);
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

    public void startDoctorAlerts(View view){


        Intent intent = new Intent(this, DoctorAlerts.class);
        startActivity(intent);
    }

    public void startDoctorPatientsList(View view){

        //store patient
        int position = mSpinner.getSelectedItemPosition();
        ParseObject patient = results.get(position);
        String objectId = patient.getObjectId();

        Intent intent = new Intent(this, DoctorViewPatientInfoPage.class);
        intent.putExtra("objectId",objectId);
        startActivity(intent);
    }

    public String[] getAnArray(String[] str){
        inString = str.clone();
        return str;
    }
}
