package com.medipal;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.BaseAdapter;
//import com.parse.ParseQueryAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.parse.ParseObject;


public class DoctorPatientsList extends ActionBarActivity {

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_patients_list);

        //get userId from parent activity
        Intent intent = this.getIntent();
        userId = intent.getStringExtra(userId);



        /*
        //use a layout with ListView (id: patientsListView) using custom adapater
        ParseQueryAdapter<ParseObject> parseQueryAdapter =
                new ParseQueryAdapter<ParseObject>(this,"Instrument");
        parseQueryAdapter.setTextKey("name");
        parseQueryAdapter.setImageKey("image");

        //get the list reference
        ListView patientsListView = (ListView) findViewById(R.id.patientsListView);
        patientsListView.setAdapter(parseQueryAdapter);
        */
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
}
