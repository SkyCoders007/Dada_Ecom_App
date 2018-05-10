package com.mxi.ecommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxi.ecommerce.R;

public class AccountInfoActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_toolbar_title;

    LinearLayout ln_order_tracking,ln_wishlist,ln_acc_info_address,ln_my_order,ln_send_feedback;
    LinearLayout img_back_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        init();
        clickListner();

        String title= getString(R.string.account_info);
        tv_toolbar_title.setText(title);
        tv_toolbar_title.setPadding(0, 0, 40, 0);
        Log.e("toolbar",title);
    }

    private void clickListner() {

     //   ln_order_tracking.setOnClickListener(this);
        ln_wishlist.setOnClickListener(this);
        ln_acc_info_address.setOnClickListener(this);
        ln_my_order.setOnClickListener(this);
        img_back_btn.setOnClickListener(this);
        ln_send_feedback.setOnClickListener(this);
    }

    private void init() {

        tv_toolbar_title=(TextView)findViewById(R.id.tv_toolbar_title);
       // ln_order_tracking=(LinearLayout) findViewById(R.id.ln_order_tracking);
        ln_wishlist=(LinearLayout) findViewById(R.id.ln_wishlist);
        ln_acc_info_address=(LinearLayout) findViewById(R.id.ln_acc_info_address);
        ln_my_order=(LinearLayout) findViewById(R.id.ln_my_order);
        img_back_btn=(LinearLayout) findViewById(R.id.img_back_btn);
        ln_send_feedback=(LinearLayout) findViewById(R.id.ln_send_feedback);


    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

//            case R.id.ln_order_tracking:
//
//                Intent intentOrderTrack = new Intent(AccountInfoActivity.this,OrderTrackingActivity.class);
//                startActivity(intentOrderTrack);
//                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//
//                break;


            case R.id.ln_wishlist:

                Intent intentWishList = new Intent(AccountInfoActivity.this,WishlistActivity.class);
                startActivity(intentWishList);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.ln_acc_info_address:

                Intent intentMyAddress = new Intent(AccountInfoActivity.this,MyAddressActivity.class);
                startActivity(intentMyAddress);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;


            case R.id.ln_my_order:

                Intent intentMyOrder = new Intent(AccountInfoActivity.this,MyOrderActivity.class);
                startActivity(intentMyOrder);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.img_back_btn:

                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                break;


            case R.id.ln_send_feedback:

                Intent sendfeedback = new Intent(AccountInfoActivity.this,SendFeedbackActivity.class);
                startActivity(sendfeedback);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
