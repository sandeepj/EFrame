package com.EFrame13;

import java.io.File;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

public class EFrame13 extends Activity {
	DBAdapter db = new DBAdapter(this);
	protected LocationManager locationManager;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
       
     // In order to do any transactions with database.. Need to open the database..
        db.open();
                   
        defaultInsert();
        
      //  if(db!=null)
        	db.close();
        System.gc();
        Intent i = new Intent(EFrame13.this, home.class);
        startActivity(i);
        finish();
     
    }
    
    /*
	Type: function
	Name: defaultInsert
	Parameters: -
	Return Type: -
	Date: 29/6/11
	Purpose: when app executed for the first time.. 
			 1) one temp album is created.. 
			 2) one ElitePictureCamera folder is created to save photos clicked by our camera feature..
			 3) default path to store downloaded photos, entered in to local db..

*/
    void defaultInsert()
    {
    	if(db.checkIfPathExits() == 0)
			db.insertPath("/sdcard/elitepicturephotos");
		
		if(db.checkIfAlbumExist("temp") == 0)
		{
			Calendar calendar = Calendar.getInstance();
	    	java.util.Date now = calendar.getTime();
	    	java.util.Date today = new java.util.Date();
			java.sql.Timestamp ti = new java.sql.Timestamp(now.getTime());
			
			db.insertAlbum("temp", 0, "", ti.toString(), "", 0, 3000);
		}
		
		File camPhotosDirectory = new File("/sdcard/ElitePictureCamera/");
		if(!(camPhotosDirectory.exists()))
			camPhotosDirectory.mkdirs();
    }
}