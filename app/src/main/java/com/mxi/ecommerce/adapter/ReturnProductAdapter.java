package com.mxi.ecommerce.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mxi.ecommerce.R;
import com.mxi.ecommerce.activity.ReturnProductActivity;
import com.mxi.ecommerce.activity.ReturnProductDetailActivity;
import com.mxi.ecommerce.comman.CommanClass;
import com.mxi.ecommerce.model.ReturnProductData;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ReturnProductAdapter extends RecyclerView.Adapter<ReturnProductAdapter.ViewHolder> {

    ArrayList<String> alName;
    ArrayList<Integer> alImage;
    Context context;
    List<ReturnProductData> returnProductDatas;
    static ProgressBar mProgressBar;
    CommanClass cc;


    public ReturnProductAdapter(ReturnProductActivity mainActivity, List<ReturnProductData> getDiscountList) {

        this.context=mainActivity;
        this.returnProductDatas=getDiscountList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {


        cc = new CommanClass(context);

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.return_order, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.tv_order_id.setText(returnProductDatas.get(i).getOrder_id());
        viewHolder.tv_product_name.setText(returnProductDatas.get(i).getProductname());
        viewHolder.tv_status.setText(returnProductDatas.get(i).getStatus());
        viewHolder.tv_product_model.setText(returnProductDatas.get(i).getModel());

        viewHolder.ln_return_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getItemPos(i);

            }
        });


        String date=returnProductDatas.get(i).getDate_add();

        String[] parts = date.split(",");
        String part1 = parts[0];
        String part2 = parts[1];
        String part3 = parts[2];

        String[] splited = part3.split(" ");
        String dates = splited[0];
        String time = splited[1];
        String time1 = splited[2];
        String time2 = splited[3];

        viewHolder.tv_date.setText(time+" "+time1+" "+time2);


        if (returnProductDatas.size()!=0){

            if (returnProductDatas.get(i).getImage().equals("")){
                Picasso.with(context).load(R.drawable.no_media).into(viewHolder.img_product_img);
                mProgressBar.setVisibility(View.GONE);
            }else {

                Picasso.with(context).load(returnProductDatas.get(i).getImage()).into(viewHolder.img_product_img, new Callback() {
                    @Override
                    public void onSuccess() {
                        mProgressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError() {
                        mProgressBar.setVisibility(View.GONE);
                    }
                });

            }


        }else {
            Picasso.with(context).load(R.drawable.no_media).into(viewHolder.img_product_img);
            mProgressBar.setVisibility(View.GONE);
        }

        ReturnProductData td = returnProductDatas.get(i);





        Log.e("images", td.getDate_add());
        Log.e("name", td.getOrder_id());



    }

    @Override
    public int getItemCount() {
        return returnProductDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView tv_product_name,tv_product_model,tv_order_id,tv_date,tv_status;
        LinearLayout ln_return_product;
        ImageView img_product_img;



        public ViewHolder(final View itemView) {
            super(itemView);

            mProgressBar = (ProgressBar) itemView.findViewById(R.id.pgb_image_loading);

            tv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
            tv_product_model = (TextView) itemView.findViewById(R.id.tv_product_model);
            tv_order_id = (TextView) itemView.findViewById(R.id.tv_order_id);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            img_product_img = (ImageView) itemView.findViewById(R.id.img_product_img);
            ln_return_product = (LinearLayout) itemView.findViewById(R.id.ln_return_product);


        }


    }

    private void getItemPos(int adapterPosition) {

        ReturnProductData cm = returnProductDatas.get(adapterPosition);

        Intent intentShopCategory = new Intent(context, ReturnProductDetailActivity.class);
        intentShopCategory.putExtra("return_id",cm.getReturn_id());
        intentShopCategory.putExtra("order_id",cm.getOrder_id());
        intentShopCategory.putExtra("name",cm.getName());
        intentShopCategory.putExtra("email",cm.getEmail());
        intentShopCategory.putExtra("mobile",cm.getMobilenumber());
        intentShopCategory.putExtra("status",cm.getStatus());
        intentShopCategory.putExtra("date_added",cm.getDate_add());
        intentShopCategory.putExtra("product_id",cm.getProduct_id());
        intentShopCategory.putExtra("product_name",cm.getProductname());
        intentShopCategory.putExtra("model",cm.getModel());
        intentShopCategory.putExtra("image",cm.getImage());
        Log.e("@@product_image",cm.getImage());

        intentShopCategory.putExtra("quantity",cm.getQuantity());
        context.startActivity(intentShopCategory);
        ((Activity)context).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

    }



}