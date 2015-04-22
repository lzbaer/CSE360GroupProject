package com.medipal;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class DatabaseHelper extends SQLiteOpenHelper
{

    private static final String DATABASE_NAME = "database.db";
    private static final String TABLE_NAME = "MEDIPAL_TABLE";
    private static final String UID = "_id";
    private static final String NAME = "Name";

    private static final int DATABASE_VERSION = 1;


    DatabaseHelper(Context context)
    {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    //called when database is created for the first time. Creation of tables and initial data
    //inside tables should be put here.
    public void onCreate(SQLiteDatabase db)
    {
        try {
            db.execSQL("CREATE TABLE MEDIPAL_TABLE(_id INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR(255));");
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    //called when the database needs to be upgraded. Use this method to
    //drop tables, add tables, or do anything else it needs to upgrade to the new schema version
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS MEDIPAL_TABLE");
        onCreate(db);
    }

    //if you add new columns you can use ALTER TABLE to insert them into a live table

    /* If you rename or remove columns you can use ALTER TABLE to rename the old table,
    then create the new table and then populate the new table with the contents
    of the old table
     */





}
/*public class DatabaseHelper extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_helper);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_database_helper, menu);
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
*/