package csc440.nuf.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//import com.google.appengine.api.datastore.Entity;
//import com.google.appengine.api.datastore.Key;
//import com.google.appengine.api.datastore.KeyFactory;

/**
 * CSC-440 SMIL Project
 * 02-18-2011
 * SMILCache.java
 * @author Alex Gilbert
 * 
 * I've edited this out because it's going to need some changes.
 * 
 */

public class SMILCache {
/**
	private static HashMap<Key,Object[]> cache;
	private static int limit;
	
	public SMILCache()
	{
		cache = new HashMap<Key,Object[]>();
		SMILCache.limit = 10;
	}
	
	public synchronized void setLimit(int limit)
	{
		SMILCache.limit = limit;
	}
	
	public int getLimit()
	{
		return SMILCache.limit;
	}
	
	public static synchronized Entity get(String file)
	{
		try{
			Key smilKey = KeyFactory.stringToKey(file);
			if( cache.containsKey( smilKey ) )
			{
				SMILCacheObject temp = (SMILCacheObject)cache.get(smilKey)[0];
				Date now = new Date();
				temp.setLast(now);
				temp.increment();
				return temp.getEntity();
			}
			else
			{
				return null;
			}
		}catch(Exception e){
			return null;
		}
	}
	
	public static synchronized boolean put(Entity entity)
	{
		try{
			Object values[] = new Object[2];
			SMILCacheObject temp = new SMILCacheObject(entity);
			if( cache.size()==limit)
			{
				cache.remove(_getLeastUsed());
				values[0]=temp;
				values[1]=_getWeightedUsage(temp);
				cache.put(entity.getKey(), values);
			}
			else
			{
				values[0]=temp;
				values[1]=_getWeightedUsage(temp);
				cache.put(entity.getKey(), values);
			}
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	*
	 * heuristic function to rank the SMILCacheObjects
	 * in the hashMap according to how often they are 
	 * used
	 *
	private static int _getWeightedUsage(SMILCacheObject co)
	{
		Date temp = co.getStart();
		int s = temp.getDate();
		temp = co.getLast();
		int l = temp.getDate();
		int count = co.getCount();
		temp = new Date();
		int n = temp.getDate();
		int ns = n-s;
		int nl = n-l;
		int timesPer = (ns-nl)/count;
		timesPer*=nl;
		timesPer%=100;
		
		return timesPer;
	}
	
	private static Key _getLeastUsed()
	{
		int least = 999999999;
		Key deleteMe = null;
		Iterator it = cache.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        Object value[] = (Object[])pairs.getValue();
	        int weight = Integer.parseInt(value[1].toString());
	        if (weight<least)
	        {
	        	least=weight;
	        	deleteMe = (Key)pairs.getKey();
	        }
	    }
	    
		return deleteMe;
	}
	*/
}
