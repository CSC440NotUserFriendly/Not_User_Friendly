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

import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import csc440.nuf.*;
import csc440.nuf.components.AbstractSMILDrawable;
import csc440.nuf.components.AbstractSMILObject;

public class ComposerActivity extends PlayerActivity implements OnTouchListener {
	private int oldx, oldy;
	private float time;
	private boolean inDrag, inCornerDrag;
	
	// LEFT TO DO:
	//	clean up uses of Timer so we aren't sorting more than necessary
	//	fix the threading issue with runTime and play/pause
	//	handle the ontouch events for selecting and deselecting objects
	//	figure out why Waiting is going empty sometimes
	@Override
	public boolean onTouch(View v, MotionEvent event) {
        boolean processedTouch = false;
		int x = (int) event.getX();
        int y = (int) event.getY();
        Log.w("touchEvent", "indrag="+(inDrag?"true":"false")+"   event="+event.getAction() + "     DOWN=" + MotionEvent.ACTION_DOWN+",UP=" + MotionEvent.ACTION_UP);
        
        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
        	if (inDrag || inCornerDrag) processedTouch = true;
        	inDrag = false;
        	inCornerDrag = false;
        }

        /*
        else if (event.getAction() == MotionEvent.ACTION_DOWN && !inDrag) {
			oldx = x;
			oldy = y;
			time = System.nanoTime();
			inDrag = true;
			return true;
        }*/
		//if (event.getAction() == MotionEvent.ACTION_DOWN) {
		//}
		
        LinkedList<AbstractSMILObject> q = OnScreen.Q().getQCopy();
        AbstractSMILObject o;
        AbstractSMILDrawable d;
        
        while (!q.isEmpty()) {
        	o = q.poll();
        	if (o instanceof AbstractSMILDrawable) {
        		// for every drawable object we have
        		d = (AbstractSMILDrawable) o;
        		if (d.checkCollision(x, y)) {
            		if (Waiting.isActive(d)) {
            			if (inCornerDrag || d.checkCornerCollision(x, y)) {	// if we're on the corner, process resize
	            			if (event.getAction() == MotionEvent.ACTION_DOWN && !inCornerDrag) {
	            				inCornerDrag = true;
	            				oldx = x;
	            				oldy = y;
	            				time = System.nanoTime();
	            			} else if (inCornerDrag) {
	            				d.moveSize(x, y);
	            				_playerCanvas.forceDraw();
	            				oldx = x;
	            				oldy = y;
	            				time = System.nanoTime();
	            			}
            			} else {	// if we're in the box, not on the corder, process drag
	            			if (event.getAction() == MotionEvent.ACTION_DOWN && !inDrag) {
	            				oldx = x;
	            				oldy = y;
	            				time = System.nanoTime();
	            				inDrag = true;
	            	        } else if (inDrag) {
	            				//Log.w("touch", "indrag!!!, moveLeft(" + (x-oldx) + "), moveTop(" + (y-oldy) + ")");
	            				d.moveLeftMargin(x-oldx);
	            				d.moveTopMargin(y-oldy);
	            				oldx = x;
	            				oldy = y;
	            				_playerCanvas.forceDraw();
	            				processedTouch = true;
	            			}
            			}
            		}	// need if for when touched element is inactive
        		}
        	}
        }
        
        try {
			Thread.sleep(16);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        if (processedTouch) return true;
        else return controlHider.onTouch(v, event);
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = getIntent();
    	_playerCanvas.setTime(intent.getIntExtra("startTime", 0));
    	
        if (intent.getBooleanExtra("reposition", false)) {
        	//playPressed = intent.getBooleanExtra("playPressed", false);
        	playPause(null, true, false);
			Log.w("composeract", "onCREATE DID IT!!");
        } else {
        	Waiting.deactivateAll();
        	playPause(null, true, false);
        }
        
        _playerCanvas.setOnTouchListener(this);
    }
    
	/**The "main" method of an android activity*/
    protected void onResume() {
        super.onResume();
        
        //playPause(null);
        
        findViewById(R.id.stop).setVisibility(View.VISIBLE);
        findViewById(R.id.stop).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ComposerActivity.this.finish();
			}
        });
    }
    
    public void playerCanvasPlay() {
    	Log.w("playercanvas", "called false");
    	_playerCanvas.play(false);
    }
}