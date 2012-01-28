package com.csc440.nuf.components;

import java.util.LinkedList;

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
	protected String xmlid;
	private LinkedList <AbstractSMILObject> parallelObjects;
	
	public SMILParallel(Attributes att){
		super(att);
		qName = "par";
		
		parallelObjects = new LinkedList<AbstractSMILObject>();
		if(att.getLocalName(0).equals("xml:id"))
            xmlid = att.getValue(att.getLocalName(0));
	}
	
	public void add(AbstractSMILObject o){
		
		//Need to do some sort of time calculations here
		if(parallelObjects.isEmpty()){
			this.begin = o.begin;
		}
		//Sync consecutive objects
		else{
			o.begin = this.begin;
		}
		
		parallelObjects.add(o);
		
	}
	
	public LinkedList<AbstractSMILObject> getElements(){
		return parallelObjects;
	}
	
	public String getXmlid() {
		return xmlid;
	}

	public void setXmlid(String xmlid) {
		this.xmlid = xmlid;
	}

	@Override
	public String printTag(){
		StringBuilder output = new StringBuilder(super.printTag() + ">\n");
		
		while(!parallelObjects.isEmpty()){
			output.append(parallelObjects.pop().printTag());
		}
		output.append("\n</par>\n");
		return output.toString();
	}
}
