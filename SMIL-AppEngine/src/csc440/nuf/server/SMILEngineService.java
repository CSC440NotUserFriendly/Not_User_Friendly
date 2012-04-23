package csc440.nuf.server;

/**
 * @author Brad
 * 
 * This is the SMIL server class really,
 * this will reside on the server and add objects to the
 * datastore.
 * 
 */


import java.util.List;

public class SMILEngineService {

	static DataStore db = new DataStore();
	
	public SMILMessage createSMILMessage() {
		System.err.println("CreateMessage!");
		return db.update(new SMILMessage());
	}

	public SMILMessage readSMILMessage(Long id) {
		return db.find(id);
	}

	public SMILMessage updateSMILMessage(SMILMessage smilmessage) {
		System.err.println("UpdateMessage!");
		smilmessage.setSender(DataStore.getUserEmail());
		smilmessage = db.update(smilmessage);
		DataStore.sendC2DMUpdate("update:" + smilmessage.getId());
		return smilmessage;
	}

	public void deleteSMILMessage(SMILMessage smilmessage) {
		db.delete(smilmessage.getId());
	}

	public List<SMILMessage> querySMILMessages() {
		return db.findAll();
	}

}
