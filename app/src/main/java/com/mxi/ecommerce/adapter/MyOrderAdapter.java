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
import android.widget.TextView;

import com.mxi.ecommerce.R;
import com.mxi.ecommerce.activity.MyOrderActivity;
import com.mxi.ecommerce.activity.OrderDetailActivity;
import com.mxi.ecommerce.model.OrderHistorydata;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {


    static Context context;

    List<OrderHistorydata> order_data;



    public MyOrderAdapter(MyOrderActivity myOrderActivity, List<OrderHistorydata> oder_history_data) {

        this.context = myOrderActivity;
        this.order_data = oder_history_data;


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.myorder_listing, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
//        viewHolder.imgThumbnail.setImageResource(Integer.parseInt(order_data.get(i).getOrder_image()));
        viewHolder.tv_order_id.setText(order_data.get(i).getOrder_id());
        viewHolder.order_total.setText(order_data.get(i).getOrder_total());
        viewHolder.oder_status.setText(order_data.get(i).getOrder_status());


        String date=order_data.get(i).getOrder_date_added();


        String CurrentString = date;
        String[] separated = CurrentString.split(" ");
        separated[0] = separated[0].trim();
        separated[1] = separated[1].trim();


        String dates=separated[0];
        Log.e("dates",dates);


        String mStringDate = dates;
        String oldFormat= "yyyy-MM-dd";
        String newFormat= "dd-MM-yyyy";

        String formatedDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(oldFormat);
        Date myDate = null;
        try {
            myDate = dateFormat.parse(mStringDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat(newFormat);
        formatedDate = timeFormat.format(myDate);

        Log.e("@@@newDate",formatedDate);


        viewHolder.oder_date.setText(formatedDate);


        viewHolder.ln_vieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getItemPos(viewHolder.getAdapterPosition());

            }
        });

    }

    private void getItemPos(int adapterPosition) {

        OrderHistorydata cm = order_data.get(adapterPosition);

        Intent intentShopCategory = new Intent(context, OrderDetailActivity.class);
        intentShopCategory.putExtra("order_id",cm.getOrder_id());
        intentShopCategory.putExtra("status",cm.getOrder_status());
        context.startActivity(intentShopCategory);
        ((Activity)context).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

    }


    @Override
    public int getItemCount() {
        return order_data.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgThumbnail;
        public TextView tvSpecies,tv_order_id,order_total,oder_date,oder_status;
        public LinearLayout trackorders,ln_myorder_product_iew_detail,ln_vieworder,ln_order_trackings;

        public ViewHolder(final View itemView) {
            super(itemView);

            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            tv_order_id = (TextView) itemView.findViewById(R.id.tv_order_id);
            oder_status = (TextView) itemView.findViewById(R.id.oder_status);
            order_total = (TextView) itemView.findViewById(R.id.order_total);
            oder_date = (TextView) itemView.findViewById(R.id.oder_date);
            trackorders = (LinearLayout) itemView.findViewById(R.id.trackorders);
            ln_myorder_product_iew_detail = (LinearLayout) itemView.findViewById(R.id.ln_myorder_product_iew_detail);
            ln_vieworder = (LinearLayout) itemView.findViewById(R.id.ln_vieworder);
           // ln_order_trackings = (LinearLayout) itemView.findViewById(R.id.ln_order_trackings);



//            trackorders.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Intent intentOrderTrack = new Intent(itemView.getContext(),OrderTrackingActivity.class);
//                    itemView.getContext().startActivity(intentOrderTrack);
//                    ((Activity)context).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//
//                }
//            });
//

        }
    }
}