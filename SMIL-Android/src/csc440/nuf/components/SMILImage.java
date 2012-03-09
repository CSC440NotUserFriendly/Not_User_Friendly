package csc440.nuf.components;

import org.xml.sax.Attributes;

/**
 * CSC-440 SMIL Project
 * 01-26-2011
 * SMILImage.java
 * @author Brad Barker
 * 
 * 
 */

public class SMILImage extends AbstractSMILObject {
	
	private final String QNAME = "img";
	protected String xmlid; //xml:id
	protected int top; //topMargin
	protected int left; //leftMargin
	protected int zIndex; //z-index
	protected String src; //source

	public SMILImage(Attributes att) {
		super(att);
		qName = "img";
		
		for(int i=0; i<att.getLength(); i++){
			
			String localName = att.getLocalName(i);
			String value = att.getValue(localName);
			
			if(localName.equals("xml:id"))
				xmlid = value;
			
			else if(localName.equals("top"))
                top = getIntValue(value);
			
			else if(localName.equals("left"))
                left = getIntValue(value);
			
			else if(localName.equals("z-index")){
				zIndex = getIntValue(value);
			}
			else if(localName.equals("src")){
				src = value;
			}
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

	public int getTopMargin() {
		return top;
	}

	public void setTopMargin(int topMargin) {
		this.top = topMargin;
	}

	public int getLeftMargin() {
		return left;
	}

	public void setLeftMargin(int leftMargin) {
		this.left = leftMargin;
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


}
