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

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlayerActivity extends Activity implements SeekBar.OnSeekBarChangeListener {
    // private ActionBar _actionBar;
    private ImageView _playPause;
    protected SeekBar _seekBar;
    protected PlayerCanvas _playerCanvas;
    private TextView timeView;
    private RelativeLayout controls, mainView;
    private boolean playing, playingCopy, fromMessage = false, playPressed = true;
    protected int messageLength;
    protected HideControlsListener controlHider;
	
	/**The "main" method of an android activity*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = getIntent();
        if (intent.getBundleExtra("bundle") != null) {
        	savedInstanceState = intent.getBundleExtra("bundle");	// honestly don't remember what this is for...
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
        
        // save the message length
        messageLength = Waiting.getMessageLength();
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
        
        if (fromMessage) {
        	_playerCanvas.setTime(intent.getIntExtra("startTime", 0));
        }
        
        
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

        _playerCanvas.play();
        
		if (!(this instanceof ComposerActivity)) {
			playPause(null, true);
		}
    }
    
    protected void onPause() {
    	super.onPause();
        playPause(null, false);
        _playerCanvas.pause();
		Log.w("playeract", "onpause DID IT!!");
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
    	Log.w("PlayPause", "playing = " + (newPlaying ? "true" : "false") + ", and v is " + (v == null ? "null" : "not null"));
    	if (newPlaying != playing) {
	    	if (newPlaying) {
	    		// if they're pressing play when the time is at the end restart the video
	    		if (_seekBar.getProgress() == messageLength) _playerCanvas.setTime(0);
	    		_playPause.setImageResource(R.drawable.pause);
	    		//_playerCanvas.play(runTime);
	    		//_playerCanvas.setRunTime(runTime);
	    	} else {
	    		_playPause.setImageResource(R.drawable.play);
	        	//_playerCanvas.pause();
	    		// need to handle play/pause and runtime properly. play/pause is starting and killing the thread, runtime doesn't
	    		//_playerCanvas.setRunTime(false);
	    	}
    		_playerCanvas.setRunTime(newPlaying);
	    	playing = newPlaying;
	    	/*if (_playerCanvas.getPauseAfterDraw()) {
	    		playing = false;
	    		_playPause.setImageResource(R.drawable.play);
	    	}*/
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