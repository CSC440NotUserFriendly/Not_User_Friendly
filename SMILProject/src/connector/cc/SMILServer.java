package connector.cc;

/**
 * CSC-440 SMIL Project
 * 02-18-2011
 * SMILServer.java
 * @author Alex Gilbert
 * 
 */
import java.io.File;
import java.util.Date;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class SMILServer {

	private DatastoreService datastore;
	
	public SMILServer()
	{
		datastore = DatastoreServiceFactory.getDatastoreService();
	}
	
	public boolean add(File file, String sender, String recipient,
			String senderEmail, String recipientEmail)
	{
		try{
			Entity smil = new Entity(file.getName());
			smil.setProperty("Sender", sender);
			smil.setProperty("Recipient", recipient);
			smil.setProperty("From", senderEmail);
			smil.setProperty("To", recipient);
			smil.setProperty("SMIL File", file);
			Key k = KeyFactory.createKey("smil", file.getName());			
			
			Date sent = new Date();
			smil.setProperty("Sent", sent);
			
			datastore.put(smil);
			
		}catch(Exception e){
			return false;
		}
		
		return true;
	}
	
	public Entity find(File file)
	{
		Entity smilFile;
		try{
			Key smilKey = KeyFactory.stringToKey(file.getName());
			smilFile = datastore.get(smilKey);
		}catch(Exception e){
			return null;
		}
		return smilFile;
	}
}