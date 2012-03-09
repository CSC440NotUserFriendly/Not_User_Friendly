package csc440.nuf.components;

import org.xml.sax.Attributes;

/**
 * CSC-440 SMIL Project
 * 01-26-2011
 * SMILText.java
 * @author Brad Barker
 * 
 * 
 */

public class SMILVideo extends AbstractSMILObject {
	
	private String xmlid;
	private int topMargin;
	private int leftMargin;
	private int zIndex;
	private String src;
	
	public SMILVideo(Attributes att){
		super(att);
		qName = "video";
		
		for(int i=0; i<att.getLength(); i++){
			
			String localName = att.getLocalName(i);
			String value = att.getValue(localName);
			
			if(localName.equals("xml:id"))
				xmlid = value;
			
			else if(localName.equals("top"))
                topMargin = getIntValue(value);
			
			else if(localName.equals("left"))
                leftMargin = getIntValue(value);
			
			else if(localName.equals("z-index"))
				zIndex = getIntValue(value);
			
			else if(localName.equals("src"))
				src = value;
			
        }
	}
	
	
	
	public int getzIndex() {
		return zIndex;
	}

	public void setzIndex(int zIndex) {
		this.zIndex = zIndex;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getXmlid() {
		return xmlid;
	}

	public void setXmlid(String xmlid) {
		this.xmlid = xmlid;
	}

	public int getTopMargin() {
		return topMargin;
	}

	public void setTopMargin(int topMargin) {
		this.topMargin = topMargin;
	}

	public int getLeftMargin() {
		return leftMargin;
	}

	public void setLeftMargin(int leftMargin) {
		this.leftMargin = leftMargin;
	}

}
