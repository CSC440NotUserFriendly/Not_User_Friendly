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

import com.csc440.nuf.WaitingQueue;
import com.csc440.nuf.components.*;

public class SMILHandler extends DefaultHandler {

    private static WaitingQueue wQueue;
    private static SMILLayout layout;
    private static SMILParallel par; //This holds the current parallel tag while we add to it;
    private static SMILSequential seq; //Same as par
    
    //Badda boom problem solved use the endElement method to close out the par / seq sections.

    @Override
    public void startElement(String uri, String localName, String qName, Attributes att) {

        if (qName.equals("root-layout")) {
        	//Not sure what to do with this yet, it can't go in the queue.
            layout = new SMILLayout(att);
        }
        if (qName.equals("par")) {
        	//Can't add this until all the objects contained within it are added.
        	//Not quite sure how to do it yet.
            par = new SMILParallel(att);
        }
        if (qName.equals("seq")) {
        	//Same problem with that as with <par>
            seq = new SMILSequential(att);
        }
        if (qName.equals("smilText")) {
        	add(new SMILText(att));
        }
        if (qName.equals("img")) {
        	add(new SMILImage(att));
        }
        if (qName.equals("video")) {
        	add(new SMILVideo(att));
        }
    }
    
    @Override
    public void endElement(String uri, String localName, String qName){
    	
    	//End of the par section
    	if(qName.equals("par")){
    		WaitingQueue.add(par);
    		par = null; //Clear it for the next section
    	}
    	
    	if(qName.equals("seq")){
    		WaitingQueue.add(seq);
    		seq = null; //Clear it for the next section
    	}

    }
    
    public void add(AbstractSMILObject o){
    	if(par != null)
    		par.add(o);
    	else if(seq != null)
    		seq.add(o);
    	else
    		WaitingQueue.add(o);
    	
    }
    
    //Testing code to print data
    @SuppressWarnings("unused")
	private void printXMLData(Attributes att) {
        for(int i=0; i<att.getLength(); i++){
            System.out.printf("%20s \t %-20s \n",
                    att.getLocalName(i), att.getValue(att.getLocalName(i)));
        
        }

    }

}

