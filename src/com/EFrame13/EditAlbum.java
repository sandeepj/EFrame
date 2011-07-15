package com.EFrame13;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class EditAlbum extends Activity{
	
	DBAdapter db = new DBAdapter(this);
	//Session ss = new Session();
	String selectedAlbum;
	
	private static int aid1;
	EditText aname = null;
	EditText music = null;
	Button change = null;
	private static String aname1;
	private static String music1;
	private static String shuffle1;
	private static String transition1;
	int flag_exist;
	TextView text1 = null;
	String music2="";
	String array_spinner[] = null;
	Spinner s = null;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
     // In order to do any transactions with database.. Need to open the database..             
        db.open();
        //selectedAlbum = ss.getSessionAlbumName();
        
        Bundle extras = getIntent().getExtras();
        selectedAlbum = extras.getString("aname_e");
        System.out.println("Album: "+selectedAlbum);
        music2 = extras.getString("music_e");
        
        final Dialog dialog2 = new Dialog(EditAlbum.this);
			dialog2.setContentView(R.layout.edit_album);
			dialog2.setCancelable(true);
			
			       
        text1 = (TextView)dialog2.findViewById(R.id.text1);     
        aname = (EditText)dialog2.findViewById(R.id.aname);	
        music = (EditText)dialog2.findViewById(R.id.music);
       		
        array_spinner=new String[5];
        array_spinner[0]="2 sec";
        array_spinner[1]="3 sec";
        array_spinner[2]="4 sec";
        array_spinner[3]="5 sec";
        array_spinner[4]="6 sec";
   
        s = (Spinner) dialog2.findViewById(R.id.test);
        ArrayAdapter adapter = new ArrayAdapter(this,
        android.R.layout.simple_spinner_item, array_spinner);
        s.setAdapter(adapter);
        
       // music2 = ss.getSessionMusicSelected();
        
      //  if((music2.equals("")))
     //   {
        	// Query to get all details of selected album...
        	Cursor c = db.getAlbum(db.getAlbumId(selectedAlbum));
        	if (c.moveToFirst())  
        	{
        		aid1 = c.getInt(0);
        		
        		aname1 = c.getString(1);
        		music1 = c.getString(3);
        		shuffle1 = c.getString(6);
        		transition1 = c.getString(7);
        		
        		aname.setText(selectedAlbum);
        		if(music2.equals(""))
        			music.setText(music1);
        		else
        			music.setText(music2);
        		
        		s.setSelection(Integer.parseInt(""+transition1.charAt(0))-2);
        	}
        //	if(c!=null)
        		c.close();
       /* }
        else
        {
        	aname.setText(selectedAlbum);
    		    		
    		s.setSelection(Integer.parseInt(""+transition1.charAt(0))-2);
    		
        	music.setText(music2);
        	//ss.setSessionMusicSelected("");
        }*/
        
		change = (Button)dialog2.findViewById(R.id.change);
		change.setOnClickListener(new Button.OnClickListener() {
		 public void onClick(View v) {
                 
			// if(db!=null)
				 db.close(); 
			 
				 dialog2.dismiss();
				 
				 System.gc();
			Intent i = new Intent(EditAlbum.this, MusicActivity.class);
			i.putExtra("aname_e", selectedAlbum);
			startActivity(i);
			
			System.out.println("Finish...");
			finish();
		 }
         });
		
		Button cancel = (Button)dialog2.findViewById(R.id.cancel);
		cancel.setOnClickListener(new Button.OnClickListener() 
		{
			 public void onClick(View v) {
	                 
				 dialog2.dismiss();
				 
			//	 if(db!=null)
					 db.close();
				 array_spinner = null;
				 
				 System.gc();
				 
				 Intent i = new Intent(EditAlbum.this, OpenAlbum.class);
				 i.putExtra("aname_e", selectedAlbum);
					startActivity(i);
					
					System.out.println("Finish...");
					finish();
			 }
	         });
		
		Button edit_button = (Button)dialog2.findViewById(R.id.edit_button);
		edit_button.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
				flag_exist = db.checkIfAlbumExist(aid1, aname.getText().toString());
			
				if(flag_exist == 0)
				{
					int index = s.getSelectedItemPosition();
					String item = (String) s.getItemAtPosition(index);
					int trans=3000;
					if(item.equals("2 sec"))
						trans=2000;
					else if(item.equals("3 sec"))
						trans=3000;
					else if(item.equals("4 sec"))
						trans=4000;
					else if(item.equals("5 sec"))
						trans=5000;
					else if(item.equals("6 sec"))
						trans=6000;
					
					// Update query to update the album details...
					db.updateAlbumEdit(aid1, 
												aname.getText().toString(), 
												music.getText().toString(),
												0,
												trans);
					
				//	if(db!=null)
						db.close();
					
					array_spinner = null;
					dialog2.dismiss();
					
					System.gc();
					Intent i = new Intent(EditAlbum.this, OpenAlbum.class);
					i.putExtra("aname_e", aname.getText().toString());
					startActivity(i);
					
					System.out.println("Finish...");
					finish();
										
				}
				else if(flag_exist == 1)
				{
					text1.setText("Album same name already exist!!"); 
				}
			}
		});
		
		dialog2.show();
    }
	

}
