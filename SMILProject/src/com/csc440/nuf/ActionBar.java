package com.csc440.nuf;

/**
 * CSC-440 SMIL Project
 * 01-22-2011
 * ActionBar.java
 * @author Jacob Ensor
 * 
 * This is an ActionBar implementation that should work in GB and HC.
 * It extends RelativeLayout. This bar will be added to most if not all of our Activities.
 * The original code for this comes from:
 * 		http://thiranjith.wordpress.com/2011/07/15/actionbar-design-pattern-example-for-android/
 * I don't know how similar it'll be by the time I'm done, but still wanted to credit the source.
 * 
 * 2-4-12 
 * Edited by: Jacob Ensor
 * File Created
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActionBar extends RelativeLayout {
	private LayoutInflater mInflater;		// Reusable {@link LayoutInflater}
	private ImageView mLogoView;			// Holds the home-icon logo
	private TextView mTitleView;			// Displays the {@link Activity} text
	private ProgressBar mProgress;			// Represents the progress bar (i.e. busy-icon)
	private LinearLayout mActionIconContainer;	// Contains the ActionIcons.

	public ActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);

		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		RelativeLayout barView = (RelativeLayout) mInflater.inflate(R.layout.actionbar, null);
		addView(barView);

		mLogoView = (ImageView) barView.findViewById(R.id.actionbar_home_logo);
		mProgress = (ProgressBar) barView.findViewById(R.id.actionbar_progress);
		mTitleView = (TextView) barView.findViewById(R.id.actionbar_title);
		mActionIconContainer = (LinearLayout) barView.findViewById(R.id.actionbar_actionIcons);
		
		mLogoView.setImageResource(R.drawable.ic_home);
		mLogoView.setVisibility(View.VISIBLE);
	}
	
	public void setHomeLogo(int resId) {
		mLogoView.setImageResource(resId);
	}
	
	/*
	public void setHomeLogo(int resId, OnClickListener onClickListener) {
		mLogoView.setImageResource(resId);
		mLogoView.setVisibility(View.VISIBLE);
		mLogoView.setOnClickListener(onClickListener);
		if (onClickListener != null) {
		}
	}
	*/
	
	public void setHomeListener(OnClickListener onClickListener) {
		if (onClickListener != null) mLogoView.setOnClickListener(onClickListener);
	}

	public void setTitle(CharSequence title) {
		mTitleView.setText(title);
	}

	public void setTitle(int resid) {
		mTitleView.setText(resid);
	}

	public void showProgressBar() {
		setProgressBarVisibility(View.VISIBLE);
	}

	public void hideProgressBar() {
		setProgressBarVisibility(View.GONE);
	}

	/**
	 * Adds ActionIcons to the ActionBar (adds to the left-end)
	 * 
	 * @param iconResourceId
	 * @param onClickListener to handle click actions on the ActionIcon.
	 */
	public void addActionIcon(int iconResourceId, OnClickListener onClickListener) {
		// Inflate
		View view = mInflater.inflate(R.layout.actionbar_icon, mActionIconContainer, false);
		ImageButton imgButton = (ImageButton) view.findViewById(R.id.actionbar_item);
		imgButton.setImageResource(iconResourceId);
		imgButton.setOnClickListener(onClickListener);

		mActionIconContainer.addView(view, mActionIconContainer.getChildCount());
	}

	/**
	 * Remove the action icon from the given index (0 based)
	 * 
	 * @param index
	 * @return <code>true</code> if the item was removed
	 */
	public boolean removeActionIconAt(int index) {
		int count = mActionIconContainer.getChildCount();
		if (count > 0 && index >= 0 && index < count) {
			mActionIconContainer.removeViewAt(index);
			return true;
		}
		return false;
	}

	/**
	 * @return <code>true</code> if the progressbar is visible
	 * @see View#VISIBLE
	 */
	public boolean isProgressBarVisible() {
		return mProgress.getVisibility() == View.VISIBLE;
	}

	/**
	 * Set the enabled state of the progress bar.
	 * 
	 * @param One of {@link View#VISIBLE}, {@link View#INVISIBLE}, or {@link View#GONE}.
	 */
	private void setProgressBarVisibility(int visibility) {
		mProgress.setVisibility(visibility);
	}
}
