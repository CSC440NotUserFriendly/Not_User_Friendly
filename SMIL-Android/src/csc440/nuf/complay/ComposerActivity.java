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

import java.util.ArrayList;
import java.util.LinkedList;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
	private final int ITEM_PRESSED_MILLIS = 200;
	private final int ITEM_LONG_PRESSED_MILLIS = 750;
	private int oldx, oldy, origx, origy;
	private float time;
	private boolean inDrag, inCornerDrag, itemPressed, itemLongPressed;
	private static AlertDialog.Builder alertAddItem;
	
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
		// this gives us deltaTime in milliseconds
		float deltaTime = (System.nanoTime()-time) / 1000000.0f;


    	if ((event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) &&
    		inDrag && deltaTime > ITEM_LONG_PRESSED_MILLIS &&
    		Math.abs(origx-x) + Math.abs(origy-y) < 10)
    		itemLongPressed = true;
    	
        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
        	if (inDrag && deltaTime < ITEM_PRESSED_MILLIS) itemPressed = true;
    		//Log.w("touchEvent", "deltaTime = " + deltaTime + ", itemPressed=" + itemPressed);
        }

		Log.w("touchEvent", "onDrag=" + inDrag + ", onCornerDrag=" + inCornerDrag + ", itemPressed=" + itemPressed);
		
        LinkedList<AbstractSMILObject> q = OnScreen.Q().getQCopy();
        AbstractSMILObject o;
        AbstractSMILDrawable d;
        boolean noCollision = true;
        
        while (!q.isEmpty()) {
        	o = q.poll();
        	if (o instanceof AbstractSMILDrawable) {
        		// for every drawable object we have
        		d = (AbstractSMILDrawable) o;
        		if (d.checkCollision(x, y)) {
        			noCollision = false;
        			processedTouch = true;
            		if (Waiting.isActive(d)) {
            			if (inCornerDrag || d.checkCornerCollision(x, y)) {	// if we're on the corner, process resize
	            			if (event.getAction() == MotionEvent.ACTION_DOWN && !inCornerDrag) {
	            				inCornerDrag = true;
	            				// currently resizing with multiple items selected isn't supported. deactivate other elements
	            				Waiting.deactivateAll();
	            				Waiting.activateElement(d);
	            				_playerCanvas.forceDraw();
	            			} else if (inCornerDrag) {
	            				d.moveSize(x, y);
	            				_playerCanvas.forceDraw();
	            			}
            			} else {	// if we're in the box, not on the corner, process drag
            				if (itemPressed) {
	            				Log.w("touchEvent", "active itemPressed");
	            				Waiting.deactivateElement(d);
	            				_playerCanvas.forceDraw();
	            				itemPressed = false;
	            				inDrag = false;
            				} else if (itemLongPressed) {
            					itemLongPressed = false;
            					inDrag = false;
            					// on a long press launch the item info
            					Intent i = new Intent(this, ComposerItem.class);
            	        		i.putExtra("editItem", Waiting.getElementId(d));
            	        		this.startActivity(i);
            				} else if (event.getAction() == MotionEvent.ACTION_DOWN && !inDrag) {
            					origx = x;
            					origy = y;
	            				oldx = x;
	            				oldy = y;
	            				time = System.nanoTime();
	            				inDrag = true;
	            	        } else if (inDrag) {
	            	        	ArrayList<AbstractSMILObject> activeElements = Waiting.getActiveElements();
	            	        	AbstractSMILDrawable activeDrawable;
        	        			Log.w("touch", "moveLeft " + (x-oldx) + ", moveTop " + (y-oldy) + ", active is drawable =" + (activeElements.get(0) instanceof AbstractSMILDrawable));
	            	        	while (!activeElements.isEmpty()) {
	            	        		if (activeElements.get(0) instanceof AbstractSMILDrawable) {
	            	        			activeDrawable = (AbstractSMILDrawable) activeElements.remove(0);
	            	        			Log.w("touch", "moved");
	            	        			activeDrawable.moveLeftMargin(x-oldx);
	            	        			activeDrawable.moveTopMargin(y-oldy);
	            	        		} else activeElements.remove(0);
	            	        	}
	            				//d.moveLeftMargin(x-oldx);
	            				//d.moveTopMargin(y-oldy);
	            				oldx = x;
	            				oldy = y;
	            				_playerCanvas.forceDraw();
	            			}
            			}
            		} else if (event.getAction() == MotionEvent.ACTION_DOWN && !itemPressed) {	// need if for when touched element is inactive
            			itemPressed = true;
            		} else if (event.getAction() == MotionEvent.ACTION_UP && itemPressed) {
            			Waiting.activateElement(d);
        				_playerCanvas.forceDraw();
        				itemPressed = false;
            		}
        		}
        	}
        }
        
        //Log.w("touchEvent", "noCollision is " + noCollision);
        if (noCollision) {
        	if (Waiting.isActiveEmpty()) processedTouch = false;
        	else if (!processedTouch && !inDrag && !inCornerDrag) {
        		Waiting.deactivateAll();
        		_playerCanvas.forceDraw();
        		processedTouch = true;	// it's still processing a touch here but it's not a bad thing...
        	}
        }

        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
        	inDrag = false;
        	inCornerDrag = false;
        }
        
        try {
			Thread.sleep(16);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        //Log.w("touch", "processedTouch = " + processedTouch);
        
        if (processedTouch) return true;
        else return controlHider.onTouch(v, event);
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = getIntent();
    	_playerCanvas.setTime(intent.getIntExtra("startTime", Waiting.getTime()));
    	
        if (intent.getBooleanExtra("reposition", false)) {
        	//playPressed = intent.getBooleanExtra("playPressed", false);
        	playPause(null, false);
        	if (intent.getIntExtra("itemId", -1) > -1) {
        		Waiting.deactivateAll();
        		Waiting.activateElement(intent.getIntExtra("itemId", -1));
            	_playerCanvas.setTime(Waiting.getActiveElementAt(0).getStartTime());
        	}
        } else {
        	Waiting.deactivateAll();
        	playPause(null, true);
        }
        
        findViewById(R.id.stop).setVisibility(View.VISIBLE);
        findViewById(R.id.stop).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ComposerActivity.this.finish();
			}
        });
        findViewById(R.id.addItem).setVisibility(View.VISIBLE);
        findViewById(R.id.addItem).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertAddItem.show();
			}
        });
        
		alertAddItem = new AlertDialog.Builder(this);
		alertAddItem.setTitle("Add Item");
		final CharSequence[] addItemOptions = {"Text", "Image"};
		alertAddItem.setItems(addItemOptions, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogInterface, int item) {
        		Intent i = new Intent(ComposerActivity.this, ComposerItem.class);
        		i.putExtra("newItem", addItemOptions[item]);
        		i.putExtra("startTime", Waiting.getTime());
        		i.putExtra("showOtherReposition", true);
        		ComposerActivity.this.startActivity(i);
        		//this.startActivityForResult(i, REQ_CODE_PLAYER);
            }
        });
        
        _playerCanvas.setOnTouchListener(this);
    }
    
	/**The "main" method of an android activity*/
    protected void onResume() {
        super.onResume();

        messageLength = Waiting.getMessageLength();
        _seekBar.setMax(messageLength);
    }
}