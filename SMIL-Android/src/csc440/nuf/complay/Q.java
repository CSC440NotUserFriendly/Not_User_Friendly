package csc440.nuf.complay;

/**
 * CSC-440 SMIL Project
 * 03-25-2012
 * SMILData.java
 * @author Jacob Ensor
 * 
 * Took all the static methods from the old WaitingQueue and made them non-static.
 * Then I have static classes for each Queue. Each of these just establishes a
 * static Q object. Now instead of using WaitingQueue, you use Waiting.Q() to access
 * the waiting queue. In addition, you may also use:
 * 		OnScreen.Q()
 * 		OffScreen.Q()
 */

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import android.graphics.Canvas;
import android.util.Log;

import csc440.nuf.components.*;

public class Q {
	//Used as a Queue.
	//Remember instanceof when trying to determine what kind of objects these are!
	private LinkedList <AbstractSMILObject> objectQ;
	private SMILLayout layout;
	private float density = 1;
	private String qType;
	
	public Q(String qType) {
		objectQ = new LinkedList<AbstractSMILObject>();
		this.qType = qType;
	}
	
	public void setScreenDensity(float newDensity) {
		density = newDensity;
	}
	
	public float getDensity() {
		// we can have this look at other things to scale the presentation as well, can talk about this later
		return density;
	}
	
	public void push(AbstractSMILObject smil){
		objectQ.addFirst(smil);
	}
	
	public AbstractSMILObject peek(){
		return objectQ.peek();
	}
	
	public AbstractSMILObject pop(){
		return objectQ.poll();	
	}
	
	public void prepQ(){
		Collections.sort(objectQ);
	}
	
	public boolean isEmpty(){
		return objectQ.isEmpty();
	}

	public SMILLayout getLayout() {
		return layout;
	}

	public void setLayout(SMILLayout layout) {
		this.layout = layout;
	}

	public int getMessageLength() {
		return objectQ.getLast().getEndTime();
	}
	
	public int getLength() {
		return objectQ.size();
	}
	
	public LinkedList<AbstractSMILObject> getQ() {
		return objectQ;
	}
	
	public void draw(Canvas canvas) {
		LinkedList <AbstractSMILObject> Qcopy = new LinkedList <AbstractSMILObject>(objectQ);
		while (!Qcopy.isEmpty())  {
			//Log.w("Timer", "OnScreenQ size: " + OnScreenQ.size());
			Qcopy.poll().draw(canvas);
		}
	}
	
	public void sortByEndTimeASC() {
		Collections.sort(objectQ, new EndTimeASC());
	}
	
	public void sortByStartTimeDESC() {
		Collections.sort(objectQ, new StartTimeDESC());
	}

	public void clear() {
		objectQ.clear();
	}
	
	// outputs the start and end times of each element as start-end like below:
	// 0-5, 2-6, 5-10
	// the tag of the log is the name of the Queue, WaitingQ/OnScreenQ/OffScreenQ
	// whatever string is passed in prepends the list of times. For debugging purposes.
	public void logQ(String log) {
		LinkedList <AbstractSMILObject> Qcopy = new LinkedList <AbstractSMILObject>(objectQ);
		while (!Qcopy.isEmpty())  {
			//Log.w("Timer", "OnScreenQ size: " + OnScreenQ.size());
			log += Qcopy.peek().getStartTime() + "-" + Qcopy.poll().getEndTime() + ", ";
		}
		
		Log.w(qType, log);
	}
}


class EndTimeASC implements Comparator< AbstractSMILObject> {
	public int compare(AbstractSMILObject lhs, AbstractSMILObject rhs) {
		return lhs.getEndTime() - rhs.getEndTime();
	}
}
class StartTimeDESC implements Comparator< AbstractSMILObject> {
	public int compare(AbstractSMILObject lhs, AbstractSMILObject rhs) {
		return rhs.getStartTime() - lhs.getStartTime();
	}
}
/*
class EndTimeDESC implements Comparator< AbstractSMILObject> {
	public int compare(AbstractSMILObject lhs, AbstractSMILObject rhs) {
		return lhs.getEndTime() - rhs.getEndTime();
	}
}
*/