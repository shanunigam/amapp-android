package org.anoopam.ext.smart.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * This Class Contains All Method Related To IjoomerHorizontalScroll.
 * 
 *
 * 
 */
public class SmartHorizontalScroll extends HorizontalScrollView {

	
	/**
	 * Overrides methods
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}
	
	public SmartHorizontalScroll(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SmartHorizontalScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SmartHorizontalScroll(Context context) {
		super(context);
		init(context);
	}

	void init(Context context) {
		setHorizontalFadingEdgeEnabled(false);

		setVerticalFadingEdgeEnabled(false);

		setVerticalScrollBarEnabled(false);

		setHorizontalScrollBarEnabled(false);

	}

}
