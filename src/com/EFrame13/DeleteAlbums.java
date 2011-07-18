package com.EFrame13;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import android.widget.CompoundButton.OnCheckedChangeListener;

public class DeleteAlbums extends Activity{
	
	DBAdapter db = new DBAdapter(this); 
	//Session ss = new Session();
	String albumSelected="";
	Cursor cursor;
	int pos, k, total, cnt=0;
	int []album_cover_ids = null;
	String []album_covers = null;
	String []album_names = null;
	Button done = null;
	 private int columnIndex;
	 EditText search_item = null;
	 String selectedPhotoName="";
	 int albumsToDelete[] = null;

	 /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	    	super.onCreate(savedInstanceState);
	    	setContentView(R.layout.delete_albums);
     
         	// In order to do any transactions with database.. Need to open the database..
	    	db.open();
	    	
     
     Button back = (Button)findViewById(R.id.back);
     back.setOnClickListener(new Button.OnClickListener() 
		{ public void onClick (View v)
			{
		//	if(db!=null)
				db.close();
			
				album_cover_ids = null;
				album_covers = null;
				album_names = null;
				albumsToDelete = null;
				
				System.gc();

				Intent i = new Intent(DeleteAlbums.this, ModeListView.class);
				startActivity(i);
				finish();
			}
		});
     
     done = (Button)findViewById(R.id.done);
     done.setOnClickListener(new Button.OnClickListener() 
 			{ public void onClick (View v)
 				{
 				// Fire query in order to delete all selected albums
 					for(int i=0; i<cnt; i++)
 					{ 						
                        db.deleteAlbum_photo(albumsToDelete[i]);                      
 						db.deleteAlbum(albumsToDelete[i]);
 					}
 					
 				//	if(db!=null)
 					db.close();
 					
 					album_cover_ids = null;
 					album_covers = null;
 					album_names = null;
 					albumsToDelete = null;
 					
					System.gc();

 					Intent i = new Intent(DeleteAlbums.this, ModeListView.class);
 					startActivity(i);
					finish();
 				}
 			});
     
    
     //Getting all album covers and names in order to display to user...
     album_cover_ids = db.getAllAlbumCovers();
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
      //		if(c!=null)
      			c.close();
      	}
      	else
      	{
      		album_covers[i] = "";
      	}
      }
     
      albumsToDelete = new int[album_names.length];
     displayPhotos();
 }
 
 void displayPhotos()
 {
 	GridView sdcardImages = (GridView) findViewById(R.id.sdcard);
     sdcardImages.setAdapter(new ImageAdapter(this));
     
     
 }
 
 private class ImageAdapter extends BaseAdapter {

     private Context context;

     public ImageAdapter(Context localContext) {
         context = localContext;
     }

     public int getCount() {
         return album_names.length;
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
             v = li.inflate(R.layout.delete_albums_row, null);
              
             // Binding album covers with album names to display...
             ImageView iv = (ImageView)v.findViewById(R.id.icon_image1);
             if(!(album_covers[position].equals("")))
             {
            	// ImageView iv = (ImageView)v.findViewById(R.id.icon_image1);
            	 Bitmap bMap = BitmapFactory.decodeFile(album_covers[position]);
            	 if(bMap!=null)		       
            	 {
            		 Bitmap newImage = Bitmap.createScaledBitmap(bMap, 80, 80, true);
                     iv.setImageBitmap(newImage);
            		// iv.setImageBitmap(bMap);
            	 }
            	 else
            		 iv.setImageResource(R.drawable.moved_photo);
             }
             else
             {
             	
	                iv.setImageResource(R.drawable.default_a);
             }
             

             final CheckBox check1 = (CheckBox)v.findViewById(R.id.check1);
             check1.setText("Details:\nAlbum Name: "+album_names[position]);
             check1.setId(position);
             
             check1.setOnCheckedChangeListener(new OnCheckedChangeListener()
             {
                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                 {
                     if ( isChecked )
                     {
                    	 // Add album in albums to delete List...
                    	 albumsToDelete[cnt] = db.getAlbumId(album_names[check1.getId()]);
                    	 cnt++;
                     }
                     else
                     {
                    	 // Remove album from albums to delete List...	
                     	int j = db.getAlbumId(album_names[check1.getId()]);
                     	int l=0;
                     	for(int i=0; i<cnt; i++)
                     	{
                     		if(albumsToDelete[i] == j)
                     			l=i;
                     	}
                     	
                     	for(int i=l; i<cnt; i++)
                     	{
                     		int temp = albumsToDelete[i];
                     		albumsToDelete[i] = albumsToDelete[i+1];
                     	}
							
                     	cnt--;
                     }
                 }
             });
             
       
         return v;
         
     }
 }

	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
}