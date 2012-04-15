package csc440.nuf.complay;

/**
 * CSC-440 SMIL Project
 * 02-11-2012
 * Timer.java
 * @author Jacob Ensor
 * 
 * 
 */

//import android.util.Log;

public class Timer {
	private int time;
	//private LinkedList <AbstractSMILObject> OnScreenQ, OffScreenQ;
	
	Timer() {
		//OffScreenQ = new LinkedList<AbstractSMILObject>();
		//onScreenQ = new LinkedList<AbstractSMILObject>();
		time = -1;
		Waiting.Q().prepQ();
		OnScreen.Q().clear();
		OffScreen.Q().clear();
	}
	
	public int getTime() {
		return time;
	}

	// idea is to use this to increment the time each frame, and setTime when the seekbar is used
	// return a boolean which is true if changes have been made, but false if the OnScreenQ is the same
	// in the canvas class, if this returns true we'll redraw everything.
	public boolean timePlusPlus() {
		return setTime(time+1);
	}
	
	// this takes an int (newTime) and adds/removes items to/from each queue so that they reflect
	// the newTime. It then overwrites the current time variable and returns a boolean stating whether
	// or not the OnScreenQ was changed during the operation.
	public boolean setTime(int newTime) {
		boolean changed = false;
		if (newTime == time) return changed;
		else if (newTime > time) { // if the newTime is greater than the old time:
			// first check to see if any of your items OnScreen are ready to go off.
			while (!OnScreen.Q().isEmpty() && OnScreen.Q().peek().getEndTime() <= newTime) {
				changed = true;
				OffScreen.Q().push(OnScreen.Q().pop());
			}
			
			// then check if any of the items in the Waiting.Q() are ready to go on.
			while (!Waiting.Q().isEmpty() && Waiting.Q().peek().getStartTime() <= newTime) {
				changed = true;
				OnScreen.Q().push(Waiting.Q().pop());
				//OnScreen.Q().logQ("item added ");
				//Waiting.Q().logQ("item popped ");
			}
			
			// sort the OnScreenQ by end time, so the first check will work out well the next time around
			if (changed) OnScreen.Q().sortByEndTimeASC();

			// used to sort the OffScreenQ by end time here, but don't think it's necessary
		} else {	// if the newTime is less than the old time
			// first see if anything OffScreen needs to go back OnScreen
			while (!OffScreen.Q().isEmpty() && OffScreen.Q().peek().getEndTime() > newTime && OffScreen.Q().peek().getStartTime() >= newTime) {
				changed = true;
				OnScreen.Q().push(OffScreen.Q().pop());
			}
			
			// then see if anything OnScreen needs to be backed up into the WaitingQueue
			OnScreen.Q().sortByStartTimeDESC();	// sort by startTime
			while (!OnScreen.Q().isEmpty() && OnScreen.Q().peek().getStartTime() > newTime) {
				changed = true;
				Waiting.Q().push(OnScreen.Q().pop());
			}

			OnScreen.Q().sortByEndTimeASC();		// once only what we need is on the queue, sort it properly
		}
		
		time = newTime;
		return changed;
	}
}
