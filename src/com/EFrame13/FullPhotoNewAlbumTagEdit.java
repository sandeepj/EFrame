package com.EFrame13;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class FullPhotoNewAlbumTagEdit extends Activity{

	DBAdapter db = new DBAdapter(this);
//	Session ss = new Session();
	String selectedPhoto, albumSelected;
	
	int pid;
	String size = "";
	String country = "";
	String state = "";
	String city = "";
	String place = "";
	String area = "";
	String tag = "";
	String date_time = "";
	String frame = "";
	int flag_exist;
	EditText clicked_on=null;
	EditText e_country=null;
	EditText e_state=null;
	EditText e_city=null;
	EditText e_area=null;
	EditText e_place=null;
	EditText e_tag=null;//e_frame;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
     // In order to do any transactions with database.. Need to open the database..
        db.open();
    //    selectedPhoto = ss.getSessionSelectedPhoto();
        
        Bundle extras = getIntent().getExtras();
        albumSelected = extras.getString("aname_e");
        selectedPhoto = extras.getString("pname_e");
        
        final Dialog dialog2 = new Dialog(FullPhotoNewAlbumTagEdit.this);
		dialog2.setContentView(R.layout.edit_photo);
		dialog2.setCancelable(true);
        
        TextView photo_loc = (TextView)dialog2.findViewById(R.id.photo_loc);
		photo_loc.setText(selectedPhoto);
        
		clicked_on = (EditText)dialog2.findViewById(R.id.clicked_on);	
		e_country = (EditText)dialog2.findViewById(R.id.country);
		e_state = (EditText)dialog2.findViewById(R.id.state);
		e_city = (EditText)dialog2.findViewById(R.id.city);
		e_area = (EditText)dialog2.findViewById(R.id.area);	
		e_place = (EditText)dialog2.findViewById(R.id.place);
		e_tag = (EditText)dialog2.findViewById(R.id.tag);
		
		
		flag_exist = db.checkIfPhotoExist(selectedPhoto);
		
		// Get photo details from db.. and display..
		// Else display only photo link..
		if(flag_exist != 0)
		{
			Cursor c = db.getPhoto(db.getPhotoId(selectedPhoto));
			if (c.moveToFirst())  
			{
				pid = c.getInt(0);
				clicked_on.setText(c.getString(3));
        		e_country.setText(c.getString(4));
        		e_state.setText(c.getString(5));
        		e_city.setText(c.getString(6));
        		e_area.setText(c.getString(7));
        		e_place.setText(c.getString(8));
        		e_tag.setText(c.getString(9));
        		
			}
		//	if(c!=null)
			c.close();
		}
		else
		{
			Calendar calendar = Calendar.getInstance();
        	java.util.Date now = calendar.getTime();
       	 	java.sql.Timestamp ti = new java.sql.Timestamp(now.getTime());

       	 	clicked_on.setText(ti.toString());
    	}
		
		Button cancel = (Button)dialog2.findViewById(R.id.cancel);
		cancel.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
				dialog2.dismiss();
				
			//	if(db!=null)
				db.close();
				System.gc();
				Intent i = new Intent(FullPhotoNewAlbumTagEdit.this, FullPhotoNewAlbumTag.class);
				i.putExtra("aname_e", albumSelected);
				i.putExtra("pname_e", selectedPhoto);				
				startActivity(i);
finish();
			}
		});
		
		Button edit_button = (Button)dialog2.findViewById(R.id.edit_button);
		edit_button.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
				if(flag_exist != 0)
				{				
					// photo details updated..
					db.updatePhoto(pid, selectedPhoto, 
							"60MB",clicked_on.getText().toString(),
							e_country.getText().toString(),
							e_state.getText().toString(),
							e_city.getText().toString(),
							e_area.getText().toString(),
							e_place.getText().toString(),
							e_tag.getText().toString(),
							"");
					//if(db!=null)
					db.close();
					dialog2.dismiss();
System.gc();
					Intent i = new Intent(FullPhotoNewAlbumTagEdit.this, FullPhotoNewAlbumTag.class);
					i.putExtra("aname_e", albumSelected);
					i.putExtra("pname_e", selectedPhoto);
					startActivity(i);
				finish();						
				}
				else
				{		
					// photo entry inserted in db...
               	 	db.insertPhoto(selectedPhoto, "60MB",
                							clicked_on.getText().toString(),
                							e_country.getText().toString(),
                							e_state.getText().toString(),
                							e_city.getText().toString(),
                							e_area.getText().toString(),
                							e_place.getText().toString(),
                							e_tag.getText().toString(),
                							"");
              // 	 if(db!=null)
               	 db.close();
               	dialog2.dismiss();
System.gc();
               	 Intent i = new Intent(FullPhotoNewAlbumTagEdit.this, FullPhotoNewAlbumTag.class);
               	i.putExtra("aname_e", albumSelected);
				i.putExtra("pname_e", selectedPhoto);
				 startActivity(i);
                finish();
				}
			}
		});
		dialog2.show();
        
        
    }
	
}

