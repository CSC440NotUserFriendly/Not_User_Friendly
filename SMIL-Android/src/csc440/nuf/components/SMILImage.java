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
import csc440.nuf.ViewMessageActivity;
import csc440.nuf.cache.SMILCache;
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
    private Bitmap scaled;
	//private final String QNAME = "img";
	protected String xmlid; //xml:id
	protected int zIndex; //z-index
	protected String src; //source
	protected int width;
	protected int height;

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
				src = ViewMessageActivity.getDir() + "/" + value;
			}
			else if(localName.equals("width")){
				width = getIntValue(value);
			}
			else if(localName.equals("height")){
				height = getIntValue(value);
			}
			
        }
	}
	
	public SMILImage() {
		top = 10;
		left = 10;
		src = null;
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
		System.out.println("SRC: " + src);
		original = BitmapFactory.decodeFile(src);
		scaled = Bitmap.createScaledBitmap(original, width, height, true);
	}
	
	public void draw(Canvas canvas) {
		if (begin != -1 &&
		  end != -1 &&
		  src != null) {
			System.out.println("Scaled" + scaled);
			setSrc(src); //Fixs reuse error
			System.out.println("Scaled" + scaled);
			super.draw(canvas);
			canvas.drawBitmap(scaled, left * Waiting.getDensity(), top * Waiting.getDensity(), null);
		}
	}
	
	@Override
	public int getWidth() {
		if (scaled == null) return 0;
		return scaled.getWidth();
	}

	@Override
	public int getHeight() {
		if (scaled == null) return 0;
		return scaled.getHeight();
	}

	@Override
	public void moveSize(int x, int y) {
		Rect d = getRectDimensions();
        width = x - d.left + 10;
        height = y - d.top + 10;

        scaled = Bitmap.createScaledBitmap(original, width, height, true);
	}

}
