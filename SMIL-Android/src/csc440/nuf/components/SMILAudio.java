package csc440.nuf.components;

import org.xml.sax.Attributes;


/**
 * CSC-440 SMIL Project
 * 01-26-2011
 * SMILAudio.java
 * @author Brad Barker
 * 
 * 
 */


public class SMILAudio extends AbstractSMILObject {
	
	private final String qName = "audio";
	protected String xmlid; //xml:id
	protected String src; //source
	
	public SMILAudio(Attributes att){
		super(att);
		
		for(int i=0; i<att.getLength(); i++){
			
			String localName = att.getLocalName(i);
			String value = att.getValue(localName);
		
			if(localName.equals("xml:id"))
				xmlid = value;
		
			else if(localName.equals("src"))
				src = value;
		}
		
	}
	@Override
	public String printTag(){
		return super.printTag() + " />";
	}

	public String getXmlid() {
		return xmlid;
	}

	public void setXmlid(String xmlid) {
		this.xmlid = xmlid;
	}
	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

}
