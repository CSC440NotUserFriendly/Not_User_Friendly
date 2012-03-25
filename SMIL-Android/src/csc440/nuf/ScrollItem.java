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

public class ScrollItem {
	private RelativeLayout _relative;
	private ImageView _iconView;
	private TextView _titleView;
	private LinearLayout _linear;
	
	public ScrollItem(View v) {
		this(v, false);
	}
	
	public ScrollItem(View v, boolean useLinear) {
		_relative = (RelativeLayout) v;
		_iconView = (ImageView) v.findViewById(R.id.icon);
		_titleView = (TextView) v.findViewById(R.id.text);
		_linear = (LinearLayout) v.findViewById(R.id.linear);
		
		if (!useLinear) {
			RelativeLayout.LayoutParams l = (android.widget.RelativeLayout.LayoutParams) _titleView.getLayoutParams();
			l.addRule(RelativeLayout.CENTER_VERTICAL);
			_titleView.setLayoutParams(l);
		}
	}
	
	public void setIcon(int resId) {
		_iconView.setImageResource(resId);
		_iconView.setVisibility(View.VISIBLE);
	}
	
	public void setListener(View.OnClickListener onClickListener) {
		if (onClickListener != null) _relative.setOnClickListener(onClickListener);
	}
	
	public void setTitle(CharSequence title) {
		_titleView.setText(title);
	}

	public void setTitle(int resid) {
		_titleView.setText(resid);
	}
	
	public void addToLinear(View v) {
		_linear.addView(v, _linear.getChildCount());
	}
	
	/*
	 * Adds a 1px high linear divider with a few pixel padding on the top and bottom
	 * I need to save this code, it took forever to figure out. Will come in handy for future projects.
	 */
	public void addLineToLinear(Context c) {
		float density = c.getResources().getDisplayMetrics().density;
		View line = new View(c);
		LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, (int)(1 * density));
		l.setMargins(0, (int)(3* density), 0, (int)(3 * density));
		line.setLayoutParams(l);
		line.setBackgroundColor(R.color.scrollitem_stroke);
		addToLinear(line);
	}

	public int getId() {
		return _relative.getId();
	}
	
	public void setId(int id) {
		_relative.setId(id);
	}
}
