package csc440.nuf.components;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.xml.sax.Attributes;


/**
 * CSC-440 SMIL Project
 * 01-22-2011
 * AbstractSMILObject.java
 * @author Brad Barker
 * 
 * Abstract class for defining mandatory requirements of SMIL objects.
 * All SMIL Object types MUST INHERIT FROM THIS CLASS, NO EXCEPTIONS!
 */

public abstract class AbstractSMILObject implements Comparable< AbstractSMILObject> {
	
	//Fields common to all SMIL Objects;
	protected String qName;
	private String someOtherName;//For something Jake is doing 
	protected int begin;
	protected int end;
	protected int dur;
	
	public AbstractSMILObject(String qName, int begin, int dur) {
		this.qName = qName;
		this.begin = begin;
		setDuration(dur);
	}
	
	public AbstractSMILObject() {
		begin = -1;
		end = -1;
		dur = 0;
	}
	
	public AbstractSMILObject(Attributes att){
		for(int i=0; i<att.getLength(); i++){
			
			String localName = att.getLocalName(i);
			String value = att.getValue(localName);
			
			if(localName.equals("begin"))
                begin = (int)(Double.parseDouble(value));
			else if(localName.equals("dur"))
				setDuration(getIntValue(value));
				
		}
		
	}
	
	public String printTag() {
		
		StringBuilder output = new StringBuilder();
		String openTag = "<%s";
		
		//I probably need three of these, one for the concrete class
		//and one for each of the two super classes, too late to do it right
		Class<?> subClass = this.getClass();
		Class<?> superClass = subClass.getSuperclass();
		
		//Bull, guessing these lists are un-editable, I'm just winging it.
		Field [] f = subClass.getDeclaredFields();
		Field [] f2 = superClass.getDeclaredFields();
		List<Field> l1 = Arrays.asList(f);
		List<Field> l2 = Arrays.asList(f2);
		ArrayList <Field> l = new ArrayList<Field>();
		l.addAll(l1);
		l.addAll(l2);
		
		output.append(String.format(openTag, this.qName));
		
		output.append(" begin=\"" + begin + "\"");
		output.append(" end=\"" + end + "\"");
		output.append(" dur=\"" + dur + "\"");
		
		
		for(int i=0; i<l.size(); i++){
			try {
				l.get(i).setAccessible(false); //We don't want private fields
				
				System.out.println("Fieldsub: " + l.get(i).getName());
				System.out.println("Fieldsup: " + l.get(i).getName());
				String temp = l.get(i).getName();
				
				if(l.get(i).get(this) != null && !l.get(i).getName().equals("qname")){
					if(temp == "xmlid")
						output.append(" " + "xml:id" + "=\"" + l.get(i).get(this) + "\"");
					else if(temp == "zIndex")
						;//output.append(" " + "z-index" + "=\"" + l.get(i).get(this) + "\"");
					else
						output.append(" " + temp + "=\"" + l.get(i).get(this) + "\" ");
				}
			} 
			catch (IllegalAccessException e) {
				//Do Nothing
				//We are intentionally skipping private fields that don't go in the tag
			}
		}
		
		return output.toString();

	}
	
	//This is for parsing the numerical values that are ended with units (20px).
	public static int getIntValue(String s){
		
		String [] temp = s.split("\\D");
		
		if(temp[0].equals("")){
			return -1; //Invalid field
		}
		
		return	Integer.parseInt(temp[0]);
	}
	/*
	 * This should be qName as well but for some
	 * reason Jake is doing some crap that's breaking
	 * the parser
	 */
	public void setName(String someOtherName) {
		this.someOtherName = someOtherName;
	}
	public String getName() {
		return someOtherName;
	}
	
	//Setters for fields
	public void setStartTime(int startTime){
		this.begin = startTime;
	}
	public void setEndTime(int endTime){
		this.end = endTime;
	}
	
	//Getters for fields
	public int getStartTime(){
		return begin;
	}
	public int getEndTime(){
		return end;
	}
	
	public int compareTo(AbstractSMILObject o){
		return this.begin - o.begin;
	}
	
	/*
	 * "This" is why Polymorphism is awesome!
	 * when calling "this" from a subclass it will be
	 * in reference to itself and not THIS abstract class.
	 * 
	 */
	@Override
	public String toString(){
		
		StringBuilder output = new StringBuilder();
		String template = "%-15s=%10s\n";
		
		output.append(this.getClass().getName() + "\n");
		Field [] f = this.getClass().getDeclaredFields();
		
		
		output.append(String.format(template, "startTime", begin));
		output.append(String.format(template, "endTime", end));
		output.append(String.format(template, "Duration", dur));
		
		for(int i=0; i<f.length; i++){
			try {
				
				f[i].setAccessible(true); //Allows access to private fields!
				output.append(String.format(template, f[i].getName(), f[i].get(this)));
				
			} 
			catch (IllegalAccessException e) {
				output.append(f[i].getName() + " : " + "Unable to access\n");
			}
			
		}
		
		
		return output.toString();
	}

	public int getDuration() {
		return dur;
	}

	public void setDuration(int duration) {
		this.dur = duration;
		if (duration != 0 && (begin != end || end == 0)) {
			end = begin + duration;
		}
	}
}
