package com.EFrame13;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class Settings extends Activity{
	
	Dialog dialog;
	DBAdapter db = new DBAdapter(this);
	int epid, pathid;;
	InputStream is = null;
	int flag=0;
	int valid = 0;
	int flag1=0;
	
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
     // In order to do any transactions with database.. Need to open the database..
        db.open();
        
        dialog = new Dialog(Settings.this);
		dialog.setContentView(R.layout.settings_dialog);
		dialog.setCancelable(true);
				
		OnClickListener radio_listener = new OnClickListener() {
             public void onClick(View v) {
                 
                 RadioButton rb = (RadioButton) v;
                                 
                 if((rb.getText()).equals("Enter Elite PictureFrame ID"))
                 {
                	 
                	 
                	final Dialog dialog1 = new Dialog(Settings.this);
         			dialog1.setContentView(R.layout.add_epfid_dialog);
         			dialog1.setCancelable(true);
         			
         			final TextView error = (TextView)dialog1.findViewById(R.id.error);
                	final EditText epfid = (EditText)dialog1.findViewById(R.id.epfid);
                	
                   	String epfid1="";
                	
                   	// Get eliteID that is alloted while registration...                   	
                	Cursor mCursor = db.getEpfid();
                	if (mCursor.moveToFirst())  
					{
                		epfid1 = mCursor.getString(1);
					}
                	if(mCursor!=null)
                	mCursor.close();
                	
                	epfid.setText(epfid1);
                	error.setText("");
                	
         			Button cancel = (Button) dialog1.findViewById(R.id.cancel);
         			cancel.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                    	
                    			dialog1.dismiss();
                    		//	if(db!=null)
                    			db.close();
                    			System.gc();
                    			Intent i = new Intent(Settings.this, home.class);
                    			startActivity(i);
                    		finish();
                        }
                    });
       	         
       	         
         			
         			Button ok = (Button) dialog1.findViewById(R.id.ok);
         			ok.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                            
                    	String temp=epfid.getText().toString();
                    	if(temp.equals(""))
                    	{
                    		error.setText("ID not entered!! ");
                    		flag1=2;
                    	}
                    	//Check if Email is a valid Email ID...
                    	else if((checkEmail(temp+"@elitepictureframe.com")) && (checkValidity(temp)))
                    	{
                    		epid=0;
                        	                        	
                        	Cursor mCursor = db.getEpfid();
                        	if (mCursor.moveToFirst())  
        					{
                        		epid = mCursor.getInt(0);
                        	}
                        	//if(mCursor!=null)
                        	mCursor.close();
                        	
                    		if(epid == 0)
                    		{
                    			// Since id not in db.. insert it..
                    			db.insertEpfid(temp);
                    			error.setText("ID added successfully ");
                    			dialog1.dismiss();
                    			
                    		//	if(db!=null)
                    			db.close();
                    			System.gc();
                    			Intent i = new Intent(Settings.this, home.class);
                    			startActivity(i);
finish();
                    		}
                    		else
                    		{
                    			// Since in db.. updated to new ID...
                    			db.updateEpfid(epid, temp);
                    			error.setText("ID updated successfully ");
                    			dialog1.dismiss();
                    			
                    		//	if(db!=null)
                    			db.close();
                    			System.gc();
                    			Intent i = new Intent(Settings.this, home.class);
                    			startActivity(i);
finish();
                    		}
                      	}
                    	else
                    	{
                    		error.setText("Invalid ID.. ");
                    		
                    	}                    	
                        }
                    });
       	         
         			dialog1.show();
         			
                	
                 }
                 else if((rb.getText()).equals("Photos storage Path"))
                 {
                	 	final Dialog dialog2 = new Dialog(Settings.this);
	         			dialog2.setContentView(R.layout.add_photos_path);
	         			dialog2.setCancelable(true);
	         			
	         			final TextView error = (TextView)dialog2.findViewById(R.id.error);
                    	final EditText path = (EditText)dialog2.findViewById(R.id.path);
                    		                    	
                    	String path1="";
                    	
                    	// Get stored path from Db...
                    	Cursor mCursor = db.getPath();
                    	if (mCursor.moveToFirst())  
    					{
                    		pathid = mCursor.getInt(0);
                    		path1 = mCursor.getString(1);
    					}
                   // 	if(mCursor!=null)
                    	mCursor.close();
                    	
                    	path.setText(path1);
                    	error.setText("");
                    	
	         			Button cancel = (Button) dialog2.findViewById(R.id.cancel);
	         			cancel.setOnClickListener(new OnClickListener() {

	                    public void onClick(View v) {
	                            dialog2.dismiss();
	                            
	                      //      if(db!=null)
	                            	db.close();
	                            System.gc();
	                            Intent i = new Intent(Settings.this, home.class);
	             				startActivity(i);
finish();
	                        }
	                    });
	       	         
	       	         
	         			
	         			Button ok = (Button) dialog2.findViewById(R.id.ok);
	         			ok.setOnClickListener(new OnClickListener() {

	                    public void onClick(View v) {
	                            
	                    	String temp = path.getText().toString();
	                    	if(temp.equals(""))
	                    		error.setText("Path not entered!! ");
	                    	else 
	                    	{
	                    		// Updates path if made any changes...
	                    		db.updatePath(pathid, temp);
	                    		error.setText("Path updated successfully ");
	                    	}
	                    	
	                    	                    	
	                        }
	                    });
	       	         
	         			dialog2.show();

                 }
                 else if((rb.getText()).equals("Register"))
                 {
                	// if(db!=null)
                	 db.close();
                	 dialog.dismiss();
System.gc();
                	Intent i = new Intent(Settings.this, RegisterMember.class);
     				startActivity(i);
finish();
                 }
              
                
             }
         };
		
		 final RadioButton enter_id = (RadioButton) dialog.findViewById(R.id.enter_id);
         final RadioButton register = (RadioButton) dialog.findViewById(R.id.register);
         final RadioButton photos_path = (RadioButton) dialog.findViewById(R.id.photos_path);
         
         enter_id.setOnClickListener(radio_listener);
         register.setOnClickListener(radio_listener);
         photos_path.setOnClickListener(radio_listener);
        			
			         
         Button ok = (Button) dialog.findViewById(R.id.ok);
		 ok.setOnClickListener(new OnClickListener() {

         public void onClick(View v) {
                 dialog.dismiss();
                 
               //  if(db!=null)
                 db.close();
                 System.gc();
                 Intent i = new Intent(Settings.this, home.class);
  				startActivity(i);
finish();
             }
         });
         
         dialog.show();
        
    }
    
    /*
	Type: function
	Name: checkValidity
	Parameters: EliteID(String)
	Return Type: -
	Date: 29/6/11
	Purpose: Checks if newly entered ID is Valid ID from Server DB

*/
    
    boolean checkValidity(String ename)
    {
    	String result="";
    	String temp="";
    	
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("epfid",ename));
        
         try
         {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://bpsi.us/blueplanetsolutions/elitepictureframe/elite_check_name_validity_server.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
         }
         catch(Exception e)
         {
        	 	System.out.println("Error in connection to Server");
        	 	flag=1;
        	 	valid=0;
         }
      
         if(flag!=1)
         {
        
        try
        {
        	BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
        	StringBuilder sb = new StringBuilder();
        	String line = null;
     
        	while ((line = reader.readLine()) != null) 
        	{
        		sb.append(line + "\n");
        	}
        	is.close();
        	result=sb.toString();
        }
        catch(Exception e)
        {
        	System.out.println("Error converting result");
        	valid=0;
        }
      
     	try{
      
     		// Handle response..
            JSONArray jArray = new JSONArray(result);
            
            	for(int i=0;i<jArray.length();i++)
            	{
            			JSONObject json_data = jArray.getJSONObject(i);
            			temp = temp+"\nId: "+json_data.getString("epfid");
            			System.out.println("Given Id is valid...");
            			
            	}
            	
            	valid=1;
            	//name.setText("Welcome "+uname.getText().toString());
            	
     	}
     	catch(JSONException e)
     	{
     		System.out.println("ID is invalid...");
			valid=0;
     	}
     	finally
     	{
     		if(valid==0)
     			return false;
     		else
     			return true;
     	}
     }
         if(valid==0)
  			return false;
  		else
  			return true;
    	
    }
    
    
    // Validate Email ID...
    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
        );

    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }
}
