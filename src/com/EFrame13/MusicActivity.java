package com.EFrame13;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MusicActivity extends Activity {
      ListView musiclist=null;
      Cursor musiccursor;
      int music_column_index;
      int count;
      MediaPlayer mMediaPlayer;
      TextView text=null;
    //  Session ss = new Session();
      String selectedAlbum;
  
      /** Called when the activity is first created. */
      @Override
      public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.song_list);
            
            Bundle extras = getIntent().getExtras();
            selectedAlbum = extras.getString("aname_e");
            System.out.println("Album:"+selectedAlbum);
            text = (TextView)findViewById(R.id.text);
            
            Button back = (Button)findViewById(R.id.back);
            back.setOnClickListener(new Button.OnClickListener() 
    		{ public void onClick (View v)
    			{
    			System.gc();
    				Intent i = new Intent(MusicActivity.this, EditAlbum.class);
    				i.putExtra("music_e", "");
                    i.putExtra("aname_e", selectedAlbum);
                    System.out.println("Album: "+selectedAlbum);
    				startActivity(i);
    				System.out.println("Finish...");
    				finish();
    			}
    		});
            
            init_phone_music_grid();
            
            // Display all songs availble on the device...
      }

      private void init_phone_music_grid() {
            System.gc();
            String[] proj = { MediaStore.Audio.Media._ID,
            		MediaStore.Audio.Media.DATA,
            		MediaStore.Audio.Media.DISPLAY_NAME,
            		MediaStore.Video.Media.SIZE };
            musiccursor = managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            							proj, null, null, null);
            count = musiccursor.getCount();
            
            if(count == 0)
               	text.setText("Sorry.. No songs on your SD card!!");
                        
            musiclist = (ListView) findViewById(R.id.PhoneMusicList); 
            musiclist.setAdapter(new MusicAdapter(getApplicationContext()));

            musiclist.setOnItemClickListener(musicgridlistener);
            mMediaPlayer = new MediaPlayer();
      }

      private OnItemClickListener musicgridlistener = new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position,long id) 
            {
                  System.gc();
                  music_column_index = musiccursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                  musiccursor.moveToPosition(position);
                  String filename = musiccursor.getString(music_column_index);
                  
          //        ss.setSessionMusicSelected(filename);
                  System.gc();
                  Intent i = new Intent(MusicActivity.this, EditAlbum.class);
                  i.putExtra("music_e", filename);
                  i.putExtra("aname_e", selectedAlbum);
				  startActivity(i);
				  System.out.println("Finish...");
					finish();
                 
            }
      };

      public class MusicAdapter extends BaseAdapter {
            private Context mContext;

            public MusicAdapter(Context c) {
                  mContext = c;
            }

            public int getCount() {
                  return count;
            }

            public Object getItem(int position) {
                  return position;
            }

            public long getItemId(int position) {
                  return position;
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                  System.gc();
                  TextView tv = new TextView(mContext.getApplicationContext());
                  String id = null;
                  if (convertView == null) {
                        music_column_index = musiccursor
.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                        musiccursor.moveToPosition(position);
                        id = musiccursor.getString(music_column_index);
                        music_column_index = musiccursor
.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
                        musiccursor.moveToPosition(position);
                        id += " Size(KB):" + musiccursor.getString(music_column_index);
                        tv.setText(id);
                  } else
                        tv = (TextView) convertView;
                  return tv;
            }
      }
}

