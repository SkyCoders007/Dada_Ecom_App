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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mxi.ecommerce.activity.TopDealDetailActivity;
import com.mxi.ecommerce.model.CategoryData;
import com.mxi.ecommerce.R;


import java.util.ArrayList;

public class DealListingAdapter extends RecyclerView.Adapter<DealListingAdapter.MyViewHolder> {

    private ArrayList<CategoryData> dataSet;
    Context context;

    static int position;

    public DealListingAdapter(Context context, ArrayList<CategoryData> data) {

        this.dataSet = data;
        this.context=  context;

    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewVersion;
        CardView card_view;
        ImageView imageViewIcon;
        LinearLayout ln_deal_line;



        public MyViewHolder(View itemView) {
            super(itemView);

            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
            this.card_view=(CardView)itemView.findViewById(R.id.card_view);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_by_category, parent, false);


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;

        ImageView imageView = holder.imageViewIcon;

        textViewName.setText(dataSet.get(listPosition).getName());

        Glide.with(context)
                .load(dataSet.get(listPosition).getImage())
                .into(holder.imageViewIcon);

       // imageView.setImageResource(dataSet.get(listPosition).getImage());

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                position=listPosition;

                Log.e("shopbycategoryposition","Click-"+position);


                if (position==0) {

                    Intent intentTopDeal1 = new Intent(view.getContext(), TopDealDetailActivity.class);
                    intentTopDeal1.putExtra("pos0", position);
                    intentTopDeal1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intentTopDeal1);
                    ((Activity)context).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);


                    Log.e("position0", position + "");

                }
                else if (position==1)
                {
                    Intent intentTopDeal2 = new Intent(view.getContext(), TopDealDetailActivity.class);
                    intentTopDeal2.putExtra("pos1", position);
                    intentTopDeal2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intentTopDeal2);
                    ((Activity)context).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);


                    Log.e("position1", position + "");
                }
                else if (position==2)
                {
                    Intent intentTopDeal3 = new Intent(view.getContext(), TopDealDetailActivity.class);
                    intentTopDeal3.putExtra("pos2", position);
                    intentTopDeal3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intentTopDeal3);
                    ((Activity)context).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                    Log.e("position2", position + "");
                }
                else if (position==3)
                {
                    Intent intentTopDeal4 = new Intent(view.getContext(), TopDealDetailActivity.class);
                    intentTopDeal4.putExtra("pos3", position);
                    intentTopDeal4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intentTopDeal4);
                    ((Activity)context).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                    Log.e("position3", position + "");
                }
                else if (position==4)
                {
                    Intent intentTopDeal5 = new Intent(view.getContext(), TopDealDetailActivity.class);
                    intentTopDeal5.putExtra("pos4", position);
                    intentTopDeal5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intentTopDeal5);
                    ((Activity)context).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                    Log.e("position4", position + "");
                }
                else if (position==5)
                {
                    Intent intentTopDeal6 = new Intent(view.getContext(), TopDealDetailActivity.class);
                    intentTopDeal6.putExtra("pos5", position);
                    intentTopDeal6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intentTopDeal6);
                    ((Activity)context).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                    Log.e("position5", position + "");
                }
                else if (position==6)

                {
                    Intent intentTopDeal7 = new Intent(view.getContext(), TopDealDetailActivity.class);
                    intentTopDeal7.putExtra("pos6", position);
                    intentTopDeal7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intentTopDeal7);
                    ((Activity)context).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                    Log.e("position6", position + "");
                }else {

                    Log.e("nothing","...........");
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}