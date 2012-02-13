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
import com.csc440.nuf.components.SMILText;
import com.csc440.nuf.WaitingQueue;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.jar.Attributes;

public class PlayerActivity extends Activity implements SeekBar.OnSeekBarChangeListener {
    // private ActionBar _actionBar;
    private ImageView _playPause;
    private SeekBar _seekBar;
    private PlayerCanvas _playerCanvas;
    private TextView timeView;
    private boolean playing, playingCopy;
    private int messageLength;
	
	/**The "main" method of an android activity*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // since we're using ActionBar, first take off the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.player_main);
        
        /*
         * If we want to demo this in class we need to figure out a way to manually
         * construct things and add them to the queue here. Brad can you look into it? 
         * None of the methods I've tried have worked.
        Attributes att = new Attributes();
        att.put(new BasicAttribute("xml:id", 1));
        att.put("text", "this is some text!");
        att.put("textAlign", "left");
        att.put("top", "1");
        att.put("left", "1");
        att.put("textColor", "#FFFFFF");
        att.put("textFontSize", "20");
        att.put("z-index", "1");
        att.put("begin", "0");
        att.put("dur", "10");

        SMILText t = new SMILText((org.xml.sax.Attributes) att);
        WaitingQueue.push(t);
        
        messageLength = WaitingQueue.getLength();
        */
        
        messageLength = 10;
        
        _playPause = (ImageView) findViewById(R.id.playPause);
        _seekBar = (SeekBar) findViewById(R.id.seekBar);
        _seekBar.setOnSeekBarChangeListener(this);
        _seekBar.setMax(messageLength);
        _playerCanvas = (PlayerCanvas) findViewById(R.id.canvas);
        _playerCanvas.setSeekBar(_seekBar);
        timeView = (TextView) findViewById(R.id.time);
        
        
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
        playPause(null, true);
    }
    
    protected void onPause() {
    	super.onPause();
        playPause(null, false);
    }
    
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		timeView.setText(String.valueOf(progress));
    	if (progress >= messageLength) playPause(null, false);
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
    	if (newPlaying) {
    		_playPause.setImageResource(R.drawable.pause);
        	_playerCanvas.play();
    	} else {
    		_playPause.setImageResource(R.drawable.play);
        	_playerCanvas.pause();
    	}
    	playing = newPlaying;
    }
    
    public void stop(View v) {
    	this.finish();
    }
}