package csc440.nuf.complay;

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



import csc440.nuf.ActionBar;
import csc440.nuf.R;
import csc440.nuf.ScrollItemManager;
import csc440.nuf.components.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.Spinner;

public class ComposerItem extends Activity implements OnClickListener {
	private final int REQ_CODE_PLAYER = 1111;
	private ScrollItemManager items;
	//private Bundle state = null;
	private ActionBar _actionBar;
	//private boolean playPressed = false;
	private int action;
	private String itemType;
	//private static boolean autoPlay = true;
	//private static AlertDialog alertAutoPlay;
	private AbstractSMILObject o;
	private EditText name, startTime, duration;
	private TextView selectedFile;
	private Spinner color;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//state = savedInstanceState;
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.composer_item);
		
        startTime = (EditText) findViewById(R.id.startTime);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// an action of -1 indicates a new item. A different action indicates the ID of the item being edited
			itemType = extras.getString("newItem");
			action = extras.getInt("editItem", -1);
			if (action >= 0) {
				o = Waiting.getElementAtId(action);
				itemType = o.getName();
			} else {
				
				// if it's a new item, create it, add it to the Q and re-sort
				if (itemType.equals("Text"))
					o = new SMILText();
				else if (itemType.equals("Image"))
					o = new SMILImage(getResources());
				startTime.setText(String.valueOf(extras.getInt("startTime", 0)));
				Timer timer = new Timer();
				Waiting.Q().push(o);
				Waiting.Q().prepQ();
				timer.setTime(Waiting.getTime());
			}
		}
		
		/*
		SharedPreferences myPrefs = this.getSharedPreferences("username", MODE_WORLD_READABLE);
        autoPlay = myPrefs.getBoolean("autoPlay", true);
        */
		
		_actionBar = (ActionBar) findViewById(R.id.actionBar);
		String title;
		if (action < 0) title = "Adding ";
		else title = "Editing ";
		title += itemType;
        _actionBar.setTitle(title);
        _actionBar.setHomeListener(new OnClickListener() {
			public void onClick(View v) {
				ComposerItem.this.finish();
			}
        });

        items = new ScrollItemManager(findViewById(R.id.scrollHolder));
        if (extras.getBoolean("showReposition", false)) {
	        items.addItem(getApplicationContext());
	        items.setTitle("Reposition");
	        items.setListener(this, 1);
        }
        if (extras.getBoolean("showOtherReposition", false)) {
	        items.addItem(getApplicationContext());
	        items.setTitle("Back to Reposition");
	        items.setListener(this, 3);
        }
        
        items.addItem(getApplicationContext());
        items.setTitle("Delete");
        items.setListener(this, 2);

        ViewFlipper flippy = (ViewFlipper) findViewById(R.id.flipProperties);
        name = (EditText) findViewById(R.id.name);
        duration = (EditText) findViewById(R.id.duration);
        
        if (action >= 0) {	// if we're editing an item
	        name.setText(o.getName());
	        startTime.setText(o.getStartTime() != -1 ? String.valueOf(o.getStartTime()) : "");
	        duration.setText(String.valueOf(o.getDuration()));
        }

        if (o instanceof SMILText) {
        	color = (Spinner) findViewById(R.id.color);
        	String[] colorList = getResources().getStringArray(R.array.supported_colors);
        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colorList);
        	color.setAdapter(adapter);
        	if (action >= 0) {	// if we're editing an item, set the color on the spinner
        		String startingColor = ((SMILText)o).getTextColor();
        		for (int i = 0; i < colorList.length; i++) if (colorList[i].equals(startingColor)) color.setSelection(i);
        	}
        	flippy.setDisplayedChild(0);
        } else {
        	selectedFile = (TextView) findViewById(R.id.selectedFile);
        	// if we're editing, set selectedFile
        	if (action >= 0) selectedFile.setText(((SMILImage)o).getSrc());
        	flippy.setDisplayedChild(1);
        }
    }
	
	protected void onResume() {
		super.onResume();
		
		// we're really going to need a user preference to override this, and have the PlayerActivity just request Landscape
		// otherwise people without accelerometers won't be able to get to the player
		/*
		if (autoPlay && !playPressed && 
			this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			playMessage(false);
		}
		*/
	}
	
	protected void onPause() {
		super.onPause();

		o.setName(name.getText().toString());
		try {
			o.setStartTime(Integer.parseInt(startTime.getText().toString()));
		} catch (NumberFormatException e) {
			o.setStartTime(-1);
		}
		try {
			o.setDuration(Integer.parseInt(duration.getText().toString()));
		} catch (NumberFormatException e) {
			o.setDuration(0);
		}

        if (o instanceof SMILText) {
    		((SMILText)o).setText(name.getText().toString());
    		((SMILText)o).setTextColor((String)color.getSelectedItem());
        } else {
    		((SMILImage)o).setSrc(selectedFile.getText().toString());
        }
	}
	
	public void reposition() {
		Waiting.deactivateAll();
		Waiting.activateElement(o);
		Intent i = new Intent(this, ComposerActivity.class);
		try {
			i.putExtra("startTime", Integer.parseInt(startTime.getText().toString()));
		} catch (NumberFormatException e) {
			i.putExtra("startTime", 0);
		}
		i.putExtra("reposition", true);
		this.startActivityForResult(i, REQ_CODE_PLAYER);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case 1:	// reposition button
			/*if (autoPlay)		we'll need this if we make this activity call the player when rotated
				alertAutoPlay.show();
			else */
			reposition();
			break;
		case 3: // reposition by back
			finish();
			break;
		case 2: // delete button
			// should add in an alert here
			Timer timer = new Timer();
			Waiting.Q().getQ().remove(o);
			timer.setTime(Waiting.getTime());
			finish();
			break;
		/*
		case 2:	// Reset Preference Button
			SharedPreferences.Editor myPrefEdit = this.getSharedPreferences("username", MODE_WORLD_READABLE).edit();
  			myPrefEdit.putBoolean("autoPlay", true);
  			myPrefEdit.commit();
  			autoPlay = true;
			break;
		*/
		} 
	}
	

}