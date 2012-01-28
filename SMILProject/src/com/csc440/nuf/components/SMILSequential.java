package com.csc440.nuf.components;

import java.util.Collections;
import java.util.LinkedList;

import org.xml.sax.Attributes;

/**
 * CSC-440 SMIL Project
 * 01-26-2011
 * SMILSequential.java
 * @author Brad Barker
 * 
 * List within a list to group components of a
 * sequential section.
 * 
 */

public class SMILSequential extends AbstractSMILObject{
	
	protected String xmlid;
	private LinkedList <AbstractSMILObject> sequentialObjects;

	public SMILSequential(Attributes att) {
		super(att);
		qName = "seq";
		
		sequentialObjects = new LinkedList<AbstractSMILObject>();
		if(att.getLocalName(0) != null){
			if(att.getLocalName(0).equals("xml:id"))
				xmlid = att.getValue(att.getLocalName(0));
		}
	}
	
	public void add(AbstractSMILObject o){
		
		//Need to do some sort of time calculations here
		//If it's the first obj in the seq?
		if(sequentialObjects.isEmpty()){
			this.begin = o.begin;
			o.end = o.begin + o.dur;
		}
		//Setting tail to head sequence
		else{
			o.begin = sequentialObjects.getLast().end;
			o.end = o.begin + o.dur;
		}
		
		sequentialObjects.add(o);	
	}
	
	public LinkedList<AbstractSMILObject> getElements() {
		//May not be needed unless items are out of order in the xml.
		Collections.sort(sequentialObjects);
		return sequentialObjects;
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
		
		while(!sequentialObjects.isEmpty()){
			output.append(sequentialObjects.pop().printTag());
		}
		output.append("\n</seq>\n");
		return output.toString();
	}
	
}
