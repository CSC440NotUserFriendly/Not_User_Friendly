package connector.cc;

import java.util.List;
import java.io.*;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;


/**
 * This class handles CRUD operations related to Item entity.
 * 
 *
 */

public class Item {

  /**
   * Create or update Item for a particular product. Product entity has one to many
   * relation-ship with Item entity
   * 
   * @param productName
   *          : product name for which the item is created.
   * @param itemName
   *          : item name
   * @param price
   *          : price of the item
   * @return
   */
  public static Entity createOrUpdateItem(File file, String sender, String recipient) {
    Entity item = getSingleItem(itemName);
    if(item == null){
      item = new Entity("Item");
      item.setProperty("File", file);
      item.setProperty("Sender", sender);
      item.setProperty("Recipient", recipient);
    }
    Util.persistEntity(item);
    return item;
  }

  /**
   * get All the items in the list
   * 
   * @param kind
   *          : item kind
   * @return all the items
   */
  public static Iterable<Entity> getAllItems() {
  	Iterable<Entity> entities = Util.listEntities("Item", null, null);
  	return entities;
  }

  /**
   * Get the item by name, return an Iterable
   * 
   * @param itemName
   *          : item name
   * @return Item Entity
   */
  public static Iterable<Entity> getItem(File file) {
  	Iterable<Entity> entities = Util.listEntities("Item", "File", file);
  	return entities;
  }

  /**
   * get Item with item name
   * @param itemName: get itemName
   * @return  item entity
   */
  public static Entity getSingleItem(File file) {
    Query query = new Query("Item");
    query.addFilter("File", FilterOperator.EQUAL, file);
    List<Entity> results = Util.getDatastoreServiceInstance().prepare(query).asList(FetchOptions.Builder.withDefaults());
    if (!results.isEmpty()) {
      return (Entity)results.remove(0);
    }
    return null;
  }
  

}
