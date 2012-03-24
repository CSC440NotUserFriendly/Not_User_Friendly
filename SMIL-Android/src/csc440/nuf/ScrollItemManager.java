package csc440.nuf;

/**
 * CSC-440 SMIL Project
 * 01-22-2011
 * ScrollItem.java
 * @author Jacob Ensor
 * 
 * This is a class that will manage ScrollItems, which is an included layout.
 * See res/layout/scroll_item.xml
 * In other XML's we <include layout="@layout/scroll_item" android:id="theID" />
 * Then we findViewById(R.id.theID) and pass that into the ScrollItem constructor.
 * 
 * 2-4-12 
 * Edited by: Jacob Ensor
 * File Created
 */

import java.util.LinkedList;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScrollItemManager {
	private LinearLayout _linear;
	private LinkedList<ScrollItem> items;
	private ScrollItem item;
	
	public ScrollItemManager(View v) {
		_linear = (LinearLayout) v.findViewById(R.id.scrollLinear);
		items = new LinkedList<ScrollItem>();
	}
	
	/*
	 * Adds a 1px high linear divider with a few pixel padding on the top and bottom
	 * I need to save this code, it took forever to figure out. Will come in handy for future projects.
	 */
	public void addItem(Context c) {
		addItem(c, false);
	}
	public void addItem(Context c, boolean useLinear) {
		/*
		if (!items.isEmpty()) {
			float density = c.getResources().getDisplayMetrics().density;
			LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(item.getLayoutParams());
			l.setMargins(0, (int)(10 * density), 0, (int)(10 * density));
			item.setLayoutParams(l);
		}*/
		// this is a little hackier than i would like but oh well, this block adds padding in between items
		if (!items.isEmpty()) {
			float density = c.getResources().getDisplayMetrics().density;
			View padding = new View(c);
			padding.setMinimumHeight((int) (20*density));
			_linear.addView(padding);
		}
		
		View item = ((LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
			.inflate(R.layout.scroll_item, null);
		
		ScrollItem si = new ScrollItem(item, useLinear);
		items.add(si);
		_linear.addView(item);
	}

	public void setIcon(int resId) {
		items.peek().setIcon(resId);
	}
	
	public void setListener(View.OnClickListener onClickListener) {
		items.peek().setListener(onClickListener);
	}
	
	public void setTitle(CharSequence title) {
		items.peek().setTitle(title);
	}

	public void setTitle(int resid) {
		items.peek().setTitle(resid);
	}
	
	public void addToLinear(View v) {
		items.peek().addToLinear(v);
	}
	
	public void addLine(Context c) {
		items.peek().addLineToLinear(c);
	}
}
