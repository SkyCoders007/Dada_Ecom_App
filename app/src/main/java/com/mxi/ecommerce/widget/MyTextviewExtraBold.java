package com.mxi.ecommerce.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by mxi on 26/12/17.
 */

public class MyTextviewExtraBold extends TextView {

    public MyTextviewExtraBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextviewExtraBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextviewExtraBold(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/WORKSANS-EXTRABOLD.TTF");
        setTypeface(tf, 1);

    }

}

