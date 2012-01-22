package com.csc440.nuf.parser;

/**
 * CSC-440 SMIL Project
 * 01-22-2011
 * SMILData.java
 * @author Brad Barker
 * 
 * Static class to contain SMIL objects.
 * SMILParser will extract the data and add objects to this class.
 * The SMILWriter will iterate through this class writing objects back into
 * XML format.
 * 
 */

import java.util.LinkedList;
import com.csc440.nuf.AbstractSMILObject;

public class SMILData {
	
	//Used as a Queue.
	private static LinkedList <AbstractSMILObject> objectQ = new LinkedList<AbstractSMILObject>();
	
	public static void add(AbstractSMILObject smil){
		
		objectQ.push(smil);
	}
	
	public static AbstractSMILObject getNext(){
		
		return objectQ.pop();	
	}

}