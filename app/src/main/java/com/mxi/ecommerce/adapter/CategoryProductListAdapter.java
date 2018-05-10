package com.mxi.ecommerce.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxi.ecommerce.activity.CategoryDetailActivity;
import com.mxi.ecommerce.model.CategoryData;
import com.mxi.ecommerce.R;

import java.util.ArrayList;

public class CategoryProductListAdapter extends RecyclerView.Adapter<CategoryProductListAdapter.MyViewHolder> {

    private ArrayList<CategoryData> dataSet;
    Context context;

    int position;

    public CategoryProductListAdapter(Context context, ArrayList<CategoryData> data) {

        this.dataSet = data;
        this.context=  context;

    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewVersion;
        CardView card_view;
        ImageView imageViewIcon;



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

        imageView.setImageResource(dataSet.get(listPosition).getImage());

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                position=listPosition;

                Log.e("shopbycategoryposition","Click-"+position);


                if (position==0) {

                    Intent intentCategoryDetail = new Intent(view.getContext(), CategoryDetailActivity.class);
                    intentCategoryDetail.putExtra("pos0", position);
                    intentCategoryDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intentCategoryDetail);


                    Log.e("position0", position + "");

                }
                else if (position==1)
                {
                    Intent intentCategoryDetail2 = new Intent(view.getContext(), CategoryDetailActivity.class);
                    intentCategoryDetail2.putExtra("pos1", position);
                    intentCategoryDetail2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intentCategoryDetail2);


                    Log.e("position1", position + "");
                }
                else if (position==2)
                {
                    Intent intentCategoryDetail3 = new Intent(view.getContext(), CategoryDetailActivity.class);
                    intentCategoryDetail3.putExtra("pos2", position);
                    intentCategoryDetail3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intentCategoryDetail3);


                    Log.e("position2", position + "");
                }
                else if (position==3)
                {
                    Intent intentCategoryDetail4 = new Intent(view.getContext(), CategoryDetailActivity.class);
                    intentCategoryDetail4.putExtra("pos3", position);
                    intentCategoryDetail4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intentCategoryDetail4);


                    Log.e("position3", position + "");
                }
                else if (position==4)
                {
                    Intent intentCategoryDetail5 = new Intent(view.getContext(), CategoryDetailActivity.class);
                    intentCategoryDetail5.putExtra("pos4", position);
                    intentCategoryDetail5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intentCategoryDetail5);


                    Log.e("position4", position + "");
                }
                else if (position==5)
                {
                    Intent intentCategoryDetail6 = new Intent(view.getContext(), CategoryDetailActivity.class);
                    intentCategoryDetail6.putExtra("pos5", position);
                    intentCategoryDetail6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intentCategoryDetail6);


                    Log.e("position5", position + "");
                }
                else if (position==6)

                {
                    Intent intentCategoryDetail7 = new Intent(view.getContext(), CategoryDetailActivity.class);
                    intentCategoryDetail7.putExtra("pos6", position);
                    intentCategoryDetail7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intentCategoryDetail7);


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