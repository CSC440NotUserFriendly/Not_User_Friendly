package csc440.nuf.complay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import csc440.nuf.components.AbstractSMILObject;

public class Waiting {
	private static Q q = new Q("WaitingQ");
	private static int time = 0;	// need a static way to keep the time, easy fix...
	private static float density = 1;
	private static ArrayList<AbstractSMILObject> activeElements = new ArrayList<AbstractSMILObject>();
	
	public static Q Q() {
		return q;
	}

	public static int getMessageLength() {
		ArrayList<AbstractSMILObject> a = allQArrayList();
		Collections.sort(a, new EndTimeDESC());
		return a.get(0).getEndTime();
	}

	public static int getElementId(AbstractSMILObject o) {
		ArrayList<AbstractSMILObject> a = allQArrayList();
		return a.indexOf(o);
	}
	
	public static AbstractSMILObject getElementAtId(int i) {
		ArrayList<AbstractSMILObject> a = allQArrayList();
		return a.get(i);
	}
	
	public static ArrayList<AbstractSMILObject> allQArrayList() {
		ArrayList<AbstractSMILObject> array = new ArrayList<AbstractSMILObject>();
		LinkedList<AbstractSMILObject> w = Waiting.Q().getQCopy(),
			on = OnScreen.Q().getQCopy(),
			off = OffScreen.Q().getQCopy();
		while (!w.isEmpty()) array.add(w.poll());
		while (!on.isEmpty()) array.add(on.poll());
		while (!off.isEmpty()) array.add(off.poll());
		Collections.sort(array);
		return array;
	}
	
	public static int getTime() {
		return time;
	}
	
	public static void setTime(int time) {
		Waiting.time = time;
	}
	
	public static void gatherInWaiting() {
		while (!OnScreen.Q().isEmpty()) Waiting.Q().push(OnScreen.Q().pop());
		while (!OffScreen.Q().isEmpty()) Waiting.Q().push(OffScreen.Q().pop());
		Waiting.Q().prepQ();
	}

	public static void activateElement(int index) {
		activeElements.add(allQArrayList().get(index));
	}
	
	public static void activateElement(AbstractSMILObject o) {
		activeElements.add(o);
	}
	
	public static void deactivateElement(int index) {
		activeElements.remove(allQArrayList().get(index));
	}
	
	public static void deactivateElement(AbstractSMILObject o) {
		activeElements.remove(o);
	}
	
	public static void deactivateAll() {
		activeElements = new ArrayList<AbstractSMILObject>();
	}
	
	public static AbstractSMILObject getActiveElementAt(int index) {
		if (index >= activeElements.size()) return null;
		return activeElements.get(index);
	}

	public static boolean isActive(AbstractSMILObject o) {
		if (activeElements.size() == 0) return false;
		return activeElements.contains(o);
	}
	
	public static boolean isActiveEmpty() {
		if (activeElements.size() == 0) return true;
		return false;
	}
	
	public static ArrayList<AbstractSMILObject> getActiveElements() {
		return new ArrayList<AbstractSMILObject>(activeElements);
	}

	public static void setScreenDensity(float newDensity) {
		density = newDensity;
	}
	
	public static float getDensity() {
		// we can have this look at other things to scale the presentation as well, can talk about this later
		return density;
	}
}