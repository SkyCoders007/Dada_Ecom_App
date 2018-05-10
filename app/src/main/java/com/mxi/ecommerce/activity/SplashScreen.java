package com.mxi.ecommerce.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.ecommerce.R;
import com.mxi.ecommerce.comman.AppController;
import com.mxi.ecommerce.comman.CommanClass;
import com.mxi.ecommerce.comman.ConnectionUrl;
import com.mxi.ecommerce.comman.PersistentCookieStore;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    int PERMISSION_ALL = 1;
    CommanClass cc;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        cc = new CommanClass(this);

//        CookieManager cookieManager = new CookieManager(new PersistentCookieStore(this),
//                CookiePolicy.ACCEPT_ORIGINAL_SERVER);
//        CookieHandler.setDefault(cookieManager);



        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkReadWritePermission();
        } else {
            CountDown();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    CountDown();
                } else {
                    //  Toast.makeText(NewPrescriptionRequest.this, "WRITE_CONTACTS Denied", Toast.LENGTH_SHORT).show();
                    showErrorDialog("Please allow the permission for better performance", true);
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void checkReadWritePermission() {
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                return;
            }
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        } else {
            CountDown();
        }
    }


    private void CountDown() {
        if(cc.loadPrefString3("PHPSESSID").isEmpty() && cc.loadPrefString3("default").isEmpty()){
            CookieManager cookieManager = new CookieManager(new PersistentCookieStore(SplashScreen.this),
                    CookiePolicy.ACCEPT_ORIGINAL_SERVER);
            CookieHandler.setDefault(cookieManager);
        }

        if(cc.isConnectingToInternet()){
            makeJsonCallCartCounter();
        }

        new Handler().postDelayed(new Runnable() {




            @Override
            public void run() {

                if(cc.isConnectingToInternet()){

//                    makeJsonCallCartCounter();

                    if(cc.loadPrefString3("PHPSESSID").isEmpty() && cc.loadPrefString3("default").isEmpty()){
                        CookieManager cookieManager = new CookieManager(new PersistentCookieStore(SplashScreen.this),
                                CookiePolicy.ACCEPT_ORIGINAL_SERVER);
                        CookieHandler.setDefault(cookieManager);
                    }
                    if(cc.loadPrefBoolean("isLogin")){

                        Intent intentMainActivity = new Intent(SplashScreen.this, MainActivity.class);
                        intentMainActivity.putExtra("LoginMain","loginmain");
                        intentMainActivity.putExtra("EXTRA","openFragment2");
                        startActivity(intentMainActivity);
                        finish();

                    }else{

                        Intent intentPopLanguage = new Intent(SplashScreen.this, PopLanguageScreen.class);
                        intentPopLanguage.putExtra("EXTRA","openFragment2");
                        startActivity(intentPopLanguage);
                        finish();
                    }

                }else{
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashScreen.this);
                    alertDialogBuilder
                            .setMessage(R.string.dialog_internet_alert)
                            .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

            }
        }, SPLASH_TIME_OUT);

    }


    public void showErrorDialog(String msg, final boolean isFromPermission) {
        progressBar.setVisibility(View.INVISIBLE);
        AlertDialog.Builder alert = new AlertDialog.Builder(SplashScreen.this);
        alert.setTitle("MY INNER PHARMACY");
        alert.setMessage(msg);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (isFromPermission) {
                    checkReadWritePermission();
                } else {
                    dialog.dismiss();
                }
            }
        });
        alert.show();
    }

    private void makeJsonCallCartCounter() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, ConnectionUrl.CartCounter,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:cart_counter", response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error_cart_counter", error + "");
            }
        }) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

//                String resHead=response.headers.get("1811-token");
//                Log.e("@@resHead",resHead+"");
//                cc.savePrefString("1811-token",resHead);

                Log.i("response",response.headers.toString());
//                Map<String, String> responseHeaders = response.headers;
//                String rawCookies = responseHeaders.get("Set-Cookie");
//                Log.i("cartcounter_cookies",rawCookies);
                return super.parseNetworkResponse(response);

            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("language", cc.loadPrefString("language"));
                headers.put("1811-token", cc.loadPrefString("1811-token"));
//                headers.put("Cookie", "PHPSESSID="+cc.loadPrefString3("PHPSESSID")+";"+"default="+cc.loadPrefString3("default"));
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");


    }



}
