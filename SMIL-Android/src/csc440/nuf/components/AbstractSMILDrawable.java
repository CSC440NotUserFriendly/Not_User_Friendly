package csc440.nuf.components;

import org.xml.sax.Attributes;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import csc440.nuf.complay.Waiting;


public abstract class AbstractSMILDrawable extends AbstractSMILObject {
	protected int top, //topMargin
		left, //leftMargin
		zIndex; //z-index
	private final int FATFINGER = 10;
	private final int CORNERSIZE = 30;
	
	public AbstractSMILDrawable(String name, int begin, int dur) {
		super(name, begin, dur);
	}

	public AbstractSMILDrawable(Attributes att) {
		super(att);
	}
	
	public AbstractSMILDrawable() {
	}

	public void draw(Canvas canvas) {
		if (Waiting.isActive(this)) {
			Rect d = getRectDimensions();
			d.left = d.right - CORNERSIZE;
			d.top = d.bottom - CORNERSIZE;
			
			Paint rect = new Paint();
			rect.setColor(Color.WHITE);
			canvas.drawRect(d, rect);
			
			DashPathEffect dashPath = new DashPathEffect(new float[]{10,10}, 1);
			rect.setPathEffect(dashPath);
			rect.setStyle(Paint.Style.STROKE);
			rect.setStrokeWidth(3 * Waiting.getDensity());
			
			canvas.drawRect(getRectDimensions(), rect);
			//canvas.drawRect(left * Waiting.getDensity(), (top-getHeight()) * Waiting.getDensity(),
			//	(left+getWidth()) * Waiting.getDensity(), top * Waiting.getDensity(), rect);
		}
	}
	
	public int getTopMargin() {
		return top;
	}

	public void setTopMargin(int topMargin) {
		this.top = topMargin;
	}

	public int moveTopMargin(int i) {
		top += i;
		return top;
	}

	public int getLeftMargin() {
		return left;
	}

	public void setLeftMargin(int leftMargin) {
		this.left = leftMargin;
	}
	
	public int moveLeftMargin(int i) {
		left += i;
		return left;
	}
	
	public int getzIndex() {
		return zIndex;
	}

	public void setzIndex(int zIndex) {
		this.zIndex = zIndex;
	}
	
	public Rect getRectDimensions() {
		Rect d = new Rect();
		d.left = (int) (left * Waiting.getDensity());
		d.top = (int) ((top - getHeight()) * Waiting.getDensity());
		d.right = (int) ((left + getWidth()) * Waiting.getDensity());
		d.bottom = (int) (top * Waiting.getDensity());
		Log.w("rect", "left=" + d.left + ", top=" + d.top + ", right=" + d.right + ", bottom=" + d.bottom);
		return d;
	}
	
	public boolean checkCollision(int x, int y) {
		Rect d = getRectDimensions();
		if (x > (d.left - FATFINGER) &&
			x < (d.right + FATFINGER) &&
			y > (d.top - FATFINGER) &&
			y < (d.bottom + FATFINGER))
			return true;
		else
			return false;
	}
	
	public boolean checkCornerCollision(int x, int y) {
		Rect d = getRectDimensions();
		if (x > (d.right - CORNERSIZE - FATFINGER) &&
				x < (d.right + FATFINGER) &&
				y > (d.bottom - CORNERSIZE - FATFINGER) &&
				y < (d.bottom + FATFINGER))
			return true;
		else
			return false;
	}

	public abstract int getWidth();
	public abstract int getHeight();
	public abstract void moveSize(int x, int y);
}