package csc440.nuf.server;

import java.io.File;

import com.google.web.bindery.requestfactory.shared.Locator;


public class SMILMessageLocator extends Locator<SMILMessage, Void> {

	@Override
	public SMILMessage create(Class<? extends SMILMessage> clazz) {
		return new SMILMessage(new File("SMILBlank"), DataStore.getUserId(), null,
				DataStore.getUserEmail(), null);
	}

	@Override
	public SMILMessage find(Class<? extends SMILMessage> clazz, Void id) {
		return create(clazz);
	}

	@Override
	public Class<SMILMessage> getDomainType() {
		return SMILMessage.class;
	}

	@Override
	public Void getId(SMILMessage domainObject) {
		return null;
	}

	@Override
	public Class<Void> getIdType() {
		return Void.class;
	}

	@Override
	public Object getVersion(SMILMessage domainObject) {
		return null;
	}

}
