package csc440.nuf.shared;

import java.util.Date;

import com.google.web.bindery.requestfactory.shared.ProxyForName;
import com.google.web.bindery.requestfactory.shared.EntityProxy;

@ProxyForName(value = "csc440.nuf.server.SMILMessage", locator = "csc440.nuf.server.SMILMessageLocator")
public interface SMILMessageProxy extends EntityProxy {
	String getSender();
	String getRecipient();
	String getKey();
	String getMessage();
	String getFilename();
	Date getCreated();
	Date getModified();
	Long getId();
	boolean isRead();
	
	void setSender(String sender);
	void setRecipient(String recipient);
	void setKey(String key);
	void setMessage(String message);
	void setFilename(String filename);
	void setCreated(Date created);
	void setModified(Date modified);
	void setId(Long id);
	void setRead(boolean read);
	
}
