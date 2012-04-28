/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package csc440.nuf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import csc440.nuf.cache.SMILCache;
import csc440.nuf.client.MyRequestFactory;
import csc440.nuf.client.MyRequestFactory.HelloWorldRequest;
import csc440.nuf.complay.*;
import csc440.nuf.components.SMILText;
import csc440.nuf.shared.SMILMessageProxy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Main activity - requests "Hello, World" messages from the server and provides
 * a menu item to invoke the accounts activity.
 */
public class SMILActivity extends Activity implements OnClickListener {
    /**
     * Tag for logging.
     */
    private static final String TAG = "SMILActivity";
    private ActionBar _actionBar;
    private ScrollItemManager items;
    public static boolean wait; //Wait for download
    public static List<SMILMessageProxy> inbox;
    public static List<SMILMessageProxy> outbox;
    private ProgressDialog prog, prog2;
    private static String account; //registered gmail account

    /**
     * The current context.
     */
    private Context mContext = this;

    /**
     * A {@link BroadcastReceiver} to receive the response from a register or
     * unregister request, and to update the UI.
     */
    private final BroadcastReceiver mUpdateUIReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String accountName = intent.getStringExtra(DeviceRegistrar.ACCOUNT_NAME_EXTRA);
            int status = intent.getIntExtra(DeviceRegistrar.STATUS_EXTRA,
                    DeviceRegistrar.ERROR_STATUS);
            String message = null;
            String connectionStatus = Util.DISCONNECTED;
            if (status == DeviceRegistrar.REGISTERED_STATUS) {
                message = getResources().getString(R.string.registration_succeeded);
                connectionStatus = Util.CONNECTED;
            } else if (status == DeviceRegistrar.UNREGISTERED_STATUS) {
                message = getResources().getString(R.string.unregistration_succeeded);
            } else {
                message = getResources().getString(R.string.registration_error);
            }

            // Set connection status
            SharedPreferences prefs = Util.getSharedPreferences(mContext);
            prefs.edit().putString(Util.CONNECTION_STATUS, connectionStatus).commit();

            // Display a notification
            Util.generateNotification(mContext, String.format(message, accountName));
        }
    };

    /**
     * Begins the activity.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        //Get the inbox
        wait = true; //Block until inbox is received
        new SMILCache(this); //Give smilcache a reference to this
        
        final SharedPreferences prefs = Util.getSharedPreferences(mContext);
        account = prefs.getString(Util.ACCOUNT_NAME, "Unknown");
        
        // Register a receiver to provide register/unregister notifications
        registerReceiver(mUpdateUIReceiver, new IntentFilter(Util.UPDATE_UI_INTENT));
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.main);

        
        _actionBar = (ActionBar) findViewById(R.id.actionBar);
        _actionBar.setTitle(R.string.app_name);
        _actionBar.setHomeLogo(R.drawable.ic_launcher);

        items = new ScrollItemManager(findViewById(R.id.scrollHolder));
        
        items.addItem(getApplicationContext()); 
        items.setTitle("Compose Message");
        items.setIcon(R.drawable.ic_home);
        items.setListener(this, 1);

        items.addItem(getApplicationContext());
        items.setTitle("Compose From Template");
        items.setIcon(R.drawable.ic_home);
        items.setListener(this, 2);

        items.addItem(getApplicationContext());
        items.setTitle("View Inbox");
        items.setIcon(R.drawable.inbox);
        items.setListener(this, 3);

        items.addItem(getApplicationContext());
        items.setTitle("View Outbox");
        items.setIcon(R.drawable.inbox);
        items.setListener(this, 4);
    }

    @Override
    public void onResume() {
        super.onResume();
        wait = true;
        Waiting.Q().clear();
        OnScreen.Q().clear();
        OffScreen.Q().clear();
        
        SharedPreferences prefs = Util.getSharedPreferences(mContext);
        String connectionStatus = prefs.getString(Util.CONNECTION_STATUS, Util.DISCONNECTED);
        if (Util.DISCONNECTED.equals(connectionStatus)) {
            startActivity(new Intent(this, AccountsActivity.class));
        }
        else{
        	AsyncFetchSMILMessage async = new AsyncFetchSMILMessage(this);
        	
        	async.execute();
        }
        
     // Our stuff from our original main activity   
     // since we're using ActionBar, first take off the title bar
        
        
        
        /*
        _actionBar.setHomeListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(SMILProjectActivity.this, PlayerActivity.class);
				SMILProjectActivity.this.startActivity(i);
			}
        });
        */
        
        
        //Use this to test app engine connection;
        //setScreenContent(R.layout.hello_world);
        
    }

	@Override
	public void onClick(View v) {
		Intent i;
		
		switch (v.getId()) {
		case 1:	// Compose Message
			i = new Intent(this, NewMessageActivity.class);
			i.putExtra("isNew", true);
			this.startActivity(i);
			break;
		case 2:	// Compose From Template
			i = new Intent(this, PlayerActivity.class);
			this.startActivity(i);
			break;
		case 3:	// View Inbox
			if(!wait){
				i = new Intent(this, InboxList.class);
				i.putExtra("isInbox", true);
				this.startActivity(i);
			}
			else{
				prog = ProgressDialog.show(this, "Communicating with the cloud.",
			            "Please wait until the inbox is retrieved.", true);
			}
			break;
		case 4:
			if(!wait){ //Outbox
				i = new Intent(this, InboxList.class);
				i.putExtra("isInbox", false);
				this.startActivity(i);
			}
			else{
				prog2 = ProgressDialog.show(this, "Communicating with the cloud.",
			            "Please wait until the outbox is retrieved.", true);
			}
		} 
	}
	
    /**
     * Shuts down the activity.
     */
    @Override
    public void onDestroy() {
        unregisterReceiver(mUpdateUIReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        // Invoke the Register activity
        menu.getItem(0).setIntent(new Intent(this, AccountsActivity.class));
        return true;
    }

    // Manage UI Screens
    private void setHelloWorldScreenContent() {
        setContentView(R.layout.hello_world);

        final TextView helloWorld = (TextView) findViewById(R.id.hello_world);
        final Button sayHelloButton = (Button) findViewById(R.id.say_hello);
        sayHelloButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                sayHelloButton.setEnabled(false);
                helloWorld.setText(R.string.contacting_server);

                // Use an AsyncTask to avoid blocking the UI thread
                new AsyncTask<Void, Void, String>() {
                    private String message;

                    @Override
                    protected String doInBackground(Void... arg0) {
                        MyRequestFactory requestFactory = Util.getRequestFactory(mContext,
                                MyRequestFactory.class);
                        final HelloWorldRequest request = requestFactory.helloWorldRequest();
                        Log.i(TAG, "Sending request to server");
                        request.getMessage().fire(new Receiver<String>() {
                            @Override
                            public void onFailure(ServerFailure error) {
                                message = "Failure: " + error.getMessage();
                            }

                            @Override
                            public void onSuccess(String result) {
                                message = result;
                            }
                        });
                        return message;
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        helloWorld.setText(result);
                        sayHelloButton.setEnabled(true);
                    }
                }.execute();
            }
        });
    }
	
    /**
     * Sets the screen content based on the screen id.
     */
    private void setScreenContent(int screenId) {
        setContentView(screenId);
        switch (screenId) {
            case R.layout.hello_world:
                setHelloWorldScreenContent();
                break;
        }
    }

	public void addMessages(List<SMILMessageProxy> result) {

		setInbox(result);
		//wait = false;
		
	}

	public List<SMILMessageProxy> getInbox() {
		return inbox;
	}

	public void setInbox(List<SMILMessageProxy> in) {
		SMILActivity.inbox = new ArrayList<SMILMessageProxy>();
		SMILActivity.outbox = new ArrayList<SMILMessageProxy>();
		for(int i=0; i<in.size(); i++){ //inbox recipient==
			if(in.get(i).getRecipient().equals(account)) 
				inbox.add(in.get(i)); //Recipient = you
			if(in.get(i).getSender().equals(account))
				outbox.add(in.get(i)); //Sender = you
		}
		
		
		wait = false;
		if(prog != null && prog.isShowing()){
			prog.dismiss();
			onClick(findViewById(3));
		}
		if(prog2 != null && prog2.isShowing()){
			prog2.dismiss();
			onClick(findViewById(4));
		}
	}
	public static String getAccount()
	{
		return account;
	}
}
