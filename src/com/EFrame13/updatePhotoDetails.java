package com.EFrame13;


import java.util.ArrayList;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class updatePhotoDetails extends Activity{
	
	DBAdapter db = new DBAdapter(this); 
	Button back=null, search=null;
	EditText search_item=null;
	TextView noOfPhotos=null;
	int flag=0;
	String tag="";
	Cursor cursor;
	private int columnIndex;
	int total;
	Spinner s=null;
	int ItemSelected;
	//Session ss = new Session();
	String selectedPhotoName="";
	String []str = new String[10];
	int j=0,k=0;
	ArrayList<String> PhotoList = new ArrayList<String>();
	ArrayList<String> PhotoList1 = new ArrayList<String>();
	int search_flag=0;
	private Runnable viewLocation;
	private ProgressDialog m_ProgressDialog = null;
	
	/** Called when the activity is first created. */ 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.update_all_photos);
        
     // In order to do any transactions with database.. Need to open the database..
        db.open();
        
        search_item = (EditText)findViewById(R.id.search_item);
        
        back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new Button.OnClickListener() 
    			{ public void onClick (View v)
    				{
    					if(PhotoList.size()>0)
    						PhotoList.clear();
    					
    					if(PhotoList1.size()>0)
    						PhotoList1.clear();	
    					
    					//if(db!=null)
    					db.close();
    					str =null;
System.gc();
    					Intent i = new Intent(updatePhotoDetails.this, home.class);
    					startActivity(i);
finish();
    				}
    			});
        
                       
        search = (Button)findViewById(R.id.search);
        search.setOnClickListener(new Button.OnClickListener() 
    			{ public void onClick (View v)
    				{
    					
    					tag = search_item.getText().toString();
    					
    					if(!(tag.equals("")))
    						ConvertStringToArray(tag);
    					else
    						all_photos();
    				}
    			});
        
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search_item.getWindowToken(), 0);
        
        all_photos();
        
     }
    
    
    // This function is used to provide delay... and nothing else...
    void getLocation()
    {
    	try
    	{
    		Thread.sleep(13000);
    	}
        catch (Exception e) 
        {
        	Log.e("BACKGROUND_PROC", e.getMessage());
        }
    	runOnUiThread(returnRes);
    	
    }

    
    private Runnable returnRes = new Runnable() {

        public void run() {
            m_ProgressDialog.dismiss();
            
            
            
        }
    };

    /*
	Type: function
	Name: listNer
	Parameters: -
	Return Type: -
	Date: 29/6/11
	Purpose: Bind photos and info to grid view..

*/
    void listNer()
    {
    	noOfPhotos = (TextView)findViewById(R.id.noOfPhotos);
    	
    	if(search_flag == 1)
    		noOfPhotos.setText("Edit Photos["+PhotoList1.size()+"]");
    	else
    		noOfPhotos.setText("Edit Photos["+PhotoList.size()+"]");
    	
    	GridView sdcardImages = (GridView) findViewById(R.id.sdcard);
        sdcardImages.setAdapter(new ImageAdapter(this));
        
        viewLocation = new Runnable(){
            public void run() {
                getLocation();
            }
        };
        Thread thread =  new Thread(null, viewLocation, "MagentoBackground");
        thread.start();
        m_ProgressDialog = ProgressDialog.show(updatePhotoDetails.this,    
              "Elite PictureFrame", "Searching photos on device..", true);
        
    	sdcardImages.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) 
            {
            	if(search_flag == 1)
            	    selectedPhotoName = PhotoList1.get(position);//imagePath1[position];
            	else
            		selectedPhotoName = PhotoList.get(position);
            	
            	// selected photo is stored in session.. And can view it full screen..
            //	ss.setSessionSelectedPhoto(selectedPhotoName);
            	
            	if(PhotoList.size()>0)
            		PhotoList.clear();
            	
            	if(PhotoList1.size()>0)
            		PhotoList1.clear();
            	
            	str = null;
				//if(db!=null)
    			db.close(); 
				
          //  	search_flag=0;
            	System.gc();
				Intent i = new Intent(updatePhotoDetails.this, FullPhotoEdit.class);
				i.putExtra("pname_e", selectedPhotoName);
				startActivity(i);
            	finish();
           }	
        });
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
    void search_Photos(String []str1)
    {
    	search_flag=1;
    	
    	PhotoList1.clear();
    	System.gc();
    	
    	for(int i=0; i<PhotoList.size(); i++)
    	{
    		
    		String str5 = PhotoList.get(i);
    		
    		for(int j=0; j<str1.length; j++)
    		{
    			if(str5.contains(str1[j]))
    				PhotoList1.add(str5);
    				
    			
    		}
    	}
    	
    	    	
    	Cursor mCursor = db.getAndPhotoTag(str1);
    	
    	while(mCursor.moveToNext())
    	{
    		String str5 = mCursor.getString(0);
    		
    		if(!(PhotoList1.contains(str5)))
    			PhotoList1.add(str5);
   
    	}
    //	if(mCursor!=null)
    	mCursor.close();
    	
    	
        listNer();
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
    	flag=0;
    	    	
        try
        {
        	//Fire query in order to get List of all photos on SD Card
        	 String[] projection1 = {MediaStore.Images.Media.DATA};
             cursor = managedQuery( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                     projection1, 
                     null,       
                     null,
                     MediaStore.Images.Thumbnails._ID);
             
          // Add those photos to Out ArrayList
        	for(int position=0; position<cursor.getCount(); position++)
        	{
        		columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        		cursor.moveToPosition(position);
        		PhotoList.add(cursor.getString(columnIndex));
        		
        	}
        	//if(cursor!=null)
        	cursor.close();
        	// Attach retrieved data to the grid view
        listNer();
    	}
    	catch(Exception e)
    	{
    		Toast toast = Toast.makeText(updatePhotoDetails.this, 
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
                v = li.inflate(R.layout.update_all_photos_row, null);
                try
                {
                String image;
                
                if(search_flag == 1)
                	image = PhotoList1.get(position);//imagePath1[position];
                else
                	image = PhotoList.get(position);
                
                ImageView iv = (ImageView)v.findViewById(R.id.icon_image1);
                /*
                BitmapFactory.Options bfo = new BitmapFactory.Options();
                bfo.inSampleSize = 6;
                bfo.outWidth = 150;
                bfo.outHeight = 150;
                Bitmap photo = BitmapFactory.decodeFile(image, bfo);
                if(photo!=null)
                	iv.setImageBitmap(photo);
                */
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
                
                
                String date_time1="";
                String place1="";
                
                TextView photo_details = (TextView)v.findViewById(R.id.photo_details);
                
                if((db.checkIfPhotoExist(image)) != 0)
                {	
                	Cursor c = db.getPhoto(db.getPhotoId(image));
                	if (c.moveToFirst())  
                	{
                		if(!(c.getString(3).equals("")))
                			date_time1=c.getString(3);
                		                		
                		if(!(c.getString(8).equals("")))
                			place1=c.getString(8);
                		
                		if(!(c.getString(7).equals("")))
                		{
                			if(!(place1.equals("")))
                				place1=place1 + ","+c.getString(7);
                			else
                				place1=c.getString(7);
                		}
                		
                		if(!(c.getString(6).equals("")))
                		{
                			if(!(place1.equals("")))
                				place1=place1 + ","+c.getString(6);
                			else
                				place1=c.getString(6);
                		}
                		
                		if(!(c.getString(5).equals("")))
                		{
                			if(!(place1.equals("")))
                				place1=place1 + ","+c.getString(5);
                			else
                				place1=c.getString(5);
                		}
                		
                		if(!(c.getString(4).equals("")))
                		{
                			if(!(place1.equals("")))
                				place1=place1 + ","+c.getString(4);
                			else
                				place1=c.getString(4);
                		}
                		
                	}
                //	if(c!=null)
                	c.close();
                	
                	if((!(date_time1.equals(""))) && (!(place1.equals(""))))
                		photo_details.setText("Details:\nDate: "+date_time1+"\nPlace: "+place1);
                	else if((date_time1.equals("")) && (!(place1.equals(""))))
                		photo_details.setText("Details:\nPlace: "+place1);
                	else if((!(date_time1.equals(""))) && (place1.equals("")))
                		photo_details.setText("Details:\nDate: "+date_time1);
                	else
                		photo_details.setText("Details:\nImage: "+image);
                }
                else
                {
                	photo_details.setText("Details:\nImage: "+image);
                }
                
            }
        	catch(Exception e)
        	{
        		Toast toast = Toast.makeText(updatePhotoDetails.this, 
                		"\nProblem in attaching photos....\nImage: "+position,
                		Toast.LENGTH_LONG);
                toast.show();
        	}
                
                return v;
        }
        
    }

}
