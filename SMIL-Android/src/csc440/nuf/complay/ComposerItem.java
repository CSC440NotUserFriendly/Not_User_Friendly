package csc440.nuf.complay;

/**
 * CSC-440 SMIL Project
 * 01-22-2011
 * SMILProjectActivity.java
 * @author Jacob Ensor
 * 
 * Shows a form to allow people to edit whatever item they selected
 * Most of the image picker code came from:
 * http://www.londatiga.net/it/how-to-create-android-image-picker/
 * 
 */



import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import csc440.nuf.ActionBar;
import csc440.nuf.R;
import csc440.nuf.ScrollItemManager;
import csc440.nuf.components.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
    private final int PICK_FROM_CAMERA = 1;
    private final int PICK_FROM_FILE = 2;
    private Uri mImageCaptureUri;
 
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
	private AlertDialog selectImageDialog;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//state = savedInstanceState;
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.composer_item);
		
        startTime = (EditText) findViewById(R.id.startTime);
        duration = (EditText) findViewById(R.id.duration);
        
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
				else if (itemType.equals("Image")){
					o = new SMILImage();
					((SMILImage)o).setWidth(100);
					((SMILImage)o).setHeight(100);
					
				}
				startTime.setText(String.valueOf(extras.getInt("startTime", 0)));
				duration.setText("3");
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
        	final String [] items           = new String [] {"From Camera", "From SD Card"};
            ArrayAdapter<String> adapter  = new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
            AlertDialog.Builder builder     = new AlertDialog.Builder(this);
     
            builder.setTitle("Select Image");
            builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
                public void onClick( DialogInterface dialog, int item ) {
                    if (item == 0) {
                        Intent intent    = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File file        = new File(Environment.getExternalStorageDirectory(),
                                            "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                        mImageCaptureUri = Uri.fromFile(file);
     
                        try {
                            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                            intent.putExtra("return-data", true);
                            startActivityForResult(intent, PICK_FROM_CAMERA);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
     
                        dialog.cancel();
                    } else {
                        Intent intent = new Intent();
     
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
     
                        startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                    }
                }
            } );
     
            selectImageDialog = builder.create();
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
        //} else {
    		//((SMILImage)o).setSrc(selectedFile.getText().toString());
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

	public void onClick(View v) {
		switch (v.getId()) {
		case 1:	// reposition button
			/*if (autoPlay)		we'll need this if we make this activity call the player when rotated
				alertAutoPlay.show();
			else */
			reposition();
			break;
		case 3: // reposition by back
			Waiting.activateElement(o);
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
	
	public void selectImage(View v) {
		selectImageDialog.show();
	}
	

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try{
        if (resultCode != RESULT_OK) return;
 
        //Bitmap bitmap   = null;
        String path     = "";
 
        if (requestCode == PICK_FROM_FILE) {
            mImageCaptureUri = data.getData();
            path = getRealPathFromURI(mImageCaptureUri); //from Gallery
            if (path == null)
                path = mImageCaptureUri.getPath(); //from File Manager
        } else {
            path    = mImageCaptureUri.getPath();
            
           // bitmap  = BitmapFactory.decodeFile(path);
        }
       
        File f = new File(path);
        String s = f.getName();
        if(ComposerList.filename != null){
        	System.out.println(ComposerList.filename);
        	String newPath = "/mnt/sdcard/SMIL/" + ComposerList.filename 
        			+ "/media/" + s;
        	File fnew = new File(newPath);
        	if(!fnew.exists())
        		fnew.createNewFile();
        	FileUtils.copyFile(f, fnew);
        	
        	//mImageView.setImageBitmap(bitmap);
            ((SMILImage)o).setSrc(newPath);
            selectedFile.setText(newPath);
        }
        else{
        	//mImageView.setImageBitmap(bitmap);
        	((SMILImage)o).setSrc(path);
        	selectedFile.setText(path);
        }
		}
		catch(Exception e){
			e.printStackTrace();
		}
    }
 
    public String getRealPathFromURI(Uri contentUri) {
        String [] proj      = {MediaStore.Images.Media.DATA};
        Cursor cursor       = managedQuery( contentUri, proj, null, null,null);
 
        if (cursor == null) return null;
 
        int column_index    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
 
        cursor.moveToFirst();
 
        return cursor.getString(column_index);
    }

    public void incStartTime(View v) {
    	setTextInt(startTime, 1);
    }
    
    public void decStartTime(View v) {
    	setTextInt(startTime, -1);
    }

    public void incDuration(View v) {
    	setTextInt(duration, 1);
    }
    
    public void decDuration(View v) {
    	setTextInt(duration, -1);
    }

    public void setTextInt(EditText t, int step) {
    	int i;
		try {
			i = Integer.parseInt(t.getText().toString());
		} catch (NumberFormatException e) {
			i = 0;
		}
		if (i + step < 0) step = 0;
		t.setText(String.valueOf(i + step));
    }
}