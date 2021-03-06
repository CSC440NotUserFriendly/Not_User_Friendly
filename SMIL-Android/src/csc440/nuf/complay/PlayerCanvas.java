package csc440.nuf.complay;

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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.SeekBar;


public class PlayerCanvas extends SurfaceView implements Runnable {
	private Timer timer;
	private boolean playing, forceDraw = false, runTime = false;
	private SurfaceHolder holder;
	private Thread renderThread = null;
	private SeekBar _seekBar;
	private float deltaTime;
	
    public PlayerCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        timer = new Timer();
        holder = getHolder();
    }

    public void setSeekBar(SeekBar seekBar) {
    	_seekBar = seekBar;
    }
    
    public void play() {
    	if (playing != true) {
	    	renderThread = new Thread(this);
	    	renderThread.start();
    	}
    	playing = true;
    }
    
    public void run() {
    	long startTime = System.nanoTime();
        boolean firstIteration = true;
    	while (playing) {
    		if (!holder.getSurface().isValid()) continue;
    		
    		if (!firstIteration && !forceDraw && runTime) {
	    		// this gives us deltaTime in seconds
	    		deltaTime = (System.nanoTime()-startTime) / 1000000000.0f;
	    		// this will make our loop only execute once a second
	    		if (deltaTime < 1) continue;
    		} else if (firstIteration) {
    			firstIteration = false;
    			if (!runTime) {
                	timer.timePlusPlus();
                	_seekBar.setProgress(timer.getTime());
                	forceDraw = true;
    			}
    		}
    		// I think this will be a much more elegant solution than sleeping the thread
    		
    		/* 
    		 * When we have stuff in the waitingQueue here we will be looping through the 
    		 * onScreenQ and drawing all of the objects. For now I just have it drawing this
    		 * random stuff. Used this output to test the timing and seekbar interaction.
    		 */
    		boolean Qchanged = false;
            if (runTime) {
            	Qchanged = timer.timePlusPlus();
            	_seekBar.setProgress(timer.getTime());
            }
            
    		if (Qchanged || forceDraw) {
        		Canvas canvas = holder.lockCanvas();
        		
                canvas.drawColor(Color.BLACK);
                OnScreen.Q().draw(canvas);
	            holder.unlockCanvasAndPost(canvas);
	            forceDraw = false;
    		}

    		startTime = System.nanoTime();
        }
    }

    public void pause() {
    	playing = false;
    	while (true) {
    		try {
    			renderThread.join();
    			break;
    		} catch (InterruptedException e) {
    			// try again
    		}
    	}
    }
    
    public void setTime(int time) {
    	timer.setTime(time-1);
    	deltaTime = 1;
    }

    public void forceDraw() {
    	forceDraw = true;
    }
    
    public void setRunTime(boolean runTime) {
    	this.runTime = runTime;
    }
}
