package csc440.nuf.complay;

import csc440.nuf.R;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

class HideControlsListener implements OnTouchListener {
	private View controls;
	private long lastTouched;
	private final int SECONDS_TO_SHOW = 3;
	private boolean override = false;
	
	HideControlsListener(View controls) {
		this.controls = controls;
		if (getControlsVisible()) resetTime();
	}

	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
			//Log.w("listener", v.getId() + " == " + R.id.playerCanvas);
			if (v.getId() == R.id.playerCanvas) toggleControlsVisible();
			else if (!getControlsVisible()) setControlsVisible(true);
		}
		
		// return false if we didn't handle the touch entirely
		return true;
	}

	public void checkControls() {
		//Log.w("listener", "checkControls fired");
		if (getControlsVisible() && !override) {
			float deltaTime = (System.nanoTime()-lastTouched) / 1000000000.0f;
			if (deltaTime > SECONDS_TO_SHOW) setControlsVisible(false);
		}

		//Log.w("controls checked", "value: " + getControlsVisible());
	}

	public void resetTime() {
		lastTouched = System.nanoTime();
	}
	
	public void setOverride(boolean override) {
		this.override = false;
		if (override) setControlsVisible(true);
		else resetTime();
	}
	
	public boolean getOverride() {
		return override;
	}
	
	public boolean getControlsVisible() {
		return (controls.getVisibility() == View.VISIBLE);
	}
	
	public void setControlsVisible(boolean visible) {
		if (visible) {
			controls.setVisibility(View.VISIBLE);
			resetTime();
		} else {
			controls.setVisibility(View.INVISIBLE);
		}
		//Log.w("listener", "controls visibility set to: " + getControlsVisible());
	}
	
	public void toggleControlsVisible() {
		setControlsVisible(!getControlsVisible());
		if (getControlsVisible() && !override) resetTime();
	}
}