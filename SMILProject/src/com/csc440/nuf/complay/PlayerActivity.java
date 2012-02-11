package com.csc440.nuf.complay;

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
import com.csc440.nuf.R;
import com.csc440.nuf.R.layout;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;

public class PlayerActivity extends Activity implements SeekBar.OnSeekBarChangeListener {
    // private ActionBar _actionBar;
    private ImageView _playPause;
    private SeekBar _seekBar;
    private PlayerCanvas _playerCanvas;
    private boolean playing = true, playingCopy;
	
	/**The "main" method of an android activity*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // since we're using ActionBar, first take off the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.player_main);
        
        _playPause = (ImageView) findViewById(R.id.playPause);
        playPause(null, playing);
        
        _seekBar = (SeekBar) findViewById(R.id.seekBar);
        _seekBar.setOnSeekBarChangeListener(this);
        _playerCanvas = (PlayerCanvas) findViewById(R.id.canvas);
        
        
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
    	_playerCanvas.play();
    }
    
    protected void onPause() {
    	super.onResume();
    	_playerCanvas.pause();
    }
    
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
		playingCopy = playing;
		if (playing) {
			playPause(null, false);
		}
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		if (playingCopy) playPause(null, true);
	}
	
    public void playPause(View v) {
    	playPause(v, (playing ? false : true));
    }
    
    public void playPause(View v, boolean newPlaying) {
    	if (newPlaying) 
    		_playPause.setImageResource(R.drawable.pause);
    	else 
    		_playPause.setImageResource(R.drawable.play);
    	playing = newPlaying;
    }

    public void stop(View v) {
    	this.finish();
    }
}