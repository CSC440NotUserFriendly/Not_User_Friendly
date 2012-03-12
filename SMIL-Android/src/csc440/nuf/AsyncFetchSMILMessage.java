package csc440.nuf;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

import csc440.nuf.shared.SMILMessageRequestFactory;
import csc440.nuf.shared.SMILMessageProxy;
import csc440.nuf.SMILActivity;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class AsyncFetchSMILMessage extends AsyncTask<Long, Void, List<SMILMessageProxy>> {

    private final SMILActivity activity;
    private boolean newTask = false;

    public AsyncFetchSMILMessage(SMILActivity activity) {
        super();
        this.activity = activity;
    }

    @Override
    protected List<SMILMessageProxy> doInBackground(Long... arguments) {
        final List<SMILMessageProxy> list = new ArrayList<SMILMessageProxy>();

        // INSERT RPC HERE!
        SMILMessageRequestFactory factory = Util.getRequestFactory(activity,
        		SMILMessageRequestFactory.class);
        
        if (arguments.length == 0 || arguments[0] == -1) {
	        factory.SMILEngineRequest().querySMILMessages().fire(new Receiver<List<SMILMessageProxy>>() {
				@Override
				public void onSuccess(List<SMILMessageProxy> arg0) {
					list.addAll(arg0);
				}
	        });
        } else {
        	newTask = true;
        	factory.SMILEngineRequest().readSMILMessage(arguments[0]).fire(new Receiver<SMILMessageProxy>() {

				@Override
				public void onSuccess(SMILMessageProxy arg0) {
					list.add(arg0);
				}
			});
        }

        return list;
    }

    @Override
    protected void onPostExecute(List<SMILMessageProxy> result) {
    	if (newTask) {
    		//activity.addMessages(result);
    	} else {
    		//activity.setMessages(result);
    	}
    }
}
