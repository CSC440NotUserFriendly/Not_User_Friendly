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


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import csc440.nuf.ActionBar;
import csc440.nuf.ScrollItemManager;
import csc440.nuf.complay.ComposerSend;
import csc440.nuf.complay.OffScreen;
import csc440.nuf.complay.OnScreen;
import csc440.nuf.complay.PlayerActivity;
import csc440.nuf.complay.Waiting;
import csc440.nuf.components.AbstractSMILObject;
import csc440.nuf.components.SMILText;
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
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OutboxList extends Activity implements OnClickListener {
	private final int REQ_CODE_PLAYER = 1111;
	private static int startTime = 0;
	private ScrollItemManager items;
	private Bundle state = null;
	private ActionBar _actionBar;
	private boolean playPressed = false;
	private static boolean autoPlay = false;
	private static AlertDialog.Builder alertAddItem;
	private ListView list;
	private List<SMILMessageProxy> inbox;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		state = savedInstanceState;
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.composer_list);
		
		/*
		SharedPreferences myPrefs = this.getSharedPreferences("username", MODE_WORLD_READABLE);
        autoPlay = myPrefs.getBoolean("autoPlay", true);
		 */
		
		list = (ListView) findViewById(R.id.list);
		list.setClickable(true);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				SMILMessageProxy sm = inbox.get(arg2);
				System.err.println(sm.getFilename());
        		Intent i = new Intent(OutboxList.this, ViewMessageActivity.class);
        		i.putExtra("title", sm.getFilename());
        		i.putExtra("message", sm.getMessage());
        		i.putExtra("sender", sm.getSender());
        		i.putExtra("index", arg2);
        		
        		System.out.println(sm.getModified());
        		i.putExtra("date", new SimpleDateFormat("MM/dd/yyyy").format(sm.getModified()));
        		i.putExtra("key", sm.getKey());
        		OutboxList.this.startActivity(i);
				
			}
		});
		
        /*
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
		  			SharedPreferences.Editor myPrefEdit = ComposerList.this.getSharedPreferences("username", MODE_WORLD_READABLE).edit();
		  			myPrefEdit.putBoolean("autoPlay", false);
		  			myPrefEdit.commit();
		  			ComposerList.autoPlay = false;
		  			playMessage(true);
		  			return;  
		      } 
		});
		*/
		alertAddItem = new AlertDialog.Builder(this);
		alertAddItem.setTitle("Add Item");
		final CharSequence[] addItemOptions = {"Text", "Image"};
		alertAddItem.setItems(addItemOptions, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogInterface, int item) {
            	System.out.println("Item num: " + item);
        		//Intent i = new Intent(InboxList.this, ViewMessageActivity.class);
        		//i.putExtra("newItem", addItemOptions[item]);
        		//InboxList.this.startActivity(i);
        		//this.startActivityForResult(i, REQ_CODE_PLAYER);
            }
        });
		
		_actionBar = (ActionBar) findViewById(R.id.actionBar);
        _actionBar.setTitle("Outbox");
        _actionBar.setHomeListener(new OnClickListener() {
			public void onClick(View v) {
				OutboxList.this.finish();
			}
        });
        _actionBar.addActionIcon(R.drawable.ic_list, new OnClickListener() {
			public void onClick(View v) {
				alertAddItem.show();
			}
        });
        _actionBar.addActionIcon(R.drawable.ic_movie, new OnClickListener() {
			public void onClick(View v) {
				playMessage(true);
				return;
			}
        });
        _actionBar.addActionIcon(R.drawable.ic_send, new OnClickListener() {
			public void onClick(View v) {
        		Intent i = new Intent(OutboxList.this, ComposerSend.class);
        		OutboxList.this.startActivity(i);
				return;
			}
        });

        /*
        items = new ScrollItemManager(findViewById(R.id.scrollHolder));

        items.addItem(getApplicationContext());
        items.setTitle("Text Item 1");
        items.setListener(this, 1);
        */

    }
	
	protected void onResume() {
		super.onResume();
		inbox = SMILActivity.inbox;
		refreshList();
		System.out.println("Get Messages!");
		
		// we're really going to need a user preference to override this, and have the PlayerActivity just request Landscape
		// otherwise people without accelerometers won't be able to get to the player
		/*
		if (autoPlay && !playPressed && 
			this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			playMessage(false);
		}
		*/
		
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

	public void refreshList() {
		System.out.println(Setup.SENDER_ID);
		List<SMILMessageProxy> in = new ArrayList<SMILMessageProxy>();
		System.out.println("Size: " + inbox.size());
		for(int i=0; i<inbox.size(); i++){ //inbox recipient==
			System.err.println(inbox.get(i).getSender() + " : " + Setup.SENDER_ID);
			if(inbox.get(i).getSender().equals(Setup.SENDER_ID)) 
				in.add(inbox.get(i));
		}
		System.out.println(in.toString());
		
		//Log.w("refreshList", "list size: " + array.size() + ", wQ size: " + Waiting.Q().getQ().size() + ", w size: " + w.size());
		list.setAdapter(new InboxListAdapter(this, R.layout.composer_row, in));
	}
	
	public void playMessage(boolean playPressed) {
		if (!autoPlay) playPressed = true;
		
		Intent i = new Intent(this, PlayerActivity.class);
		i.putExtra("bundle", state);
		i.putExtra("startTime", startTime);		// tells the player what second to start at
		i.putExtra("playPressed", playPressed);		// tells the player what second to start at
		this.startActivityForResult(i, REQ_CODE_PLAYER);
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent(this, InboxItem.class);
		i.putExtra("editItem", v.getId());
		this.startActivity(i);
	}
	
	public void addMessages(List<SMILMessageProxy> result) {
		inbox = result;
		refreshList();
		
	}
	
	private class InboxListAdapter extends ArrayAdapter<SMILMessageProxy> {

			private List<SMILMessageProxy> inbox;
	
	        public InboxListAdapter(Context context, int textViewResourceId, List<SMILMessageProxy> inbox) {
	                super(context, textViewResourceId, inbox);
	                this.inbox = inbox;
	        }
	
	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	                View v = convertView;
	                if (v == null) {
	                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                    v = vi.inflate(R.layout.inbox_row, null);
	                }
	                SMILMessageProxy o = inbox.get(position);
	                //Log.w("getView", "o name: " + o.getName()); wtf is this
	                if (o != null) {
	                        TextView ttl = (TextView) v.findViewById(R.id.topinboxtextL),
	                        		ttr = (TextView) v.findViewById(R.id.topinboxtextR),
	                        	middle = (TextView) v.findViewById(R.id.middleinboxtext),
	                        	bottom = (TextView) v.findViewById(R.id.bottominboxtext);
	                        	
	                        	
                        	//btr = (TextView) v.findViewById(R.id.bottomtextright),
	                        if (ttl != null) {
	                              ttl.setText("Name: " + o.getFilename());
	                              
	                        }
	                        if (ttr != null) {
	                        	if(o.getModified() != null){
	                              ttr.setText("Date: " + String.valueOf(o.getModified().getDay()) + "/" + String.valueOf(o.getModified().getMonth()));
	                        	}
		                        else{
		                        	ttr.setText("Date: ##/##");
		                        }
	                        }
	                        
	                        if (middle != null) {
	                        	middle.setText("Sender: " + o.getSender());
	                        }
	                        if (bottom != null) {
	                        	bottom.setText("Subject: " + o.getMessage());
	                        }
	                        
	                        if(!o.isRead()){
                          	  ttl.setTypeface(null, Typeface.BOLD);
                          	  ttr.setTypeface(null, Typeface.BOLD);
                          	  middle.setTypeface(null, Typeface.BOLD);
                          	  bottom.setTypeface(null, Typeface.BOLD);
	                        }
	              
	                }
	                return v;
	        }
	}
}