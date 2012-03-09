package csc440.nuf.parser;

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

import csc440.nuf.WaitingQueue;
import csc440.nuf.components.*;

public class SMILHandler extends DefaultHandler {

    private static SMILParallel par; //This holds the current parallel tag while we add to it;
    private static SMILSequential seq; //Same as par
    private static AbstractSMILObject currentObject;
    

    //This is called on every opening tag.
    @Override
    public void startElement(String uri, String localName, String qName, Attributes att) {
    	

        if (qName.equals("root-layout")) {
        	//Not sure what to do with this yet, it can't go in the queue.
            WaitingQueue.setLayout(new SMILLayout(att));
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
        	currentObject = new SMILText(att);
        }
        if (qName.equals("img")) {
        	currentObject = new SMILImage(att);
        }
        if (qName.equals("video")) {
        	currentObject = new SMILVideo(att);
        }
    }
    
    /*
     * Kind of retarded but ch.length and length are NOT
     * the same thing.  length is exactly the length of the visible
     * text inside the XML tags, ch.length includes extra garbage.
     * 
     * This method is for text contained between opening and ending 
     * XML tags.  Such as: <smilText>ch[]</smilText>
     * 
     * Maintains formatting; newlines, spaces, and tabs included
     * 
     */
    @Override
    public void characters(char[] ch, int start, int length){
    	
    	if(currentObject instanceof SMILText){
    		//Only text up to length not ch.length
    		((SMILText)currentObject).setText(new String(ch, 0, length));
    	}
    }
    
    /*
     * Called at every ending tag.
     * We have to wait until here to add and "forget" about out objects
     * so that we have a chance to pick up any information between tags,
     * such as with the text tag.
     */
    @Override
    public void endElement(String uri, String localName, String qName){
    	
    	if(currentObject != null){
    		add(currentObject);
    	}
    	currentObject = null;
    	
    	//End of the par section
    	if(qName.equals("par")){
    		WaitingQueue.push(par);
    		par = null; //Clear it for the next section
    	}
    	
    	if(qName.equals("seq")){
    		WaitingQueue.push(seq);
    		seq = null; //Clear it for the next section
    	}

    }
    //Simply adds to the Queue but makes sure we're not inside a par/seq section
    public void add(AbstractSMILObject o){
    	if(par != null)
    		par.add(o);
    	else if(seq != null)
    		seq.add(o);
    	else
    		WaitingQueue.push(o);
    	
    }
}

