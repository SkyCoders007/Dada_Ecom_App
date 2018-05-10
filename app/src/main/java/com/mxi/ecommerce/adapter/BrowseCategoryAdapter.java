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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mxi.ecommerce.R;
import com.mxi.ecommerce.activity.CategoryDetailActivity;
import com.mxi.ecommerce.activity.MainActivity;
import com.mxi.ecommerce.comman.CommanClass;
import com.mxi.ecommerce.model.CategoryDataTwo;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BrowseCategoryAdapter extends RecyclerView.Adapter<BrowseCategoryAdapter.ViewHolder> {


    ArrayList<Integer> alImage;
    ArrayList<String> alName;
    Context context;
    List<CategoryDataTwo> categoryModels;
    static ProgressBar mProgressBar;
    CommanClass cc;


    public BrowseCategoryAdapter(MainActivity mainActivity, List<CategoryDataTwo> categoryList) {
        this.context=mainActivity;
        this.categoryModels=categoryList;
        cc=new CommanClass(mainActivity);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.new_product_category, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        final CategoryDataTwo td = categoryModels.get(i);

        viewHolder.tvSpecies.setText(categoryModels.get(i).getCategoryname());


        viewHolder.top_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getItemPos(viewHolder.getAdapterPosition());

            }
        });

        Log.e("images", td.getImageUrl());
        Log.e("name", td.getCategoryname());
        Log.e("id", td.getCategoey_id());

        if (categoryModels.size()!=0){

            if (categoryModels.get(i).getImageUrl().equals("")){
                Picasso.with(context).load(R.drawable.no_media).into(viewHolder.imgThumbnail);
                mProgressBar.setVisibility(View.GONE);
            }else {

                Picasso.with(context).load(categoryModels.get(i).getImageUrl()).into(viewHolder.imgThumbnail, new Callback() {
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
        return categoryModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {

        public ImageView imgThumbnail;
        public TextView tvSpecies;
        public CardView card_view_category_mainscreen;
        LinearLayout top_layout;

        public ViewHolder(final View itemView) {
            super(itemView);

            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.pgb_image_loading);
            tvSpecies = (TextView) itemView.findViewById(R.id.tvSpecies);
            top_layout = (LinearLayout) itemView.findViewById(R.id.top_layout);
//            card_view_category_mainscreen = (CardView) itemView.findViewById(R.id.card_view_category_mainscreen);
//
//
//            card_view_category_mainscreen.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent i=new Intent(itemView.getContext(), ShopCategoryActivity.class);
//                    itemView.getContext().startActivity(i);
//                }
//            });
        }
    }

    private void getItemPos(int itemPos) {

        CategoryDataTwo cm = categoryModels.get(itemPos);

//        Intent intentShopCategory = new Intent(context, ShopCategoryActivity.class);
        cc.savePrefBoolean("withoutSubCategory",true);
        Intent intentShopCategory = new Intent(context, CategoryDetailActivity.class);
        intentShopCategory.putExtra("categry_id",cm.getCategoey_id());
        intentShopCategory.putExtra("name",cm.getCategoryname());
        context.startActivity(intentShopCategory);
        ((Activity)context).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

    }

}