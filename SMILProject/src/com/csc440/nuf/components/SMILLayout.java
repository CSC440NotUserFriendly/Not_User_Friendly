package com.csc440.nuf.components;

import org.xml.sax.Attributes;

/**
 * CSC-440 SMIL Project
 * 01-26-2011
 * SMILLayout.java
 * @author Brad Barker
 * 
 * Contains all of the information from the <layout> tag.
 * 
 * Sample layout xml attribute object:
 * 
 * root-layout
 *            xml:id          root                 
 *             title          Sample               
 *             width          500                  
 *            height          500    
 * 
 */

public class SMILLayout {
	
	private String xmlid;
	private String title;
	private String backgroundColor;
	private int height;
	private int width;
	//What else?....
	
	public SMILLayout(Attributes att){
		
		for(int i=0; i<att.getLength(); i++){
			
			if(att.getLocalName(i).equals("xml:id"))
                    xmlid = att.getValue(att.getLocalName(i));
			
			else if(att.getLocalName(i).equals("title"))
                title = att.getValue(att.getLocalName(i));
			
			else if(att.getLocalName(i).equals("backgroundColor"))
                backgroundColor = att.getValue(att.getLocalName(i));
			
			else if(att.getLocalName(i).equals("height"))
                height = Integer.parseInt(att.getValue(att.getLocalName(i)));
			
			else if(att.getLocalName(i).equals("width")){
				Integer.parseInt(att.getValue(att.getLocalName(i)));
			}
                   
        }
		
	}
	
	public String getXmlid() {
		return xmlid;
	}
	public void setXmlid(String xmlid) {
		this.xmlid = xmlid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	

}