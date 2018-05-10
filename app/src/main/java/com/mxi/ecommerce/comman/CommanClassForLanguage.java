package com.mxi.ecommerce.comman;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by aksahy on 26/8/17.
 */

public class CommanClassForLanguage {
    private Context _context;
    SharedPreferences pref;

    public CommanClassForLanguage(Context context) {
        this._context = context;

        pref = _context.getSharedPreferences("1811 Ecommerce",
                _context.MODE_PRIVATE);
    }


    public String savePrefString(String key, String value) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
        return key;
    }

    public void savePrefBoolean(String key, Boolean value) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String loadPrefString(String key) {
        // TODO Auto-generated method stub
        String strSaved = pref.getString(key, "");
        return strSaved;
    }

    public Boolean loadPrefBoolean(String key) {
        // TODO Auto-generated method stub
        boolean isbool = pref.getBoolean(key, false);
        return isbool;
    }
}
