package com.EFrame13;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SiteDownloadTemp extends Activity{


	//Session ss = new Session();
	Dialog dialog;
	int count;
	String paths[]=null;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download);
        
        System.out.println("In Download temp..");
        
        // Get the current count i.e. no. of photos remaining to download... 
        
        Bundle extras = getIntent().getExtras();
        count = extras.getInt("count_e");
        paths = extras.getStringArray("paths_e");
        
        System.out.println("Current Count(home): "+count);
        if(count>0)
        {
           	Intent i = new Intent(SiteDownloadTemp.this, image.class);
           	i.putExtra("path_e", paths[count-1]);
           	i.putExtra("paths_e", paths);
           	i.putExtra("count_e", count);
		System.gc();
        	startActivity(i);
finish();
        }
        else
        {
        	display(paths.length);
        	
        }
    }
    
    /*
	Type: function
	Name: display
	Parameters: no. of photos downloaded(int)
	Return Type: -
	Date: 29/6/11
	Purpose: Dialog to show no. of photos downloaded....

*/
    void display(final int c)
    {
    	dialog = new Dialog(SiteDownloadTemp.this);
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
                 Intent i = new Intent(SiteDownloadTemp.this, InsertDBAndUpdate.class);
                 i.putExtra("count_e", c);
                 i.putExtra("paths_e", paths);
System.gc();
     			 startActivity(i);
finish();
             }
         });
         dialog.show();
		
    }
	
}
