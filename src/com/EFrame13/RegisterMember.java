package com.EFrame13;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

public class RegisterMember extends Activity{
	
	TextView lo = null;	
	Button back=null;
	TextView dateDisplay=null;
	String gender="M";
	int contacttype=0;
	private int mYear;
    private int mMonth;
    private int mDay;
    ImageButton changeDate=null;
    static final int DATE_DIALOG_ID = 0;
    int valid = 0;
    InputStream is = null;
    TextView text2=null;
    TextView text1=null;
    TextView validity=null;
    int flag=0;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_member);
        
        dateDisplay = (TextView)findViewById(R.id.dateDisplay);
        
        final EditText fname = (EditText)findViewById(R.id.fname);
    	final EditText mname = (EditText)findViewById(R.id.mname);
    	final EditText lname = (EditText)findViewById(R.id.lname);
    	final EditText phno = (EditText)findViewById(R.id.phno);
    	final EditText email = (EditText)findViewById(R.id.email);
    	final EditText phno1 = (EditText)findViewById(R.id.phno1);
    	final EditText email1 = (EditText)findViewById(R.id.email1);
    	final EditText epfid = (EditText)findViewById(R.id.epfid);
    	final EditText pwd = (EditText)findViewById(R.id.pwd);
    	final EditText pwd1 = (EditText)findViewById(R.id.pwd1);
    	validity = (TextView)findViewById(R.id.validity);
    	validity.setText("                    ");
    	text1 = (TextView)findViewById(R.id.text1);
      	text1.setText("");
      	text2 = (TextView)findViewById(R.id.text2);
      	text2.setText("");
      		      	
    	OnClickListener radio_listener = new OnClickListener() {
             public void onClick(View v) {
                 
                 RadioButton rb = (RadioButton) v;
                 if((rb.getText().toString()).equals("Male"))
                	 gender = "M";
                 else  if((rb.getText().toString()).equals("Female"))
                	 gender = "F";
                 
             }
           };
    	
    	final RadioButton male = (RadioButton) findViewById(R.id.male);
        final RadioButton female = (RadioButton) findViewById(R.id.female);
        
        male.setOnClickListener(radio_listener);
        female.setOnClickListener(radio_listener);
        
        // Check if internet connection is available...
        if(!(isOnline()))
        	text1.setText("Internet Connection is not available...");
        
        back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{ 
				//back.setBackgroundResource(R.drawable.back_h);
System.gc();
				Intent i = new Intent(RegisterMember.this, home.class);
				startActivity(i);
finish();
			}
		});  
        
        Button checkValidity = (Button)findViewById(R.id.checkValidity);
        checkValidity.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{ 
			
				if((epfid.getText().toString()).equals(""))
					text1.setText("Elite PictureFrameID not entered!!");
				else
				{
					checkValidity(epfid.getText().toString());
				}
				
			}
		});  
        
        final Button register = (Button)findViewById(R.id.register);
        register.setOnClickListener(new Button.OnClickListener() 
  		{ public void onClick (View v)
  			{ 
  			register.setBackgroundResource(R.drawable.register_h);
  			text1.setText("");
  			if((fname.getText().toString()).equals(""))
      	   		text1.setText("First name not entered!!");
  			else if((mname.getText().toString()).equals(""))
      	   		text1.setText("Middle name not entered!!");
  			else if((lname.getText().toString()).equals(""))
      	   		text1.setText("Last name not entered!!");
  			else if(!((phno.getText().toString()).equals(phno1.getText().toString())))
      	   		text1.setText("Entered and re-entered Phone number do not match!!");
  			else if(!((email.getText().toString()).equals(email1.getText().toString())))
      	   		text1.setText("Entered and re-entered Email-ID do not match!!");
  			else if(((phno.getText().toString()).equals("")) && ((email.getText().toString()).equals("")))
      	   		text1.setText("Either EmailID or Phone number is compulsary..");
  			else if((!((phno.getText().toString()).equals(""))) &&
  					!(checkNo(phno.getText().toString())))
  				text1.setText("Invalid Phone Number");
  			else if((!((email.getText().toString()).equals(""))) && 
  					(!(checkEmail(email.getText().toString()))))
					text1.setText("Invalid EmailID");
  			else if((pwd.getText().toString()).equals(""))
      	   		text1.setText("Password not entered!!");
  			else if((pwd1.getText().toString()).equals(""))
      	   		text1.setText("Password not entered!!");
  			else if(!((pwd.getText().toString()).equals(pwd1.getText().toString())))
      	   		text1.setText("Entered and re-entered Password do not match!!");
  			else if((epfid.getText().toString()).equals(""))
      	   		text1.setText("Elite PictureFrameID not entered!!");
  			else if( valid == 0)
					text1.setText("Please check Elite PictureFrameID validity!!");
  			else
  			{
  				
  				String result = "";
  		    	String temp="";
  		   		  				
  				String date = dateDisplay.getText().toString();
  				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
  		        nameValuePairs.add(new BasicNameValuePair("fname",fname.getText().toString()));
  		        nameValuePairs.add(new BasicNameValuePair("mname",mname.getText().toString()));
  		        nameValuePairs.add(new BasicNameValuePair("lname",lname.getText().toString()));
		        nameValuePairs.add(new BasicNameValuePair("phone_no",phno.getText().toString()));
		        nameValuePairs.add(new BasicNameValuePair("email_id",email.getText().toString()));
		        nameValuePairs.add(new BasicNameValuePair("pass",pwd.getText().toString()));
		        nameValuePairs.add(new BasicNameValuePair("epfid",epfid.getText().toString()));
		        nameValuePairs.add(new BasicNameValuePair("bdate", date));
		        nameValuePairs.add(new BasicNameValuePair("gender",gender));
		         		       
  		         try
  		         {
  		                HttpClient httpclient = new DefaultHttpClient();
  		                HttpPost httppost = new HttpPost("http://bpsi.us/blueplanetsolutions/elitepictureframe/elite_register_server.php");
  		                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
  		                HttpResponse response = httpclient.execute(httppost);
  		                HttpEntity entity = response.getEntity();
  		                is = entity.getContent();
  		                text2.setText("Registration successful!!");
  		                fname.setText("");
  		                mname.setText("");
  		                lname.setText("");
  		                phno.setText("");
  		                email.setText("");
  		                epfid.setText("");
  		                phno1.setText("");
  		                email1.setText("");
  		                text1.setText("");
  		                validity.setText("                    ");
  		                valid = 0;
  		                final Calendar c = Calendar.getInstance();
  		                mYear = c.get(Calendar.YEAR);
  		                mMonth = c.get(Calendar.MONTH);
  		                mDay = c.get(Calendar.DAY_OF_MONTH);
  			        
  		                updateDisplay();
  		         }
  		         catch(Exception e)
  		         {
  		        	 	text1.setText("Could not add entry...");
  		         }
  		  	}
  			}
  		});
        
        changeDate = (ImageButton)findViewById(R.id.changeDate);
        changeDate.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{ 
				showDialog(DATE_DIALOG_ID);
			}
		});
        
        dateDisplay = (TextView)findViewById(R.id.dateDisplay);
        
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        
        updateDisplay();

        final Button reset = (Button)findViewById(R.id.reset);
        reset.setOnClickListener(new Button.OnClickListener() 
  		{ public void onClick (View v)
  			{ 
  				reset.setBackgroundResource(R.drawable.reset_h);
  				fname.setText("");
  				mname.setText("");
  				lname.setText("");
  				phno.setText("");
  				email.setText("");
  				epfid.setText("");
  				phno1.setText("");
  				email1.setText("");
  				text1.setText("");
  				text2.setText("");
  				validity.setText("                    ");
	            valid = 0;
	            reset.setBackgroundResource(R.drawable.reset);
  			}
  		});
      
    }
    
    /*
	Type: function
	Name: checkValidity
	Parameters: EliteID (String)
	Return Type: -
	Date: 29/6/11
	Purpose: Check if user desired ID is available or not...

*/
    void checkValidity(String ename)
    {
    	String result="";
    	String temp="";
    	text1.setText("");
    	
    	//send eliteID to php file.. which will return whethr ID available or not... 
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("epfid",ename));
              
        //http post
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
        	 	text1.setText("Error in connection to Server");
        	 	flag=1;
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
    	 text1.setText("Error converting result");
        }
      
     	try{
      
     		// Handling the response...
            JSONArray jArray = new JSONArray(result);
            
            	for(int i=0;i<jArray.length();i++)
            	{
            			JSONObject json_data = jArray.getJSONObject(i);
            			temp = temp+"\nId: "+json_data.getString("epfid");
            	}
            	validity.setText("Given Id is not available...");
            	valid = 0;
                        	
     	}
     	catch(JSONException e)
     	{
     		validity.setText("ID is available...");
			valid = 1;
     	}
         }
    	
    }
    
    // Validate Email ID..
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
    
    DatePickerDialog.OnDateSetListener mDateSetListener =
        new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, 
                                  int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                updateDisplay();
            }
        };
    
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
            return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }
        return null;
    }
    
    void updateDisplay() {
    	dateDisplay.setText(
            new StringBuilder()
                    .append(mYear).append("-")
                    .append(mMonth + 1).append("-")
                    .append(mDay).append(" "));
    }
    
    /*
	Type: function
	Name: isOnline
	Parameters: -
	Return Type: -
	Date: 29/6/11
	Purpose: check if internet connection is available

*/
    public boolean isOnline() {
   	 ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
   	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
   	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
   	        return true;
   	    }
   	    return false;
   	}
    
    
    /*
	Type: function
	Name: checkNo
	Parameters: no
	Return Type: -
	Date: 29/6/11
	Purpose: check if no. os valid number...

*/
    boolean checkNo(String sPhoneNumber)
    {
    	
    	 if (sPhoneNumber == null || sPhoneNumber.length() < 10 )
             return false;
         
         for (int i = 0; i < 10; i++) {

             if (!Character.isDigit(sPhoneNumber.charAt(i)))
                 return false;
         }
         
         return true;
    }

}
