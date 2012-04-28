package csc440.nuf.server;

/**
 * @author Brad
 * 
 * This is googles example datastore class changed to fit our app.
 * 
 * This basically handles the queries to the datastore.
 * 
 */

import com.google.android.c2dm.server.PMF;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletContext;

public class DataStore {


  /**
   * Remove this object from the data store.
   */
  public void delete(Long id) {
    PersistenceManager pm = PMF.get().getPersistenceManager();
    try {
      SMILMessage item = pm.getObjectById(SMILMessage.class, id);
      pm.deletePersistent(item);
    } finally {
      pm.close();
    }
  }

  /**
     * Find a {@link Task} by id.
     * 
     * @param id the {@link SMILMessage} id
     * @return the associated {@link SMILMessage}, or null if not found
     */
    @SuppressWarnings("unchecked")
    public SMILMessage find(Long id) {
      if (id == null) {
        return null;
      }
  
      PersistenceManager pm = PMF.get().getPersistenceManager();
      try {
        Query query = pm.newQuery("select from " + SMILMessage.class.getName()
            + " where id==" + id.toString() + " && sender=='" + getUserEmail() + "'");
        System.out.println(query.toString());
        List<SMILMessage> list = (List<SMILMessage>) query.execute();
        return list.size() == 0 ? null : list.get(0);
      } catch (RuntimeException e) {
        System.out.println(e);
        throw e;
      } finally {
        pm.close();
      }
    }

@SuppressWarnings("unchecked")
public List<SMILMessage> findAll() {
  PersistenceManager pm = PMF.get().getPersistenceManager();
  try {
      Query query = pm.newQuery("select from " + SMILMessage.class.getName()
          + " where sender=='" + getUserEmail() + "'");
      List<SMILMessage> list = (List<SMILMessage>) query.execute();
      
      //Stupid workaround, datastore queries won't accept an 'or' operation
      Query query2 = pm.newQuery("select from " + SMILMessage.class.getName()
              + " where recipient=='" + getUserEmail() + "'");
      List<SMILMessage> list2 = (List<SMILMessage>) query2.execute();
      
      if (list.size() == 0 || list2.size() == 0) {
           //Workaround for this issue:
           //http://code.google.com/p/datanucleus-appengine/issues/detail?id=24
          list.size();
          list2.size();
        }
      //Also a stupid work around, lists 1&2 are read only
      List <SMILMessage> list3 = new ArrayList<SMILMessage>();
      list3.addAll(list);
      list3.addAll(list2);
      
    return list3;
  } catch (RuntimeException e) {
    System.out.println(e);
    throw e;
  } finally {
    pm.close();
  }
}

  /**
   * Persist this object in the datastore.
   */
  public SMILMessage update(SMILMessage item) {
	
    item.setSender(getUserId());

    PersistenceManager pm = PMF.get().getPersistenceManager();
    try {
      pm.makePersistent(item);
      
    } finally {
      pm.close();
    }
    return item;
  }

  public static String getUserId() {
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    return user.getUserId();
  }
  
  public static String getUserEmail() {
	    UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    return user.getEmail();
	  }
	  
  
	
  public static void sendC2DMUpdate(SMILMessage message) {
	    UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
		ServletContext context = RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext();
		SendMessage.sendMessage(context, message.getRecipient(), "Message from: " + user.getEmail() + message.getFilename());
}




}
