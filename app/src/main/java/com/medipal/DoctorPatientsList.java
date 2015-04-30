package com.medipal;

import android.app.ActionBar;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
//import com.parse.ParseQueryAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class DoctorPatientsList extends ActionBarActivity {

    private ListView mPatientsListView;
    private RadioGroup mRadioGroup;
    private RadioButton mRecencyRadioButton;
    private RadioButton mUrgencyRadioButton;
    private Spinner mPatientSpinner;
    private List<ParseObject> results = null;
    private ArrayAdapter<CharSequence> adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_patients_list);

        //get patients
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        // String docID = ParseUser.getCurrentUser().getString("DoctorID");
        query.whereEqualTo("DoctorID", ParseUser.getCurrentUser().getString("DoctorID"));
        query.whereNotEqualTo("isDoctor", true);
        query.selectKeys(Arrays.asList("First_Name", "Last_Name", "urgency", "updatedAt"));

        try {
            results = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //create UI elements
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mRecencyRadioButton = (RadioButton) findViewById(R.id.recency_radio_button);
        mUrgencyRadioButton = (RadioButton) findViewById(R.id.urgency_radio_button);
        mPatientSpinner = (Spinner) findViewById(R.id.patient_spinner);
        mPatientsListView = (ListView) findViewById(R.id.patient_list);

        updateSpinner();

        //set listeners
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.urgency_radio_button:
                        //sortByUrgency();
                        updateSpinner();
                        break;
                    case R.id.recency_radio_button:
                        //sortByRecency();
                        updateSpinner();
                        break;
                }
            }
        });

    }


    private void updateSpinner() {

        String[] patientNames = new String[results.size()];
        for(int i =0; i<patientNames.length;i++){
            patientNames[i] = results.get(i).getString("First_Name") + " " + results.get(i).getString("Last_Name");
                    //+"/t" + results.get(i).getDouble("urgency");
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,patientNames);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        //mPatientSpinner.setAdapter(adapter);
        mPatientsListView.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_doctor_patients_list, menu);
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

    

    public void onActionListener(View view){
        TextView PatientName = (TextView) view;
        Toast.makeText(getBaseContext(),
                PatientName.getText(),
                Toast.LENGTH_SHORT).show();


    }


}

