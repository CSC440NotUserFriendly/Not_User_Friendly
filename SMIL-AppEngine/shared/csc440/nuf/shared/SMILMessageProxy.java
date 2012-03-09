package csc440.nuf.shared;

import com.google.web.bindery.requestfactory.shared.ProxyForName;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyForName(value = "csc440.nuf.server.SMILMessage", locator = "csc440.nuf.server.SMILMessageLocator")
public interface SMILMessageProxy extends ValueProxy {

}
