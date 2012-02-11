package com.csc440.nuf.complay;

/**
 * CSC-440 SMIL Project
 * 02-11-2012
 * Timer.java
 * @author Jacob Ensor
 * 
 * 
 */

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import com.csc440.nuf.WaitingQueue;
import com.csc440.nuf.components.AbstractSMILObject;

public class Timer {
	private int time;
	private LinkedList <AbstractSMILObject> onScreenQ, offScreenQ;
	
	Timer() {
		offScreenQ = new LinkedList<AbstractSMILObject>();
		onScreenQ = new LinkedList<AbstractSMILObject>();
		time = -1;
	}
	
	public int getTime() {
		return time;
	}

	// idea is to use this to increment the time each frame, and setTime when the seekbar is used
	// return a boolean which is true if changes have been made, but false if the onScreenQ is the same
	// in the canvas class, if this returns true we'll redraw everything.
	public boolean timePlusPlus() {
		return setTime(time+1);
	}
	
	// this takes an int (newTime) and adds/removes items to/from each queue so that they reflect
	// the newTime. It then overwrites the current time variable and returns a boolean stating whether
	// or not the onScreenQ was changed during the operation.
	public boolean setTime(int newTime) {
		boolean changed = false;
		if (newTime == time) return changed;
		else if (newTime > time) { // if the newTime is greater than the old time:
			// first check to see if any of your items onScreen are ready to go off.
			while (!onScreenQ.isEmpty() && onScreenQ.peek().getEndTime() <= newTime) {
				changed = true;
				offScreenQ.push(onScreenQ.pop());
			}
			
			// then check if any of the items in the WaitingQueue are ready to go on.
			while (!WaitingQueue.isEmpty() && WaitingQueue.peek().getStartTime() <= newTime) {
				changed = true;
				onScreenQ.push(WaitingQueue.pop());
			}
			
			// sort the onScreenQ by end time, so the first check will work out well the next time around
			if (changed) Collections.sort(onScreenQ, new EndTimeASC());

			//Collections.sort(offScreenQ, new EndTimeDESC());		// don't think we'll need to sort the offScreenQ
			
			
		} else {	// if the newTime is less than the old time
			// first see if anything offScreen needs to go back onScreen
			while (!offScreenQ.isEmpty() && offScreenQ.peek().getEndTime() > newTime && offScreenQ.peek().getStartTime() >= newTime) {
				changed = true;
				onScreenQ.push(offScreenQ.pop());
			}
			
			// then see if anything onScreen needs to be backed up into the WaitingQueue
			Collections.sort(onScreenQ, new StartTimeASC());	// sort by startTime
			while (!onScreenQ.isEmpty() && onScreenQ.peek().getStartTime() < newTime) {
				changed = true;
				WaitingQueue.push(onScreenQ.pop());
			}

			Collections.sort(onScreenQ, new EndTimeASC());		// once only what we need is on the queue, sort it properly
		}
		
		time = newTime;
		return changed;
	}
	
	public LinkedList <AbstractSMILObject> getOnScreenQ() {
		return onScreenQ;
	}
}

class EndTimeASC implements Comparator< AbstractSMILObject> {
	public int compare(AbstractSMILObject lhs, AbstractSMILObject rhs) {
		return lhs.getEndTime() - rhs.getEndTime();
	}
}
class StartTimeASC implements Comparator< AbstractSMILObject> {
	public int compare(AbstractSMILObject lhs, AbstractSMILObject rhs) {
		return lhs.getStartTime() - rhs.getStartTime();
	}
}
/*
class EndTimeDESC implements Comparator< AbstractSMILObject> {
	public int compare(AbstractSMILObject lhs, AbstractSMILObject rhs) {
		return lhs.getEndTime() - rhs.getEndTime();
	}
}
*/