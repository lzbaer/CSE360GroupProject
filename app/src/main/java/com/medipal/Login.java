package com.medipal;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.*;


public class Login extends ActionBarActivity {

    //UI elements
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mEmailLoginFormView;
    private View mSignOutButtons;
    private View mLoginFormView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        // TODO populateAutoComplete();
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        //create button & action listener for signing in
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    public void attemptLogin()
    {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address, if user entered one
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

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            authenticateLogin(email, password);
        }
    }

    private void authenticateLogin(String email, String password){

        // Send data to Parse.com for verification
        ParseUser.logInInBackground(email, password,
                new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            // If user exist and authenticated, send user to Welcome.class
                            Intent intent;
                            if(user.getBoolean("isDoctor"))
                            {
                                intent = new Intent(
                                        Login.this,
                                        DoctorSplash.class);

                            }
                            else {
                                intent = new Intent(
                                        Login.this,
                                        PatientSplash.class);
                            }
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.login_successful),
                                    Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    getString(R.string.error_incorrect_login),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.length() < 256;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4 && password.length() < 256;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
}
