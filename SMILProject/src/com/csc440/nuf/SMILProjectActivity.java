package com.csc440.nuf;

/**
 * CSC-440 SMIL Project
 * 01-22-2011
 * SMILProjectActivity.java
 * @author Brad Barker
 * 
 * Follow this format for all java files created. The header
 * should look exactly as above. Also include a detailed description
 * of the class here and finally anyone who makes changes to the code edit
 * as seen below.
 * 
 * 01-22-2011 
 * Edited by: Brad Barker
 * Added this comment.
 * 
 */


import com.csc440.nuf.complay.PlayerActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class SMILProjectActivity extends Activity {
    private ActionBar _actionBar;
    private ScrollItem item1, item2, item3;
    
	/**The "main" method of an android activity*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // since we're using ActionBar, first take off the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        
        _actionBar = (ActionBar) findViewById(R.id.actionBar);
        _actionBar.setTitle(R.string.app_name);
        _actionBar.setHomeLogo(R.drawable.ic_launcher);

        item1 = new ScrollItem(findViewById(R.id.item1));
        item1.setTitle("Compose Message");
        item1.setIcon(R.drawable.ic_home);
        item1.setListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(SMILProjectActivity.this, PlayerActivity.class);
				SMILProjectActivity.this.startActivity(i);
			}
        });
        
        item2 = new ScrollItem(findViewById(R.id.item2));
        item2.setTitle("Compose From Template");
        item2.setIcon(R.drawable.ic_home);
        item2.setListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(SMILProjectActivity.this, PlayerActivity.class);
				SMILProjectActivity.this.startActivity(i);
			}
        });
        
        item3 = new ScrollItem(findViewById(R.id.item3));
        item3.setTitle("View Inbox");
        item3.setIcon(R.drawable.inbox);
        item3.setListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(SMILProjectActivity.this, PlayerActivity.class);
				SMILProjectActivity.this.startActivity(i);
			}
        });
        
        /*
        _actionBar.setHomeListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(SMILProjectActivity.this, PlayerActivity.class);
				SMILProjectActivity.this.startActivity(i);
			}
        });
        */
    }
    
    public void item1Clicked(View v) {
    	
    }
}