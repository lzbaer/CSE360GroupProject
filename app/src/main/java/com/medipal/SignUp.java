package com.medipal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.*;

import java.util.Arrays;
import java.util.List;

/**
 * allow user to sign up as doctor/patient
 * prevent invalid inputs in fields
 * prevent duplicate users
 * GUI created by Patrick
 * parse by Ankit
 * data validation methods by Raymond
 *
 */

public class SignUp extends ActionBarActivity {

    //UI elements
    private EditText mFirstNameView;
    private EditText mLastNameView;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private RadioGroup mRadioGroup;
    private RadioButton mPatientRadioButton;
    private RadioButton mDoctorRadioButton;
    private LinearLayout mPatientRadioButtonLayout;
    private LinearLayout mDoctorRadioButtonLayout;
    private EditText mDoctorIdView;
    private ImageButton mDoctorIdHelpButton;
    private View mProgressView;
    private View mEmailLoginFormView;
    private View mSignOutButtons;
    private View mLoginFormView;

    //set up animation
    private int mAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Set up the login form.
        mFirstNameView = (EditText) findViewById(R.id.first_name);
        mLastNameView=(EditText) findViewById(R.id.last_name);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptSignUp();
                    return true;
                }
                return false;
            }
        });

        // Retrieve and cache the system's default "short" animation time.
        mAnimationDuration = getResources().getInteger(
                android.R.integer.config_mediumAnimTime);

        //create radio buttons, patient is default specified by XML
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mPatientRadioButton = (RadioButton) findViewById(R.id.patient_radio_button);
        mDoctorRadioButton = (RadioButton) findViewById(R.id.doctor_radio_button);

        //create both options and hide doctor's version by default
        mPatientRadioButtonLayout = (LinearLayout) findViewById(R.id.patient_radio_button_layout);
        mDoctorRadioButtonLayout = (LinearLayout) findViewById(R.id.doctor_radio_button_layout);
        mDoctorRadioButtonLayout.setVisibility(LinearLayout.GONE);

        //set action listener for radiogroup
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.patient_radio_button:
                        //crossfade between layouts
                        mPatientRadioButtonLayout.setAlpha(0f);
                        mPatientRadioButtonLayout.setVisibility(LinearLayout.VISIBLE);

                        // Animate the patient view to 100% opacity, and clear any animation
                        // listener set on the view.
                        mPatientRadioButtonLayout.animate()
                                .alpha(1f)
                                .setDuration(mAnimationDuration)
                                .setListener(null);

                        // Animate the doctor view to 0% opacity. After the animation ends,
                        // set its visibility to GONE as an optimization step (it won't
                        // participate in layout passes, etc.)
                        mDoctorRadioButtonLayout.animate()
                                .alpha(0f)
                                .setDuration(mAnimationDuration)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        mDoctorRadioButtonLayout.setVisibility(View.GONE);
                                    }
                                });
                        break;
                    case R.id.doctor_radio_button:
                        // Animate the doctor view to 0% opacity. After the animation ends,
                        // set its visibility to GONE as an optimization step (it won't
                        // participate in layout passes, etc.)
                        mPatientRadioButtonLayout.animate()
                                .alpha(0f)
                                .setDuration(mAnimationDuration)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        mPatientRadioButtonLayout.setVisibility(View.GONE);
                                    }
                                });

                        //crossfade between layouts
                        mDoctorRadioButtonLayout.setAlpha(0f);
                        mDoctorRadioButtonLayout.setVisibility(LinearLayout.VISIBLE);

                        // Animate the patient view to 100% opacity, and clear any animation
                        // listener set on the view.
                        mDoctorRadioButtonLayout.animate()
                                .alpha(1f)
                                .setDuration(mAnimationDuration)
                                .setListener(null);
                        break;
                    default:
                        throw (new Error(getString(R.string.error_checked_id_invalid)){});
                }
            }
        });

        //create button & action listener for signing in
        Button mEmailSignInButton = (Button) findViewById(R.id.sign_up_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });

        //create doctor id form
        mDoctorIdView = (EditText) findViewById(R.id.doctor_id);

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

    public void attemptSignUp(){
        //Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mFirstNameView.setError(null);
        mLastNameView.setError(null);
        mDoctorIdView.setError(null);

        // Store values at the time of the login attempt.
        String first = mFirstNameView.getText().toString();
        String last = mLastNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String doctorIdStr = mDoctorIdView.getText().toString();
        String symptoms = "";
        System.out.print(email);
        boolean cancel = false;
        View focusView = null;

        //check for a valid first name
        if (TextUtils.isEmpty(first)) {
            mFirstNameView.setError(getString(R.string.error_field_required));
            focusView = mFirstNameView;
            cancel = true;
        }

        //check for a valid last name
        if (TextUtils.isEmpty(last)) {
            mLastNameView.setError(getString(R.string.error_field_required));
            focusView = mLastNameView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        //check for valid doctor ID
        if (mPatientRadioButton.isChecked()&& doctorIdStr.isEmpty())
        {
            mDoctorIdView.setError(getString(R.string.error_invalid_doctor_id));
            focusView = mDoctorIdView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            registerNewUser(email,password,first,last,doctorIdStr,symptoms);
        }
    }

    /**
     * method by Ankit, Lisa, Raymond, Patrick, Tiayan
     * @param email
     * @param password
     * @param first
     * @param last
     * @param doctorIdStr
     * @param symptoms
     */
    private void registerNewUser(String email, String password, String first, String last, String doctorIdStr, String symptoms) {
        //format name
        first = first.substring(0,1).toUpperCase() + first.substring(1,first.length());
        last = last.substring(0,1).toUpperCase() + last.substring(1,last.length());

        //Create Parse
        ParseUser user = new ParseUser();
        user.setUsername(email);
        user.setPassword(password);
        user.put("First_Name", first);
        user.put("Last_Name", last);
        user.put("DoctorID", doctorIdStr);

        //GUARD AGAINSTS UNDEFINED VARIABLES
        user.put("Symptoms", symptoms);
        final int ZERO = 0;
        user.put("CurrentIllness", ZERO);
        user.put("predictedIllness", ZERO);
        user.put("urgency", ZERO);
        if (mDoctorRadioButton.isChecked()) {
            user.put("isDoctor", true);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
            query.whereEqualTo("isDoctor",true);
            List<ParseObject> service = null;
            query.selectKeys(Arrays.asList("DoctorID"));

            try {
                service = query.find();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int id = service.size()+1;
            user.put("DoctorID", Integer.toString(id));
            user.saveInBackground();
        }
        else
        {
            user.put("isDoctor",false);
        }
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Show a simple Toast message upon successful registration
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.sign_up_successful),
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else if(e.getCode() == ParseException.USERNAME_TAKEN){
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.error_email_exists), Toast.LENGTH_SHORT)
                            .show();
                } else if(e.getCode() == ParseException.CONNECTION_FAILED){
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.error_connection_failed), Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.error_unknown), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    private boolean isEmailValid(String email) {
        return email.contains("@")
                && email.length() <255;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4
                && password.length() < 255;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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