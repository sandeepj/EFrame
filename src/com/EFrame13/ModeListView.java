package com.EFrame13;


import android.app.Activity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;

public class ModeListView extends Activity {
	
	int selectedAlbum;
	String selectedAlbumName;
	int []album_cover_ids=null;
	String []album_covers=null;
	String []album_names=null;
	DBAdapter db = new DBAdapter(this);
	//Session ss = new Session();
	Button viewDetails=null;
	BitmapFactory.Options options;
		
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_album);
           
     // In order to do any transactions with database.. Need to open the database..
        try
        {
        	db.open();
        }
        catch(Exception e)
    	{
    		Runtime rt = Runtime.getRuntime();
    		Toast toast = Toast.makeText(ModeListView.this, 
            		"\nProblem in opening db..\nFree memo: "+rt.freeMemory(),
            		Toast.LENGTH_LONG);
            toast.show();
    	}
         
        Button home = (Button)findViewById(R.id.home);
        home.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
	//		if(db!=null)
			try
			{
				db.close();
			}
			catch(Exception e)
        	{
        		Runtime rt = Runtime.getRuntime();
        		Toast toast = Toast.makeText(ModeListView.this, 
                		"\nProblem in closing db..\nFree memo: "+rt.freeMemory(),
                		Toast.LENGTH_LONG);
                toast.show();
        	}
			try
			{
				album_cover_ids = null;
				album_covers = null;
				album_names = null;
				System.gc();
			}
			catch(Exception e)
        	{
        		Runtime rt = Runtime.getRuntime();
        		Toast toast = Toast.makeText(ModeListView.this, 
                		"\nProblem in setting to null....\nFree memo: "+rt.freeMemory(),
                		Toast.LENGTH_LONG);
                toast.show();
        	}
				Intent i = new Intent(ModeListView.this, home.class);
				startActivity(i);
				finish();
			}
		}); 
        
        Button deleteAlbum = (Button)findViewById(R.id.deleteAlbum);
        deleteAlbum.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
		//	if(db!=null)
				try
				{
				db.close();
				}
				catch(Exception e)
            	{
            		Runtime rt = Runtime.getRuntime();
            		Toast toast = Toast.makeText(ModeListView.this, 
                    		"\nProblem in closing db..\nFree memo: "+rt.freeMemory(),
                    		Toast.LENGTH_LONG);
                    toast.show();
            	}
				try
				{
				album_cover_ids = null;
				album_covers = null;
				album_names = null;
				System.gc();
				}
				catch(Exception e)
            	{
            		Runtime rt = Runtime.getRuntime();
            		Toast toast = Toast.makeText(ModeListView.this, 
                    		"\nProblem in setting to null....\nFree memo: "+rt.freeMemory(),
                    		Toast.LENGTH_LONG);
                    toast.show();
            	}
				Intent i = new Intent(ModeListView.this, DeleteAlbums.class);
				startActivity(i);
finish();
			}
		}); 
       
        try
        {
        // Get all album covers in oder to display...
       album_cover_ids = db.getAllAlbumCovers();
       
    // get all album names in order to display to user
       album_names = db.getAlbumNames();
       album_covers = new String[album_names.length]; 
       
        for(int i=0; i<album_names.length; i++)
        {
        	if(album_cover_ids[i] != 0)
        	{
        		Cursor c = db.getPhoto(album_cover_ids[i]);
        		if (c.moveToFirst())
        		{
        			String temp = c.getString(1);
        			album_covers[i] = temp;
        		}
       // 		if(c!=null)
        		c.close();
        	}
        	else
        	{
        		album_covers[i] = "";
        	}
        }
        }
        catch(Exception e)
    	{
    		Runtime rt = Runtime.getRuntime();
    		Toast toast = Toast.makeText(ModeListView.this, 
            		"\nProblem in getting data from db..\nFree memo: "+rt.freeMemory(),
            		Toast.LENGTH_LONG);
            toast.show();
    	}
      //Attaching details to grid view...
        GridView gridview1 = (GridView) findViewById(R.id.gridview1);
        gridview1.setAdapter(new ImageAdapter(this));

        gridview1.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
               	
            	// Storing selected albums name in session for further use...
           // 	ss.setSessionAlbumName(album_names[position]);
        	
            	//  	if(db!=null)
            	try
            	{
            	db.close();
            	}
            	catch(Exception e)
            	{
            		Runtime rt = Runtime.getRuntime();
            		Toast toast = Toast.makeText(ModeListView.this, 
                    		"\nProblem in closing....\nFree memo: "+rt.freeMemory(),
                    		Toast.LENGTH_LONG);
                    toast.show();
            	}
            	String temp_aname="";
            	
            	try
            	{
            	temp_aname = album_names[position];
            	}
            	catch(Exception e)
            	{
            		Runtime rt = Runtime.getRuntime();
            		Toast toast = Toast.makeText(ModeListView.this, 
                    		"\nProblem in album name....\nFree memo: "+rt.freeMemory(),
                    		Toast.LENGTH_LONG);
                    toast.show();
            	}
            	try
            	{
            	album_cover_ids = null;
				album_covers = null;
				album_names = null;
		
            	System.gc();
            	}
            	catch(Exception e)
            	{
            		Runtime rt = Runtime.getRuntime();
            		Toast toast = Toast.makeText(ModeListView.this, 
                    		"\nProblem in setting to null....\nFree memo: "+rt.freeMemory(),
                    		Toast.LENGTH_LONG);
                    toast.show();
            	}
            	Intent i = new Intent(ModeListView.this, OpenAlbum.class);
            	i.putExtra("aname_e", temp_aname);
                startActivity(i);
finish();
            }
        });
      
        }
        
        public class ImageAdapter extends BaseAdapter{
        	private Context mContext;
        	
            public ImageAdapter(Context c) {
                mContext = c;
            }

            public int getCount() {
                return db.getnoOfAlbums();
            }

            public Object getItem(int position) {
                return null;
            }

            public long getItemId(int position) {
                return 0;
            }

            public View getView(final int position, View convertView, ViewGroup parent) {
            	 View v;
		           
		                LayoutInflater li = getLayoutInflater();
		                v = li.inflate(R.layout.view_album_row, null);
		                TextView tv = (TextView)v.findViewById(R.id.icon_text);
		                tv.setText(album_names[position]);
		                ImageView iv = (ImageView)v.findViewById(R.id.icon_image);
		               try
		               {
		                
		                if(!(album_covers[position].equals("")))
		                {
		                	Bitmap bMap = BitmapFactory.decodeFile(album_covers[position]);
		                	if(bMap!=null)	     
		                    {
		                    	Bitmap newImage = Bitmap.createScaledBitmap(bMap, 80, 80, true);
		                        iv.setImageBitmap(newImage);
		                    }
		                    else
		                    {
		                    	iv.setImageResource(R.drawable.icon);
		                    }

		                }
		                else
		                {
			                iv.setImageResource(R.drawable.default_a);
		                }
		               }
		               catch(Exception e)
		           	{
		           		Runtime rt = Runtime.getRuntime();
		           		Toast toast = Toast.makeText(ModeListView.this, 
		                   		"\nProblem inattaching images..\nFree memo: "+rt.freeMemory(),
		                   		Toast.LENGTH_LONG);
		                   toast.show();
		           	}
		                iv.setOnClickListener(new Button.OnClickListener() 
		        		{ public void onClick (View v)
		        			{ 
		        		//	ss.setSessionAlbumName(album_names[position]);
		        	//		if(db!=null)
		        			try
		        			{
		                	db.close();
		        			}
		        			catch(Exception e)
		                	{
		                		Runtime rt = Runtime.getRuntime();
		                		Toast toast = Toast.makeText(ModeListView.this, 
		                        		"\nProblem in closing DB....\nFree memo: "+rt.freeMemory(),
		                        		Toast.LENGTH_LONG);
		                        toast.show();
		                	}
		        			String temp_aname="";
		        			try
		        			{
		        			temp_aname = album_names[position];
		        			}
		        			catch(Exception e)
		                	{
		                		Runtime rt = Runtime.getRuntime();
		                		Toast toast = Toast.makeText(ModeListView.this, 
		                        		"\nProblem in album name....\nFree memo: "+rt.freeMemory(),
		                        		Toast.LENGTH_LONG);
		                        toast.show();
		                	}
		        			try
		        			{
		        			album_cover_ids = null;
		    				album_covers = null;
		    				album_names = null;
		        			System.gc();
		        			}
		        			catch(Exception e)
		                	{
		                		Runtime rt = Runtime.getRuntime();
		                		Toast toast = Toast.makeText(ModeListView.this, 
		                        		"\nProblem in setting to null....\nFree memo: "+rt.freeMemory(),
		                        		Toast.LENGTH_LONG);
		                        toast.show();
		                	}
		        			if(!(temp_aname.equals("")))
		        			{
		        				Intent i = new Intent(ModeListView.this, OpenAlbum.class);
		        				i.putExtra("aname_e", temp_aname);
		        				startActivity(i);
		        				finish();
		        			}
		        			}
		        		});
		            return v;

            }
        }
        
}

  
         
    	
    
    	

        
        
        
        
    
