package csc440.nuf.complay;

/**
 * CSC-440 SMIL Project
 * 01-22-2011
 * SMILProjectActivity.java
 * @author Jacob Ensor
 * 
 * This activity shows a listview of the elements in your SMIL message. Pressing it spawns the element info activity.
 * Long pressing it spawns the composer in the mode to reposition that element.
 * There are also action bar buttons for adding items, playing, and sending the message.
 * 
 */


import java.util.ArrayList;

import csc440.nuf.ActionBar;
import csc440.nuf.R;
import csc440.nuf.components.AbstractSMILObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ComposerList extends Activity {
	private final int REQ_CODE_PLAYER = 1111;
	private static int startTime = 0;
	private Bundle state = null;
	private ActionBar _actionBar;
	private static boolean autoPlay = false;
	private static AlertDialog.Builder alertAddItem;
	private ListView list;
	
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
			public void onItemClick(AdapterView<?> arg0, View arg1, int selectedIndex,
					long arg3) {
        		Intent i = new Intent(ComposerList.this, ComposerItem.class);
        		i.putExtra("editItem", selectedIndex);
        		i.putExtra("showReposition", true);
        		ComposerList.this.startActivity(i);
			}
		});
		list.setLongClickable(true);
		list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int selectedIndex,
					long arg3) {
        		Intent i = new Intent(ComposerList.this, ComposerActivity.class);
        		i.putExtra("itemId", selectedIndex);
        		i.putExtra("reposition", true);
        		ComposerList.this.startActivityForResult(i, REQ_CODE_PLAYER);
        		return true;
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
        		Intent i = new Intent(ComposerList.this, ComposerItem.class);
        		i.putExtra("newItem", addItemOptions[item]);
        		i.putExtra("showReposition", true);
        		ComposerList.this.startActivity(i);
        		//this.startActivityForResult(i, REQ_CODE_PLAYER);
            }
        });
		
		_actionBar = (ActionBar) findViewById(R.id.actionBar);
        _actionBar.setTitle("Compose");
        _actionBar.setHomeListener(new OnClickListener() {
			public void onClick(View v) {
				ComposerList.this.finish();
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
        		Intent i = new Intent(ComposerList.this, ComposerSend.class);
        		ComposerList.this.startActivity(i);
				return;
			}
        });
    }
	
	protected void onResume() {
		super.onResume();
		refreshList();
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
			//playPressed = data.getBooleanExtra("playPressed", false);
			// Log.w("messageActivity", "start time saved: " + startTime);
		}
		
	}

	public void refreshList() {
		//Log.w("refreshList", "list size: " + array.size() + ", wQ size: " + Waiting.Q().getQ().size() + ", w size: " + w.size());
		list.setAdapter(new ComposerListAdapter(this, R.layout.composer_row, Waiting.allQArrayList()));
	}
	
	public void playMessage(boolean playPressed) {
		if (!autoPlay) playPressed = true;
		
		Intent i = new Intent(this, ComposerActivity.class);
		i.putExtra("bundle", state);
		i.putExtra("startTime", startTime);		// tells the player what second to start at
		i.putExtra("playPressed", playPressed);		// tells the player what second to start at
		this.startActivityForResult(i, REQ_CODE_PLAYER);
	}

	/*
	@Override
	public void onClick(View v) {
		Intent i = new Intent(this, ComposerItem.class);
		i.putExtra("editItem", v.getId());
		this.startActivity(i);
	}
	*/
	
	private class ComposerListAdapter extends ArrayAdapter<AbstractSMILObject> {
	        private ArrayList<AbstractSMILObject> items;
	
	        public ComposerListAdapter(Context context, int textViewResourceId, ArrayList<AbstractSMILObject> items) {
	                super(context, textViewResourceId, items);
	                this.items = items;
	        }
	
	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	                View v = convertView;
	                if (v == null) {
	                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                    v = vi.inflate(R.layout.composer_row, null);
	                }
	                AbstractSMILObject o = items.get(position);
	                //Log.w("getView", "o name: " + o.getName());
	                if (o != null) {
	                        TextView tt = (TextView) v.findViewById(R.id.toptext),
	                        	btl = (TextView) v.findViewById(R.id.bottomtextleft);
                        	//btr = (TextView) v.findViewById(R.id.bottomtextright),
	                        if (tt != null) {
	                              tt.setText("Name: " + o.getName());
	                        }
	                        /*
	                        if (btr != null) {
	                              btr.setText("Start: "+ o.getStartTime());
	                        }
	                        */
	                        if (btl != null) {
	                        	btl.setText("Start: " + o.getStartTime() + "      Duration: " + o.getDuration());
	                        }
	                }
	                return v;
	        }
	}
}