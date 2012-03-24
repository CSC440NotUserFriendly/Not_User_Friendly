package csc440.nuf.complay;

/**
 * CSC-440 SMIL Project
 * 2-2-2012
 * PlayerActivity.java
 * @author Jacob Ensor
 * 
 * 2-2-2012 
 * Edited by: Jacob Ensor
 * File Created
 */

//import com.csc440.nuf.ActionBar;

import csc440.nuf.R;
import csc440.nuf.components.SMILText;
import csc440.nuf.WaitingQueue;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.jar.Attributes;

public class PlayerActivity extends Activity implements SeekBar.OnSeekBarChangeListener {
    // private ActionBar _actionBar;
    private ImageView _playPause;
    private SeekBar _seekBar;
    private PlayerCanvas _playerCanvas;
    private TextView timeView;
    private RelativeLayout controls, mainView;
    private boolean playing, playingCopy, fromMessage = false, playPressed = true;
    private int messageLength;
    private HideControlsListener controlHider;
	
	/**The "main" method of an android activity*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = getIntent();
        if (intent.getBundleExtra("bundle") != null) {
        	savedInstanceState = intent.getBundleExtra("bundle");
        	fromMessage = true;
        	playPressed = intent.getBooleanExtra("playPressed", false);
        }
        
        // first take off the title bar, request landscape mode, and set fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (playPressed) this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        setContentView(R.layout.player_main);
        // when this activity is created we need to save the screen density for use in drawing
        WaitingQueue.setScreenDensity(getResources().getDisplayMetrics().density);
        
        // for now we're manually making a WaitingQueue
        while (!WaitingQueue.isEmpty()) WaitingQueue.pop();
        SMILText[] t = new SMILText[4];
        t[0] = new SMILText(0, 5, 70, 70, "Hey", 40, "yellow");
        t[1] = new SMILText(2, 3, 150, 130, "this is a", 60, "white");
        t[2] = new SMILText(3, 7, 230, 210, "(: SMIL :)", 90, "red");
        t[3] = new SMILText(5, 5, 300, 300, "PRESENTATION!", 40, "blue");
        for (int i = 0; i < 4; i++) WaitingQueue.push(t[i]);
        WaitingQueue.prepQ();
        
        // save the message length
        messageLength = WaitingQueue.getMessageLength();
        //Log.w("PlayerActivity", "getMessageLength is: " + messageLength);
        
        // initialize our variables
        _playPause = (ImageView) findViewById(R.id.playPause);
        _seekBar = (SeekBar) findViewById(R.id.seekBar);
        _seekBar.setOnSeekBarChangeListener(this);
        _seekBar.setMax(messageLength);
        _playerCanvas = (PlayerCanvas) findViewById(R.id.playerCanvas);
        _playerCanvas.setSeekBar(_seekBar);
        timeView = (TextView) findViewById(R.id.time);
        
        controls = (RelativeLayout) findViewById(R.id.controls);
        mainView = (RelativeLayout) findViewById(R.id.mainView);
        controlHider = new HideControlsListener(controls);
        mainView.setOnTouchListener(controlHider);
        _playerCanvas.setOnTouchListener(controlHider);
        
        timeView.setText(formatSecondsToMinutes(messageLength));
        
        if (fromMessage) 
        	_playerCanvas.setTime(intent.getIntExtra("startTime", 0));
        
        
        /*
        _actionBar = (ActionBar) findViewById(R.id.actionBar);
        _actionBar.setTitle("Message Activity");
        
        
        _actionBar.setHomeListener(new OnClickListener() {
			public void onClick(View v) {
        		PlayerActivity.this.finish();
			}
        });
        */
    }
    
    protected void onResume() {
    	super.onResume();
		if (!playPressed && 
			this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
    	    if (fromMessage) {
				Log.w("playerActivity", "startTime added: " + _seekBar.getProgress());
	    		Intent i = getIntent();
	    		if (_seekBar.getProgress() != messageLength) i.putExtra("startTime", _seekBar.getProgress());
	    		this.setResult(RESULT_OK, i);
    	    }
	    	this.finish();
		}
        playPause(null, true);
    }
    
    protected void onPause() {
    	super.onPause();
        playPause(null, false);
        if (playPressed) getIntent().putExtra("playPressed", playPressed);
    }
    
	public void onProgressChanged(SeekBar seekBar, int secondsIn, boolean fromUser) {
		controlHider.checkControls();
		
		timeView.setText(formatSecondsToMinutes(secondsIn));
    	if (secondsIn >= messageLength) playPause(null, false);
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
		playingCopy = playing;
		if (playing) {
			playPause(null, false);
		}
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		if (playingCopy) playPause(null, true);
		_playerCanvas.setTime(seekBar.getProgress());
	}
	
    public void playPause(View v) {
    	playPause(v, (playing ? false : true));
    }
    
    public void playPause(View v, boolean newPlaying) {
    	if (newPlaying != playing) {
	    	if (newPlaying) {
	    		// if they're pressing play when the time is at the end restart the video
	    		if (_seekBar.getProgress() == messageLength) _playerCanvas.setTime(0);
	    		_playPause.setImageResource(R.drawable.pause);
	        	_playerCanvas.play();
	    	} else {
	    		_playPause.setImageResource(R.drawable.play);
	        	_playerCanvas.pause();
	    	}
	    	playing = newPlaying;
	    	controlHider.setOverride(!newPlaying); // when the movie is done playing we want the controls shown by default
    	}
    }
    
    /* To get the stop button to work with the message view activity would be a pain in the ass that I'm not bothering with.
    public void stop(View v) {
    	this.finish();
    }
    */
    
    public String formatSecondsToMinutes(int secondsIn) {
    	String progress;
    	int minutesIn = secondsIn / 60;
		secondsIn = secondsIn % 60;
		progress = String.valueOf(minutesIn) + ":";
		if (secondsIn < 10) progress += "0";
		progress += String.valueOf(secondsIn);
		
		return progress;
    }

}