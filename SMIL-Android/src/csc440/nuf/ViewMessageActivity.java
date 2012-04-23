package csc440.nuf;

/**
 * CSC-440 SMIL Project
 * 01-22-2011
 * SMILProjectActivity.java
 * @author Brad Barker
 * 
 * Follow this format for all java files created. The header
 * should look exactly as above. Also include a detailed description
 * of the class here and finally anyone who makes changes to the code edit
 * as seen below.
 * 
 * 01-22-2011 
 * Edited by: Brad Barker
 * Added this comment.
 * 
 */


import csc440.nuf.cache.SMILCache;
import csc440.nuf.complay.PlayerActivity;
import csc440.nuf.complay.Waiting;
import csc440.nuf.shared.SMILMessageProxy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewMessageActivity extends Activity implements OnClickListener {
	private final int REQ_CODE_PLAYER = 1111;
	private static int startTime = 0;
	private ScrollItemManager items;
	private Bundle state = null;
	private Bundle data;
	private ActionBar _actionBar;
	private boolean playPressed = false;
	private static boolean autoPlay = true;
	private static AlertDialog alertAutoPlay;
	private static String workingDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SMIL";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		state = savedInstanceState;
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.message_view);
		
		SharedPreferences myPrefs = this.getSharedPreferences("username", MODE_WORLD_READABLE);
        autoPlay = myPrefs.getBoolean("autoPlay", true);
                
        alertAutoPlay = new AlertDialog.Builder(this).create();
		alertAutoPlay.setTitle("Did you know?");
		alertAutoPlay.setMessage("If you turn your phone into landscape mode the message will automatically play. Turn your phone back up to return to the message screen.");
		alertAutoPlay.setButton("Let me try!", new DialogInterface.OnClickListener() {  
		      public void onClick(DialogInterface dialog, int which) {  
		  			return;  
		      } 
		});
		alertAutoPlay.setButton2("Not for me! (disable feature)", new DialogInterface.OnClickListener() {  
		      public void onClick(DialogInterface dialog, int which) {  
		  			SharedPreferences.Editor myPrefEdit = ViewMessageActivity.this.getSharedPreferences("username", MODE_WORLD_READABLE).edit();
		  			myPrefEdit.putBoolean("autoPlay", false);
		  			myPrefEdit.commit();
		  			ViewMessageActivity.autoPlay = false;
		  			playMessage(true);
		  			return;  
		      } 
		});
		
		_actionBar = (ActionBar) findViewById(R.id.actionBar);
        _actionBar.setTitle("Message Details");
        _actionBar.setHomeListener(new OnClickListener() {
			public void onClick(View v) {
				ViewMessageActivity.this.finish();
			}
        });
        
        data = getIntent().getExtras();
        workingDir += "/" + data.getString("title");

        items = new ScrollItemManager(findViewById(R.id.scrollHolder));
        items.addItem(getApplicationContext(), true);
        items.setTitle(data.getString("title"));
        items.setIcon(R.drawable.ic_home);
        items.setListener(this, 1);
        TextView item1Text1 = new TextView(this);
        item1Text1.setText("From: " + data.getString("Sender") + "                   on " + data.getString("date"));
        item1Text1.setTextColor(Color.BLACK);
        items.addToLinear(item1Text1);
        
        items.addItem(getApplicationContext(), true);
        TextView item2Text1 = new TextView(this);
        item2Text1.setText("Subject:");
        item2Text1.setTextColor(Color.BLACK);
        TextView item2Text2 = new TextView(this);
        item2Text2.setText(data.getString("message"));
        item2Text2.setTextColor(Color.BLACK);
        items.addToLinear(item2Text1);
        items.addLine(getApplicationContext());
        items.addToLinear(item2Text2);

        items.addItem(getApplicationContext());
        items.setTitle("Play Message");
        items.setListener(this, 1);

        items.addItem(getApplicationContext());
        items.setTitle("Reply");
        //items.setListener(this, 1);

        items.addItem(getApplicationContext());
        items.setTitle("Forward");
        items.setListener(this, 2);

        items.addItem(getApplicationContext());
        items.setTitle("Reset Preference");
        items.setListener(this, 3);
    }
	
	protected void onResume() {
		super.onResume();
		// we're really going to need a user preference to override this, and have the PlayerActivity just request Landscape
		// otherwise people without accelerometers won't be able to get to the player
		if (autoPlay && !playPressed && 
			this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			playMessage(false);
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//Log.w("messageActivity", "activity result code: " + requestCode + ", RESULT_OK? " + (resultCode == RESULT_OK));
		if (requestCode == REQ_CODE_PLAYER && resultCode == RESULT_OK) {
			startTime = data.getIntExtra("startTime", 0);	// save what second the player was stopped at
			// at first I was just passing playPressed into the player so it knew not to kill the activity if the screen was in portrait.
			// needed it here to so I wouldn't keep relauching the player if the phone was in landscape. so I just pass the same value back in.
			playPressed = data.getBooleanExtra("playPressed", false);
			// Log.w("messageActivity", "start time saved: " + startTime);
		}
		
	}
	
	public void playMessage(boolean playPressed) {
		if (!autoPlay) playPressed = true;
		
		SMILMessageProxy sm = SMILActivity.inbox.get(data.getInt("index"));
		SMILCache.getFile(sm);
		Waiting.Q().prepQ();
		
		Intent i = new Intent(this, PlayerActivity.class);
		i.putExtra("bundle", state);
		i.putExtra("startTime", startTime);		// tells the player what second to start at
		i.putExtra("playPressed", playPressed);		// tells the player what second to start at
		this.startActivityForResult(i, REQ_CODE_PLAYER);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case 1:	// Play Button
			if (autoPlay)
				alertAutoPlay.show();
			else playMessage(true);
			break;
			
		case 2:
			break;
			
		case 3:	// Reset Preference Button
			SharedPreferences.Editor myPrefEdit = this.getSharedPreferences("username", MODE_WORLD_READABLE).edit();
  			myPrefEdit.putBoolean("autoPlay", true);
  			myPrefEdit.commit();
  			autoPlay = true;
			break;
		} 
	}
	public static String getDir(){
		return workingDir;
	}
}