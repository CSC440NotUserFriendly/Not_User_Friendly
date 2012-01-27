package com.csc440.nuf.components;

/**
 * CSC-440 SMIL Project
 * 01-22-2011
 * AbstractSMILObject.java
 * @author Brad Barker
 * 
 * Abstract class for defining mandatory requirements of SMIL objects.
 * All SMIL Object types MUST INHERIT FROM THIS CLASS, NO EXCEPTIONS!
 */

public abstract class AbstractSMILObject implements Comparable< AbstractSMILObject> {
	
	//Fields common to all SMIL Objects;
	private int startTime;
	private int endTime;
	
	//Must implement this method for the parser.
	abstract public void printData();
	
	
	//Setters for fields
	public void setStartTime(int startTime){
		this.startTime = startTime;
	}
	public void setEndTime(int endTime){
		this.endTime = endTime;
	}
	
	//Getters for fields
	public int getStartTime(){
		return startTime;
	}
	public int getEndTime(){
		return endTime;
	}
	
	@Override
	public int compareTo(AbstractSMILObject o){
		return this.startTime - o.startTime;
	}

}
