package com.mxi.ecommerce.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by mxicoders on 11/9/17.
 */

public class MyButtonLight extends Button {

    public MyButtonLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyButtonLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyButtonLight(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/WORKSANS-LIGHT.TTF");
        setTypeface(tf, 1);

    }

}
