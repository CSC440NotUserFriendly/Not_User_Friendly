package csc440.nuf.server;

import com.google.web.bindery.requestfactory.shared.ServiceLocator;


public class SMILEngineServiceLocator implements ServiceLocator {

	@Override
	public Object getInstance(Class<?> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
