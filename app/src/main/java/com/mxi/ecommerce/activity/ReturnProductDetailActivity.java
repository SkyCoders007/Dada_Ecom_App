package com.mxi.ecommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxi.ecommerce.R;
import com.mxi.ecommerce.comman.CommanClass;
import com.squareup.picasso.Picasso;

public class ReturnProductDetailActivity extends AppCompatActivity {

    TextView tv_product_name,tv_order_date,tv_product_id,tv_order_id,tv_customer_name,tv_customer_email,tv_mobile_number;
    ImageView img_product_img;


    String model,status,returnid,quantity,order_id,product_name,order_date,product_id,customer_name,customer_email,mobile_number,product_img;
    String title = "Product Detail";
    TextView tv_toolbar_title;

    LinearLayout ln_reason_next_one;
    CommanClass cc;
    LinearLayout img_back_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_product_detail);

        cc=new CommanClass(this);

        tv_product_name=(TextView)findViewById(R.id.tv_product_name);
        tv_order_date=(TextView)findViewById(R.id.tv_order_date);
        tv_product_id=(TextView)findViewById(R.id.tv_product_id);
        tv_order_id=(TextView)findViewById(R.id.tv_order_id);
        tv_customer_name=(TextView)findViewById(R.id.tv_customer_name);
        tv_customer_email=(TextView)findViewById(R.id.tv_customer_email);
        tv_mobile_number=(TextView)findViewById(R.id.tv_mobile_number);
        img_product_img=(ImageView) findViewById(R.id.img_product_img);
        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);

        ln_reason_next_one = (LinearLayout) findViewById(R.id.ln_reason_next_one);
        img_back_btn = (LinearLayout) findViewById(R.id.img_back_btn);

        img_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });





        Intent get = getIntent();
        returnid = get.getStringExtra("return_id");
        customer_name = get.getStringExtra("name");
        order_id = get.getStringExtra("order_id");
        customer_email = get.getStringExtra("email");
        mobile_number = get.getStringExtra("mobile");
        status = get.getStringExtra("status");
        order_date = get.getStringExtra("date_added");
        product_id = get.getStringExtra("product_id");
        product_name = get.getStringExtra("product_name");
        model = get.getStringExtra("model");
        product_img = get.getStringExtra("image");
        quantity = get.getStringExtra("quantity");


        cc.savePrefString("return_order_id",order_id);
        cc.savePrefString("return_email",customer_email);
        cc.savePrefString("return_telephone",mobile_number);
        cc.savePrefString("return_product_name",product_name);
        cc.savePrefString("return_product_model",model);
        cc.savePrefString("return_product_quantity",quantity);
        cc.savePrefString("return_product_date",order_date);
        cc.savePrefString("return_product_id",product_id);



        Log.e("product_images",product_img);

        tv_toolbar_title.setText(title);
        tv_toolbar_title.setPadding(0, 0, 40, 0);
        Log.e("toolbar", title);




        String[] parts = order_date.split(",");
        String part1 = parts[0];
        String part2 = parts[1];
        String part3 = parts[2];

        String[] splited = part3.split(" ");
        String dates = splited[0];
        String time = splited[1];
        String time1 = splited[2];
        String time2 = splited[3];


        tv_order_date.setText(time+""+time1+""+time2);
//        tv_order_date.setText(order_date);

        tv_customer_name.setText(customer_name);
        tv_order_id.setText(order_id);
        tv_customer_email.setText(customer_email);
        tv_mobile_number.setText(mobile_number);
        tv_product_name.setText(product_name);
        tv_product_id.setText(product_id);


        if (product_img.isEmpty()){
           img_product_img.setImageResource(R.drawable.no_media);
        }else {
           Picasso.with(this).load(product_img).fit().centerCrop()
                   .placeholder(R.drawable.no_media)
                   .error(R.drawable.no_media)
                   .into(img_product_img);
       }


        ln_reason_next_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ln_reason_next_one=new Intent(ReturnProductDetailActivity.this,ReturnReasonActivity.class);
                startActivity(ln_reason_next_one);
            }
        });


        //Glide.with(this).load(product_img).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(img_product_img);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
