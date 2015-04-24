package com.medipal;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userId="";
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            //log out activity
            userId="";
            this.finish();//try activityname.finish instead of this
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("userId",userId);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startLogin(View view)
    {
        //if no user is currently logged in
        if(userId.length()==0)
        {
            Intent intent = new Intent(this, Login.class);
            startActivityForResult(intent, 1);
        }else{
            //otherwise a user is logged in, go to splash page
            this.startUserSplashPage();
        }
    }

    public void startSignUp(View view)
    {
        //if no user is currently logged in
        if(userId.length()==0)
        {
            Intent intent = new Intent(this, SignUp.class);
            startActivity(intent);
        }else{
            //otherwise a user is logged in, do not go to sign up
            Toast.makeText(getBaseContext(), getString(R.string.error_signed_in_sign_up), Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("MainActivity.onActivityResult");
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                userId=data.getStringExtra("userId");
                System.out.println(userId);
            }
            if (resultCode == RESULT_CANCELED) {

            }
        }

        this.startUserSplashPage();

    }//onActivityResult

    private void startUserSplashPage()
    {
        System.out.println("MainActivity.startUserSplashPage");

        Intent intent = null;

        boolean doctor = isUserDoctor(userId);
        if(doctor)
        {
            intent = new Intent(this, DoctorSplash.class);
        }else{
            intent = new Intent(this, PatientSplash.class);
        }
        intent.putExtra("userId",userId);
        startActivity(intent);
    }

    private boolean isUserDoctor(String userId) {
        //TODO check if user is patient or doctor
        return false;
    }


}
