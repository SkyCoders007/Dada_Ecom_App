package com.mxi.ecommerce.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by mxi on 26/12/17.
 */

public class MyTextviewExtraLight extends TextView {

    public MyTextviewExtraLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextviewExtraLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextviewExtraLight(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/WORKSANS-EXTRALIGHT.TTF");
        setTypeface(tf, 1);

    }

}

