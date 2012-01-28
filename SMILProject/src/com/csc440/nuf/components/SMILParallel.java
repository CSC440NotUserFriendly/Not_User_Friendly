package com.csc440.nuf.components;

import java.util.ArrayList;

import org.xml.sax.Attributes;

/**
 * CSC-440 SMIL Project
 * 01-26-2011
 * SMILParallel.java
 * @author Brad Barker
 * 
 * 
 */

public class SMILParallel extends AbstractSMILObject{
	
	//Inheriting startTime & endTime
	//Keep in mind the end time of one object will be the start of the next!
	private String xmlid;
	private ArrayList <AbstractSMILObject> parallelObjects;
	
	public SMILParallel(Attributes att){
		if(att.getLocalName(0).equals("xml:id"))
            xmlid = att.getValue(att.getLocalName(0));
	}
	
	public void add(AbstractSMILObject o){
		
		//Need to do some sort of time calculations here
		//Most likely - o.getStartTime(this.startTime);
		parallelObjects.add(o);
		
	}
	
	public AbstractSMILObject[] get(){
		return parallelObjects.toArray(null);
	}
	
	public String getXmlid() {
		return xmlid;
	}

	public void setXmlid(String xmlid) {
		this.xmlid = xmlid;
	}

	@Override
	public void printData() {
		// TODO Auto-generated method stub
		
	}
	
	
	

}