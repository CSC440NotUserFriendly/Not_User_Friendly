package com.csc440.nuf.complay;

/**
 * CSC-440 SMIL Project
 * 2-2-2012
 * PlayerCanvas.java
 * @author Jacob Ensor and Brad Barker
 * 
 * 2-9-2012 
 * Edited by: Jacob Ensor
 * File Created
 */

import java.util.jar.Attributes;

import com.csc440.nuf.WaitingQueue;
import com.csc440.nuf.components.SMILText;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PlayerCanvas extends SurfaceView implements Runnable {
	private Timer timer;
	private boolean playing;
	private SurfaceHolder holder;
	private Thread renderThread = null;
	
    public PlayerCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);

        timer = new Timer();
        holder = getHolder();
        
        /*
        Attributes att = new Attributes();
        att.put("xml:id", "1");
        att.put("text", "this is some text!");
        att.put("textAlign", "left");
        att.put("top", "1");
        att.put("left", "1");
        att.put("textColor", "#FFFFFF");
        att.put("textFontSize", "20px");
        att.put("z-index", "1");
        att.put("begin", "0");
        att.put("dur", "10");

        SMILText t = new SMILText((org.xml.sax.Attributes) att);
        WaitingQueue.push(t);
        
        
        timer = new Timer();
        
        while (true) {
        	if (timer.timePlusPlus())
        		invalidate();
        	Thread.sleep(1000);
        }*/        
    }

    public void play() {
    	playing = true;
    	renderThread = new Thread(this);
    	renderThread.start();
    }
    
    public void run() {
    	while (playing) {
    		if (!holder.getSurface().isValid()) continue;
    		
    		Canvas canvas = holder.lockCanvas();
    		
            Paint white = new Paint();
            white.setColor(Color.WHITE);
            canvas.drawLine(0, 0, 40, 40, white);
            
    		if (timer.timePlusPlus()) 
                canvas.drawLine(0, 0, 40, 40, white);
            
            holder.unlockCanvasAndPost(canvas);
            
            /*
        	try {
				renderThread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
        }
    }

    public void pause() {
    	playing = false;
    	while (true) {
    		try {
    			renderThread.join();
    		} catch (InterruptedException e) {
    			// try again
    		}
    	}
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        Paint white = new Paint();
        white.setColor(Color.WHITE);
        canvas.drawLine(0, 0, 20, 20, white);
    }
}
