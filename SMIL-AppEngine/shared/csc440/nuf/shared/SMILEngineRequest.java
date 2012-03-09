package csc440.nuf.shared;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@ServiceName(value = "csc440.nuf.server.SMILEngineService", locator = "csc440.nuf.server.SMILEngineServiceLocator")
public interface SMILEngineRequest extends RequestContext {

	Request<SMILMessageProxy> createSMILMessage();

	Request<SMILMessageProxy> readSMILMessage(Long id);

	Request<SMILMessageProxy> updateSMILMessage(SMILMessageProxy smilmessage);

	Request<Void> deleteSMILMessage(SMILMessageProxy smilmessage);

	Request<List<SMILMessageProxy>> querySMILMessages();

}
