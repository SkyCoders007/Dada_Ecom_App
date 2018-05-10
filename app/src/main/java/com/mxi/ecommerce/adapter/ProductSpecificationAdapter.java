package com.mxi.ecommerce.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mxi.ecommerce.R;
import com.mxi.ecommerce.model.AttributeGroups;

import java.util.ArrayList;

/**
 * Created by aksahy on 25/1/18.
 */

public class ProductSpecificationAdapter extends RecyclerView.Adapter<ProductSpecificationAdapter.ViewHolder> {

    ArrayList<AttributeGroups> attributeGroupList;
    ArrayList<Integer> alImage;
    Context context;

    public ProductSpecificationAdapter(Context context, ArrayList<AttributeGroups> alName) {
        super();
        this.context = context;
        this.attributeGroupList = alName;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.raw_attribute_specification, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.tvSpecificationHead.setText(attributeGroupList.get(i).getName());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        viewHolder.rv_sub_attributes.setLayoutManager(linearLayoutManager);
        ProductSubSpecificationAdapter subSpecificationAdapter =
                new ProductSubSpecificationAdapter(context, attributeGroupList.get(i).getAttribute());
        viewHolder.rv_sub_attributes.setAdapter(subSpecificationAdapter);
    }

    @Override
    public int getItemCount() {
        return attributeGroupList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        private TextView tvSpecificationHead;
        RecyclerView rv_sub_attributes;

        public ViewHolder(View view) {
            super(view);
            tvSpecificationHead = (TextView) view.findViewById(R.id.tv_specification_head);
            rv_sub_attributes = (RecyclerView) view.findViewById(R.id.rv_sub_attributes);

        }
    }

}
