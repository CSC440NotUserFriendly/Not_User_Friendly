package connector.cc;

import java.util.Date;

import com.google.appengine.api.datastore.Entity;
//import com.google.appengine.api.datastore.Key;
/**
 * CSC-440 SMIL Project
 * 02-18-2011
 * SMILCacheObject.java
 * @author Alex Gilbert
 * 
 */

public class SMILCacheObject {

	private Entity e;
	private Date start;
	private Date last;
	private int count;
	
	public SMILCacheObject(Entity e)
	{
		this.e = e;
		Date start = new Date();
		this.start = start;
		this.last = start;
	}
	
	public Date getStart()
	{
		return this.start;
	}
	
	public Date getLast()
	{
		return this.last;
	}
	
	public Entity getEntity()
	{
		return this.e;
	}
	
	public int getCount()
	{
		return this.count;
	}
	
	public void setLast(Date last)
	{
		this.last = last;
	}
	
	public void increment()
	{
		this.count++;
	}
}
