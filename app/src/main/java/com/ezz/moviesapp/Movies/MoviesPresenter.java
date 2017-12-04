package com.ezz.moviesapp.Movies;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ezz.moviesapp.Helpers.GsonHelper;
import com.ezz.moviesapp.Helpers.ServicesCaller;
import com.ezz.moviesapp.Helpers.Utils;
import com.ezz.moviesapp.Models.Movies.Movie;
import com.ezz.moviesapp.Models.Movies.MovieResponse;
import com.ezz.moviesapp.Movies.MoviesActivity.MoviesSort;

import java.util.ArrayList;

import static com.ezz.moviesapp.Helpers.MoviesContentProvider.MOVIES_CONTENT_URI;
import static com.ezz.moviesapp.Helpers.SqliteHelper.MOVIE_DATE_INDEX;
import static com.ezz.moviesapp.Helpers.SqliteHelper.MOVIE_ID_INDEX;
import static com.ezz.moviesapp.Helpers.SqliteHelper.MOVIE_IMAGE_INDEX;
import static com.ezz.moviesapp.Helpers.SqliteHelper.MOVIE_OVERVIEW_INDEX;
import static com.ezz.moviesapp.Helpers.SqliteHelper.MOVIE_POSTER_URL_INDEX;
import static com.ezz.moviesapp.Helpers.SqliteHelper.MOVIE_RATE_INDEX;
import static com.ezz.moviesapp.Helpers.SqliteHelper.MOVIE_TITLE_INDEX;


/**
 * Created by samar ezz on 11/10/2017.
 */

public class MoviesPresenter {
    private MoviesView moviesView;
    private ServicesCaller servicesCaller;

    public MoviesPresenter(MoviesView moviesView) {
        this.moviesView = moviesView;
    }

    public void getMovies(Context context, MoviesSort moviesSort) {
        moviesView.showOrHideProgress(true);
        if (moviesSort == MoviesSort.FAVORITES) {
            Cursor cursor = context.getContentResolver().query(MOVIES_CONTENT_URI, null, null, null, null);
            ArrayList<Movie> movieArrayList = getAllMovies(cursor);
            if (movieArrayList.size() > 0)
                moviesView.onMoviesLoaded(movieArrayList, true);
            else {
                moviesView.noFavourites();
            }
        } else {
            if (Utils.checkIfConnectedToTheInternet(context)) {
                servicesCaller = ServicesCaller.getInstance(context);
                Uri.Builder uri = new Uri.Builder().appendQueryParameter(ServicesCaller.API_KEY, ServicesCaller.API_KEY_VALUE);
                String url = null;
                if (moviesSort == MoviesSort.POPULAR) {
                    url = String.format("%s%s", ServicesCaller.POPULAR_MOVIES, uri.toString());
                } else if (moviesSort == MoviesSort.TOP_RATED) {
                    url = String.format("%s%s", ServicesCaller.TOP_RATED_MOVIES, uri.toString());
                }
                servicesCaller.getMovies(url, successListener, errorListener);
            } else {
                moviesView.noInternet();
            }
        }
    }

    private Response.Listener<String> successListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            MovieResponse movieResponse = GsonHelper.movieResponse(response);
            moviesView.onMoviesLoaded(movieResponse.getMovies(), false);
        }
    };
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            moviesView.networkError();
        }
    };

    private ArrayList<Movie> getAllMovies(Cursor cursor) {
        ArrayList<Movie> arrayList = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                Movie movie = new Movie();
                movie.setId(cursor.getInt(MOVIE_ID_INDEX));
                movie.setTitle(cursor.getString(MOVIE_TITLE_INDEX));
                movie.setOverview(cursor.getString(MOVIE_OVERVIEW_INDEX));
                movie.setReleaseDate(cursor.getString(MOVIE_DATE_INDEX));
                movie.setVoteAverage(cursor.getDouble(MOVIE_RATE_INDEX));
                movie.setPosterPath(cursor.getString(MOVIE_POSTER_URL_INDEX));
                movie.setMovieImage(cursor.getBlob(MOVIE_IMAGE_INDEX));
                arrayList.add(movie);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return arrayList;
    }
}
