package csc440.nuf.complay;

/**
 * CSC-440 SMIL Project
 * 2-2-2012
 * ComposerActivity.java
 * @author Jacob Ensor
 * 
 * 2-2-2012 
 * Edited by: Jacob Ensor
 * File Created
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import csc440.nuf.*;

public class ComposerActivity extends PlayerActivity implements OnTouchListener {

	@Override
	public boolean onTouch(View v, MotionEvent m) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
    
	/**The "main" method of an android activity*/
    protected void onResume() {
        super.onResume();
        
        playPause(null);
        
        findViewById(R.id.stop).setVisibility(View.VISIBLE);
        findViewById(R.id.stop).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ComposerActivity.this.finish();
			}
        });
        
        //Sets the view to the configuration of main.xml
        //setContentView(R.layout.composer_main);
    }
}