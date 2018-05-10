package com.mxi.ecommerce.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by mxi on 26/12/17.
 */

public class MyEdittextThin extends EditText {

    public MyEdittextThin(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyEdittextThin(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEdittextThin(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/WORKSANS-THIN.TTF");
        setTypeface(tf, 1);

    }

}
