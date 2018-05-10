package com.mxi.ecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mxi.ecommerce.R;


/**
 * Created by mxicoders on 14/7/17.
 */

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    String[] countryNames;
    LayoutInflater inflter;

    public SpinnerAdapter(Context applicationContext, String[] countryNames) {
        this.context = applicationContext;
        this.countryNames = countryNames;
        inflter = (LayoutInflater.from(applicationContext));
    }


    @Override
    public int getCount() {
        return countryNames.length;
    }

    @Override
    public Object getItem(int i) {
        return countryNames.length;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_state_spinner, null);
        TextView names = (TextView) view.findViewById(R.id.tvLanguage);
        names.setText(countryNames[i]);
        return view;
    }
}