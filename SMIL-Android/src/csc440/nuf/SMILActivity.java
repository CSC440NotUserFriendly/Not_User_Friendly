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

import java.util.Date;
import java.util.List;

import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

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
    private ProgressDialog prog;

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

        /*
        // for now we're manually making a WaitingQueue
        Waiting.Q().clear();
        OnScreen.Q().clear();
        OffScreen.Q().clear();
        SMILText[] t = new SMILText[4];
        t[0] = new SMILText(0, 5, 70, 70, "Hey", 40, "yellow");
        t[1] = new SMILText(2, 3, 150, 130, "this is a", 60, "white");
        t[2] = new SMILText(3, 7, 230, 210, "(: SMIL :)", 90, "red");
        t[3] = new SMILText(5, 5, 300, 300, "PRESENTATION!", 40, "blue");
        for (int i = 0; i < 4; i++) Waiting.Q().push(t[i]);
        Waiting.Q().prepQ();
        */
        
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

        items.addItem(getApplicationContext(), true);
        TextView item2Text1 = new TextView(this);
        item2Text1.setText("NEW! From: magicjj    Subject: Hey man!   Length: 0:20");
        item2Text1.setTextColor(Color.BLACK);
        TextView item2Text2 = new TextView(this);
        item2Text2.setText("From: magicjj    Subject: Test 2   Length: 0:15");
        item2Text2.setTextColor(Color.BLACK);
        TextView item2Text3 = new TextView(this);
        item2Text3.setText("From: magicjj    Subject: Test     Length: 0:17");
        item2Text3.setTextColor(Color.BLACK);
        items.addLine(getApplicationContext());
        items.addToLinear(item2Text1);
        items.addLine(getApplicationContext());
        items.addToLinear(item2Text2);
        items.addLine(getApplicationContext());
        items.addToLinear(item2Text3);
        items.addLine(getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = Util.getSharedPreferences(mContext);
        String connectionStatus = prefs.getString(Util.CONNECTION_STATUS, Util.DISCONNECTED);
        if (Util.DISCONNECTED.equals(connectionStatus)) {
            startActivity(new Intent(this, AccountsActivity.class));
        }
        AsyncFetchSMILMessage async = new AsyncFetchSMILMessage(this);
        wait = true;
        async.execute();
        
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
			i = new Intent(this, ComposerList.class);
			this.startActivity(i);
			break;
		case 2:	// Compose From Template
			i = new Intent(this, PlayerActivity.class);
			this.startActivity(i);
			break;
		case 3:	// View Inbox
			if(!wait){
				i = new Intent(this, InboxList.class);
				this.startActivity(i);
			}
			else{
				prog = ProgressDialog.show(this, "Communicating with the cloud.",
			            "Please wait until the inbox is retrieved.", true);

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

	public void setInbox(List<SMILMessageProxy> inbox) {
		SMILActivity.inbox = inbox;
		wait = false;
		if(prog != null && prog.isShowing()){
			prog.dismiss();
			onClick(findViewById(3));
		}
	}
}
