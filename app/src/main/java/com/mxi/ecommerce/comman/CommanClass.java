package com.mxi.ecommerce.comman;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.mxi.ecommerce.R;
import com.mxi.ecommerce.activity.LoginActivity;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CommanClass {

    private Context _context;
    SharedPreferences pref;
    SharedPreferences pref1;
    SharedPreferences pref2;
    SharedPreferences pref3;

    public CommanClass(Context context) {
        this._context = context;

        pref = _context.getSharedPreferences("1811",
                _context.MODE_PRIVATE);

        pref1 = _context.getSharedPreferences("18112",
                _context.MODE_PRIVATE);

        pref2 = _context.getSharedPreferences("18113",_context.MODE_PRIVATE);


        pref3 = _context.getSharedPreferences("18114",_context.MODE_PRIVATE);


    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public void showToast(String text) {
        // TODO Auto-generated method stub
        Toast.makeText(_context, text, Toast.LENGTH_SHORT).show();
    }

    public void showSnackbar(View coordinatorLayout, String text) {

        Snackbar
                .make(coordinatorLayout, text, Snackbar.LENGTH_LONG).show();
    }

    public void savePrefString(String key, String value) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void savePrefString3(String key, String value) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref3.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public void savePrefString1(String key, String value) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref1.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void savePrefString2(String key, String value) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref2.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public void savePrefInteger(String key, int value) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
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

    public String loadPrefString3(String key) {
        // TODO Auto-generated method stub
        String strSaved = pref3.getString(key, "");
        return strSaved;
    }

    public String loadPrefString1(String key) {
        // TODO Auto-generated method stub
        String strSaved = pref1.getString(key, "");
        return strSaved;
    }

    public String loadPrefString2(String key) {
        // TODO Auto-generated method stub
        String strSaved = pref2.getString(key, "");
        return strSaved;
    }

    public int loadPrefInt(String key) {
        // TODO Auto-generated method stub
        int strSaved = pref.getInt(key,0);
        return strSaved;
    }

    public Boolean loadPrefBoolean(String key) {
        // TODO Auto-generated method stub
        boolean isbool = pref.getBoolean(key, false);
        return isbool;
    }

    public void logoutapp() {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public void logoutapp1() {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref1.edit();
        editor.clear();
        editor.commit();
    }

    public void logoutapp2() {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref2.edit();
        editor.clear();
        editor.commit();
    }

    public String MyText(String text) {
        String s = "";
        try {
            s = new String(text.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s;

    }

    @SuppressLint("SimpleDateFormat")
    public String dateConvert(String timestmp)
            throws ParseException {
        String str_date_to;
        Date date = null;
        SimpleDateFormat formate = new SimpleDateFormat("MM-dd-yyyy");
        try {
            date = formate.parse(timestmp);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SimpleDateFormat formate_to = new SimpleDateFormat("dd MMM yyyy");
        str_date_to = formate_to.format(date);

        return str_date_to;
    }

    public void showDialogUserNotLogin() {
        savePrefBoolean("isOfflineAlertOn", true);
        final Dialog dialog = new Dialog(_context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_logout);

        TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
        TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);

        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent i = new Intent(_context, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                _context.startActivity(i);
            }
        });

        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
