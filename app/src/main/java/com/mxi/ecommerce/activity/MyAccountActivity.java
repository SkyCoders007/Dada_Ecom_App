package com.mxi.ecommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxi.ecommerce.R;


public class MyAccountActivity extends AppCompatActivity implements View.OnClickListener {

    String title= "My account";
    TextView tv_toolbar_title;

    LinearLayout ln_change_password,ln_edit_profile,ln_acc_info;

    LinearLayout ln_home_bottom,ln_category_bottom,ln_deal_bottom,ln_wishlist_bottom,ln_mycc_bottom;

    LinearLayout img_back_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);


        init();
        clickListner();

        tv_toolbar_title.setText(title);
        tv_toolbar_title.setPadding(0, 0, 40, 0);
        Log.e("toolbar",title);


        img_back_btn.setVisibility(View.GONE);
    }

    private void clickListner() {

        ln_change_password.setOnClickListener(this);
        ln_edit_profile.setOnClickListener(this);
        ln_acc_info.setOnClickListener(this);
        img_back_btn.setOnClickListener(this);

        ln_home_bottom.setOnClickListener(this);
        ln_deal_bottom.setOnClickListener(this);
        ln_wishlist_bottom.setOnClickListener(this);
        ln_mycc_bottom.setOnClickListener(this);
        ln_category_bottom.setOnClickListener(this);
    }

    private void init() {

        tv_toolbar_title=(TextView)findViewById(R.id.tv_toolbar_title);
        ln_change_password=(LinearLayout)findViewById(R.id.ln_change_password);
        ln_edit_profile=(LinearLayout)findViewById(R.id.ln_edit_profile);
        ln_acc_info=(LinearLayout)findViewById(R.id.ln_acc_info);
        img_back_btn=(LinearLayout) findViewById(R.id.img_back_btn);

        ln_home_bottom=(LinearLayout)findViewById(R.id.ln_home_bottom);
        ln_category_bottom=(LinearLayout)findViewById(R.id.ln_category_bottom);
        ln_deal_bottom=(LinearLayout)findViewById(R.id.ln_deal_bottom);
        ln_mycc_bottom=(LinearLayout)findViewById(R.id.ln_mycc_bottom);
        ln_wishlist_bottom=(LinearLayout)findViewById(R.id.ln_wishlist_bottom);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ln_home_bottom:


                Intent intentMainActivity = new Intent(MyAccountActivity.this,MainActivity.class);
                intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                overridePendingTransition(0,0);
                startActivity(intentMainActivity);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();


                break;

            case R.id.ln_category_bottom:

                Intent intentShopCategory = new Intent(MyAccountActivity.this,ShopCategoryActivity.class);
                startActivity(intentShopCategory);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.ln_mycc_bottom:


                break;

            case R.id.ln_wishlist_bottom:

                Intent intentWishList = new Intent(MyAccountActivity.this,WishlistActivity.class);
                startActivity(intentWishList);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.ln_deal_bottom:

                Intent intentMyAccount = new Intent(MyAccountActivity.this,TopDealDetailActivity.class);
                startActivity(intentMyAccount);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.ln_change_password:

                Intent intentChangePass = new Intent(MyAccountActivity.this,ChangePasswordActivity.class);
                startActivity(intentChangePass);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.ln_edit_profile:

                Intent intentEditProfile = new Intent(MyAccountActivity.this,EditProfileActivity.class);
                startActivity(intentEditProfile);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.ln_acc_info:

                Intent intentAccountInfo = new Intent(MyAccountActivity.this,AccountInfoActivity.class);
                startActivity(intentAccountInfo);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;

            case R.id.img_back_btn:

                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                break;
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
