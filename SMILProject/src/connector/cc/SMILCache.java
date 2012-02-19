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

	private HashMap<Key,SMILCacheObject> cache;
	private int limit;
	
	public SMILCache()
	{
		cache = new HashMap<Key,SMILCacheObject>();
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
	
	public Map<String,Object> get(File file)
	{
		try{
			Key smilKey = KeyFactory.stringToKey(file.getName());
			if( cache.containsKey( smilKey ) )
			{
				SMILCacheObject temp = cache.get(smilKey);
				Date now = new Date();
				temp.setLast(now);
				temp.increment();
				return temp.getEntity().getProperties();
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
			SMILCacheObject temp = new SMILCacheObject(entity);
			if( cache.size()==limit)
			{
				cache.remove(_getLeastUsed());
			}
			else
			{
				cache.put(entity.getKey(), temp);
			}
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	private SMILCacheObject _getLeastUsed()
	{
		Map<Integer,SMILCacheObject> old= new HashMap<Integer,SMILCacheObject>();
		
		Iterator it = cache.entrySet().iterator();
	    while (it.hasNext()) 
	    {
	    	Map.Entry next = (Map.Entry)it.next();
	    	SMILCacheObject temp = (SMILCacheObject)next.getValue();
	    	if( old.size()<5 )
	    	{
	    		old.put(temp.getCount(),temp);
	    	}
	    	else 
	    	{
	    		
	    	}
	    }
		return new SMILCacheObject(new Entity("temp"));
	}
}
