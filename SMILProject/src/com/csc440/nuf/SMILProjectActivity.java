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

import android.app.Activity;
import android.os.Bundle;

public class SMILProjectActivity extends Activity {
    
	/**The "main" method of an android activity*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        //Sets the view to the configuration of main.xml
        setContentView(R.layout.main);
    }
}