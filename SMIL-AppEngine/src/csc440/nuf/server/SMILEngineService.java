package csc440.nuf.server;

/**
 * @author Brad
 * 
 * This is the SMIL server class really,
 * this will reside on the server and add objects to the
 * datastore.
 * 
 */


import java.io.File;
import java.util.List;

import csc440.nuf.annotation.ServiceMethod;


public class SMILEngineService {

	static DataStore db = new DataStore();
	
	@ServiceMethod
	public SMILMessage createSMILMessage() {
		return db.update(new SMILMessage(new File("SMILBlank"), DataStore.getUserId(), null,
				DataStore.getUserEmail(), null));
	}

	@ServiceMethod
	public SMILMessage readSMILMessage(Long id) {
		return db.find(id);
	}

	@ServiceMethod
	public SMILMessage updateSMILMessage(SMILMessage smilmessage) {
		smilmessage.setSenderEmail(DataStore.getUserEmail());
		smilmessage = db.update(smilmessage);
		DataStore.sendC2DMUpdate("update:" + smilmessage.getId());
		return smilmessage;
	}

	@ServiceMethod
	public void deleteSMILMessage(SMILMessage smilmessage) {
		db.delete(smilmessage.getId());
	}

	@ServiceMethod
	public List<SMILMessage> querySMILMessages() {
		return db.findAll();
	}

}
