package com.medipal_new;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;


public class LogNew extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_log_new);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void send_button(View view)
    {
        EditText editText = (EditText) findViewById(R.id.name_field);
        String name = editText.getText().toString();
        double[] symptoms = new double[9];
        double pain = ((SeekBar) findViewById(R.id.pain_bar)).getProgress();
        double fatigue = ((SeekBar) findViewById(R.id.fatigue_bar)).getProgress();
        double drowsiness = ((SeekBar) findViewById(R.id.drowsiness_bar)).getProgress();
        double nausea = ((SeekBar) findViewById(R.id.nausea_bar)).getProgress();
        double appetite = ((SeekBar) findViewById(R.id.appetite_bar)).getProgress();
        double breath = ((SeekBar) findViewById(R.id.breath_bar)).getProgress();
        double depression = ((SeekBar) findViewById(R.id.depression_bar)).getProgress();
        double anxiety = ((SeekBar) findViewById(R.id.anxiety_bar)).getProgress();
        double well_being = 100 - ((SeekBar) findViewById(R.id.well_being_bar)).getProgress();
        ViewPatient.display += name +
                "/" + pain +
                "/" + fatigue +
                "/" + drowsiness +
                "/" + nausea +
                "/" + appetite +
                "/" + breath +
                "/" + depression +
                "/" + anxiety +
                "/" + well_being +
                "\n";
        Intent intent = new Intent(this, LoginScreen.class);
        startActivity(intent);
    }
}
