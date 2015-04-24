package com.medipal;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;


public class PatientEditInfoPage extends ActionBarActivity {

    //current user
    private String userId;

    //UI elements
    private TextView mFullNameView;
    private TextView mEmailView;
    private EditText mDoctorIdView;
    private ImageButton mDoctorIdHelpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_edit_info_page);

        //get userId from parent activity
        Intent intent = this.getIntent();
        userId = intent.getStringExtra(userId);

        //set up account information
        mFullNameView = (TextView) findViewById(R.id.full_name);
        mEmailView = (TextView) findViewById(R.id.email);
        mDoctorIdView = (EditText) findViewById(R.id.doctor_id);
        mDoctorIdView.clearFocus();

        Spinner spinner = (Spinner) findViewById(R.id.condition_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.conditions_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //replace placeholder text with user information
        //TODO get all these values from database
        String firstName = null + " "; //add space between names
        String lastName = null;
        String email = null;
        String doctorId = null;

        //TODO uncomment this
        //display account information to user
        //mFullNameView.setText(firstName+lastName);
        //mEmailView.setText(email);
        //mDoctorIdView.setText(doctorId);

        //TODO remove this
        mFullNameView.setText("Full Name");
        mEmailView.setText("theuserhasintentionallyinputtedanemailthatisfartoomassivetobedisplayedononelinefortestingpurposes@domain.com");
        mDoctorIdView.setText("1");

        //create button & action listener for signing in
        Button mUpdateInfo = (Button) findViewById(R.id.update_info_button);
        mUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptUpdate();
            }
        });

        //create help button
        mDoctorIdHelpButton = (ImageButton) findViewById(R.id.doctor_id_help_button);
        mDoctorIdHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDoctorIdView.requestFocus();
                ((EditText)mDoctorIdView).setError(getString(R.string.doctor_id_tool_tip), null);
            }
        });
    }

    private void attemptUpdate() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_patient_edit_info_page, menu);
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
}
