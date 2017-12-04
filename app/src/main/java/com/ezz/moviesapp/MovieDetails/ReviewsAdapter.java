package com.ezz.moviesapp.MovieDetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ezz.moviesapp.Models.Reviews.Review;
import com.ezz.moviesapp.R;

import java.util.ArrayList;

/**
 * Created by samar ezz on 11/28/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {
    private Context context;
    private ArrayList<Review> reviewArrayList;
    private ReviewClickListener reviewClickListener;

    public ReviewsAdapter(Context context, ArrayList<Review> reviewArrayList, ReviewClickListener reviewClickListener) {
        this.context = context;
        this.reviewArrayList = reviewArrayList;
        this.reviewClickListener = reviewClickListener;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutInflater.from(context).inflate(R.layout.item_review, parent, false));
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.txtReview.setText(reviewArrayList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }


    class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtReview;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            txtReview = itemView.findViewById(R.id.txtReview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            reviewClickListener.onReviewClicked(getLayoutPosition());
        }
    }

    public interface ReviewClickListener {
        void onReviewClicked(int position);
    }
}
