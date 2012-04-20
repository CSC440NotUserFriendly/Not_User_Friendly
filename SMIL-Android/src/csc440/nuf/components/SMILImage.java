package csc440.nuf.components;

import org.xml.sax.Attributes;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import csc440.nuf.R;
import csc440.nuf.complay.Waiting;

/**
 * CSC-440 SMIL Project
 * 01-26-2011
 * SMILImage.java
 * @author Brad Barker
 * 
 * 
 */

public class SMILImage extends AbstractSMILDrawable {
    private Bitmap original;
    private Bitmap myImg;
	//private final String QNAME = "img";
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
	
	public SMILImage(Resources res) {
		original = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
		top = 100;
		left = 150;
		src = "launcher";
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
	
	public void draw(Canvas canvas) {
		if (begin != -1 &&
		  end != -1 &&
		  src != null) {
			super.draw(canvas);
			canvas.drawBitmap(original, left * Waiting.getDensity(), top * Waiting.getDensity(), null);
		}
	}
	
	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return original.getWidth();
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return original.getHeight();
	}

	@Override
	public void moveSize(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Rect getRectDimensions() {
		Rect d = new Rect();
		d.left = (int) (left * Waiting.getDensity());
		d.top = (int) ((top) * Waiting.getDensity());
		d.right = (int) ((left + getWidth()) * Waiting.getDensity());
		d.bottom = (int) ((top + getHeight()) * Waiting.getDensity());
		Log.w("rect", "left=" + d.left + ", top=" + d.top + ", right=" + d.right + ", bottom=" + d.bottom);
		return d;
	}
}
