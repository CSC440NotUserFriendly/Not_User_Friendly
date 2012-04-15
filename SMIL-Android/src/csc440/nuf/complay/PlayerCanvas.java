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
	private boolean playing;
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
    	playing = true;
    	renderThread = new Thread(this);
    	renderThread.start();
    }
    
    public void run() {
    	long startTime = System.nanoTime();
    	    	
        Paint white = new Paint();
        white.setColor(Color.WHITE);
        Paint red = new Paint();
        red.setColor(Color.RED);
        
    	while (playing) {
    		
    		if (!holder.getSurface().isValid()) continue;
    		
    		// this gives us deltaTime in seconds
    		deltaTime = (System.nanoTime()-startTime) / 1000000000.0f;
    		// this will make our loop only execute once a second
    		if (deltaTime < 1) continue;
    		// I think this will be a much more elegant solution than sleeping the thread
    		
    		/* 
    		 * When we have stuff in the waitingQueue here we will be looping through the 
    		 * onScreenQ and drawing all of the objects. For now I just have it drawing this
    		 * random stuff. Used this output to test the timing and seekbar interaction.
    		 */
            
    		if (timer.timePlusPlus()) {
        		Canvas canvas = holder.lockCanvas();
        		
                canvas.drawColor(Color.BLACK);
                OnScreen.Q().draw(canvas);
	    		//if (!Waiting.Q().isEmpty()) Log.w("PlayerCanvas", "Waiting Queue Start Time " + Waiting.Q().peek().getStartTime());
    		
	            holder.unlockCanvasAndPost(canvas);
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
    		_seekBar.setProgress(timer.getTime());
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
}
