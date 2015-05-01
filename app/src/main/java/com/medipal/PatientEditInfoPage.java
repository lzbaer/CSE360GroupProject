package com.medipal;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import android.widget.Toast;

import com.parse.ParseUser;


public class PatientEditInfoPage extends ActionBarActivity {

    //UI elements
    private TextView mFullNameView;
    private TextView mEmailView;
    private EditText mDoctorIdView;
    private ImageButton mDoctorIdHelpButton;
    private Spinner mConditionSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_edit_info_page);

        //set up account information
        mFullNameView = (TextView) findViewById(R.id.full_name);
        mEmailView = (TextView) findViewById(R.id.email);
        mDoctorIdView = (EditText) findViewById(R.id.doctor_id);
        mConditionSpinner = (Spinner) findViewById(R.id.condition_spinner);
        mDoctorIdView.clearFocus();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.conditions_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mConditionSpinner.setAdapter(adapter);

        //replace placeholder text with user information
        String firstName = ParseUser.getCurrentUser().getString("First_Name") + " "; //add space between names
        String lastName = ParseUser.getCurrentUser().getString("Last_Name");
        String email = ParseUser.getCurrentUser().getString("username");
        String doctorId = ParseUser.getCurrentUser().getString("DoctorID");
        int conditionPosition = ParseUser.getCurrentUser().getInt("CurrentIllness");

        //set text fields
        mFullNameView.setText(firstName+lastName);
        mEmailView.setText(email);
        mDoctorIdView.setText(doctorId);
        mConditionSpinner.setSelection(conditionPosition);

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

        String doctorId = mDoctorIdView.getText().toString();
        int conditionPosition = mConditionSpinner.getSelectedItemPosition();
        ParseUser currentUser = ParseUser.getCurrentUser();
        //check for a valid doctorID
        View focusView = null;
        boolean cancel = false;
        if (doctorId.length() == 0) {
            mDoctorIdView.setError(getString(R.string.error_field_required));
            focusView = mDoctorIdView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            currentUser.put("DoctorID",doctorId);
            currentUser.put("CurrentIllness",conditionPosition);
            currentUser.saveInBackground();

            Toast.makeText(getApplicationContext(),
                    getString(R.string.info_updated),
                    Toast.LENGTH_SHORT).show();
            finish();
        }
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
        if (id == R.id.action_logout) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
