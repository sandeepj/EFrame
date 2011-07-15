package com.EFrame13;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

	private static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = " ElitePictureFrame";
    
    //  Album Table
    public static final String KEY_ALBUMID = "aid";
    public static final String KEY_ALBUM_NAME = "album_name";
    public static final String KEY_ALBUM_COVER = "album_cover";
    public static final String KEY_ALBUM_MUSIC = "album_music";
    public static final String KEY_ALBUM_CREATED_ON = "album_created_on";
    public static final String KEY_ALBUM_FRAMEID = "album_frame_location";
    public static final String KEY_SHUFFLE_MODE = "shuffle_mode";
    public static final String KEY_SLIDESHOW_TIME = "slideshow_time";
    
    
    // Photo Table
    public static final String KEY_PHOTOID = "pid";
    public static final String KEY_PHOTO_NAME = "photo_name";
    public static final String KEY_PHOTO_SIZE = "photo_size";
    public static final String KEY_PHOTO_CLICKED_ON = "photo_clicked_on";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_STATE = "state";
    public static final String KEY_CITY = "city";
    public static final String KEY_AREA = "area";
    public static final String KEY_PLACE = "place";
    public static final String KEY_TAG_NAME = "tag_name";
    public static final String KEY_PHOTO_FRAMEID = "photo_frame_location";
      
    
    // Album-Photo Table
    public static final String KEY_APID = "apid";
    public static final String KEY_AID = "album_id";
    public static final String KEY_PID = "photo_id";
    
 // Email-Details Table
    public static final String KEY_PRID = "eid";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_PWD = "pwd";
    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";
    
    
 // Privacy-Details Table
    public static final String KEY_EID = "prid";
   // public static final String KEY_ALBUM_NAME = "album_name";
   //public static final String KEY_PWD = "pwd";
       
    // Frame Table
    public static final String KEY_FID = "fid";
    public static final String KEY_FRAME_NAME = "frame_name";
    public static final String KEY_FRAME_LOCATION = "frame_location";
    
    
    //		EPFID_TABLE
    public static final String KEY_EPID = "epid";
    public static final String KEY_EPFID = "epfid";    
    
    //		PATH_TABLE
    public static final String KEY_PATHID = "pathid";
    public static final String KEY_PATH = "path";    
    
    
    // Creating all tables
    private static final String PATH_TABLE = "PathTable";
    private static final String EPFID_TABLE = "EpfidTable";
    private static final String FRAME_TABLE = "frame";
    private static final String ALBUM_TABLE = "album";
    private static final String PHOTO_TABLE = "photo";
    private static final String ALBUM_PHOTO_TABLE = "album_photo";
    private static final String EMAIL_DETAILS_TABLE = "email_details";
    private static final String PRIVACY_DETAILS_TABLE = "privacy_details";
       
    private static final int DATABASE_VERSION = 6;
                
    private static final String PATH_TABLE_CREATE =
        "create table PathTable (pathid integer primary key autoincrement, "
        + "path text not null);";
    
    private static final String EPFID_TABLE_CREATE =
        "create table EpfidTable (epid integer primary key autoincrement, "
        + "epfid text not null);";
    
    private static final String FRAME_CREATE =
        "create table frame (fid integer primary key autoincrement, "
        + "frame_name text not null, frame_location text not null);";
    
    private static final String EMAIL_DETAILS_CREATE =
        "create table email_details (eid integer primary key autoincrement, "
        + "address text not null, pwd text not null, "
        +"title text not null, body text not null);";
    
    private static final String PRIVACY_DETAILS_CREATE =
        "create table email_details (prid integer primary key autoincrement, "
        + "album_name text not null, "
        +"pwd text not null);";
    
    private static final String ALBUM_CREATE =
        "create table album (aid integer primary key autoincrement, "
        + "album_name text not null, album_cover integer not null, album_music text not null, "
        + "album_created_on timestamp not null, album_frame_location text not null, "
        + "shuffle_mode integer not null, slideshow_time integer not null);";
    
    private static final String PHOTO_CREATE =
        "create table photo (pid integer primary key autoincrement, "
        + "photo_name text not null, photo_size text not null, "
        + "photo_clicked_on timestamp not null, country text not null, "
        + "state text not null, city text not null, "
        + "area text not null, place text not null, "
        + "tag_name text not null, "
        + "photo_frame_location text not null);";
    
    private static final String ALBUM_PHOTO_CREATE =
        "create table album_photo (apid integer primary key autoincrement, "
    	+ "album_id integer, photo_id integer, "
        + "FOREIGN KEY(album_id) REFERENCES album(aid), "
        + "FOREIGN KEY(photo_id) REFERENCES photo(pid));";
    
        
    private final Context context; 
    
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
        
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
        	db.execSQL(PATH_TABLE_CREATE);
        	db.execSQL(EPFID_TABLE_CREATE);
        	db.execSQL(FRAME_CREATE);
        	db.execSQL(PHOTO_CREATE);
            db.execSQL(ALBUM_CREATE);
            db.execSQL(ALBUM_PHOTO_CREATE);
           
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion 
                    + " to "
                    + newVersion + ", which will destroy all old data");
            
            db.execSQL("DROP TABLE IF EXISTS album_photo");
            db.execSQL("DROP TABLE IF EXISTS album");
            db.execSQL("DROP TABLE IF EXISTS photo");
            db.execSQL("DROP TABLE IF EXISTS frame");
            db.execSQL("DROP TABLE IF EXISTS EpfidTable");
            db.execSQL("DROP TABLE IF EXISTS PathTable");
            onCreate(db);
        }
    }    
    
    //---opens the database---
    public DBAdapter open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    
    //------------------------------------------------------------------------
    
    //						PATH table
    
    //------------------------------------------------------------------------
    
    public long insertPath(String path) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_PATH, path);
        return db.insert(PATH_TABLE, null, initialValues);
    }
    
    public int checkIfPathExits()
    {
    	Cursor mCursor =
            db.query(true, PATH_TABLE, new String[] {
            		KEY_PATH
            		}, 
            		null, 
            		null,
            		null, 
            		null, 
            		null, 
            		null);
    	
    	int count = mCursor.getCount();
    	
    	mCursor.close();
    	System.gc();
    	if(count > 0)
    	   	return 1;
    	
    	return 0;
    }
    
    public Cursor getPath() throws SQLException 
    {
        Cursor mCursor =
                db.query(true, PATH_TABLE, new String[] {
                		KEY_PATHID,
                		KEY_PATH
                		}, 
                		null, 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public boolean updatePath(long rowId, String path) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_PATH, path);
        return db.update(PATH_TABLE, args, 
        		KEY_PATHID + "=" + rowId, null) > 0;
    }
    
    //------------------------------------------------------------------------
    
    //						EPFID table
    
    //------------------------------------------------------------------------
    
    public long insertEpfid(String epfid) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_EPFID, epfid);
        return db.insert(EPFID_TABLE, null, initialValues);
    }
    
    public Cursor getEpfid() throws SQLException 
    {
        Cursor mCursor =
                db.query(true, EPFID_TABLE, new String[] {
                		KEY_EPID,
                		KEY_EPFID
                		}, 
                		null, 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public boolean updateEpfid(long rowId, String epfid) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_EPFID, epfid);
        return db.update(EPFID_TABLE, args, 
        		KEY_EPID + "=" + rowId, null) > 0;
    }
    
    //						-------------------------------------
    //									Privacy-Settings TABLE
    //						-------------------------------------
    
    
    public long insertPrivacyDetails(String aname, String pwd) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ALBUM_NAME, aname);
        initialValues.put(KEY_PWD, pwd);
        return db.insert(PRIVACY_DETAILS_TABLE, null, initialValues);
    }
    
    public Cursor getAllPrivacyDetails() 
    {
        return db.query(PRIVACY_DETAILS_TABLE, new String[] {
        		KEY_PRID, 
        		KEY_ALBUM_NAME,
        		KEY_PWD 
        		}, 
                null, 
                null, 
                null,
                null, 
                null);
    }
    
    public Cursor getPrivacyDetails(String aname) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, PRIVACY_DETAILS_TABLE, new String[] {
                		KEY_PRID, 
                		KEY_PWD 
                		}, 
                		KEY_ALBUM_NAME + "='" + aname +"'", 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public int getPrivacyDetailsId(String aname) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, PRIVACY_DETAILS_TABLE, new String[] {
                		KEY_PRID
                		}, 
                		KEY_ALBUM_NAME + "='" + aname +"'", 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor.getInt(0);
    }
    
    public boolean updatePrivacyDetails(long rowId, String aname, String pwd) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_ALBUM_NAME, aname);
        args.put(KEY_PWD, pwd);
        return db.update(PRIVACY_DETAILS_TABLE, args, 
        		KEY_PRID + "=" + rowId, null) > 0;
    }
    
    public boolean deletePrivacyDetails(String aname) 
    {
    	return db.delete(PRIVACY_DETAILS_TABLE, KEY_ALBUM_NAME + 
        		"='" + aname+"'", null) > 0;
    }
    
    
    //						-------------------------------------
    //									Email-Details TABLE
    //						-------------------------------------
    
    
    public long insertEmailDetails(String address, String pwd, String title, String body) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ADDRESS, address);
        initialValues.put(KEY_PWD, pwd);
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_BODY, body);
        return db.insert(EMAIL_DETAILS_TABLE, null, initialValues);
    }
    
    public Cursor getAllEmailDetails() 
    {
        return db.query(EMAIL_DETAILS_TABLE, new String[] {
        		KEY_EID, 
        		KEY_ADDRESS,
        		KEY_PWD, 
        		KEY_TITLE, 
        		KEY_BODY 
        		}, 
                null, 
                null, 
                null,
                null, 
                null);
    }
    
    public Cursor getEmailDetails(String address) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, EMAIL_DETAILS_TABLE, new String[] {
                		KEY_EID,
                		KEY_PWD, 
                		KEY_TITLE,
                		KEY_BODY
                		}, 
                		KEY_ADDRESS + "='" + address +"'", 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public int getEmailDetailsId(String address) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, EMAIL_DETAILS_TABLE, new String[] {
                		KEY_EID
                		}, 
                		KEY_ADDRESS + "='" + address +"'", 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        
        int temp = mCursor.getInt(0);
        
        mCursor.close();
        System.gc();
        return temp;
    }
    
    public boolean updateEmailDetails(long rowId, String address, String pwd, 
    		String title, String body) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_ADDRESS, address);
        args.put(KEY_PWD, pwd);
        args.put(KEY_TITLE, title);
        args.put(KEY_BODY, body);
        return db.update(EMAIL_DETAILS_TABLE, args, 
        		KEY_EID + "=" + rowId, null) > 0;
    }
    
    public boolean deleteEmailDetails(String address) 
    {
    	return db.delete(EMAIL_DETAILS_TABLE, KEY_ADDRESS + 
        		"='" + address+"'", null) > 0;
    }
    
    
    //						-------------------------------------
    //									ALBUM TABLE
    //						-------------------------------------
    
    
    //---insert a album into the database---
    public long insertAlbum(String aname, long album_cover, String album_music, String album_created_on, 
    		String album_frame_location, long album_shuffle_mode, long album_slideshow_time) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ALBUM_NAME, aname);
        initialValues.put(KEY_ALBUM_COVER, album_cover);
        initialValues.put(KEY_ALBUM_MUSIC, album_music);
        initialValues.put(KEY_ALBUM_CREATED_ON, album_created_on);
        initialValues.put(KEY_ALBUM_FRAMEID, album_frame_location);
        initialValues.put(KEY_SHUFFLE_MODE, album_shuffle_mode);
        initialValues.put(KEY_SLIDESHOW_TIME, album_slideshow_time);
        return db.insert(ALBUM_TABLE, null, initialValues);
    }

    //---deletes a particular Album---
    public boolean deleteAlbum(long rowId) 
    {
        return db.delete(ALBUM_TABLE, KEY_ALBUMID + 
        		"=" + rowId, null) > 0;
    }

    public boolean deleteAlbum(String aname) 
    {
        return db.delete(ALBUM_TABLE, KEY_ALBUM_NAME + 
        		"='" + aname+"'", null) > 0;
    }
    
    //---retrieves all the Albums---
    public Cursor getAllAlbums() 
    {
        return db.query(ALBUM_TABLE, new String[] {
        		KEY_ALBUMID, 
        		KEY_ALBUM_NAME,
        		KEY_ALBUM_COVER, 
        		KEY_ALBUM_MUSIC, 
        		KEY_ALBUM_CREATED_ON, 
        		KEY_ALBUM_FRAMEID, 
        		KEY_SHUFFLE_MODE, 
        		KEY_SLIDESHOW_TIME}, 
                null, 
                null, 
                null,
                null, 
                null);
    }
    /*
    public String[] getAllAlbumCovers() 
    {
     	Cursor mCursor=db.rawQuery("SELECT P."+KEY_PHOTO_NAME+
    			" from "+ALBUM_TABLE+" A, "+PHOTO_TABLE +" P"+
        		" where P."+KEY_PHOTOID+"= A."+KEY_ALBUM_COVER,new String [] {});
        
     	String []album_names = new String[mCursor.getCount()];
     	int j=0;
     	
     	while(mCursor.moveToNext())
        {
     		album_names[j] = mCursor.getString(0);
        	j++;
        }
         return album_names;
    }
    */
    
    public String getPhotoLocation(int pid)
    {
    	
    	Cursor mCursor =
            db.query(true, PHOTO_TABLE, new String[] {
            		KEY_PHOTOID, 
            		KEY_PHOTO_NAME,
            		KEY_PHOTO_SIZE,
            		KEY_PHOTO_CLICKED_ON, 
            		KEY_COUNTRY, 
            		KEY_STATE, 
            		KEY_CITY,
            		KEY_AREA, 
            		KEY_PLACE, 
            		KEY_TAG_NAME,
            		KEY_PHOTO_FRAMEID
            		}, 
            		KEY_PHOTOID + "=" + pid, 
            		null,
            		null, 
            		null, 
            		null, 
            		null);
    if (mCursor != null) {
        mCursor.moveToFirst();
    }
    
    String temp = mCursor.getString(1); 
    mCursor.close();
    System.gc();
    return temp;
    	
    	/*
    	
    	Cursor mCursor=db.rawQuery("SELECT "+KEY_PHOTO_NAME+
    			" from "+PHOTO_TABLE +
        		" where "+KEY_PHOTOID+"="+pid,new String [] {});
    	
    	
    	if (mCursor.moveToFirst())
    	{	
    		String temp =  mCursor.getString(0);
    		return mCursor.getString(0);
    	} 	
    	
    	else
    	{
    		return "-";
    	}	
    	*/
    }
    
    public int[] getAllAlbumCovers() 
    {
     	Cursor mCursor=db.rawQuery("SELECT "+KEY_ALBUM_COVER+
    			" from "+ALBUM_TABLE,new String [] {});
        
     	int []album_names = new int[mCursor.getCount()];
     	int j=0;
     	
     	while(mCursor.moveToNext())
        {
     		album_names[j] = mCursor.getInt(0);
        	j++;
        }
     	
     	mCursor.close();
     	System.gc();
         return album_names;
    }
    
    public int checkIfPhotoExist(String pname)
    {
    	Cursor mCursor=db.rawQuery("SELECT "+KEY_PHOTOID+
    			" from "+PHOTO_TABLE+
        		" where "+KEY_PHOTO_NAME+"='"+pname+"'",new String [] {});
    	
    	int count = mCursor.getCount();
    	
    	mCursor.close();
    	System.gc();
    	if(count > 0)
    	   	return 1;
    	
    	return 0;
    }
    
    public int checkIfAlbumExist(String aname)
    {
    	Cursor mCursor=db.rawQuery("SELECT "+KEY_ALBUMID+
    			" from "+ALBUM_TABLE+
        		" where "+KEY_ALBUM_NAME+"='"+aname+"'",new String [] {});
    	
    	int count = mCursor.getCount();
    	
    	mCursor.close();
    	System.gc();
    	if(count > 0)
    	   	return 1;
    	
    	return 0;
    }
    
    public int checkIfAlbumExist(int aid, String aname)
    {
    	Cursor mCursor=db.rawQuery("SELECT "+KEY_ALBUMID+
    			" from "+ALBUM_TABLE+
        		" where "+KEY_ALBUM_NAME+"='"+aname+"' and "+KEY_ALBUMID+"!="+aid,new String [] {});
    	
    	int count = mCursor.getCount();
    	
    	mCursor.close();
    	System.gc();
    	if(count > 0)
    	   	return 1;
    	
    	return 0;
    }
    
    public String getAlbumCover(String aname) 
    {
     	Cursor mCursor=db.rawQuery("SELECT P."+KEY_PHOTO_NAME+
    			" from "+ALBUM_TABLE+" A, "+PHOTO_TABLE +" P"+
        		" where A."+KEY_ALBUM_NAME+"='"+aname+"' and P."+KEY_PHOTOID+"= A."+KEY_ALBUM_COVER,new String [] {});
        	
     	String temp =mCursor.getString(0); 
     	mCursor.close();
     	System.gc();
         return temp;
    }

    public int getPhotoId(String pname) 
    {
     	Cursor mCursor=db.rawQuery("SELECT "+KEY_PHOTOID+
    			" from "+PHOTO_TABLE+
        		" where "+KEY_PHOTO_NAME+"='"+pname+"'",new String [] {});
        	
     	int temp = 0; 
     	
     	
     	if (mCursor.moveToFirst())
     	{
     		temp = mCursor.getInt(0);
     		mCursor.close();
     		System.gc();
   	 	 	return temp;
   	 	 
     	}
     	else
     	{
     		mCursor.close();
     		System.gc();
     		return 0;
     	}
          
    }
    
    public boolean deletePhotoFromAlbum(long pid, long aid)
    {
    	return db.delete(ALBUM_PHOTO_TABLE, KEY_AID + 
        		"=" + aid+" and "+ KEY_PID+" ="+pid, null) > 0;
    }
    
    public int getAlbumId(String aname) 
    {
    	Cursor mCursor=db.rawQuery("SELECT "+KEY_ALBUMID+
    			" from "+ALBUM_TABLE+
        		" where "+KEY_ALBUM_NAME+"='"+aname+"'",new String [] {});
     	
    	int temp = 0; 
     	     	
     	if (mCursor.moveToFirst())
     	{
     		temp = mCursor.getInt(0);
     		mCursor.close();
     		System.gc();
   	 	 	return temp;
   	 	 
     	}
     	else
     	{
     		mCursor.close();
     		System.gc();
     		return 0;
     	}
    }
    
    public int getTotalNoOfPhotos()
    {
    	Cursor mCursor =  db.query(PHOTO_TABLE, new String[] {
        		KEY_PHOTOID
        		}, 
                null, 
                null, 
                null,
                null, 
                null);
    	
    	int temp = mCursor.getCount();
    	mCursor.close();
    	System.gc();
         return temp;
    }
    
    public String[] getAlbumNames()
    {
    	Cursor mCursor =  db.query(ALBUM_TABLE, new String[] {
        		KEY_ALBUM_NAME
        		}, 
                null, 
                null, 
                null,
                null, 
                null);
    	
    	String []album_names = new String[mCursor.getCount()];
     	int j=0;
     	
     	while(mCursor.moveToNext())
        {
     		album_names[j] = mCursor.getString(0);
        	j++;
        }
     	
     	mCursor.close();
     	System.gc();
         return album_names;
    }
    
    public int getnoOfAlbums() 
    {
        Cursor c =  db.query(ALBUM_TABLE, new String[] {
        		KEY_ALBUMID 
        		}, 
                null, 
                null, 
                null,
                null, 
                null);
        
        int temp = c.getCount(); 
        c.close();
        System.gc();
        return temp;
    }
    
    public int getFirstPhotoInAlbum(int aid) 
    {
    	Cursor mCursor=db.rawQuery("SELECT "+KEY_PID+
    			" from "+ALBUM_PHOTO_TABLE+
        		" where "+KEY_AID+"="+aid,new String [] {});
     	
    	int temp = 0; 
	     	
     	if (mCursor.moveToFirst())
     	{
     		temp = mCursor.getInt(0);
     		mCursor.close();
     		System.gc();
   	 	 	return temp;
   	 	 
     	}
     	else
     	{
     		mCursor.close();
     		System.gc();
     		return 0;
     	}
    }
    
    //---retrieves a particular Album---
    public Cursor getAlbum(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, ALBUM_TABLE, new String[] {
                		KEY_ALBUMID,
                		KEY_ALBUM_NAME,
                		KEY_ALBUM_COVER, 
                		KEY_ALBUM_MUSIC, 
                		KEY_ALBUM_CREATED_ON, 
                		KEY_ALBUM_FRAMEID, 
                		KEY_SHUFFLE_MODE, 
                		KEY_SLIDESHOW_TIME
                		}, 
                		KEY_ALBUMID + "=" + rowId, 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public int getSlideshowTime(int aid)
    {
    	Cursor mCursor = getAlbum(aid);
    	if (mCursor != null) {
            mCursor.moveToFirst();
        }    	
    	
    	int temp = mCursor.getInt(7);
    	
    	mCursor.close();
    	System.gc();
    	return temp;
    }
    
    public String getMusicLink(int aid)
    {
    	Cursor mCursor = getAlbum(aid);
    	if (mCursor != null) {
            mCursor.moveToFirst();
        }    	
    	
    	String temp = mCursor.getString(3); 
    	
    	mCursor.close();
    	System.gc();
    	return temp;
    }
    
    public String getAlbumName(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, ALBUM_TABLE, new String[] {
                		KEY_ALBUM_NAME
                		}, 
                		KEY_ALBUMID + "=" + rowId, 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        
        String temp =mCursor.getString(0); 
        
        mCursor.close();
        System.gc();
        return temp;
    }

    public boolean updateAlbumAfterInsert(long aid, long album_cover) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_ALBUM_COVER, album_cover);
        return db.update(ALBUM_TABLE, args, 
        		KEY_ALBUMID + "=" + aid, null) > 0;
    }
    
    
    //---updates a Album---
    public boolean updateAlbum(long rowId, String aname, long album_cover, String album_music, 
    		String album_created_on, String album_frame_location,long album_shuffle_mode, long album_slideshow_time) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_ALBUM_NAME, aname);
        args.put(KEY_ALBUM_COVER, album_cover);
        args.put(KEY_ALBUM_MUSIC, album_music);
        args.put(KEY_ALBUM_CREATED_ON, album_created_on);
        args.put(KEY_ALBUM_FRAMEID, album_frame_location);
        args.put(KEY_SHUFFLE_MODE, album_shuffle_mode);
        args.put(KEY_SLIDESHOW_TIME, album_slideshow_time);
        return db.update(ALBUM_TABLE, args, 
        		KEY_ALBUMID + "=" + rowId, null) > 0;
    }
    
    public boolean updateAlbumEdit(long rowId, String aname, String album_music, 
    								long album_shuffle_mode, long album_slideshow_time) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_ALBUM_NAME, aname);
        args.put(KEY_ALBUM_MUSIC, album_music);
        args.put(KEY_SHUFFLE_MODE, album_shuffle_mode);
        args.put(KEY_SLIDESHOW_TIME, album_slideshow_time);
        return db.update(ALBUM_TABLE, args, 
        		KEY_ALBUMID + "=" + rowId, null) > 0;
    }
    
    

    //						-------------------------------------
    //									PHOTO TABLE
    //						-------------------------------------
    
    
    //---insert a album into the database---
    public long insertPhoto(String pname, String psize,String photo_clicked_on, String country, 
    		String state, String city, String area,String place, String tag_name, String frame_location) 
    {
    	
    	
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_PHOTO_NAME, pname);
        initialValues.put(KEY_PHOTO_SIZE, psize);
        initialValues.put(KEY_PHOTO_CLICKED_ON, photo_clicked_on);
        initialValues.put(KEY_COUNTRY, country);
        initialValues.put(KEY_STATE, state);
        initialValues.put(KEY_CITY, city);
        initialValues.put(KEY_AREA, area);
        initialValues.put(KEY_PLACE, place);
        initialValues.put(KEY_TAG_NAME, tag_name);
        initialValues.put(KEY_PHOTO_FRAMEID, frame_location);
        return db.insert(PHOTO_TABLE, null, initialValues);
    }

    //---deletes a particular Album---
    public boolean deletePhoto(long rowId) 
    {
        return db.delete(PHOTO_TABLE, KEY_PHOTOID + 
        		"=" + rowId, null) > 0;
    }

    //---retrieves all the Albums---
    public Cursor getAllPhotos() 
    {
        return db.query(PHOTO_TABLE, new String[] {
        		KEY_PHOTOID, 
        		KEY_PHOTO_NAME,
        		KEY_PHOTO_SIZE,
        		KEY_PHOTO_CLICKED_ON, 
        		KEY_COUNTRY, 
        		KEY_STATE, 
        		KEY_CITY,
        		KEY_AREA, 
        		KEY_PLACE, 
        		KEY_TAG_NAME,
        		KEY_PHOTO_FRAMEID}, 
                null, 
                null, 
                null,
                null, 
                null);
    }

    public String[] getAllPhotosFromDB() 
    {
    	Cursor mCursor = db.query(PHOTO_TABLE, new String[] {
        		 
        		KEY_PHOTO_NAME}, 
                null, 
                null, 
                null,
                null, 
                null);
    	
    	String []photos = new String[mCursor.getCount()];
    	int j=0;
 	
    	while(mCursor.moveToNext())
    	{
    		photos[j] = mCursor.getString(0);
    		j++;
    	}
    	
    	mCursor.close();
    	System.gc();
    	return photos;
    	
    }
    
    //---retrieves a particular Album---
    public Cursor getPhoto(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, PHOTO_TABLE, new String[] {
                		KEY_PHOTOID, 
                		KEY_PHOTO_NAME,
                		KEY_PHOTO_SIZE,
                		KEY_PHOTO_CLICKED_ON, 
                		KEY_COUNTRY, 
                		KEY_STATE, 
                		KEY_CITY,
                		KEY_AREA, 
                		KEY_PLACE, 
                		KEY_TAG_NAME,
                		KEY_PHOTO_FRAMEID
                		}, 
                		KEY_PHOTOID + "=" + rowId, 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor getAllPhotos(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, PHOTO_TABLE, new String[] {
                		KEY_PHOTOID, 
                		KEY_PHOTO_NAME,
                		KEY_PHOTO_SIZE,
                		KEY_PHOTO_CLICKED_ON, 
                		KEY_COUNTRY, 
                		KEY_STATE, 
                		KEY_CITY,
                		KEY_AREA, 
                		KEY_PLACE, 
                		KEY_TAG_NAME,
                		KEY_PHOTO_FRAMEID
                		}, 
                		KEY_PHOTOID + "=" + rowId, 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public int getnoOfPhotos(long rowId)
    {
    	Cursor mCursor = 
    		db.query(ALBUM_PHOTO_TABLE, new String[] {
            		KEY_PHOTOID
            		},
            		KEY_ALBUMID + "=" + rowId, 
                    null, 
                    null, 
                    null,
                    null, 
                    null);
    	
    	int temp = mCursor.getCount();
    		
    	mCursor.close();	
    	System.gc();
    	return temp;
    }
    
    public int getnoOfPhotos(String aname)
    {
    	Cursor mCursor=db.rawQuery("select AP."+KEY_PID+" from "+ALBUM_TABLE+" A, "+ALBUM_PHOTO_TABLE+" AP" +
    			" where A."+KEY_ALBUM_NAME+"='"+aname+"' and AP."+KEY_AID+"= A."+KEY_ALBUMID,new String [] {});
        	
    	int temp = mCursor.getCount();
    		
    		mCursor.close();
    		System.gc();
         return temp;

    }
    
    public Cursor getAndPhotoTag(String []str1) 
    {
    	String tag1="";
    	if(str1.length > 1)
    	{
    	    	
    	for(int i=0; i<str1.length; i++)
    	{
    		if(tag1.equals(""))
    		{
    			tag1 = tag1+"("+KEY_TAG_NAME+" LIKE '%"+str1[i]+"%' OR "
    					+KEY_PHOTO_NAME+" LIKE '%"+str1[i]+"%' OR "
    					+KEY_PHOTO_CLICKED_ON+" LIKE '%"+str1[i]+"%' OR "
    					+KEY_COUNTRY+" LIKE '%"+str1[i]+"%' OR "
    					+KEY_STATE+" LIKE '%"+str1[i]+"%' OR "
    					+KEY_CITY+" LIKE '%"+str1[i]+"%' OR "
    					+KEY_AREA+" LIKE '%"+str1[i]+"%' OR "
    					+KEY_PLACE+" LIKE '%"+str1[i]+"%')";
    		}
    		else
    		{
    			tag1 = tag1+" AND ("+KEY_TAG_NAME+" LIKE '%"+str1[i]+"%' OR "
				+KEY_PHOTO_NAME+" LIKE '%"+str1[i]+"%' OR "
				+KEY_PHOTO_CLICKED_ON+" LIKE '%"+str1[i]+"%' OR "
				+KEY_COUNTRY+" LIKE '%"+str1[i]+"%' OR "
				+KEY_STATE+" LIKE '%"+str1[i]+"%' OR "
				+KEY_CITY+" LIKE '%"+str1[i]+"%' OR "
				+KEY_AREA+" LIKE '%"+str1[i]+"%' OR "
				+KEY_PLACE+" LIKE '%"+str1[i]+"%')";
    		}
    	}
    	}
    	else
    	{
    		String temp = str1[0];
    		tag1 = KEY_TAG_NAME+" LIKE '%"+str1[0]+"%' OR "
			+KEY_PHOTO_NAME+" LIKE '%"+str1[0]+"%' OR "
			+KEY_PHOTO_CLICKED_ON+" LIKE '%"+str1[0]+"%' OR "
			+KEY_COUNTRY+" LIKE '%"+str1[0]+"%' OR "
			+KEY_STATE+" LIKE '%"+str1[0]+"%' OR "
			+KEY_CITY+" LIKE '%"+str1[0]+"%' OR "
			+KEY_AREA+" LIKE '%"+str1[0]+"%' OR "
			+KEY_PLACE+" LIKE '%"+str1[0]+"%'";
    	}
    	Cursor mCursor=db.rawQuery("SELECT "+ 
        		KEY_PHOTO_NAME+
        		" from  "+PHOTO_TABLE +
        		" where "+KEY_PHOTO_NAME+
        		" IN ( SELECT "+KEY_PHOTO_NAME+" FROM "+PHOTO_TABLE+
        		" WHERE ("+tag1+"))",new String [] {});
    	
    	return mCursor;
    }
    
    public Cursor getPhotoTag(String tag, String seperator) 
    {
    	String tag1="";
    	if(!(seperator.equals("")))
    	{
    	String str1[] = tag.split(seperator);
    	
    	for(int i=0; i<str1.length; i++)
    	{
    		if(tag1.equals(""))
    		{
    			tag1 = tag1+KEY_TAG_NAME+" LIKE '%"+str1[i]+"%' OR "
    					+KEY_PHOTO_NAME+" LIKE '%"+str1[i]+"%' OR "
    					+KEY_PHOTO_CLICKED_ON+" LIKE '%"+str1[i]+"%' OR "
    					+KEY_COUNTRY+" LIKE '%"+str1[i]+"%' OR "
    					+KEY_STATE+" LIKE '%"+str1[i]+"%' OR "
    					+KEY_CITY+" LIKE '%"+str1[i]+"%' OR "
    					+KEY_AREA+" LIKE '%"+str1[i]+"%' OR "
    					+KEY_PLACE+" LIKE '%"+str1[i]+"%'";
    		}
    		else
    		{
    			tag1 = tag1+" OR "+KEY_TAG_NAME+" LIKE '%"+str1[i]+"%' OR "
				+KEY_PHOTO_NAME+" LIKE '%"+str1[i]+"%' OR "
				+KEY_PHOTO_CLICKED_ON+" LIKE '%"+str1[i]+"%' OR "
				+KEY_COUNTRY+" LIKE '%"+str1[i]+"%' OR "
				+KEY_STATE+" LIKE '%"+str1[i]+"%' OR "
				+KEY_CITY+" LIKE '%"+str1[i]+"%' OR "
				+KEY_AREA+" LIKE '%"+str1[i]+"%' OR "
				+KEY_PLACE+" LIKE '%"+str1[i]+"%'";
    		}
    	}
    	}
    	else
    	{
    		tag1 = KEY_TAG_NAME+" LIKE '%"+tag+"%' OR "
			+KEY_PHOTO_NAME+" LIKE '%"+tag+"%' OR "
			+KEY_PHOTO_CLICKED_ON+" LIKE '%"+tag+"%' OR "
			+KEY_COUNTRY+" LIKE '%"+tag+"%' OR "
			+KEY_STATE+" LIKE '%"+tag+"%' OR "
			+KEY_CITY+" LIKE '%"+tag+"%' OR "
			+KEY_AREA+" LIKE '%"+tag+"%' OR "
			+KEY_PLACE+" LIKE '%"+tag+"%'";
    	}
    	Cursor mCursor=db.rawQuery("SELECT "+ 
        		KEY_PHOTO_NAME+
        		" from  "+PHOTO_TABLE +
        		" where "+tag1,new String [] {});
    	
    	/*
    	Cursor mCursor=db.rawQuery("SELECT "+ 
        		KEY_PHOTO_NAME+
        		" from  "+PHOTO_TABLE +
        		" where "+KEY_TAG_NAME+" LIKE '%"+tag+"%' OR "
        				+KEY_PHOTO_NAME+" LIKE '%"+tag+"%' OR "
        				+KEY_PHOTO_CLICKED_ON+" LIKE '%"+tag+"%' OR "
        				+KEY_COUNTRY+" LIKE '%"+tag+"%' OR "
        				+KEY_STATE+" LIKE '%"+tag+"%' OR "
        				+KEY_CITY+" LIKE '%"+tag+"%' OR "
        				+KEY_AREA+" LIKE '%"+tag+"%' OR "
        				+KEY_PLACE+" LIKE '%"+tag+"%'",new String [] {});
    	*/
    	return mCursor;
    }
    
    //---updates a Album---
    public boolean updatePhoto(long rowId, String pname, String psize, String photo_clicked_on, String country, 
    		String state, String city, String area, String place,String tag_name, String frame_location) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_PHOTO_NAME, pname);
        args.put(KEY_PHOTO_SIZE, psize);
        args.put(KEY_PHOTO_CLICKED_ON, photo_clicked_on);
        args.put(KEY_COUNTRY, country);
        args.put(KEY_STATE, state);
        args.put(KEY_AREA, area);
        args.put(KEY_CITY, city);
        args.put(KEY_PLACE, place);
        args.put(KEY_TAG_NAME, tag_name);
        args.put(KEY_PHOTO_FRAMEID, frame_location);
        return db.update(PHOTO_TABLE, args, 
        		KEY_PHOTOID + "=" + rowId, null) > 0;
    }
    

    //						-------------------------------------
    //									FRAME TABLE
    //						-------------------------------------
    
    
    //---insert a FRAME into the database---
    public long insertFrame(String fname, String frame_location) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_FRAME_NAME, fname);
        initialValues.put(KEY_FRAME_LOCATION, frame_location);
        return db.insert(FRAME_TABLE, null, initialValues);
    }

    //---deletes a particular FRAME---
    public boolean deleteFrame(long rowId) 
    {
        return db.delete(FRAME_TABLE, KEY_FID + 
        		"=" + rowId, null) > 0;
    }

    //---retrieves all the FRAME---
    public Cursor getAllFrames() 
    {
        return db.query(FRAME_TABLE, new String[] {
        		KEY_FID, 
        		KEY_FRAME_NAME,
        		KEY_FRAME_LOCATION
        		}, 
                null, 
                null, 
                null,
                null, 
                null);
    }

    //---retrieves a particular FRAME---
    public Cursor getFrame(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, FRAME_TABLE, new String[] {
                		KEY_FID, 
                		KEY_FRAME_NAME,
                		KEY_FRAME_LOCATION
                		}, 
                		KEY_FID + "=" + rowId, 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a FRAME---
    public boolean updateFrame(long rowId, String fname, String frame_location) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_FRAME_NAME, fname);
        args.put(KEY_FRAME_LOCATION, frame_location);
        return db.update(FRAME_TABLE, args, 
        		KEY_FID + "=" + rowId, null) > 0;
    }
    

    //						-------------------------------------
    //									Album-Photo TABLE
    //						-------------------------------------
    
    
    //---insert a Album-Photo into the database---
    public long insertAlbum_photo(long aid, long pid) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_AID, aid);
        initialValues.put(KEY_PID, pid);
        return db.insert(ALBUM_PHOTO_TABLE, null, initialValues);
    }

    //---deletes a particular Album-Photo---
    public boolean deleteAlbum_photo(long rowId) 
    {
        return db.delete(ALBUM_PHOTO_TABLE, KEY_AID + 
        		"=" + rowId, null) > 0;
    }

    //---retrieves all the Album-Photo---
    public Cursor getAllAlbum_photos() 
    {
        return db.query(ALBUM_PHOTO_TABLE, new String[] {
        		KEY_APID, 
        		KEY_AID,
        		KEY_PID
        		}, 
                null, 
                null, 
                null,
                null, 
                null);
    }
    
    public String[] getphotoLocationOfAlbum(String aname) 
    {
    	Cursor mCursor=db.rawQuery("SELECT P."+KEY_PHOTO_NAME+
    		" from  "+ALBUM_TABLE+" A , "+ALBUM_PHOTO_TABLE+" AP, "+PHOTO_TABLE +" P"+
    		" where A."+KEY_ALBUM_NAME+"='"+aname+"' and AP."+KEY_AID+"=A."+KEY_ALBUMID+" and P."+KEY_PHOTOID+" = AP."+KEY_PID,new String [] {});
    	
    	String []photos = new String[mCursor.getCount()];
    	int j=0;
 	
    	while(mCursor.moveToNext())
    	{
    		photos[j] = mCursor.getString(0);
    		j++;
    	}
    	
    	mCursor.close();
    	System.gc();
    	return photos;
    }
    
    public int checkPhotoExistInAlbum(long aid, long pid) 
    {
    	Cursor mCursor=db.rawQuery("SELECT *"+ 
    		" from  "+ALBUM_PHOTO_TABLE+
    		" where "+KEY_AID+"="+aid+
    		" and "+KEY_PID+" = "+pid,new String [] {});
    	
    	int count = mCursor.getCount();
    	
    	mCursor.close();
    	System.gc();
    	if(count > 0)
    	   	return 1;
    	
    	return 0;
    }
    
    public String[] getphotosOfAlbum(String aname) 
    {
    	Cursor mCursor=db.rawQuery("SELECT P."+KEY_PHOTOID+",P."+ 
    		KEY_PHOTO_NAME+",P."+
    		KEY_PHOTO_SIZE+",P."+
    		KEY_PHOTO_CLICKED_ON+",P."+
    		KEY_COUNTRY+",P."+
    		KEY_STATE+",P."+
    		KEY_CITY+",P."+
    		KEY_AREA+",P."+
    		KEY_PLACE+",P."+
    		KEY_TAG_NAME+",P."+
    		KEY_PHOTO_FRAMEID+" from  "+ALBUM_TABLE+" A , "+ALBUM_PHOTO_TABLE+" AP, "+PHOTO_TABLE +" P"+
    		" where A."+KEY_ALBUM_NAME+"='"+aname+"' and AP."+KEY_AID+"=A."+KEY_ALBUMID+" and P."+KEY_PHOTOID+" = AP."+KEY_PID,new String [] {});
    	
    	String []photos = new String[mCursor.getCount()];
    	int j=0;
 	
    	while(mCursor.moveToNext())
    	{
    		photos[j] = mCursor.getString(0);
    		j++;
    	}
    	
    	mCursor.close();
    	System.gc();
    	return photos;
    }
    
    public Cursor getphotosOfAlbum(long rowId) 
    {
    	      
	Cursor mCursor=db.rawQuery("SELECT P."+KEY_PHOTOID+",P."+ 
    		KEY_PHOTO_NAME+",P."+
    		KEY_PHOTO_SIZE+",P."+
    		KEY_PHOTO_CLICKED_ON+",P."+
    		KEY_COUNTRY+",P."+
    		KEY_STATE+",P."+
    		KEY_CITY+",P."+
    		KEY_AREA+",P."+
    		KEY_PLACE+",P."+
    		KEY_TAG_NAME+",P."+
    		KEY_PHOTO_FRAMEID+" from  "+ALBUM_PHOTO_TABLE+" A, "+PHOTO_TABLE +" P"+
    		" where A."+KEY_AID+"="+rowId+" and P."+KEY_PHOTOID+" = A."+KEY_PID,new String [] {});
    	
     return mCursor;
    }

    
    public Cursor getAlbum_photo(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, ALBUM_PHOTO_TABLE, new String[] {
                		KEY_APID, 
                		KEY_AID,
                		KEY_PID
                		}, 
                		KEY_APID + "=" + rowId, 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
   

    //---updates a Album-Photo---
    public boolean updateAlbum_photo(long rowId, long aid, long pid) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_AID, aid);
        args.put(KEY_PID, pid);
        return db.update(ALBUM_PHOTO_TABLE, args, 
        		KEY_APID + "=" + rowId, null) > 0;
    }
    
	
}
