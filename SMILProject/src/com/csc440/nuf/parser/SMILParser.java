package com.csc440.nuf.parser;

/**
 * CSC-440 SMIL Project
 * 01-22-2011
 * SMILParser.java
 * @author Brad Barker
 * 
 * Accepts a URL and retrieves the XML to be extracted.
 * 
 */

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class SMILParser {
	
	SMILParser(URL SMILFile){
		try{
			
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xReader = sp.getXMLReader();
			SMILHandler sHandler = new SMILHandler();
			
			xReader.setContentHandler(sHandler);
			xReader.parse(new InputSource(SMILFile.openStream()));
			
			
		}
		catch(Exception e){
			//Bad URL
		}
		
	}

}
