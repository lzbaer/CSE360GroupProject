package com.medipal;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
//import com.parse.ParseQueryAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;


public class DoctorPatientsList extends ActionBarActivity {

    private ListView mPatientsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_patients_list);

        String patientsArray[] = new String[50];
        for (int i = 0; i < patientsArray.length; i++) {
            patientsArray[i] = "Patient #" + i;
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        // String docID = ParseUser.getCurrentUser().getString("DoctorID");
        query.whereEqualTo("DoctorID", ParseUser.getCurrentUser().getString("DoctorID"));
        query.whereNotEqualTo("isDoctor", true);
        query.selectKeys(Arrays.asList("First_Name", "Last_Name", "urgency", "updatedAt"));
        ;
        List<ParseObject> results = null;
        try {
            results = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Toast.makeText(getBaseContext(),
                results.toString(),
                Toast.LENGTH_SHORT).show();

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, R.layout.text_view, patientsArray);
        // Apply the adapter to the spinner

        final ListView mPatientsListView = (ListView) findViewById(R.id.patientsListView);
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
