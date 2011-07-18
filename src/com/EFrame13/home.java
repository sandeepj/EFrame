package com.EFrame13;



import java.io.File;
import java.util.regex.Pattern;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class home extends Activity {
	
	private Button view_album_mode = null;
	private Button cam_mode = null;
	private Button add_new_album_mode = null;
	private Button update_photo_details=null;
	
	private Button setting=null;
	private Button from_pc = null;
	private Button upload_to_pc=null;
	DBAdapter db = new DBAdapter(this);
	
	String imagePath="";
	String Sender="";
	String Msg="";
	int count=0;
	Dialog dialog;
	int epid, pathid;
	ProgressDialog myProgressDialog = null;
	
	int flag=0;
	int i5=0, j5=0,mp_count=0, count5=0;
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        
     // In order to do any transactions with database.. Need to open the database..
        db.open();
          
        
       setting = (Button)findViewById(R.id.setting);
       setting.setBackgroundResource(R.drawable.settings);
       setting.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
		{
		//	setting.setBackgroundResource(R.drawable.settings_h);
			
		//	if(db!=null)
				db.close();
			System.gc();
			Intent i = new Intent(home.this, Settings.class);
			startActivity(i);
			
			
		}
		});
         
       upload_to_pc = (Button)findViewById(R.id.upload_to_pc);
       upload_to_pc.setBackgroundResource(R.drawable.uploadpc);
       upload_to_pc.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
		{
			upload_to_pc.setBackgroundResource(R.drawable.uploadpc_h);	
			if(isOnline())
			{
					String ElitePictureFrameId="";
		    	
					Cursor mCursor = db.getEpfid();
					if (mCursor.moveToFirst())  
					{
						ElitePictureFrameId = mCursor.getString(1);
					}
				//	if(mCursor != null)
						mCursor.close();
						
					if(ElitePictureFrameId.equals(""))
					{
		    		final Dialog dialog2 = new Dialog(home.this);
					dialog2.setContentView(R.layout.view_download_details_dialog);
					//dialog2.setTitle("         		Error");
					dialog2.setCancelable(true);

					TextView viewDetailsDialog = (TextView) dialog2.findViewById(R.id.viewDetailsDialog);
					viewDetailsDialog.setText("\nSorry Elite PictureFrameID is not specified.. \n\n\n*Note: Alloted while registration");
							
									
					 Button ok = (Button) dialog2.findViewById(R.id.ok);
					 ok.setOnClickListener(new OnClickListener() {

		             public void onClick(View v) {
		                     dialog2.dismiss();
		                     
		                 }
		             });
		             dialog2.show();
		             
					}
					else
					{
				//		if(db!=null)
							db.close();
						System.gc();
						Intent i = new Intent(home.this, UploadPhotosToSite.class);
						startActivity(i);
					}
			}
				else
				{
					dialog = new Dialog(home.this);
				dialog.setContentView(R.layout.view_download_details_dialog);
				//dialog.setTitle("         		Error in connection...");
				dialog.setCancelable(true);

				TextView viewDetailsDialog = (TextView) dialog.findViewById(R.id.viewDetailsDialog);
				viewDetailsDialog.setText("\nPlease check your Internet Connection...");
									
				 Button ok = (Button) dialog.findViewById(R.id.ok);
				 ok.setOnClickListener(new OnClickListener() {

		         public void onClick(View v) {
		                 dialog.dismiss();
		                 
		             }
		         });
		         dialog.show();
				}
	
		}
		});
       
       from_pc = (Button)findViewById(R.id.from_pc);
       from_pc.setBackgroundResource(R.drawable.downloadpc);
       from_pc.setOnClickListener(new Button.OnClickListener() 
   			{ public void onClick (View v)
   				{
   				from_pc.setBackgroundResource(R.drawable.downloadpc_h);
   				if(isOnline())
				{
   					String ElitePictureFrameId="";
   		    	
   					Cursor mCursor = db.getEpfid();
   					if (mCursor.moveToFirst())  
   					{
   						ElitePictureFrameId = mCursor.getString(1);
   					}
   				//	if(mCursor!=null)
   						mCursor.close();
   					
   					if(ElitePictureFrameId.equals(""))
   					{
   		    		final Dialog dialog2 = new Dialog(home.this);
   					dialog2.setContentView(R.layout.view_download_details_dialog);
   					//dialog2.setTitle("         		Error");
   					dialog2.setCancelable(true);

   					TextView viewDetailsDialog = (TextView) dialog2.findViewById(R.id.viewDetailsDialog);
   					viewDetailsDialog.setText("\nSorry Elite PictureFrameID is not specified.. \n\n\n*Note: Alloted while registration");
   							
   					   					
   					 Button ok = (Button) dialog2.findViewById(R.id.ok);
   					 ok.setOnClickListener(new OnClickListener() {

   		             public void onClick(View v) {
   		                     dialog2.dismiss();
   		                     
   		                 }
   		             });
   		             dialog2.show();
   		             
   					}
   					else
   					{
   						//if(db!=null)
   							db.close();
   						System.gc();
   						Intent i = new Intent(home.this, DownloadFromSite.class);
   						startActivity(i);
   					}
				}
   				else
   				{
   					dialog = new Dialog(home.this);
					dialog.setContentView(R.layout.view_download_details_dialog);
					dialog.setTitle("         		Error in connection...");
					dialog.setCancelable(true);

					TextView viewDetailsDialog = (TextView) dialog.findViewById(R.id.viewDetailsDialog);
					viewDetailsDialog.setText("\nPlease check your Internet Connection...");
										
					 Button ok = (Button) dialog.findViewById(R.id.ok);
					 ok.setOnClickListener(new OnClickListener() {

			         public void onClick(View v) {
			                 dialog.dismiss();
			                 
			             }
			         });
			         dialog.show();
   				}
   				}
   			});
       
        cam_mode = (Button)findViewById(R.id.cam_mode);
    	cam_mode.setOnClickListener(new Button.OnClickListener() 
    			{ public void onClick (View v)
    				{
    					cam_mode.setBackgroundResource(R.drawable.camera_h);
    					CamAlbumList();
    				}
    			});
    	
    	view_album_mode = (Button)findViewById(R.id.view_album_mode);
    	view_album_mode.setOnClickListener(new Button.OnClickListener() 
    			{ public void onClick (View v)
    				{
    				view_album_mode.setBackgroundResource(R.drawable.myalbums_h);
    					albumList();
    				}
    			});
    
    	
    	add_new_album_mode = (Button)findViewById(R.id.add_new_album_mode);
    	add_new_album_mode.setOnClickListener(new Button.OnClickListener() 
    			{ public void onClick (View v)
    				{
    				add_new_album_mode.setBackgroundResource(R.drawable.addalbums_h);
    				addNewAlbum();
    				}
    			});
    	
    	update_photo_details = (Button)findViewById(R.id.update_photo_details);
    	update_photo_details.setOnClickListener(new Button.OnClickListener() 
    			{ public void onClick (View v)
    				{
    					update_photo_details.setBackgroundResource(R.drawable.updetails_h);
    					updatePhotoDetails();
    				}
    			});
    	
    	
    	
    	
    }
    
    /*
	Type: function
	Name: isOnline
	Parameters: -
	Return Type: -
	Date: 29/6/11
	Purpose: check if internet connection is available..

*/
    
   public boolean isOnline() 
	{
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnectedOrConnecting()) 
			{
				return true;
			}
			return false;
	}
    
    
    
   /*
	Type: function
	Name: display
	Parameters: no. of photos downloaded(int)
	Return Type: -
	Date: 29/6/11
	Purpose: dialog to display no. of photos downloaded...

*/
   
    void display(int c)
    {
    	dialog = new Dialog(home.this);
		dialog.setContentView(R.layout.view_download_details_dialog);
		//dialog.setTitle("         		Details...");
		dialog.setCancelable(true);

		TextView viewDetailsDialog = (TextView) dialog.findViewById(R.id.viewDetailsDialog);
		
		if(c>0)
			viewDetailsDialog.setText("\n"+c+" attachment(s) downloaded");
		else
			viewDetailsDialog.setText("\nNo new attachments to download");
		
			
		 Button ok = (Button) dialog.findViewById(R.id.ok);
		 ok.setOnClickListener(new OnClickListener() {

         public void onClick(View v) {
                 dialog.dismiss();
             }
         });
         dialog.show();
		
    }
    
    
    
    
  // validate Email ID...
    
    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
        );
    
    // View all photos and update their details i.e. store them in db..
    void updatePhotoDetails()
    {
    //	if(db!=null)
    		db.close();
    	System.gc();
    	Intent i = new Intent(this, updatePhotoDetails.class);
        startActivity(i);
    }
    
    // create new album and add photos to it...
    void addNewAlbum()
    {
    	//if(db!=null)
    		db.close();
    	System.gc();
    	Intent i = new Intent(this, add_album.class);
        startActivity(i);
    }
    
    //Display list of all albums created by me..
    void albumList()
    {
    	//if(db!=null)
    		db.close();
    	System.gc();
    	Intent i = new Intent(this, ModeListView.class);
        startActivity(i);
    }
	
  //Display list of all albums created by me.. plus create new album option and temp album option...
    void CamAlbumList()
    {
    	//if(db!=null)
    		db.close();
    	System.gc();
    	File camPhotosDirectory = new File("/sdcard/ElitePictureCamera/");
		if(!(camPhotosDirectory.exists()))
			camPhotosDirectory.mkdirs();
    	System.gc();
    	Intent i = new Intent(this, CamAlbumList.class);
        startActivity(i);
    }
    
       
    
}
