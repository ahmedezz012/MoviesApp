package com.ezz.moviesapp.Helpers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by samar ezz on 11/10/2017.
 */

public class ServicesCaller {

    private static ServicesCaller servicesCaller;
    private RequestQueue requestQueue;

    public static final String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/movie";

    public static final String POPULAR_MOVIES = MOVIE_DB_BASE_URL + "/popular";
    public static final String TOP_RATED_MOVIES = MOVIE_DB_BASE_URL + "/top_rated";
    public static final String VIDEOS = "videos";
    public static final String REVIEWS = "reviews";
    public static final String API_KEY = "api_key";
    public static final String API_KEY_VALUE = "";//add api key here

    public enum Tag {
        Movies,Trailers,Reviews
    }

    private ServicesCaller(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public static synchronized ServicesCaller getInstance(Context context) {
        if (servicesCaller == null) {
            servicesCaller = new ServicesCaller(context);
        }
        return servicesCaller;
    }

    private <T> void addToRequestQueue(Request<T> req) {
        requestQueue.add(req);
    }

    public void getMovies(String url, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        StringRequest moviesStringRequest = new StringRequest(Request.Method.GET, url, successListener, errorListener);
        moviesStringRequest.setTag(Tag.Movies);
        addToRequestQueue(moviesStringRequest);
    }
    public void getTrailers(String url, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        StringRequest moviesStringRequest = new StringRequest(Request.Method.GET, url, successListener, errorListener);
        moviesStringRequest.setTag(Tag.Trailers);
        addToRequestQueue(moviesStringRequest);
    }
    public void getReviews(String url, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        StringRequest moviesStringRequest = new StringRequest(Request.Method.GET, url, successListener, errorListener);
        moviesStringRequest.setTag(Tag.Reviews);
        addToRequestQueue(moviesStringRequest);
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
