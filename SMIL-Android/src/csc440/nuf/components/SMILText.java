package csc440.nuf.components;

import java.lang.reflect.Field;

import org.xml.sax.Attributes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import csc440.nuf.complay.Waiting;

/**
 * CSC-440 SMIL Project
 * 01-26-2011
 * SMILText.java
 * @author Brad Barker
 * 
 * 
 */

public class SMILText extends AbstractSMILDrawable {
	
	//If we want to modify text here then we can make this a StringBuilder.
	protected String xmlid; //xml:id
	protected String textAlign; //textAlign
	protected String textColor; //textColor
	protected int textFontSize; //textFontSize
	protected String text; 
	
	public SMILText(int begin, int dur, int top, int left, String text, int textFontSize, String textColor) {
		super(text, begin, dur);
		this.top = top;
		this.left = left;
		this.text = text;
		this.textFontSize = textFontSize;
		this.textColor = textColor;
	}
	
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (text != null) {
			int color;
			if (textColor.equals("red")) color = Color.RED;
			else if (textColor.equals("green")) color = Color.GREEN;
			else if (textColor.equals("blue")) color = Color.BLUE;
			else if (textColor.equals("yellow")) color = Color.YELLOW;
			else if (textColor.equals("cyan")) color = Color.CYAN;
			else if (textColor.equals("white")) color = Color.WHITE;
			else color = Color.WHITE;
			
	        Paint paint = new Paint();
	        paint.setColor(color);
	        paint.setTextSize(textFontSize * Waiting.getDensity());
	       // canvas.drawText
			canvas.drawText(text, left * Waiting.getDensity(), top * Waiting.getDensity(), paint);
		}
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
	
	public SMILText() {
		textColor = "white";
		textFontSize = 40;
		top = 30;
		left = 30;
	}

	public String getText() {
		return text;
	}
	
	//Important, the text is obtained through the characters() method not startElement()
	public void setText(String text) {
		this.text = text;
		this.qName = text;
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

	@Override
	public int getWidth() {
        Paint paint = new Paint();
        paint.setTextSize(textFontSize);
        return (int) paint.measureText(text);
	}

	@Override
	public int getHeight() {
        Paint paint = new Paint();
        paint.setTextSize(textFontSize);
        return (int) (Math.abs(paint.ascent()));
	}

	
	public void moveSize(int x, int y) {
		Rect d = getRectDimensions();
		//Log.w("moveSize", "size x=" + x + ", y=" + y + ", left=" + d.left + ", right=" + d.right);
		
		// may need to tweak this, esp pay attention to where you multiply in the density and where you don't
		float temp = textFontSize * (float)((x-left)/(getWidth() * Waiting.getDensity()));
		//Log.w("moveSize", "newWidth=" + (x-left) + ", oldWidth=" + (getWidth() * Waiting.getDensity()) + ", textFontSize = " + textFontSize + " * (" + (float)((x-d.left)/(getWidth() * Waiting.getDensity())) + ") = " + temp);
		textFontSize = (int) (temp > 0 ? temp : 1);
	}
}
