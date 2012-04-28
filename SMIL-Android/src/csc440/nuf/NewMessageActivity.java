package csc440.nuf;

import java.util.Date;
import java.util.List;

import csc440.nuf.cache.SMILCache;
import csc440.nuf.complay.ComposerActivity;
import csc440.nuf.complay.ComposerList;
import csc440.nuf.complay.Waiting;
import csc440.nuf.shared.SMILMessageProxy;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ViewFlipper;

public class NewMessageActivity extends Activity implements OnClickListener{

	private ActionBar _actionBar;
	private EditText title, recipient, message;
	private ViewFlipper flippy;
	private SMILMessageProxy sm;
	private final int REQ_CODE = 42;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_message);
		
		_actionBar = (ActionBar) findViewById(R.id.actionBar);
        _actionBar.setTitle("New Message");
        _actionBar.setHomeLogo(R.drawable.ic_launcher);
        
        title = (EditText)findViewById(R.id.title);
        recipient = (EditText)findViewById(R.id.recipient);
        message = (EditText)findViewById(R.id.message);
        flippy = (ViewFlipper) findViewById(R.id.flipedit);
        
        
        if(getIntent().getExtras().getBoolean("isNew"))
        	flippy.setDisplayedChild(1);
        else{
        	flippy.setDisplayedChild(0);
        	sm = SMILActivity.outbox.get(getIntent().getExtras().getInt("index"));
        	title.setText(sm.getFilename());
        	recipient.setText(sm.getRecipient());
        	message.setText(sm.getMessage());
        }
        
	}

	@Override
	public void onClick(View v) {
		System.out.println(v.getId());
		
		if(v.getId() == R.id.create){
			AsyncFetchSMILMessage async = new AsyncFetchSMILMessage(this);
			async.execute(new Long(1));
		}
		else if(v.getId() == R.id.edit){
			AsyncFetchSMILMessage async = new AsyncFetchSMILMessage(this);
			SMILMessageProxy sm2 = async.editMessage(sm);
			sm2.setFilename(title.getText().toString());
			sm2.setRecipient(recipient.getText().toString());
			sm2.setMessage(message.getText().toString());
			async.updateMessage(sm2);
			openComposer(sm);
		}
		
		else if(v.getId() == R.id.send){
			
		}
	}

	public void addMessages(SMILMessageProxy result) {
		AsyncFetchSMILMessage async = new AsyncFetchSMILMessage(this);
		sm = async.editMessage(result);
		sm.setFilename(title.getText().toString());
		sm.setSender(SMILActivity.getAccount());
		sm.setRecipient(recipient.getText().toString());
		sm.setMessage(message.getText().toString());
		sm.setCreated(new Date());
		sm.setModified(new Date());
		async.updateNewMessage(sm);
		
	}

	public void openComposer(SMILMessageProxy sm) {
		SMILCache.getFile(sm);
		Intent i = new Intent(this, ComposerList.class);
		i.putExtra("filename", sm.getFilename());
		this.startActivityForResult(i, 42);
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
			System.out.println("!!Saving!!");
			System.out.println("QueueF: " + Waiting.allQArrayList().size());
			if(sm != null){
				System.out.println("FileName: " + sm.getFilename());
			}
			else{
				System.out.println("Null message!");
			}
			SMILCache.updateFile(sm);
			System.out.println("QueueE: " + Waiting.allQArrayList().size());
		}
		
	}

	
}
