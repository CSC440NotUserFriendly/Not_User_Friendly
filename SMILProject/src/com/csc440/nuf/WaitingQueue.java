package com.csc440.nuf;

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

import java.util.Collections;
import java.util.LinkedList;
import com.csc440.nuf.components.*;

public class WaitingQueue {
	
	//Used as a Queue.
	//Remember instanceof when trying to determine what kind of objects these are!
	private static LinkedList <AbstractSMILObject> objectQ = new LinkedList<AbstractSMILObject>();
	private static SMILLayout layout;
	
	public static void push(AbstractSMILObject smil){
		objectQ.push(smil);
	}
	
	public static AbstractSMILObject peek(){
		return objectQ.peek();
	}
	
	public static AbstractSMILObject pop(){
		return objectQ.pop();	
	}
	public static void prepQ(){
		Collections.sort(objectQ);
	}
	
	public static boolean isEmpty(){
		return objectQ.isEmpty();
	}

	public static SMILLayout getLayout() {
		return layout;
	}

	public static void setLayout(SMILLayout layout) {
		WaitingQueue.layout = layout;
	}
	
	public static int getLength() {
		return objectQ.getLast().getEndTime();
	}

}
