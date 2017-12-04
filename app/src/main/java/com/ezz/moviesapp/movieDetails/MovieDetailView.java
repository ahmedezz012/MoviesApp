package com.ezz.moviesapp.movieDetails;

import com.ezz.moviesapp.models.reviews.Review;
import com.ezz.moviesapp.models.videos.Video;

import java.util.ArrayList;

/**
 * Created by samar ezz on 11/10/2017.
 */

public interface MovieDetailView {
    void bindData();

    void showOrHideProgress(boolean show);

    void movieExistOrNot(boolean isFavorite);

    void trailersNetworkError();

    void reviewsNetworkError();

    void noInternet();

    void onTrailersLoaded(ArrayList<Video> videoArrayList);

    void onReviewsLoaded(ArrayList<Review> reviewArrayList);

    void sharingError(int message);
}
