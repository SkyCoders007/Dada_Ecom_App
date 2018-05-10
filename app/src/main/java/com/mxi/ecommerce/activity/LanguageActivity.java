package com.mxi.ecommerce.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mxi.ecommerce.R;
import com.mxi.ecommerce.comman.CommanClass;

public class LanguageActivity extends AppCompatActivity implements View.OnClickListener {
    String title = "Language";
    String language = "";
    TextView tv_toolbar_title, tv_apply;
    LinearLayout img_back_btn, arabic, english;

    CommanClass cc;
    RadioButton rb_arbic, rb_english;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        cc = new CommanClass(LanguageActivity.this);

        language = cc.loadPrefString("language");

        init();
        clickListner();

        tv_toolbar_title.setText(title);
        tv_toolbar_title.setPadding(0, 0, 55, 0);
        Log.e("toolbar", title);
    }

    private void clickListner() {
        img_back_btn.setOnClickListener(this);
        arabic.setOnClickListener(this);
        english.setOnClickListener(this);

        rb_arbic.setOnClickListener(this);
        rb_english.setOnClickListener(this);
        tv_apply.setOnClickListener(this);

    }

    private void init() {
        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        tv_apply = (TextView) findViewById(R.id.tv_apply);
        img_back_btn = (LinearLayout) findViewById(R.id.img_back_btn);

        arabic = (LinearLayout) findViewById(R.id.aribic);
        english = (LinearLayout) findViewById(R.id.english);

        rb_arbic = (RadioButton) findViewById(R.id.rb_arabic);
        rb_english = (RadioButton) findViewById(R.id.rb_english);

        if (cc.loadPrefString("language").equals("arabi")) {
            cc.savePrefString("language", "arabi");
            rb_arbic.setChecked(true);
            rb_english.setChecked(false);

        } else {
            cc.savePrefString("language", "en-gb");
            rb_arbic.setChecked(false);
            rb_english.setChecked(true);

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.tv_apply:

                cc.savePrefString("language", language);

                String lan = "";

                if (language.equals("en-gb")) {
                    lan = "English";
                } else if (language.equals("arabi")) {
                    lan = "Arabic";
                }

                cc.showToast("Your Language is changed to " + lan + "");

                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

                break;

            case R.id.img_back_btn:

                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

                break;

            case R.id.rb_english:

                language = "en-gb";
//                cc.savePrefString("language", "en-gb");
                rb_arbic.setChecked(false);
                rb_english.setChecked(true);

                break;


            case R.id.rb_arabic:
                language = "arabi";
//                cc.savePrefString("language", "arabi");
                rb_english.setChecked(false);
                rb_arbic.setChecked(true);

                break;

            case R.id.english:

                language = "en-gb";
                rb_arbic.setChecked(false);
                rb_english.setChecked(true);

                break;


            case R.id.aribic:

                language = "arabi";
                rb_english.setChecked(false);
                rb_arbic.setChecked(true);

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
