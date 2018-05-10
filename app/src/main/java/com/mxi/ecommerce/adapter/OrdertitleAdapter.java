package com.mxi.ecommerce.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxi.ecommerce.R;
import com.mxi.ecommerce.activity.OrderDetailActivity;
import com.mxi.ecommerce.model.OrderDetialdata;

import java.util.List;

public class OrdertitleAdapter extends RecyclerView.Adapter<OrdertitleAdapter.ViewHolder> {


    static Context context;

    List<OrderDetialdata> order_data;



    public OrdertitleAdapter(OrderDetailActivity myOrderActivity, List<OrderDetialdata> oder_history_data) {

        this.context = myOrderActivity;
        this.order_data = oder_history_data;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.order_subtotal_listing, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
//        viewHolder.imgThumbnail.setImageResource(Integer.parseInt(order_data.get(i).getOrder_image()));
        viewHolder.tv_order_id.setText(order_data.get(i).getTitle());
        viewHolder.order_total.setText(order_data.get(i).getValue());


        String Order=order_data.get(i).getTitle();
        Log.e("oreder",Order);



        if (Order.equalsIgnoreCase("Total")) {

//            viewHolder.ln_myorder_product_iew_detail.setBackground(get);
            viewHolder.ln_myorder_product_iew_detail.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
            viewHolder.tv_order_id.setTextColor(ContextCompat.getColor(context,R.color.text_white));
            viewHolder.order_total.setTextColor(ContextCompat.getColor(context,R.color.text_white));
            viewHolder.tv_order_id.setTextSize(18);
            viewHolder.order_total.setTextSize(18);

        }


    }


    @Override
    public int getItemCount() {
        return order_data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_order_id,order_total;
        LinearLayout ln_myorder_product_iew_detail;
        public ViewHolder(final View itemView) {

            super(itemView);

            tv_order_id = (TextView) itemView.findViewById(R.id.tv_title);
            order_total = (TextView) itemView.findViewById(R.id.tv_value);
            ln_myorder_product_iew_detail = (LinearLayout) itemView.findViewById(R.id.ln_myorder_product_iew_detail);

        }
    }
}