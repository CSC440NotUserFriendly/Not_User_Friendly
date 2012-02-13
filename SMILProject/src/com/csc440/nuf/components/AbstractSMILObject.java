package com.csc440.nuf.components;

import java.lang.reflect.Field;

import org.xml.sax.Attributes;

import android.graphics.Canvas;

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
	protected String qName;
	protected int begin;
	protected int end;
	protected int dur;
	
	public AbstractSMILObject(String qName, int begin, int dur) {
		this.qName = qName;
		this.begin = begin;
		setDuration(dur);
	}
	
	public AbstractSMILObject(Attributes att){
		for(int i=0; i<att.getLength(); i++){
			
			String localName = att.getLocalName(i);
			String value = att.getValue(localName);
			
			if(localName.equals("begin"))
                begin = (int)(Double.parseDouble(value)*100);
			else if(localName.equals("dur"))
				setDuration(getIntValue(value));
		}
		
	}
	
	public String printTag() {
		
		StringBuilder output = new StringBuilder();
		String openTag = "<%s";
		
		Field [] f = this.getClass().getDeclaredFields();
	
		output.append(String.format(openTag, this.qName));
		
		for(int i=0; i<f.length; i++){
			try {
				f[i].setAccessible(false); //We don't want private fields
				
				String temp = f[i].getName();
				
				if(f[i].get(this) != null){
					if(temp == "xmlid")
						output.append(" " + "xml:id" + "=" + f[i].get(this));
					else if(temp == "zIndex")
						output.append(" " + "z-index" + "=" + f[i].get(this));
					else
						output.append(" " + temp + "=" + f[i].get(this));
				}
			} 
			catch (IllegalAccessException e) {
				//Do Nothing
				//We are intentionally skipping private fields that don't go in the tag
			}
		}
		
		return output.toString();

	}
	
	//This is for parsing the numerical values that are ended with units (20px).
	public static int getIntValue(String s){
		
		String [] temp = s.split("\\D");
		
		if(temp[0].equals("")){
			return -1; //Invalid field
		}
		
		return	Integer.parseInt(temp[0]);
	}
	
	//Setters for fields
	public void setStartTime(int startTime){
		this.begin = startTime;
	}
	public void setEndTime(int endTime){
		this.end = endTime;
	}
	
	//Getters for fields
	public int getStartTime(){
		return begin;
	}
	public int getEndTime(){
		return end;
	}
	
	public int compareTo(AbstractSMILObject o){
		return this.begin - o.begin;
	}
	
	/*
	 * "This" is why Polymorphism is awesome!
	 * when calling "this" from a subclass it will be
	 * in reference to itself and not THIS abstract class.
	 * 
	 */
	@Override
	public String toString(){
		
		StringBuilder output = new StringBuilder();
		String template = "%-15s=%10s\n";
		
		output.append(this.getClass().getName() + "\n");
		Field [] f = this.getClass().getDeclaredFields();
		
		
		output.append(String.format(template, "startTime", begin));
		output.append(String.format(template, "endTime", end));
		output.append(String.format(template, "Duration", dur));
		
		for(int i=0; i<f.length; i++){
			try {
				
				f[i].setAccessible(true); //Allows access to private fields!
				output.append(String.format(template, f[i].getName(), f[i].get(this)));
				
			} 
			catch (IllegalAccessException e) {
				output.append(f[i].getName() + " : " + "Unable to access\n");
			}
			
		}
		
		
		return output.toString();
	}

	public int getDuration() {
		return dur;
	}

	public void setDuration(int duration) {
		this.dur = duration;
		if (duration != 0 && (begin != end || end == 0)) {
			end = begin + duration;
		}
	}
	
	public void draw(Canvas canvas) {
		// implement this later
	}

}
