package com.mxi.ecommerce.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mxi.ecommerce.R;
import com.mxi.ecommerce.model.Attribute;
import com.mxi.ecommerce.widget.MyTextviewLight;
import com.mxi.ecommerce.widget.MyTextviewRegular;

import java.util.ArrayList;

/**
 * Created by aksahy on 25/1/18.
 */

public class ProductSubSpecificationAdapter extends RecyclerView.Adapter<ProductSubSpecificationAdapter.ViewHolder> {

    ArrayList<Attribute> attributeList;
    Context context;

    public ProductSubSpecificationAdapter(Context context, ArrayList<Attribute> alName) {
        super();
        this.context = context;
        this.attributeList= alName;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.raw_attribute_sub_specification, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProductSubSpecificationAdapter.ViewHolder viewHolder, int i) {
        viewHolder.tvSpecificationTitle.setText(attributeList.get(i).getName());
        viewHolder.tvSpecificationName.setText(attributeList.get(i).getText());
    }

    @Override
    public int getItemCount() {
        return attributeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private MyTextviewRegular tvSpecificationTitle;
        private MyTextviewLight tvSpecificationName;

        public ViewHolder(View view) {
            super(view);
            tvSpecificationTitle = (MyTextviewRegular) view.findViewById(R.id.tv_specification_title);
            tvSpecificationName = (MyTextviewLight) view.findViewById(R.id.tv_specification_name);
        }
    }

}
