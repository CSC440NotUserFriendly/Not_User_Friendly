package csc440.nuf;

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
import csc440.nuf.components.*;

public class WaitingQueue {
	
	//Used as a Queue.
	//Remember instanceof when trying to determine what kind of objects these are!
	private static LinkedList <AbstractSMILObject> objectQ = new LinkedList<AbstractSMILObject>();
	private static SMILLayout layout;
	private static float density = 1;
	
	public static void setScreenDensity(float newDensity) {
		density = newDensity;
	}
	
	public static float getDensity() {
		// we can have this look at other things to scale the presentation as well, can talk about this later
		return density;
	}
	
	public static void push(AbstractSMILObject smil){
		objectQ.add(smil);
	}
	
	public static AbstractSMILObject peek(){
		return objectQ.peek();
	}
	
	public static AbstractSMILObject pop(){
		return objectQ.poll();	
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

	public static int getMessageLength() {
		return objectQ.getLast().getEndTime();
	}
	public static int getLength() {
		return objectQ.size();
	}

}
