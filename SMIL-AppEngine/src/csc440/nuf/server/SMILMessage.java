package csc440.nuf.server;

/**
 * @author Brad
 * 
 * Alex I'm splitting up your smilserver class the entity needs to be in
 * its own class.  This you create an RPC service for the entity.
 * 
 */


import java.io.File;

import javax.persistence.*;

@Entity
public class SMILMessage {

	private String sender;
	private String recipient;
	private String senderEmail;
	private String recipientEmail;
	
	private File SMILFile;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	public SMILMessage(File file, String sender, String recipient,
			String senderEmail, String recipientEmail){
		
		this.setSender(sender);
		this.setRecipient(recipient);
		this.senderEmail = senderEmail;
		this.recipientEmail = recipientEmail;
		SMILFile = file;
		
	}
	
	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getRecipientEmail() {
		return recipientEmail;
	}

	public void setRecipientEmail(String recipientEmail) {
		this.recipientEmail = recipientEmail;
	}

	public File getSMILFile() {
		return SMILFile;
	}

	public void setSMILFile(File sMILFile) {
		SMILFile = sMILFile;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	
	
	
}
