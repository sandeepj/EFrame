package com.EFrame13;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class CamMode extends Activity implements SurfaceHolder.Callback{
	

	Camera camera;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	boolean previewing = false;
	LayoutInflater controlInflater = null;
	DBAdapter db = new DBAdapter(this);
	//Session ss = new Session();
	String albumName="";
	Button buttonTakePicture=null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cam_mode);
        
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
      
        // Getting selected albums name from session...
        Bundle extras = getIntent().getExtras();
        albumName = extras.getString("aname_e");
        
        TextView album_name = (TextView)findViewById(R.id.album_name);
        album_name.setText(albumName);
                
        getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceView = (SurfaceView)findViewById(R.id.camerapreview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
      
        Button back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{

				System.gc();
				Intent i = new Intent(CamMode.this, home.class);
				startActivity(i);
				finish();
			}
		});
        
        buttonTakePicture = (Button)findViewById(R.id.takepicture);
        buttonTakePicture.setBackgroundResource(R.drawable.take_snap);
        buttonTakePicture.setOnClickListener(new Button.OnClickListener(){

    public void onClick(View arg0) {
     // TODO Auto-generated method stub
    	
    	
    	buttonTakePicture.setBackgroundResource(R.drawable.take_snap_h);
     camera.takePicture(myShutterCallback,
       myPictureCallback_RAW, myPictureCallback_JPG);
    }});
    }

    ShutterCallback myShutterCallback = new ShutterCallback(){

   public void onShutter() {
    // TODO Auto-generated method stub
   
   }};

  PictureCallback myPictureCallback_RAW = new PictureCallback(){

   public void onPictureTaken(byte[] arg0, Camera arg1) {
    // TODO Auto-generated method stub
   
   }};

  PictureCallback myPictureCallback_JPG = new PictureCallback(){

   public void onPictureTaken(byte[] arg0, Camera arg1) {
    // TODO Auto-generated method stub
   	   String pname = "/sdcard/ElitePictureCamera/"+albumName+System.currentTimeMillis()+".jpg";
	   FileOutputStream outStream = null;
	   try {
		   outStream = new FileOutputStream(pname);
		   outStream.write(arg0);
		   outStream.close();   
	   
		   Calendar calendar = Calendar.getInstance();
	    	java.util.Date now = calendar.getTime();
	    	java.util.Date today = new java.util.Date();
			java.sql.Timestamp ti = new java.sql.Timestamp(now.getTime());
		
			// In order to do any transactions with database.. Need to open the database..
	        db.open();
			
			// storing photo on SD Card and link in database...
		   long id = db.insertPhoto(pname, "60MB", ti.toString(), "", "", "", "", "", pname, "");
		   
		   int pid = db.getPhotoId(pname);
		   int aid = db.getAlbumId(albumName);
		   
		   // Adding photo to the selected album...
		   id = db.insertAlbum_photo(aid, pid);
		   
		   int l = db.getFirstPhotoInAlbum(aid);
		   
		   // If 1 or more photo in any album.. Making 1st photo as its album cover...
			if(l!=0)
				db.updateAlbumAfterInsert(aid, l);
		   
			db.close();
			
	    Toast.makeText(CamMode.this,
	      "Image "+pname+" savedto album "+albumName,
	      Toast.LENGTH_LONG).show();
	   
	    buttonTakePicture.setBackgroundResource(R.drawable.take_snap);
	   } catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	   } catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	   }

	   camera.startPreview();
   }};

  public void surfaceChanged(SurfaceHolder holder, int format, int width,
    int height) {
   // TODO Auto-generated method stub
   if(previewing){
    camera.stopPreview();
    previewing = false;
   }

   if (camera != null){
    try {
     camera.setPreviewDisplay(surfaceHolder);
     camera.startPreview();
     previewing = true;
    } catch (IOException e) {
     // TODO Auto-generated catch block
     e.printStackTrace();
    }
   }
  }

  public void surfaceCreated(SurfaceHolder holder) {
   // TODO Auto-generated method stub
	  
   camera = Camera.open();
   Camera.Parameters p = camera.getParameters();
   p.set("orientation", "landscape");
   
  /* if (getResources().getConfiguration().orientation != 
       Configuration.ORIENTATION_LANDSCAPE)
   {
       p.set("orientation", "landscape");

      
   }*/

  }

  public void surfaceDestroyed(SurfaceHolder holder) {
   // TODO Auto-generated method stub
   camera.stopPreview();
   camera.release();
   camera = null;
   previewing = false;
  }
  }
