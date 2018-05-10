package com.mxi.ecommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxi.ecommerce.R;
import com.mxi.ecommerce.comman.CommanClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ProceedPaymentActivity extends AppCompatActivity implements View.OnClickListener{
    TextView tv_toolbar_title,tv_date;
    CommanClass cc;
    String title= "Thank you";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";

    LinearLayout ln_procecd;

    DateFormat dateFormat;
    LinearLayout img_back_btn;


    TextView tv_thankyou_message,tv_order_id;

    String thankyou_message,order_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed_payment);
        cc=new CommanClass(this);
        init();
        clickListner();


        thankyou_message=cc.loadPrefString("thank_you_message");
        order_id=cc.loadPrefString("thank_you_order_id");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, +7);
        Date newDate = calendar.getTime();

        Log.e("date",newDate+"");


        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        String outputDateStr = outputFormat.format(newDate);

        Log.e("date",outputDateStr);

        tv_date.setText(outputDateStr);
        tv_thankyou_message.setText(thankyou_message);
        tv_order_id.setText("Order Id:-"+""+order_id);

        tv_toolbar_title.setText(title);
        tv_toolbar_title.setPadding(0, 0, 40, 0);
        Log.e("toolbar",title);

    }

    private void clickListner() {
        ln_procecd.setOnClickListener(this);
        img_back_btn.setOnClickListener(this);
        img_back_btn.setVisibility(View.GONE);
    }

    private void init() {

        tv_toolbar_title=(TextView)findViewById(R.id.tv_toolbar_title);
        tv_date=(TextView)findViewById(R.id.tv_date);
        ln_procecd=(LinearLayout) findViewById(R.id.ln_procecd);
        img_back_btn = (LinearLayout) findViewById(R.id.img_back_btn);

        tv_thankyou_message=(TextView)findViewById(R.id.tv_thankyou_message);
        tv_order_id=(TextView)findViewById(R.id.tv_order_id);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ln_procecd:

                Intent ln_procecd =new Intent(ProceedPaymentActivity.this,MainActivity.class);
                startActivity(ln_procecd);

                break;

            case R.id.img_back_btn:

                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

                break;
        }
    }
}
