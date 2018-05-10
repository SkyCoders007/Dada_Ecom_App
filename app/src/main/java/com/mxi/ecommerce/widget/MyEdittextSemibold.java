package com.mxi.ecommerce.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by mxi on 26/12/17.
 */

public class MyEdittextSemibold extends EditText {

    public MyEdittextSemibold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyEdittextSemibold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEdittextSemibold(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/WORKSANS-SEMIBOLD.TTF");
        setTypeface(tf, 1);

    }

}
