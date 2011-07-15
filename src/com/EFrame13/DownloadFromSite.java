package com.EFrame13;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DownloadFromSite extends Activity {
	private static final String TAG = "Neha";
	private boolean isImage = false;
	private String reviewImageLink;
	DBAdapter db = new DBAdapter(this);
//	Session ss = new Session();
	InputStream is = null;
	int flag=0;
	String elite_id="";
	Dialog dialog;
	int count=0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download);

		// In order to do any transactions with database.. Need to open the database..
		db.open();
		
		// Get the elite picture frame ID from DB..
		Cursor mCursor = db.getEpfid();
	    	if (mCursor.moveToFirst())  
			{
	      		elite_id = mCursor.getString(1);
			}
	    	
	//    if(mCursor!=null)
	    	mCursor.close();
	    	
		getPhotos();
	}

	/*
	Type: function
	Name: display
	Parameters: no. of photos downloaded(int)
	Return Type: -
	Date: 29/6/11
	Purpose: Dialog to show no. of photos downloaded....

*/
	
	void display(int c)
    {
    	dialog = new Dialog(DownloadFromSite.this);
		dialog.setContentView(R.layout.view_download_details_dialog);
		dialog.setCancelable(true);

		TextView viewDetailsDialog = (TextView) dialog.findViewById(R.id.viewDetailsDialog);
		
		if(c>0)
			viewDetailsDialog.setText("\n"+c+" Photo(s) downloaded from PC");
		else
			viewDetailsDialog.setText("\nNo new Photo(s) downloaded from PC");
		
		 Button ok = (Button) dialog.findViewById(R.id.ok);
		 ok.setOnClickListener(new OnClickListener() {

         public void onClick(View v) {
                 dialog.dismiss();
                 
          //       if(db!=null)
                	 db.close();
                 	System.gc();
                                  
                 Intent i = new Intent(DownloadFromSite.this, home.class);
     			 startActivity(i);
			finish();	
             }
         });
         dialog.show();
    }
	
	/*
	Type: function
	Name: getPhotos
	Parameters: -
	Return Type: -
	Date: 29/6/11
	Purpose: Get the photos from server which are added to our elitePictureFrame ID

*/
	void getPhotos()
    {
    	String result="", temp="";
    	
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    nameValuePairs.add(new BasicNameValuePair("epfid",elite_id));
	    
	    try
        {
               HttpClient httpclient = new DefaultHttpClient();
               HttpPost httppost = new HttpPost("http://bpsi.us/blueplanetsolutions/elitepictureframe/EliteSite/getPicturesFromPC.php");
               httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
               HttpResponse response = httpclient.execute(httppost);
               HttpEntity entity = response.getEntity();
               is = entity.getContent();
        }
        catch(Exception e)
        {
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
   	
       }
    
    
    
    	try{
     
    		
           JSONArray jArray = new JSONArray(result);
           
           	Calendar calendar = Calendar.getInstance();
       		java.util.Date now = calendar.getTime();
       		
       		java.sql.Timestamp ti = new java.sql.Timestamp(now.getTime());
           
       		String []paths = null;
       		if(jArray.length() > 0)
       			paths = new String[jArray.length()];
       		
       	// Handling json response...
           	for(int i=0;i<jArray.length();i++)
           	{
           			JSONObject json_data = jArray.getJSONObject(i);
           			temp = temp+"\nId: "+json_data.getString("photo_link");
           	           			          			
           			System.out.println("Image: "+"http://bpsi.us/blueplanetsolutions/elitepictureframe/EliteSite/Images/"
   							+elite_id+"/"
   							+json_data.getString("photo_link"));
           			
           			// All paths from where photos are to be downloaded...
           			paths[i] = "http://bpsi.us/blueplanetsolutions/elitepictureframe/EliteSite/Images/"
							+elite_id+"/"
   							+json_data.getString("photo_link");
           			
           	}
           	System.out.println("paths.length: "+paths.length);
           	if(paths.length > 0)
           	{
           	//	ss.setSessionCount(paths.length);
           //		ss.setSessionImageTotDownload(paths);
           		
           	//	if(db!=null)
           			db.close();
           		
           		Intent i = new Intent(DownloadFromSite.this, SiteDownloadTemp.class);
           		i.putExtra("count_e", paths.length);
           		i.putExtra("paths_e", paths);
           		
           		paths = null;
           		System.gc();
            	startActivity(i);
			finish();
           	}
           	else
           	{
           		// If 0 photos to download...
           		display(0);
           	}
         
           	
    	}
    	catch(JSONException e)
    	{
    		System.out.println("Exception!!!");
    		display(0);
    	}
        }
    }
	
	/*
	Type: function
	Name: setImagePath
	Parameters: path(String)
	Return Type: -
	Date: 29/6/11
	Purpose: set Path where all downloaded images will be saved... 

*/
	void setImagePath(String path)
	{
		reviewImageLink = path;
		URL reviewImageURL;
		String name = reviewImageLink.substring(reviewImageLink
				.lastIndexOf("/") + 1);
		try {
			reviewImageURL = new URL(reviewImageLink);
			if (!hasExternalStoragePublicPicture(name)) 
			{
				isImage = false;
				new DownloadImageTask().execute(reviewImageURL);
				Log.v("log_tag", "if");
				isImage = true;
				File sdImageMainDirectory = new File("sdcard/ElitePicsFromPC");
				File file = new File(sdImageMainDirectory, name);
			}

		} catch (MalformedURLException e) {
			Log.v(TAG, e.toString());
		}
	}
		
	// Actual Download...
	private class DownloadImageTask extends AsyncTask<URL, Integer, Bitmap> {
		
		protected Bitmap doInBackground(URL... paths) {
			URL url;
			try {
				url = paths[0];
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				int length = connection.getContentLength();
				InputStream is = (InputStream) url.getContent();
				byte[] imageData = new byte[length];
				int buffersize = (int) Math.ceil(length / (double) 100);
				int downloaded = 0;
				int read;
				while (downloaded < length) {
					if (length < buffersize) {
						read = is.read(imageData, downloaded, length);
					} else if ((length - downloaded) <= buffersize) {
						read = is.read(imageData, downloaded, length
								- downloaded);
					} else {
						read = is.read(imageData, downloaded, buffersize);
					}
					downloaded += read;
					publishProgress((downloaded * 100) / length);
				}
				Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0,
						length);
				if (bitmap != null) {
					Log.i(TAG, "Bitmap created");
				} else {
					Log.i(TAG, "Bitmap not created");
				}
				is.close();
				return bitmap;
			} catch (MalformedURLException e) {
				Log.e(TAG, "Malformed exception: " + e.toString());
			} catch (IOException e) {
				Log.e(TAG, "IOException: " + e.toString());
			} catch (Exception e) {
				Log.e(TAG, "Exception: " + e.toString());
			}
			return null;

		}

		protected void onPostExecute(Bitmap result) {
			String name = reviewImageLink.substring(reviewImageLink
					.lastIndexOf("/") + 1);
			if (result != null) {
				hasExternalStoragePublicPicture(name);
				saveToSDCard(result, name);
				isImage = true;

			} else {
				isImage = false;

			}
		}
	}

	public void saveToSDCard(Bitmap bitmap, String name) {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mExternalStorageAvailable = mExternalStorageWriteable = true;
			Log.v(TAG, "SD Card is available for read and write "
					+ mExternalStorageAvailable + mExternalStorageWriteable);
			saveFile(bitmap, name);
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
			Log.v(TAG, "SD Card is available for read "
					+ mExternalStorageAvailable);
		} else {
			mExternalStorageAvailable = mExternalStorageWriteable = false;
			Log.v(TAG, "Please insert a SD Card to save your Video "
					+ mExternalStorageAvailable + mExternalStorageWriteable);
		}
	}

	private void saveFile(Bitmap bitmap, String name) {
		String filename = name;
		ContentValues values = new ContentValues();
		File sdImageMainDirectory = new File("sdcard/ElitePicsFromPC");
		sdImageMainDirectory.mkdirs();
		File outputFile = new File(sdImageMainDirectory, filename);
		values.put(MediaStore.MediaColumns.DATA, outputFile.toString());
		values.put(MediaStore.MediaColumns.TITLE, filename);
		values.put(MediaStore.MediaColumns.DATE_ADDED, System
				.currentTimeMillis());
		values.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
		Uri uri = this.getContentResolver().insert(
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,

				values);
		try {
			OutputStream outStream = this.getContentResolver()
					.openOutputStream(uri);
			bitmap.compress(Bitmap.CompressFormat.PNG, 95, outStream);

			outStream.flush();
			outStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean hasExternalStoragePublicPicture(String name) {
		File sdImageMainDirectory = new File("sdcard/ElitePicsFromPC");
		File file = new File(sdImageMainDirectory, name);
		if (file != null) {
			file.delete();
		}
		return file.exists();
	}

}

