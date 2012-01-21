package com.DisplayExp;

import android.app.Activity;
import android.os.Bundle;

public class Display extends Activity
{
    MyCanvas myCav;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        myCav = new MyCanvas(this);
        setContentView(myCav);
        
    }

    

}
