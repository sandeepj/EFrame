package com.EFrame13;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

public class image extends Activity {
	private static final String TAG = "Neha";
	private boolean isImage = false;
	private String reviewImageLink;
//	Session ss = new Session();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Bundle extras = getIntent().getExtras();
		String str = extras.getString("path_e");
		System.out.println("Current Path: "+str);
		setPath(str);
		int count = extras.getInt("count_e");
		System.out.println("Current Count(image): "+count);
	
		System.gc();

		Intent i = new Intent(image.this, SiteDownloadTemp.class);
		count--;
		i.putExtra("count_e", count);
		i.putExtra("paths_e", extras.getStringArray("paths_e"));
	        startActivity(i);
		finish();
		
	}

	// Download the specified image....
	
	void setPath(String path)
	{
		System.out.println("setPath str: "+path);
		reviewImageLink = path;
		URL reviewImageURL;
		String name = reviewImageLink.substring(reviewImageLink
				.lastIndexOf("/") + 1);
		try {
			reviewImageURL = new URL(reviewImageLink);
			if (!hasExternalStoragePublicPicture(name)) {
				isImage = false;
				new DownloadImageTask().execute(reviewImageURL);
				Log.v("log_tag", "if");
				isImage = true;
				File sdImageMainDirectory = new File("sdcard/ElitePicsFromPC");
				sdImageMainDirectory.mkdirs();
				File file = new File(sdImageMainDirectory, name);
				Log.v("log_tag", "Directory created");
			}

		} catch (MalformedURLException e) {
			Log.v(TAG, e.toString());
		}
	}
		
	private class DownloadImageTask extends AsyncTask<URL, Integer, Bitmap> {
		
		protected Bitmap doInBackground(URL... paths) {
			URL url;
			try {
				url = paths[0];
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
				Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0,	length);
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
			
			String name = reviewImageLink.substring(reviewImageLink.lastIndexOf("/") + 1);
			System.out.println("onPostExecute Name: "+name);
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
		System.out.println("saveFile Name: "+name);
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
