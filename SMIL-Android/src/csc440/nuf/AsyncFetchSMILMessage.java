package csc440.nuf;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import csc440.nuf.cache.SMILCache;
import csc440.nuf.shared.SMILEngineRequest;
import csc440.nuf.shared.SMILMessageRequestFactory;
import csc440.nuf.shared.SMILMessageProxy;
import csc440.nuf.SMILActivity;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class AsyncFetchSMILMessage extends AsyncTask<Long, Void, List<SMILMessageProxy>> {

    private final Activity activity;
    private int task;
    SMILMessageProxy temp;
    SMILEngineRequest req;

    SMILMessageRequestFactory factory;
    
    public AsyncFetchSMILMessage(Activity activity) {
        super();
        this.activity = activity;
        
        factory = Util.getRequestFactory(activity,
        		SMILMessageRequestFactory.class);
        
        
        
    }
    public SMILMessageProxy editMessage(SMILMessageProxy sm){
    	req = factory.SMILEngineRequest();
    	temp = req.edit(sm);
    	
    	return temp;
    }
    public void updateMessage(SMILMessageProxy sm){
    	temp = sm;
    	this.execute(new Long(2));
    }
    public void updateNewMessage(SMILMessageProxy sm){
    	temp = sm;
    	this.execute(new Long(3));
    }

    @Override
    protected List<SMILMessageProxy> doInBackground(Long... arguments) {
        final List<SMILMessageProxy> list = new ArrayList<SMILMessageProxy>();

        // INSERT RPC HERE!
        
        if (arguments.length == 0 || arguments[0] == -1) {
        	task = 1;
        	factory.SMILEngineRequest().querySMILMessages().fire(new Receiver<List<SMILMessageProxy>>() {
				@Override
				public void onSuccess(List<SMILMessageProxy> arg0) {
					System.out.println("Messages Recieved!");
					list.addAll(arg0);
				}
	        });
        } else if(arguments[0] == 1){
        	task = 2;
        	factory.SMILEngineRequest().createSMILMessage().fire(new Receiver<SMILMessageProxy>() {

				@Override
				public void onSuccess(SMILMessageProxy arg0) {
					System.err.println("CreateSuccess!");
					System.out.println("ID: " + arg0.getId());
					list.add(arg0);
				}
			});
        	
        }
        else if(arguments[0] == 2){
        	task = 3;
        	req.updateSMILMessage(temp).fire(new Receiver<SMILMessageProxy>() {

				@Override
				public void onSuccess(SMILMessageProxy arg0) {
					System.err.println("updateSuccess!");
					System.out.println("ID: " + arg0.getId());
					list.add(arg0);
				}
			});
        	
        }
        else if(arguments[0] == 3){
        	task = 4;
        	req.updateSMILMessage(temp).fire(new Receiver<SMILMessageProxy>() {

				@Override
				public void onSuccess(SMILMessageProxy arg0) {
					System.err.println("updateSuccess!");
					System.out.println("ID: " + arg0.getId());
					list.add(arg0);
				}
			});
        	
        }

        return list;
    }

    @Override
    protected void onPostExecute(List<SMILMessageProxy> result) {
    	switch(task){
    	case 1:
    		System.out.println("POST: " + result.toString());
    		//This is kind of dumb, really our activities should
    		//inherit from an abstract one that has this abstract method.
    		((SMILActivity) activity).addMessages(result);
    		break;
    	case 2:
    		System.out.println("POSTnew: " + result.toString());
    		((NewMessageActivity)activity).addMessages(result.get(0));
    		break;
    	case 3:
    		//Cache, doesn't return yet, may not need to
    		System.out.println("POSTUpdateSuccess: " + result.toString());
    		break;
    	case 4:
    		System.out.println("POSTNewUpdateSuccess: " + result.toString());
    		SMILCache.newFile(result.get(0));
    		((NewMessageActivity)activity).openComposer(result.get(0));
    		break;
    	}
    }
}
