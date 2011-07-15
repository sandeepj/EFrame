package com.EFrame13;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

public class InsertDBAndUpdate extends Activity {
	
	DBAdapter db = new DBAdapter(this);
	//Session ss = new Session();
	String elite_id="";
	InputStream is = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update);

		// In order to do any transactions with database.. Need to open the database..
		db.open();
				
		// Get elite pictureFrameID saved...
		Cursor mCursor = db.getEpfid();
	    	if (mCursor.moveToFirst())  
			{
	    		elite_id = mCursor.getString(1);
			}
	    //	if(mCursor!=null)
	    	mCursor.close();
	    	
	    	//Getting current date and time...
	    	Calendar calendar = Calendar.getInstance();
       		java.util.Date now = calendar.getTime();
       		java.util.Date today = new java.util.Date();
       		java.sql.Timestamp ti = new java.sql.Timestamp(now.getTime());
	    	
       		// Get no. of photos to download...
       		Bundle extras = getIntent().getExtras();
       		int count = extras.getInt("count_e");
       		String paths[] = extras.getStringArray("paths_e");
       		
       		for(int i=0; i<count; i++)
       		{
       			// Get all paths, photos to be downloaded...
       			String path = paths[i];
       			int temp = path.lastIndexOf("/");
       			String image = path.substring(temp+1, path.length());
       			
       			System.out.println("Image: "+image);
       			
       			// store photo on SD Card and insert entry into db...
       			db.insertPhoto("/sdcard/ElitePicsFromPC/"+image, "60MB", ti.toString(), "", 
   	    			"","", "", "", 
   	    			image+","+elite_id, "");
       			System.out.println("Data inserted");
   			   			
       			ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
		        nameValuePairs1.add(new BasicNameValuePair("epfid",elite_id));
		        nameValuePairs1.add(new BasicNameValuePair("photo_link",image));
		        
		      try
	         {
	                HttpClient httpclient = new DefaultHttpClient();
	                HttpPost httppost = new HttpPost("http://bpsi.us/blueplanetsolutions/elitepictureframe/EliteSite/updateFlagPC.php");
	                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
	                HttpResponse response = httpclient.execute(httppost);
	                HttpEntity entity = response.getEntity();
	                is = entity.getContent();
	               
	             System.out.println("Flag updated");
	         }
	         catch(Exception e)
	         {
	        	 	
	         }
       		}
       		
       	//	if(db!=null)
       			db.close();
       		System.gc();
       		Intent i = new Intent(InsertDBAndUpdate.this, home.class);
			 startActivity(i);
finish();
	}
	
}
