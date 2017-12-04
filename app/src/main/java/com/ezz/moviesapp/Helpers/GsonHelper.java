package com.ezz.moviesapp.Helpers;

import com.ezz.moviesapp.Models.Movies.MovieResponse;
import com.ezz.moviesapp.Models.Reviews.MovieReviewsResponse;
import com.ezz.moviesapp.Models.Videos.MovieVideosResponse;
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
