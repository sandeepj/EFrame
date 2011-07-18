package com.EFrame13;

import java.io.File;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CamAlbumList extends Activity{
	int selectedAlbum;
	String selectedAlbumName;
	int []album_cover_ids = null;
	String []album_covers = null;
	String []album_names = null;
	DBAdapter db = new DBAdapter(this);
	//Session ss = new Session();
	BitmapFactory.Options options;
		
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_album_camera);
    
     // In order to do any transactions with database.. Need to open the database..
        db.open();
         
        File camPhotosDirectory = new File("/sdcard/ElitePictureCamera/");
		if(!(camPhotosDirectory.exists()))
			camPhotosDirectory.mkdirs();
        
        Button home = (Button)findViewById(R.id.home);
        home.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
		//	if(db!=null)
				db.close();
				album_cover_ids = null;
				album_covers = null;
				album_names = null;
				
				System.gc();

				Intent i = new Intent(CamAlbumList.this, home.class);
				startActivity(i);
				finish();
			}
		}); 
        
        Button temp = (Button)findViewById(R.id.temp);
        temp.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
			
				
				if(db.checkIfAlbumExist("temp") == 0)
				{
					Calendar calendar = Calendar.getInstance();
			    	java.util.Date now = calendar.getTime();
			    	java.util.Date today = new java.util.Date();
					java.sql.Timestamp ti = new java.sql.Timestamp(now.getTime());
					
					db.insertAlbum("temp", 0, "", ti.toString(), "", 0, 3000);
				}
				
			//	if(db!=null)
					db.close();
				
				album_cover_ids = null;
				album_covers = null;
				album_names = null;
				
				System.gc();

				Intent i = new Intent(CamAlbumList.this, CamMode.class);
				i.putExtra("aname_e", "temp");
				startActivity(i);
				finish();
			}
		});
        
        Button create = (Button)findViewById(R.id.create);
        create.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
		//	if(db!=null)
				db.close();

				album_cover_ids = null;
				album_covers = null;
				album_names = null;
				
				System.gc();


			
				Intent i = new Intent(CamAlbumList.this, add_album_cam.class);
				startActivity(i);
				finish();
			}
		});
        
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
        			String temp1 = c.getString(1);
        			album_covers[i] = temp1;
        		}
        	//	if(c!=null)
        			c.close();
        	}
        	else
        	{
        		album_covers[i] = "";
        	}
        }
      
        //Attaching details to grid view...
        GridView gridview1 = (GridView) findViewById(R.id.gridview1);
        gridview1.setAdapter(new ImageAdapter(this));

        gridview1.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
               	
            	// Storing selected albums name in session for further use...
            	
            	
            //	if(db!=null)
            		db.close();
            	album_cover_ids = null;
				album_covers = null;
				
				System.gc();


								
            	Intent i = new Intent(CamAlbumList.this, CamMode.class);
            	i.putExtra("aname_e", album_names[position]);
            	album_names = null;
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
		         
		                iv.setOnClickListener(new Button.OnClickListener() 
		        		{ public void onClick (View v)
		        			{ 
		        			
		                	
		        		//	if(db!=null)
		        				db.close();
		        			
		        			album_cover_ids = null;
		    				album_covers = null;
		    						        								System.gc();
		        			Intent i = new Intent(CamAlbumList.this, CamMode.class);
		        			i.putExtra("aname_e", album_names[position]);
		                	album_names = null;
		                    startActivity(i);
						finish();	
		        			}
		        		});
		                		         
		            return v;

            }
        }
        
}

