package com.EFrame13;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendMail extends Activity {
	
	
	EditText mailid=null;
	EditText pwd=null;
	EditText mailTo=null;
	//Session ss = new Session();
	String selectedPhoto="", selectedAlbumName="";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
             
        // Get photo to be mailed... 
        //selectedPhoto = ss.getSessionSelectedPhoto();
        
        
        
        final Dialog dialog3 = new Dialog(SendMail.this);
		dialog3.setContentView(R.layout.send_mail);
		dialog3.setCancelable(true);
		
		Bundle extras = getIntent().getExtras();
        selectedAlbumName = extras.getString("aname_e");
        selectedPhoto = extras.getString("pname_e");
		
		Button back = (Button)dialog3.findViewById(R.id.back);
        back.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
				dialog3.dismiss();
			System.gc();
				Intent i = new Intent(SendMail.this, FullPhoto.class);
				i.putExtra("aname_e", selectedAlbumName);
				i.putExtra("pname_e", selectedPhoto);
				startActivity(i);
finish();
			}
		});
                
        Button send = (Button)dialog3.findViewById(R.id.send);
        send.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
			
			EditText mailid = (EditText)dialog3.findViewById(R.id.mailid);
			EditText pwd = (EditText)dialog3.findViewById(R.id.pwd);
			EditText mailTo = (EditText)dialog3.findViewById(R.id.mailTo);
			
			String mid = mailid.getText().toString();
			String pass = pwd.getText().toString();
			String midTo = mailid.getText().toString();
			
			if((mid.equals("")) || (pass.equals("")) || (mailTo.equals("")))
			{
				Toast.makeText(SendMail.this, "All fields not entered!!", Toast.LENGTH_LONG).show();
			}
			else
			{
			
			Mail m = new Mail(mailid.getText().toString(), pwd.getText().toString()); 
			 // 
		      String[] toArr = {mailTo.getText().toString()}; 
		      m.setTo(toArr); 
		      m.setFrom(mailid.getText().toString()); 
		      m.setSubject("Photo sent from Elite PictureFrame"); 
		      m.setBody("Hello,"+"\n"+
		    		  "\t\t This Photo is sent from Elite PictureFrame by your Friend."+
		    		  "\n\n"+
		    		  "Regards,"+"\n"+
		    		  "   NehaC"); 
		 
		      try { 
		        m.addAttachment(selectedPhoto); 
		 
		        if(m.send()) { 
		          Toast.makeText(SendMail.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
		          
		         /* Intent i = new Intent(SendMail.this, FullPhoto.class);
				  startActivity(i);
		          */
		        } else { 
		          Toast.makeText(SendMail.this, "Email was not sent.", Toast.LENGTH_LONG).show(); 
		        } 
		      } catch(Exception e) { 
		         
		        Log.e("MailApp", "Could not send email", e); 
		      } 
		    }
			}
		});
        dialog3.show();
        
       
        
    }
}