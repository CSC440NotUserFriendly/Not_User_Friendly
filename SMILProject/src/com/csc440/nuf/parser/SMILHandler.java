package com.csc440.nuf.parser;

/**
 * CSC-440 SMIL Project
 * 01-22-2011
 * SMILHandler.java
 * @author Brad Barker
 * 
 * This class is the bread and butter of the parser.
 * It will interpret the XML determining object types and
 * creating those objects to be added to the data structure.
 * 
 */


import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class SMILHandler extends DefaultHandler {
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes att){
		
	
		/*
		 * if text ...
		 * 
		 * else if image ...
		 * 
		 * else if video ...
		 * 
		 * else ....
		 * 
		 */
		
	}
	private void createTextObject(){	
		
	} 
	
	private void createImageObject(){	
		
	}
	
	private void createVideoObject(){	
	
	}
	

}
