package com.csc440.nuf.components;

import java.util.ArrayList;

import org.xml.sax.Attributes;

/**
 * CSC-440 SMIL Project
 * 01-26-2011
 * SMILSequential.java
 * @author Brad Barker
 * 
 * 
 */

public class SMILSequential extends AbstractSMILObject{
	
	private String xmlid;
	private ArrayList <AbstractSMILObject> sequentialObjects;

	public SMILSequential(Attributes att) {
		// TODO Auto-generated constructor stub
	}
	
	public void add(AbstractSMILObject o){
		
		//Need to do some sort of time calculations here
		//Most likely - o.getStartTime(this.startTime);
		sequentialObjects.add(o);
		
	}
	
	public AbstractSMILObject[] get(){
		return sequentialObjects.toArray(null);
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
