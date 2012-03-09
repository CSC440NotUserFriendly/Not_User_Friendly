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
            + " where id==" + id.toString() + " && senderEmail=='" + getUserEmail() + "'");
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
          + " where senderEmail=='" + getUserEmail() + "'");
      List<SMILMessage> list = (List<SMILMessage>) query.execute();
      if (list.size() == 0) {
           //Workaround for this issue:
           //http://code.google.com/p/datanucleus-appengine/issues/detail?id=24
          list.size();
        }

    return list;
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
	  
    item.setId(Long.parseLong(getUserId()));
    item.setSenderEmail(getUserEmail());

    PersistenceManager pm = PMF.get().getPersistenceManager();
    try {
      pm.makePersistent(item);
      return item;
    } finally {
      pm.close();
    }
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
	  
  
	
  public static void sendC2DMUpdate(String message) {
	    UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
		ServletContext context = RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext();
		SendMessage.sendMessage(context, user.getEmail(), message);
}




}
