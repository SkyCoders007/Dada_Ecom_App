package com.mxi.ecommerce.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by mxi on 26/12/17.
 */

public class MyEdittextMedium extends EditText {

    public MyEdittextMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyEdittextMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEdittextMedium(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/WORKSANS-MEDIUM.TTF");
        setTypeface(tf, 1);

    }

}
