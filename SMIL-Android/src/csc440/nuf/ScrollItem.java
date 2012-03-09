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

import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScrollItem {
	private RelativeLayout _relative;
	private ImageView _iconView;
	private TextView _titleView;
	private LinearLayout _linear;
	
	public ScrollItem(View v) {
		_relative = (RelativeLayout) v;
		_iconView = (ImageView) v.findViewById(R.id.icon);
		_titleView = (TextView) v.findViewById(R.id.text);
		_linear = (LinearLayout) v.findViewById(R.id.linear);
	}
	
	public void setIcon(int resId) {
		_iconView.setImageResource(resId);
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
	
	public void addToLinear(View v, OnClickListener onClickListener) {
		_linear.addView(v, _linear.getChildCount());
	}
}
