package com.mxi.ecommerce.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxi.ecommerce.R;
import com.mxi.ecommerce.model.UserReview;

import java.util.ArrayList;

/**
 * Created by aksahy on 25/1/18.
 */

public class UserReviewAdapter extends RecyclerView.Adapter<UserReviewAdapter.ViewHolder> {

    ArrayList<UserReview> userReviewList;
    Context context;

    public UserReviewAdapter(Context context, ArrayList<UserReview> alName) {
        super();
        this.context = context;
        this.userReviewList = alName;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.raw_review_customer, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserReviewAdapter.ViewHolder viewHolder, int i) {
        viewHolder.tvRawReviewTitle.setText(userReviewList.get(i).getAuthor());
        viewHolder.tvRawReviewDescription.setText(userReviewList.get(i).getText());
        viewHolder.tvRawReviewNameDate.setText(userReviewList.get(i).getDate_added());
        viewHolder.tvRawReviewRating.setText(userReviewList.get(i).getRating());

        viewHolder.llReviewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return userReviewList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvRawReviewRating;
        private TextView tvRawReviewTitle;
        private TextView tvRawReviewDescription;
        private TextView tvRawReviewNameDate;
        private LinearLayout llReviewOption;

        public ViewHolder(View view) {
            super(view);
            tvRawReviewRating = (TextView) view.findViewById(R.id.tv_raw_review_rating);
            tvRawReviewTitle = (TextView) view.findViewById(R.id.tv_raw_review_title);
            tvRawReviewDescription = (TextView) view.findViewById(R.id.tv_raw_review_description);
            tvRawReviewNameDate = (TextView) view.findViewById(R.id.tv_raw_review_name_date);
            llReviewOption = (LinearLayout) view.findViewById(R.id.ll_review_option);
        }
    }

}

