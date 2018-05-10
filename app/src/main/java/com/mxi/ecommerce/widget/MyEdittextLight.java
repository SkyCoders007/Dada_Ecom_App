package com.mxi.ecommerce.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by mxi on 26/12/17.
 */

public class MyEdittextLight extends EditText {

    public MyEdittextLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyEdittextLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEdittextLight(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/WORKSANS-LIGHT.TTF");
        setTypeface(tf, 1);

    }

}
