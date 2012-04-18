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
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.SeekBar;


public class PlayerCanvas extends SurfaceView implements Runnable {
	private Timer timer;
	private boolean playing, pauseAfterDraw = false, forceDraw = false, runTime = true;
	private SurfaceHolder holder;
	private Thread renderThread = null;
	private SeekBar _seekBar;
	private float deltaTime;
	private int messageLength;
	
    public PlayerCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        timer = new Timer();
        holder = getHolder();
    }

    public void setSeekBar(SeekBar seekBar) {
    	_seekBar = seekBar;
    	messageLength = _seekBar.getMax();
    }
    
    public void play() {
    	play(true);
    }
    
    public void play(boolean runTime) {
    	playing = true;
    	this.runTime = runTime;
    	renderThread = new Thread(this);
    	renderThread.start();
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
                Log.w("drawn", "on screen");
	    		//if (!Waiting.Q().isEmpty()) Log.w("PlayerCanvas", "Waiting Queue Start Time " + Waiting.Q().peek().getStartTime());
    		
	            holder.unlockCanvasAndPost(canvas);
	            forceDraw = false;
    		}
            /* this was Brad's initial suggestion for controlling time, I've nixed it for now
        	try {
				renderThread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/

    		startTime = System.nanoTime();
    		/*
    		if (pauseAfterDraw) {
    			pauseAfterDraw = false;
    			pause();
    		}*/
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
