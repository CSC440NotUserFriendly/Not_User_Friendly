package connector.cc;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connector.cc.*;


public class CloudtasksService {

	private DataStore datastore;
	private Serve serve;
	private Download download;
	private Upload upload;
	private GetMode getMode;
	
	HttpServletRequest req = null;
	HttpServletResponse res = null;
	
	CloudtasksService(){
		datastore = new DataStore();
	}
	
	public Task createTask(File f, String userId, String email, String recipient) {
		Task t = new Task();
		t.setInsertDate(new Date());
		t.setDone(false);
		t.setEmailAddress(email);
		t.setFile(f);
		Random rn = new Random();
		t.setId(rn.toString());
		t.setUserId(userId);
		t.setRecipient(recipient);
		return t;
	}

	public void readTask(File f) throws ServletException, IOException {
		
		req.setAttribute(f.toString(),f);
		download.doGet(req, res);
	}

	public void insertTask(Task task) throws ServletException, IOException {
		req.setAttribute(task.getFile().toString(), task);
		upload.doPost(req, res);
	}
	
	/* implement if we are going to 
	 * remove a task from the db
	 */
	public void deleteTask(Task task) {
		
	}
	/* implement if and when we decide
	 * if we want to implement a search
	 * case
	 */
	public List queryTasks() {
		// TODO Auto-generated method stub
		return null;
	}
}
