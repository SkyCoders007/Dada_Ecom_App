package com.mxi.ecommerce.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mxi.ecommerce.activity.MainActivity;
import com.mxi.ecommerce.activity.ProductDetailActivity;
import com.mxi.ecommerce.comman.CommanClass;
import com.mxi.ecommerce.model.GetDiscountData;
import com.mxi.ecommerce.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GetDiscountAdapter extends RecyclerView.Adapter<GetDiscountAdapter.ViewHolder> {

    ArrayList<String> alName;
    ArrayList<Integer> alImage;
    Context context;
    List<GetDiscountData> discountlist;
    static ProgressBar mProgressBar;
    CommanClass cc;


    public GetDiscountAdapter(MainActivity mainActivity, List<GetDiscountData> getDiscountList) {

        this.context=mainActivity;
        this.discountlist=getDiscountList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {


        cc = new CommanClass(context);

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.topdeal_category_adapters, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        viewHolder.imgThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getPositionItem1(viewHolder.getAdapterPosition());



                Intent intentProductDetail = new Intent(context, ProductDetailActivity.class);
                intentProductDetail.putExtra("product_id",cc.loadPrefString("productid"));
                String product_id = cc.loadPrefString("productid");
                context.startActivity(intentProductDetail);
                ((Activity)context).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                Log.e("productidholder",viewHolder.getAdapterPosition()+"");
                Log.e("productidholder1",product_id);

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.tvSpecies.setText(discountlist.get(i).getGetDiscountProductname());

        GetDiscountData td = discountlist.get(i);

        Log.e("images", td.getImageUrl());
        Log.e("name", td.getGetDiscountProductname());

        if (discountlist.size()!=0){
            Picasso.with(context).load(discountlist.get(i).getImageUrl()).into(viewHolder.imgThumbnail, new Callback() {
                @Override
                public void onSuccess() {
                    mProgressBar.setVisibility(View.GONE);
                }
                @Override
                public void onError() {
                    mProgressBar.setVisibility(View.GONE);
                }
            });

        }else {
            Picasso.with(context).load(R.drawable.no_media).into(viewHolder.imgThumbnail);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return discountlist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgThumbnail;
        public TextView tvSpecies;
        public RelativeLayout top_layout;



        public ViewHolder(final View itemView) {
            super(itemView);

            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            tvSpecies = (TextView) itemView.findViewById(R.id.tv_title);
            mProgressBar= (ProgressBar) itemView.findViewById(R.id.pgb_image_loading);
            top_layout= (RelativeLayout) itemView.findViewById(R.id.top_layout);

        }


    }

    private void getPositionItem1(int adapterPosition) {
        GetDiscountData gm = discountlist.get(adapterPosition);
        gm.getProductid();

        cc.savePrefString("productid", String.valueOf(gm.getProductid()));

        Log.e("prodctpositionid",gm.getProductid()+"");

    }


}