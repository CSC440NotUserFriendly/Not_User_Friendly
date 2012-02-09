package com.csc440.nuf.complay;

/**
 * CSC-440 SMIL Project
 * 2-2-2012
 * PlayerActivity.java
 * @author Jacob Ensor
 * 
 * 2-2-2012 
 * Edited by: Jacob Ensor
 * File Created
 */

import com.csc440.nuf.ActionBar;
import com.csc440.nuf.R;
import com.csc440.nuf.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class PlayerActivity extends Activity {
    private ActionBar mActionBar;
	
	/**The "main" method of an android activity*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // since we're using ActionBar, first take off the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.player_main);
        
        
        mActionBar = (ActionBar) findViewById(R.id.actionBar);
        mActionBar.setTitle("Message Activity");
        
        mActionBar.setHomeListener(new OnClickListener() {
			public void onClick(View v) {
        		PlayerActivity.this.finish();
			}
        });
    }

}