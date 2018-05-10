package com.mxi.ecommerce.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.mxi.ecommerce.comman.CommanClass;
import com.mxi.ecommerce.comman.CommanClassForLanguage;
import com.mxi.ecommerce.R;

public class PopLanguageScreen extends AppCompatActivity implements View.OnClickListener{

    RadioButton rb_arbic,rb_english;
    private static int SPLASH_TIME_OUT = 1000;
    CommanClass cc;
    CommanClassForLanguage ccl;
    String language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poplanguge_screen);


        cc = new CommanClass(this);
        ccl = new CommanClassForLanguage(this);


        init();
        clickListner();
        showForgotPasswordPopup();


    }


    private void clickListner() {
    }

    private void init() {
    }

    private void showForgotPasswordPopup() {
        final Dialog dialog = new Dialog(PopLanguageScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup_dialogs);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);

        final LinearLayout ln_arabics = (LinearLayout) dialog.findViewById(R.id.ln_aribic);
        final LinearLayout ln_english = (LinearLayout) dialog.findViewById(R.id.ln_english);

        final  RadioButton radioButton_arabic=(RadioButton) dialog.findViewById(R.id.rb_arabic);
        final  RadioButton radioButton_english=(RadioButton) dialog.findViewById(R.id.rb_english);

        ln_arabics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String arbi="arabi";
                cc.savePrefString("language",arbi);
                Log.e("ArbiLanguage",arbi);

                radioButton_english.setChecked(false);
                radioButton_arabic.setChecked(true);


                new Handler().postDelayed(new Runnable() {

                    //

                    //* Showing splash screen with a timer. This will be useful when you

                    //* want to show case your app logo / company

                    //

                    @Override
                    public void run() {

                        Intent intentLogin = new Intent(PopLanguageScreen.this, LoginActivity.class);
                        startActivity(intentLogin);
                        finish();

                    }
                }, SPLASH_TIME_OUT);

            }
        });

        ln_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String english="en-gb";
                cc.savePrefString("language",english);
                Log.e("EglishLanguage",english);

                radioButton_arabic.setChecked(false);
                radioButton_english.setChecked(true);

                new Handler().postDelayed(new Runnable() {

                    //

                    //* Showing splash screen with a timer. This will be useful when you

                    //* want to show case your app logo / company

                    //

                    @Override
                    public void run() {

                        Intent intentLogin1 = new Intent(PopLanguageScreen.this, LoginActivity.class);
                        startActivity(intentLogin1);
                        finish();

                        dialog.dismiss();
                    }
                }, SPLASH_TIME_OUT);


            }
        });

        radioButton_arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                radioButton_english.setChecked(false);
                radioButton_arabic.setChecked(true);

                String arbi="arabi";
                cc.savePrefString("language",arbi);
                Log.e("ArbiLanguage",arbi);

                Intent intentLogin2 = new Intent(PopLanguageScreen.this,LoginActivity.class);
                startActivity(intentLogin2);
                finish();

                dialog.dismiss();

            }
        });


        radioButton_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                radioButton_arabic.setChecked(false);
                radioButton_english.setChecked(true);


                String english="en-gb";
                cc.savePrefString("language",english);
                Log.e("EglishLanguage",english);

                Intent intentLogin3 = new Intent(PopLanguageScreen.this,LoginActivity.class);
                startActivity(intentLogin3);
                finish();

                dialog.dismiss();

            }
        });



        dialog.show();
    }

    @Override
    protected void onResume() {


        super.onResume();

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {


//            case R.id.english:
//
//
//                rb_arbic.setChecked(false);
//                rb_english.setChecked(true);
//
//                break;
//
//
//            case R.id.aribic:c/ds,
//
//                rb_english.setChecked(false);
//                rb_arbic.setChecked(true);
//
//                break;

        }
        }

}


