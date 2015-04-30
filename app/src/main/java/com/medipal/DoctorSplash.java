package com.medipal;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.*;

import com.parse.*;



public class DoctorSplash extends ActionBarActivity {

    //private List<ParseObject> patientsList;
    private ParseObject[] patientsList;
    //UI elements
    private String[] inString = null;
    private TextView mFullNameView;
    private TextView mEmailView;
    private TextView mDoctorIdView;
    private ListView mPatientsListView;
    private List<CharSequence> alerts = null;
    private List<ParseObject> results = null;
    private List<ParseObject> problematic = null;
    private List<ParseObject> significantlyProblematic = null;
    private ParseQuery query =null;
    private ParseQuery alertQuery =null;
    private Spinner mSpinner =null;
    private ArrayAdapter<CharSequence> spinnerAdapter= null;
    private ArrayAdapter<CharSequence> listAdapter= null;

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

        //get patients
        query = ParseQuery.getQuery("_User");
        // String docID = ParseUser.getCurrentUser().getString("DoctorID");
        query.whereEqualTo("DoctorID", ParseUser.getCurrentUser().getString("DoctorID"));
        query.whereNotEqualTo("isDoctor", true);
        query.selectKeys(Arrays.asList("First_Name", "Last_Name", "urgency", "updatedAt"));

        try {
            results = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String[] patientNames=getAlerts();

        //set spinner
        if(results!=null){
            patientNames = new String[results.size()];
            for(int i =0; i<patientNames.length;i++){
                patientNames[i] = results.get(i).getString("First_Name") + " " + results.get(i).getString("Last_Name");
                //+"/t" + results.get(i).getDouble("urgency");
            }
        }

        //String[] alertsTexts = {"Raymond Seto: Signifcantyl Progblematic!", "Patrick Michaelsen: Problematic!","Lisa Baer: Problematic!"};
        String[] alertsTexts = getAlerts();

        // Create an ArrayAdapter using the string array and a default spinner layout
        spinnerAdapter = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,patientNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);

        //create an arrayadapter for list
        listAdapter = new ArrayAdapter<CharSequence>(this,R.layout.text_view,alertsTexts);
        mPatientsListView.setAdapter(listAdapter);



//        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
//        query.getInBackground("VIAj830EfU", new GetCallback<ParseObject>() {
//            public void done(ParseObject object, ParseException e) {
//                if (e == null) {
//                    String toPrint = object.getString("First_Name");
//                    Toast.makeText(getApplicationContext(),
//                            "Name " + toPrint,
//                            Toast.LENGTH_SHORT).show();
//                } else {
//
//                }
//            }
//        });

        /*
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
       // String docID = ParseUser.getCurrentUser().getString("DoctorID");
        query.whereEqualTo("DoctorID", ParseUser.getCurrentUser().getString("DoctorID"));
        query.whereNotEqualTo("isDoctor",true);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {

                } else {

                }
                patientsList = new ParseObject[scoreList.size()];
                for (int i = 0; i < scoreList.size(); i++) {
                    Toast.makeText(getApplicationContext(),
                            "Patient " + (i+1) + " " + scoreList.get(i).getString("First_Name"),
                            Toast.LENGTH_SHORT).show();
                    patientsList[i] = scoreList.get(i);

                }
            }
        });
                */
       // System.out.print(patientsList[0]);


    }

    private String[] getAlerts() {

        ArrayList<String> stringlist1 = new ArrayList<String>();

        //get patients
        alertQuery = ParseQuery.getQuery("_User");
        query.whereEqualTo("DoctorID", ParseUser.getCurrentUser().getString("DoctorID"));
        alertQuery.whereNotEqualTo("isDoctor", true);
        alertQuery.whereEqualTo("urgency",2);
        query.selectKeys(Arrays.asList("First_Name", "Last_Name", "urgency"));

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
        alertQuery.whereEqualTo("urgency",1);
        query.selectKeys(Arrays.asList("First_Name", "Last_Name", "urgency"));

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
        int position = mSpinner.getSelectedItemPosition();
        String objectId = results.get(position).getString("objectId");
        Intent intent = new Intent(this, DoctorViewPatientInfoPage.class);
        intent.putExtra("objectId",objectId);
        startActivity(intent);
    }

    public String[] getAnArray(String[] str){
        inString = str.clone();
        return str;
    }
}
