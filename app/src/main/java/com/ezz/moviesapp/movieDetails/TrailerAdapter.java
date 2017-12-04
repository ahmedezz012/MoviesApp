package com.ezz.moviesapp.movieDetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ezz.moviesapp.models.videos.Video;
import com.ezz.moviesapp.R;

import java.util.ArrayList;

/**
 * Created by samar ezz on 11/27/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private Context context;
    private ArrayList<Video> videoArrayList;
    private TrailerClickListener trailerClickListener;

    public TrailerAdapter(Context context, ArrayList<Video> videoArrayList, TrailerClickListener trailerClickListener) {
        this.context = context;
        this.trailerClickListener = trailerClickListener;
        this.videoArrayList = videoArrayList;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrailerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_trailer, parent, false));
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.txtTitle.setText(String.format("%s %s", context.getString(R.string.trailer), String.valueOf(position + 1)));
    }

    @Override
    public int getItemCount() {
        return videoArrayList.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtTitle;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            trailerClickListener.onTrailerClicked(getLayoutPosition());
        }
    }

    interface TrailerClickListener {
        void onTrailerClicked(int position);
    }
}
