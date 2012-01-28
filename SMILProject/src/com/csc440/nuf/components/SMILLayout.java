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
			
			String localName = att.getLocalName(i);
			String value = att.getValue(localName);
			
			if(localName.equals("xml:id"))
                    xmlid = value;
			
			else if(localName.equals("title"))
                title = value;
			
			else if(localName.equals("backgroundColor"))
                backgroundColor = value;
			
			else if(localName.equals("height"))
                height = getIntValue(value);
			
			else if(localName.equals("width")){
				width = getIntValue(value);
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
	
	public static int getIntValue(String s){
		
		String [] temp = s.split("\\D");
		
		if(temp[0].equals("")){
			return -1; //Invalid field
		}
		
		return	Integer.parseInt(temp[0]);
		
	}
	
	@Override
	public String toString(){
		String s = String.format("%-15s=%10s\n%-15s=%10s\n" +
				"%-15s=%10s\n%-15s=%10d\n%-15s=%10d",
				"XML:id", xmlid,
				"Title", title,
				"Background", backgroundColor,
				"Height", height,
				"Width", width
				);
		
		return s;
	}
	

}
