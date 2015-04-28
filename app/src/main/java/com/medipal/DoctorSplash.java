package com.medipal;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseUser;

public class DoctorSplash extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_splash);

        //set title bar
        String lastName="";
        lastName = ParseUser.getCurrentUser().getString("Last_Name");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Welcome, Dr. " + lastName + "!");

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
        Intent intent = new Intent(this, DoctorPatientsList.class);
        startActivity(intent);
    }
}
