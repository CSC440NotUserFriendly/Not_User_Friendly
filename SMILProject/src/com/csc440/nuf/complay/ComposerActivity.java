package com.csc440.nuf.complay;

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

import com.csc440.nuf.R;
import android.app.Activity;
import android.os.Bundle;

public class ComposerActivity extends Activity {
    
	/**The "main" method of an android activity*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        //Sets the view to the configuration of main.xml
        setContentView(R.layout.composer_main);
    }
}