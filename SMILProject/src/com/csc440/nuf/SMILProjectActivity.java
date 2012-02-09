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
    private ActionBar mActionBar;
    //private LayoutInflator mInflator;
    private RelativeLayout mLinear;
    
	/**The "main" method of an android activity*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // since we're using ActionBar, first take off the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        
        mActionBar = (ActionBar) findViewById(R.id.actionBar);
        mActionBar.setTitle(R.string.app_name);
        mActionBar.setHomeLogo(R.drawable.ic_launcher);
        	
        mLinear = (RelativeLayout) findViewById(R.id.item1);
        
        //ScrollItem temp = new ScrollItem(this, null);
        //mLinear.addView(temp);
        
        mActionBar.setHomeListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(SMILProjectActivity.this, PlayerActivity.class);
				SMILProjectActivity.this.startActivity(i);
			}
        });
    }
    
    public void item1Clicked(View v) {
    	
    }
}