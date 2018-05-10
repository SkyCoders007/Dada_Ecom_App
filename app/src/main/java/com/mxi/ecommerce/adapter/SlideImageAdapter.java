package com.mxi.ecommerce.adapter;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.ImageLoader;
import com.mxi.ecommerce.R;
import com.mxi.ecommerce.activity.MainActivity;
import com.mxi.ecommerce.model.SlidingData;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.mxi.ecommerce.R.id.img;


public class SlideImageAdapter extends PagerAdapter {
    // Declare Variables
    Context context;
    List<SlidingData> dataAdapters;
    String Tag = "1811";
    LayoutInflater inflater;
    ImageLoader imageLoader;
    public ProgressBar mProgressBar;
    ImageView imgflag;


    public SlideImageAdapter(MainActivity mainActivity, List<SlidingData> SlidingModelList) {
        this.context = mainActivity;
        this.dataAdapters = SlidingModelList;
    }

    @Override
    public int getCount() {
        return dataAdapters.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        // Declare Variables


        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.sliding_image, container,
                false);


        imgflag = (ImageView) itemView.findViewById(img);
        mProgressBar = (ProgressBar) itemView.findViewById(R.id.pgb_image_loading_style_swiper);
        Log.e("slidingimage", dataAdapters.get(position).getImageUrl());

        Log.e("position", dataAdapters.get(position) + "");

        ((ViewPager) container).addView(itemView);

        if (dataAdapters.size() != 0) {
            Picasso.with(context).load(dataAdapters.get(position).getImageUrl()).resize(600, 200).into(imgflag, new Callback() {
                @Override
                public void onSuccess() {
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                }
            });

        } else {
            Picasso.with(context).load(R.drawable.no_media).into(imgflag);
            mProgressBar.setVisibility(View.GONE);
        }

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}