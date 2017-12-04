package com.ezz.moviesapp.movies;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ezz.moviesapp.helpers.Utils;
import com.ezz.moviesapp.models.movies.Movie;
import com.ezz.moviesapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by samar ezz on 11/7/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private Context context;
    private ArrayList<Movie> movieArrayList;
    private MovieClickListener movieClickListener;
    private boolean isFavourite;

    public MoviesAdapter(Context context,
                         ArrayList<Movie> movieArrayList,
                         MovieClickListener movieClickListener, boolean isFavourite) {
        this.context = context;
        this.movieArrayList = movieArrayList;
        this.movieClickListener = movieClickListener;
        this.isFavourite = isFavourite;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movieArrayList.get(position);
        if (isFavourite) {
            if (movie.getMovieImage() != null) {
                holder.imgMoviePoster.setImageBitmap(BitmapFactory.decodeByteArray(movie.getMovieImage(), 0, movie.getMovieImage().length));
            } else {
                Picasso.with(context).load(String.format("%s%s", Utils.BASE_IMAGE_URL, movie.getPosterPath())).into(holder.imgMoviePoster);
            }
        } else
            Picasso.with(context).load(String.format("%s%s", Utils.BASE_IMAGE_URL, movie.getPosterPath())).into(holder.imgMoviePoster);
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgMoviePoster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imgMoviePoster = itemView.findViewById(R.id.imgMoviePoster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            movieClickListener.onMovieClicked(movieArrayList.get(getAdapterPosition()));
        }
    }
}
