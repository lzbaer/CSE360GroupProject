package com.medipal;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.*;

/**
 * class handles login, this is home class
 * created by Patrick, Ankit
 */
public class MainActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add your initialization code here
        Parse.initialize(this, "Vyjj2ByDhfsUI7j7c5M8NeIaJIi1TWDdh2Nt0sSB", "iGFhL69a0QKjHSLbcMgvlN942mwdBqyKb6RYgeC4");

//        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);

        //log user out every time activity starts
        //if user reopens app, user is loggeed out
        ParseUser.logOut();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //set up logout button using action bar
        if (id == R.id.action_logout) {
            if(ParseUser.getCurrentUser()!=null){
                ParseUser.logOut();
                Toast.makeText(getBaseContext(), getString(R.string.logout_successful), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(
                        getBaseContext(),
                        "Already signed out!",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    //called from XML generated objects
    public void startLogin(View view)
    {
        //if no user is currently logged in
        if(ParseUser.getCurrentUser() == null)
        {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }else{
            //otherwise a user is logged in, go to splash page
            this.startUserSplashPage();
        }
    }

    //called from XML generated objects
    public void startSignUp(View view)
    {
        //if no user is currently logged in
        if(ParseUser.getCurrentUser() == null)
        {
            Intent intent = new Intent(this, SignUp.class);
            startActivity(intent);
        }else{
            //otherwise a user is logged in, do not go to sign up
            Toast.makeText(getBaseContext(), getString(R.string.error_signed_in_sign_up), Toast.LENGTH_SHORT).show();
        }
    }

    private void startUserSplashPage()
    {
        Intent intent = null;

        ParseUser currentUser = ParseUser.getCurrentUser();
        boolean doctor = currentUser.getBoolean("isDoctor");
        if(doctor)
        {
            intent = new Intent(this, DoctorSplash.class);
        }else{
            intent = new Intent(this, PatientSplash.class);
        }

        startActivity(intent);
    }

}
