package csc440.nuf.components;

import org.xml.sax.Attributes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * CSC-440 SMIL Project
 * 01-26-2011
 * SMILText.java
 * @author Brad Barker
 * 
 * 
 */

public class SMILText extends AbstractSMILObject {
	
	//If we want to modify text here then we can make this a StringBuilder.
	protected String xmlid; //xml:id
	protected String textAlign; //textAlign
	protected int top; //topMargin
	protected int left; //leftMargin
	protected String textColor; //textColor
	protected int textFontSize; //textFontSize
	protected int zIndex; //z-index
	private String text; 
	
	public SMILText(int begin, int dur, int top, int left, String text, int textFontSize, String textColor) {
		super(text, begin, dur);
		this.top = top;
		this.left = left;
		this.text = text;
		this.textFontSize = textFontSize;
		this.textColor = textColor;
	}
	
	@Override
	public void draw(Canvas canvas) {
		// implement this later
		int color;
		if (textColor == "red") color = Color.RED;
		else if (textColor == "green") color = Color.GREEN;
		else if (textColor == "blue") color = Color.BLUE;
		else if (textColor == "yellow") color = Color.YELLOW;
		else if (textColor == "cyan") color = Color.CYAN;
		else color = Color.WHITE;
		
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(textFontSize);
        
		canvas.drawText(text, top, left, paint);
		Log.w("SMILText", "text was drawn: " + text);
	}
	
	public SMILText(Attributes att) {
		super(att); //For setting common elements
		qName = "smilText";
		
		for(int i=0; i<att.getLength(); i++){
			
			String localName = att.getLocalName(i);
			String value = att.getValue(localName);
			
			if(localName.equals("xml:id"))
				xmlid = value;
			
			else if(localName.equals("text"))
                text = value;
			
			else if(localName.equals("textAlign"))
                textAlign = value;
			
			else if(localName.equals("top"))
                top = getIntValue(value);
			
			else if(localName.equals("left"))
                left = getIntValue(value);
			
			else if(localName.equals("textColor"))
                textColor = value;
			
			else if(localName.equals("textFontSize"))
                textFontSize = getIntValue(value);
			
			else if(localName.equals("z-index")){
				zIndex = getIntValue(value);
			}
        }
	}
	
	public String getText() {
		return text;
	}
	
	//Important, the text is obtained through the characters() method not startElement()
	public void setText(String text) {
		this.text = text;
	}

	//For XML format
	@Override
	public String printTag() {

		return super.printTag() + ">\n" + text + "\n</smilText>\n";

	}


	public String getXmlid() {
		return xmlid;
	}

	public void setXmlid(String xmlid) {
		this.xmlid = xmlid;
	}

	public String getTextAlign() {
		return textAlign;
	}

	public void setTextAlign(String textAlign) {
		this.textAlign = textAlign;
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

	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	public int getTextFontSize() {
		return textFontSize;
	}

	public void setTextFontSize(int textFontSize) {
		this.textFontSize = textFontSize;
	}

	public int getzIndex() {
		return zIndex;
	}

	public void setzIndex(int zIndex) {
		this.zIndex = zIndex;
	}


}
