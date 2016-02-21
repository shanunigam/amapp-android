package org.anoopam.ext.smart.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;

import org.anoopam.ext.smart.framework.SmartApplication;

/**
 * This Class Contains All Method Related To IjoomerRadioButton.
 * 
 *
 * 
 */
public class SmartRadioButton extends AppCompatRadioButton {


	public SmartRadioButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SmartRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SmartRadioButton(Context context) {
		super(context);
		init(context);
	}

	private void init(Context mContext) {


		try {
			if (SmartApplication.REF_SMART_APPLICATION.FONT != null) {
				if (getTypeface() != null) {
					setTypeface(SmartApplication.REF_SMART_APPLICATION.FONT, getTypeface().getStyle());
				} else {
					setTypeface(SmartApplication.REF_SMART_APPLICATION.FONT);
				}
			} else {
				Typeface tf = Typeface.createFromAsset(mContext.getAssets(), SmartApplication.REF_SMART_APPLICATION.FONT_NAME);
				if (getTypeface() != null) {
					setTypeface(tf, getTypeface().getStyle());
				} else {
					setTypeface(SmartApplication.REF_SMART_APPLICATION.FONT);
				}
				SmartApplication.REF_SMART_APPLICATION.FONT=tf;
			}
		} catch (Throwable e) {
		}


	}

}
