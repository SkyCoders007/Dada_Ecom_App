package com.mxi.ecommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxi.ecommerce.adapter.DealListingAdapter;
import com.mxi.ecommerce.model.CategoryData;
import com.mxi.ecommerce.R;

import java.util.ArrayList;



public class DealActivity extends AppCompatActivity implements View.OnClickListener{


    LinearLayout ln_home_bottom,ln_category_bottom,ln_deal_bottom,ln_wishlist_bottom,ln_mycc_bottom;

    String title= "Deals";
    TextView tv_toolbar_title;

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager mlayoutmanger_recycleview_shopcategory;
    private static RecyclerView recycleview_shopcategory;
    private static ArrayList<CategoryData> data;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    LinearLayout img_back_btn;

    @Override
    protected void onCreate(Bundle  savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);

        init();

        clickListner();

        recycleview_shopcategory = (RecyclerView) findViewById(R.id.recycleview_shopcategory);
        recycleview_shopcategory.setHasFixedSize(true);


        mlayoutmanger_recycleview_shopcategory = new LinearLayoutManager(this);
        recycleview_shopcategory.setLayoutManager(mlayoutmanger_recycleview_shopcategory);


        data = new ArrayList<CategoryData>();
        for (int i = 0; i < com.mxi.ecommerce.categoryproductdata.CategoryData.nameArray.length; i++) {
            data.add(new CategoryData(

                    com.mxi.ecommerce.categoryproductdata.CategoryData.nameArray[i],
                    com.mxi.ecommerce.categoryproductdata.CategoryData.drawableArray[i]

            ));
        }

        removedItems = new ArrayList<Integer>();


        adapter = new DealListingAdapter(DealActivity.this,data);
        recycleview_shopcategory.setAdapter(adapter);

        tv_toolbar_title.setText(title);
        tv_toolbar_title.setPadding(0, 0, 40, 0);
        Log.e("toolbar",title);
        img_back_btn.setVisibility(View.GONE);
    }

    private void clickListner() {

        ln_home_bottom.setOnClickListener(this);
        ln_deal_bottom.setOnClickListener(this);
        ln_wishlist_bottom.setOnClickListener(this);
        ln_mycc_bottom.setOnClickListener(this);
        ln_category_bottom.setOnClickListener(this);

        img_back_btn.setOnClickListener(this);
    }

    private void init() {

        tv_toolbar_title=(TextView)findViewById(R.id.tv_toolbar_title);
        ln_home_bottom=(LinearLayout)findViewById(R.id.ln_home_bottom);
        ln_category_bottom=(LinearLayout)findViewById(R.id.ln_category_bottom);
        ln_deal_bottom=(LinearLayout)findViewById(R.id.ln_deal_bottom);
        ln_mycc_bottom=(LinearLayout)findViewById(R.id.ln_mycc_bottom);
        ln_wishlist_bottom=(LinearLayout)findViewById(R.id.ln_wishlist_bottom);
        img_back_btn=(LinearLayout) findViewById(R.id.img_back_btn);

    }

    @Override
    public void onClick(View view)

    {

          switch (view.getId()){

              case R.id.ln_home_bottom:

                  Intent intentMainActivity = new Intent(DealActivity.this,MainActivity.class);
                  intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                  overridePendingTransition(0,0);
                  startActivity(intentMainActivity);
                  overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                  finish();

                  break;

              case R.id.ln_category_bottom:

                  Intent intentShopCategory = new Intent(DealActivity.this,ShopCategoryActivity.class);
                  startActivity(intentShopCategory);
                  overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                  break;

              case R.id.ln_mycc_bottom:

                  Intent intentMyAccount = new Intent(DealActivity.this,MyAccountActivity.class);
                  startActivity(intentMyAccount);
                  overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                  break;

              case R.id.ln_wishlist_bottom:

                  Intent intentWishList = new Intent(DealActivity.this,WishlistActivity.class);
                  startActivity(intentWishList);
                  overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                  break;

              case R.id.ln_deal_bottom:

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
