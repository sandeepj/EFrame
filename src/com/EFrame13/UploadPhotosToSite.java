package com.EFrame13;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class UploadPhotosToSite extends Activity{
	
	DBAdapter db = new DBAdapter(this); 
	//Session ss = new Session();
	String albumSelected="";
	Cursor cursor;
	int pos, k, total, i=0;;
	Button done=null, search=null;
	 private int columnIndex;
	 EditText search_item=null;
	 TextView noOfPhotos=null;
	 String selectedPhotoName="";
	 String UploadTemp="";
	 int count=0;
	 String tag="";
	 int flag = 0;
	 int j=0,l=0;
	 String elite_id="";
	 InputStream is = null;
	 ArrayList<String> arrayList = new ArrayList<String>();
	 ArrayList<String> PhotoList = new ArrayList<String>();
	 ArrayList<String> PhotoList1 = new ArrayList<String>();
	 int search_flag=0;
	 
	 
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_photos_open_album);
        
     // In order to do any transactions with database.. Need to open the database..
        db.open();
            
        // Get EliteID stored in db... which is alloted while registration..
        Cursor mCursor = db.getEpfid();
    	if (mCursor.moveToFirst())  
		{
    		elite_id = mCursor.getString(1);
		}
    	mCursor.close();
        
        Button back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
			//if(db!=null)
				db.close();
			
			if(PhotoList.size()>0)
				PhotoList.clear();
			
			if(PhotoList1.size()>0)
				PhotoList1.clear();	
			
			if(arrayList.size()>0)
				arrayList.clear();	
			System.gc();
				Intent i = new Intent(UploadPhotosToSite.this, home.class);
				startActivity(i);
finish();
			}
		});
        
        done = (Button)findViewById(R.id.done);
        done.setOnClickListener(new Button.OnClickListener() 
    			{ public void onClick (View v)
    				{
    					System.out.println("No of photos selected: "+arrayList.size());
    					for (String s : arrayList) 
    					{
    						System.out.println(s);
    						doFileUpload(s);
    						insertPC(s.substring((s.lastIndexOf("/"))+1));
    					}
    				//	if(db!=null)
    					db.close();
    					
    					if(PhotoList.size()>0)
    						PhotoList.clear();
    					
    					if(PhotoList1.size()>0)
    						PhotoList1.clear();	
    					
    					if(arrayList.size()>0)
    						arrayList.clear();	
    					System.gc();
    					Intent i = new Intent(UploadPhotosToSite.this, home.class);
    					startActivity(i);
finish();
    				}
    			});
        
        
        
        search = (Button)findViewById(R.id.search);
        search.setOnClickListener(new Button.OnClickListener() 
    			{ public void onClick (View v)
    				{
    					search_item = (EditText)findViewById(R.id.search_item);
    					tag = search_item.getText().toString();
    					
    					if(!(tag.equals("")))
    						ConvertStringToArray(tag);
    					else
    						all_photos();
    				}
    			});
       
        all_photos();
    }

    
    /*
	Type: function
	Name: insertPC
	Parameters: Image(String)
	Return Type: -
	Date: 29/6/11
	Purpose: upload selected photos on PC

*/
    void insertPC(String image)
    {
    	System.out.println("Id: "+image);
    	System.out.println("Insert PC image: "+elite_id);
       	
    	ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
        nameValuePairs1.add(new BasicNameValuePair("epfid",elite_id));
        nameValuePairs1.add(new BasicNameValuePair("photo_link",image));
        
        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://bpsi.us/blueplanetsolutions/elitepictureframe/EliteSite/android_to_pc_insert.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
           
         System.out.println(image+" entry inserted");
        }
        catch(Exception e)
        {
        	System.out.println("Could not insert...");
        }
		
    }
     
    /*
	Type: function
	Name: ConvertStringToArray
	Parameters: text entered for search by user (String)
	Return Type: -
	Date: 29/6/11
	Purpose: Convert the entered text into array of tags to be searched

*/
    void ConvertStringToArray(String tag)
	{
    	String str[] = new String[10];
    	
    	k=0;
    	j=0;
    	
		for(int i=0; i<tag.length(); i++)
		{
			if((tag.charAt(i) == ' ') && (i == 0))
			{
				while((tag.charAt(j) == ' ') || (tag.charAt(j) == ','))
				{
					j++;
				}
				i=j;
			}
			else if((tag.charAt(i) == ',') && (i == 0))
			{
				while((tag.charAt(j) == ' ') || (tag.charAt(j) == ','))
				{
					j++;
				}
				i=j;
			}
			else if(i == (tag.length()-1))
			{
				str[k] = tag.substring(j,i+1);
				k++;
			}
			else if(tag.charAt(i) == ',')
			{
					str[k] = tag.substring(j,i);
					k++;
				
				j=i+1;
				
				if((tag.charAt(j) == ' ') || (tag.charAt(j) == ','))
				{
					while((tag.charAt(j) == ' ') || (tag.charAt(j) == ','))
					{
						j++;
					}
				}
				i=j;
			}
			else if(tag.charAt(i) == ' ')
			{
					str[k] = tag.substring(j,i);
					k++;
				
				j=i+1;
				
				if((tag.charAt(j) == ' ') || (tag.charAt(j) == ','))
				{
					while((tag.charAt(j) == ' ') || (tag.charAt(j) == ','))
					{
						j++;
						
					}
				}
				i=j;
			}
		}
		
		String []str1 = new String[k];
		
		for(int i=0; i<str1.length; i++)
		{
			str1[i] = str[i];
		}
		
		search_Photos(str1);
	}

    /*
	Type: function
	Name: search_Photos
	Parameters: array of tags (String[])
	Return Type: -
	Date: 29/6/11
	Purpose: Get list of photos with the array of tags

*/        
    void search_Photos(String[] str)
    {
    	flag=1;
    	    		
    	search_flag=1;
    	PhotoList1.clear();
    	
    	for(int i=0; i<PhotoList.size(); i++)
    	{
    		System.out.println("....");
    		String str5 = PhotoList.get(i);
    		System.out.println("I: "+i+" Path: "+str5);
    		System.out.println("....");
    		for(int j=0; j<str.length; j++)
    		{
    			if(str5.contains(str[j]))
    			{
    				System.out.println(str5+" contain "+str[j]);
    				PhotoList1.add(str5);
    				System.out.println("Added to new list - "+str5);
    				
    			}
    			
    		}
    	}    	
    	
    	Cursor mCursor = db.getAndPhotoTag(str);
    	while(mCursor.moveToNext())
    	{
    		String str5 = mCursor.getString(0);
    		if(!(PhotoList1.contains(str5)))
    		{
    			PhotoList1.add(str5);
    			System.out.println("Getting added to the list1 - "+str5);
    		}
    	}
    	//if(mCursor!=null)
    	mCursor.close();
    	
    	noOfPhotos = (TextView)findViewById(R.id.noOfPhotos);
    	if(search_flag == 1)
    		noOfPhotos.setText("Add Photos["+PhotoList1.size()+"]");
    	else
    		noOfPhotos.setText("Add Photos["+PhotoList.size()+"]");
    	
    	GridView sdcardImages = (GridView) findViewById(R.id.sdcard);
        sdcardImages.setAdapter(new ImageAdapter(this));
    }
    
    /*
	Type: function
	Name: all_photos
	Parameters: -
	Return Type: -
	Date: 29/6/11
	Purpose: Get list of photos on SD Card 

*/    
    void all_photos()
    {
    	try
        {
        	 String[] projection1 = {MediaStore.Images.Media.DATA};
             cursor = managedQuery( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                     projection1, 
                     null,       
                     null,
                     MediaStore.Images.Thumbnails._ID);
        	for(int position=0; position<cursor.getCount(); position++)
        	{
        		columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        		cursor.moveToPosition(position);
        		PhotoList.add(cursor.getString(columnIndex));
        		
        	}
        //	if(cursor!=null)
        	cursor.close();
    	
        noOfPhotos = (TextView)findViewById(R.id.noOfPhotos);
        noOfPhotos.setText("Edit Photos["+PhotoList.size()+"]");
        
    	GridView sdcardImages = (GridView) findViewById(R.id.sdcard);
        sdcardImages.setAdapter(new ImageAdapter(this));
        
        }
    	catch(Exception e)
    	{
    		Toast toast = Toast.makeText(UploadPhotosToSite.this, 
            		"Count: "+total+"\nProblem in creating photos list....",
            		Toast.LENGTH_LONG);
            toast.show();
    	}

    }

    private class ImageAdapter extends BaseAdapter {

        private Context context;

        public ImageAdapter(Context localContext) {
            context = localContext;
        }

        public int getCount() {
        	
        	if(search_flag == 1)
        		return PhotoList1.size();
        	else
        		return PhotoList.size();
        	         
        }
        public Object getItem(int position) {
            return position;
        }
        public long getItemId(int position) {
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
        	
        	View v;
            
                LayoutInflater li = getLayoutInflater();
                v = li.inflate(R.layout.add_photos_open_album_row, null);
                  
                final String image;
                
                if(search_flag == 1)
                	image = PhotoList1.get(position);//imagePath1[position];
                else
                	image = PhotoList.get(position);
                
                ImageView iv = (ImageView)v.findViewById(R.id.icon_image1);
                Bitmap bMap = BitmapFactory.decodeFile(image);
                if(bMap!=null)	     
                {
                	Bitmap newImage = Bitmap.createScaledBitmap(bMap, 80, 80, true);
                    iv.setImageBitmap(newImage);
                }
                else
                {
                	iv.setImageResource(R.drawable.icon);
                }

                               
                final CheckBox check1 = (CheckBox)v.findViewById(R.id.check1);
                
                String date_time="";
                String place="";
                
                if((db.checkIfPhotoExist(image)) != 0)
                {	
                	Cursor c = db.getPhoto(db.getPhotoId(image));
                	if (c.moveToFirst())  
                	{
                		if(!(c.getString(3).equals("")))
                			date_time=c.getString(3);
                		                		
                		if(!(c.getString(8).equals("")))
                			place=c.getString(8);
                		
                		if(!(c.getString(7).equals("")))
                		{
                			if(!(place.equals("")))
                				place=place + ","+c.getString(7);
                			else
                				place=c.getString(7);
                		}
                		
                		if(!(c.getString(6).equals("")))
                		{
                			if(!(place.equals("")))
                				place=place + ","+c.getString(6);
                			else
                				place=c.getString(6);
                		}
                		
                		if(!(c.getString(5).equals("")))
                		{
                			if(!(place.equals("")))
                				place=place + ","+c.getString(5);
                			else
                				place=c.getString(5);
                		}
                		
                		if(!(c.getString(4).equals("")))
                		{
                			if(!(place.equals("")))
                				place=place + ","+c.getString(4);
                			else
                				place=c.getString(4);
                		}
                	}
                //	if(c!=null)
                		c.close();
                	
                	if((!(date_time.equals(""))) && (!(place.equals(""))))
                		check1.setText("Details:\nDate: "+date_time+"\nPlace: "+place);
                	else if((date_time.equals("")) && (!(place.equals(""))))
                		check1.setText("Details:\nPlace: "+place);
                	else if((!(date_time.equals(""))) && (place.equals("")))
                		check1.setText("Details:\nDate: "+date_time);
                	else
                		check1.setText("Details:\nImage: "+image);
                }
                else
                {
                	check1.setText("Details:\nImage: "+image);
                }
                check1.setId(position);
                
                check1.setOnCheckedChangeListener(new OnCheckedChangeListener()
                {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                    {
                    	String image1;
                    	if(search_flag == 1)
                    		image1 = PhotoList1.get(check1.getId());
                    	else 
                    		image1 = PhotoList.get(check1.getId());
                    	
                        if ( isChecked )
                        {
                           	arrayList.add(image1);
                           	System.out.println("Added to list: "+image1);
                        }
                        else
                        {
                           	arrayList.remove(image1);
                        }
                    }
                });
         
                return v;
            
        }
         
        
    }

    private void doFileUpload(String exsistingFileName){

  	  HttpURLConnection conn = null;
  	  DataOutputStream dos = null;
  	  DataInputStream inStream = null; 
  	   	
  	  System.out.println("exsistingFileName: "+exsistingFileName);
  	  
  	  String lineEnd = "\r\n";
  	  String twoHyphens = "--";
  	  String boundary =  "*****";


  	  int bytesRead, bytesAvailable, bufferSize;

  	  byte[] buffer;

  	  int maxBufferSize = 1*1024*1024;

  	  String urlString = "http://bpsi.us/blueplanetsolutions/elitepictureframe/EliteSite/uploadpic2.php";

  	  try
  	  {
  		  System.out.println("exsistingFileName: "+exsistingFileName); 
  	  	  FileInputStream fileInputStream = new FileInputStream(new File(exsistingFileName) );
  	  	  URL url = new URL(urlString);
  	  	  conn = (HttpURLConnection) url.openConnection();
  	  	  conn.setDoInput(true);
  	  	  conn.setDoOutput(true);
  	  	  conn.setUseCaches(false);
  	  	  conn.setRequestMethod("POST");
  	  	  conn.setRequestProperty("Connection", "Keep-Alive");
  	  	  conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
  	  	  dos = new DataOutputStream( conn.getOutputStream() );
  	  	   	  	  
  	  	  dos.writeBytes(twoHyphens + boundary + lineEnd);
  	  	  System.out.println("exsistingFileName: "+exsistingFileName);
  	  	  dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + exsistingFileName +"\"" + lineEnd);
  	  	  dos.writeBytes(lineEnd);

  	      bytesAvailable = fileInputStream.available();
  	      bufferSize = Math.min(bytesAvailable, maxBufferSize);
  	      buffer = new byte[bufferSize];

  	      bytesRead = fileInputStream.read(buffer, 0, bufferSize);

  	      while (bytesRead > 0)
  	      {
  	    	  	dos.write(buffer, 0, bufferSize);
  	    	  	bytesAvailable = fileInputStream.available();
  	    	  	bufferSize = Math.min(bytesAvailable, maxBufferSize);
  	    	  	bytesRead = fileInputStream.read(buffer, 0, bufferSize);
  	      }
  	  
  	      dos.writeBytes(lineEnd);
  	      dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
  	      
  	      BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
  	      String inputLine;
				
  	      while ((inputLine = in.readLine()) != null) 
  	      {
  	    	  
  	      }
					
  	      fileInputStream.close();
  	      dos.flush();
  	      dos.close();

  	  	}
  	  	catch (MalformedURLException ex)
  	  	{
  	       Log.e("Error:", "error: " + ex.getMessage(), ex);
  	  	}

  	  catch (IOException ioe)
  	  {
  	       Log.e("Error:", "error: " + ioe.getMessage(), ioe);
  	  }

  	  try {
  	        inStream = new DataInputStream ( conn.getInputStream() );
  	        String str;
  	       
  	        while (( str = inStream.readLine()) != null)
  	        {
  	             Log.e("Error: ","Server Response"+str);
  	        }
  	        inStream.close();

  	  }
  	  catch (IOException ioex){
  	       Log.e("Error: ", "error: " + ioex.getMessage(), ioex);
  	  }

  	}
    
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
