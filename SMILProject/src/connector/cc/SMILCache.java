package connector.cc;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * CSC-440 SMIL Project
 * 02-18-2011
 * SMILCache.java
 * @author Alex Gilbert
 * 
 */

public class SMILCache {

	private HashMap<Key,Object[]> cache;
	private int limit;
	
	public SMILCache()
	{
		cache = new HashMap<Key,Object[]>();
		this.limit = 10;
	}
	
	public void setLimit(int limit)
	{
		this.limit = limit;
	}
	
	public int getLimit()
	{
		return this.limit;
	}
	
	public Entity get(File file)
	{
		try{
			Key smilKey = KeyFactory.stringToKey(file.getName());
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
	
	public boolean put(Entity entity)
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
	
	/*
	 * heuristic function to rank the SMILCacheObjects
	 * in the hashMap according to how often they are 
	 * used
	 */
	private int _getWeightedUsage(SMILCacheObject co)
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
	
	private Key _getLeastUsed()
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
}
