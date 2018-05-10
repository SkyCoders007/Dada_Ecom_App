package com.mxi.ecommerce.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by mxicoders on 11/9/17.
 */

public class MyEdittextBold extends EditText {

    public MyEdittextBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyEdittextBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEdittextBold(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/WORKSANS-BOLD.TTF");
        setTypeface(tf, 1);

    }

}
