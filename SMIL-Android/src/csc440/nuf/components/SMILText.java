package csc440.nuf.components;

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
	private String text; 
	
	public SMILText(int begin, int dur, int top, int left, String text, int textFontSize, String textColor) {
		super("smilText", begin, dur);
		qName = "smilText";
		this.top = top;
		this.left = left;
		this.text = text;
		this.textFontSize = textFontSize;
		this.textColor = textColor;
	}
	
	public void draw(Canvas canvas) {
		if (begin != -1 &&
		  end != -1 &&
		  text != null) {
			super.draw(canvas);
			canvas.drawText(text, left * Waiting.getDensity(), top * Waiting.getDensity(), getPaint());
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
		qName = "smilText";
		textColor = "white";
		textFontSize = 40;
		top = 40;
		left = 10;
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
        return (int) getPaint(false, false).measureText(text);
	}

	@Override
	public int getHeight() {
        return (int) (Math.abs(getPaint(false, false).ascent()));
	}

	
	public void moveSize(int x, int y) {
		Rect d = getRectDimensions();
		//Log.w("moveSize", "size x=" + x + ", y=" + y + ", left=" + d.left + ", right=" + d.right);
		
		// may need to tweak this, esp pay attention to where you multiply in the density and where you don't
		float temp = textFontSize * (float)((x-d.left)/(getWidth() * Waiting.getDensity()));
		//Log.w("moveSize", "newWidth=" + (x-left) + ", oldWidth=" + (getWidth() * Waiting.getDensity()) + ", textFontSize = " + textFontSize + " * (" + (float)((x-d.left)/(getWidth() * Waiting.getDensity())) + ") = " + temp);
		textFontSize = (int) (temp > 0 ? temp : 1);
	}

	private Paint getPaint() { return getPaint(true); }
	private Paint getPaint(boolean setColor) { return getPaint(setColor, true); }
	
	private Paint getPaint(boolean setColor, boolean useDensity) {
		Paint paint = new Paint();
		if (setColor) {
			int color;
			if (textColor.equals("red")) color = Color.RED;
			else if (textColor.equals("green")) color = Color.GREEN;
			else if (textColor.equals("blue")) color = Color.BLUE;
			else if (textColor.equals("yellow")) color = Color.YELLOW;
			else if (textColor.equals("cyan")) color = Color.CYAN;
			else if (textColor.equals("white")) color = Color.WHITE;
			else if (textColor.equals("light gray")) color = Color.LTGRAY;
			else if (textColor.equals("gray")) color = Color.GRAY;
			else if (textColor.equals("dark gray")) color = Color.DKGRAY;
			else if (textColor.equals("magenta")) color = Color.MAGENTA;
			else color = Color.WHITE;
	        paint.setColor(color);
		}
		
        if (useDensity) paint.setTextSize(textFontSize * Waiting.getDensity());
        else paint.setTextSize(textFontSize);
        paint.setAntiAlias(true);
        return paint;
	}
	
	@Override
	public Rect getRectDimensions() {
		Rect d = new Rect();
		d.left = (int) (left * Waiting.getDensity());
		d.top = (int) ((top - getHeight()) * Waiting.getDensity());
		d.right = (int) ((left + getWidth()) * Waiting.getDensity());
		d.bottom = (int) (top * Waiting.getDensity());
		//Log.w("rect", "left=" + d.left + ", top=" + d.top + ", right=" + d.right + ", bottom=" + d.bottom);
		return d;
	}
}
