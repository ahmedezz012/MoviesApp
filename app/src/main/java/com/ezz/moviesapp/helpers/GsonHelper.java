package com.ezz.moviesapp.helpers;

import com.ezz.moviesapp.models.movies.MovieResponse;
import com.ezz.moviesapp.models.reviews.MovieReviewsResponse;
import com.ezz.moviesapp.models.videos.MovieVideosResponse;
import com.google.gson.Gson;

/**
 * Created by samar ezz on 11/10/2017.
 */

public class GsonHelper {

    public static MovieResponse movieResponse(String response) {
        Gson gson = new Gson();
        return gson.fromJson(response, MovieResponse.class);
    }

    public static MovieVideosResponse movieVideosResponse(String response) {
        Gson gson = new Gson();
        return gson.fromJson(response, MovieVideosResponse.class);
    }

    public static MovieReviewsResponse movieReviewsResponse(String response) {
        Gson gson = new Gson();
        return gson.fromJson(response, MovieReviewsResponse.class);
    }
}
