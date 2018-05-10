package com.mxi.ecommerce.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mxi.ecommerce.activity.MainActivity;
import com.mxi.ecommerce.activity.ProductDetailActivity;
import com.mxi.ecommerce.comman.CommanClass;
import com.mxi.ecommerce.model.NewPopularCatelogData;
import com.mxi.ecommerce.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewPopularProductAdapter extends RecyclerView.Adapter<NewPopularProductAdapter.ViewHolder> {

    ArrayList<Integer> alImage;
    Context context;
    static ProgressBar mProgressBar;
    List<NewPopularCatelogData> popular;
    CommanClass cc;

    public NewPopularProductAdapter(MainActivity mainActivity, List<NewPopularCatelogData> newPopularCatelogList) {

        this.context=mainActivity;
        this.popular=newPopularCatelogList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        cc=new CommanClass(context);

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.newpopularproduct, viewGroup, false);
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

        if (popular.size()!=0){

            if (popular.get(i).getImageUrl().equals("")){
                Picasso.with(context).load(R.drawable.no_media).into(viewHolder.imgThumbnail);
                mProgressBar.setVisibility(View.GONE);
            }else {

                Picasso.with(context).load(popular.get(i).getImageUrl()).into(viewHolder.imgThumbnail, new Callback() {
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
            Picasso.with(context).load(R.drawable.no_media).into(viewHolder.imgThumbnail);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return popular.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {

        public ImageView imgThumbnail;
        public TextView tvSpecies;
        public CardView cardview_new_product;

        public ViewHolder(final View itemView) {
            super(itemView);

            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            cardview_new_product = (CardView) itemView.findViewById(R.id.cardview_new_product);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.pgb_image_loading);

        }
    }


    private void getPositionItem1(int adapterPosition) {
        NewPopularCatelogData nm = popular.get(adapterPosition);
        nm.getProductid();

        cc.savePrefString("productid", String.valueOf(nm.getProductid()));

        Log.e("prodctpositionid",nm.getProductid()+"");

    }

}