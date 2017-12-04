package com.ezz.moviesapp.MovieDetails;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ezz.moviesapp.Helpers.GsonHelper;
import com.ezz.moviesapp.Helpers.ServicesCaller;
import com.ezz.moviesapp.Helpers.Utils;
import com.ezz.moviesapp.Models.Movies.Movie;
import com.ezz.moviesapp.Models.Reviews.MovieReviewsResponse;
import com.ezz.moviesapp.Models.Reviews.Review;
import com.ezz.moviesapp.Models.Videos.MovieVideosResponse;
import com.ezz.moviesapp.Models.Videos.Video;
import com.ezz.moviesapp.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.ezz.moviesapp.Helpers.MoviesContentProvider.MOVIES_CONTENT_URI;
import static com.ezz.moviesapp.Helpers.MoviesContentProvider.REVIEWS_CONTENT_URI;
import static com.ezz.moviesapp.Helpers.MoviesContentProvider.TRAILERS_CONTENT_URI;
import static com.ezz.moviesapp.Helpers.SqliteHelper.MOVIE_DATE_COLUMN;
import static com.ezz.moviesapp.Helpers.SqliteHelper.MOVIE_ID_COLUMN;
import static com.ezz.moviesapp.Helpers.SqliteHelper.MOVIE_OVERVIEW_COLUMN;
import static com.ezz.moviesapp.Helpers.SqliteHelper.MOVIE_POSTER_COLUMN;
import static com.ezz.moviesapp.Helpers.SqliteHelper.MOVIE_POSTER_URL_COLUMN;
import static com.ezz.moviesapp.Helpers.SqliteHelper.MOVIE_RATE_COLUMN;
import static com.ezz.moviesapp.Helpers.SqliteHelper.MOVIE_TITLE_COLUMN;
import static com.ezz.moviesapp.Helpers.SqliteHelper.REVIEW_CONTENT_COLUMN;
import static com.ezz.moviesapp.Helpers.SqliteHelper.REVIEW_CONTENT_INDEX;
import static com.ezz.moviesapp.Helpers.SqliteHelper.REVIEW_URL_COLUMN;
import static com.ezz.moviesapp.Helpers.SqliteHelper.REVIEW_URL_INDEX;
import static com.ezz.moviesapp.Helpers.SqliteHelper.TRAILER_KEY_COLUMN;
import static com.ezz.moviesapp.Helpers.SqliteHelper.TRAILER_KEY_INDEX;

/**
 * Created by samar ezz on 11/22/2017.
 */

public class MovieDetailPresenter {
    private MovieDetailView movieDetailView;
    private ServicesCaller servicesCaller;
    private boolean isFavorite;

    public MovieDetailPresenter(MovieDetailView movieDetailView) {
        this.movieDetailView = movieDetailView;
    }

    public void getTrailersAndReviews(Context context, int movieId) {
        Uri uri = MOVIES_CONTENT_URI.buildUpon().appendPath(String.valueOf(movieId)).build();
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                isFavorite = false;
                movieDetailView.movieExistOrNot(isFavorite);
            } else {
                isFavorite = true;
                movieDetailView.movieExistOrNot(isFavorite);
            }
            cursor.close();
        }
        getTrailers(context, movieId);
        getReviews(context, movieId);
    }

    public void getTrailers(Context context, int movieId) {
        if (isFavorite) {
            Uri uri = TRAILERS_CONTENT_URI.buildUpon().appendPath(String.valueOf(movieId)).build();
            Cursor trailersCursor = context.getContentResolver().query(uri, null, null, null, null);
            if (trailersCursor != null) {
                if (trailersCursor.getCount() > 0) {
                    movieDetailView.onTrailersLoaded(getTrailers(trailersCursor));
                }
            }
        } else {
            if (Utils.checkIfConnectedToTheInternet(context)) {
                movieDetailView.showOrHideProgress(true);
                servicesCaller = ServicesCaller.getInstance(context);
                Uri.Builder uri = new Uri.Builder()
                        .appendPath(String.valueOf(movieId))
                        .appendPath(ServicesCaller.VIDEOS)
                        .appendQueryParameter(ServicesCaller.API_KEY, ServicesCaller.API_KEY_VALUE);
                String url;
                url = String.format("%s%s", ServicesCaller.MOVIE_DB_BASE_URL, uri.toString());
                servicesCaller.getTrailers(url, trailersSuccessListener, trailersErrorListener);
            } else {
                movieDetailView.showOrHideProgress(false);
                movieDetailView.noInternet();
            }
        }
    }

    public void getReviews(Context context, int movieId) {
        if (isFavorite) {
            Uri uri = REVIEWS_CONTENT_URI.buildUpon().appendPath(String.valueOf(movieId)).build();
            Cursor reviewsCursor = context.getContentResolver().query(uri, null, null, null, null);
            if (reviewsCursor.getCount() > 0) {
                movieDetailView.onReviewsLoaded(getReviews(reviewsCursor));
            }
        } else {
            if (Utils.checkIfConnectedToTheInternet(context)) {
                movieDetailView.showOrHideProgress(true);
                servicesCaller = ServicesCaller.getInstance(context);
                Uri.Builder uri = new Uri.Builder()
                        .appendPath(String.valueOf(movieId))
                        .appendPath(ServicesCaller.REVIEWS)
                        .appendQueryParameter(ServicesCaller.API_KEY, ServicesCaller.API_KEY_VALUE);
                String url;
                url = String.format("%s%s", ServicesCaller.MOVIE_DB_BASE_URL, uri.toString());
                servicesCaller.getReviews(url, reviewsSuccessListener, reviewsErrorListener);
            } else {
                movieDetailView.showOrHideProgress(false);
                movieDetailView.noInternet();
            }
        }
    }

    private Response.Listener<String> trailersSuccessListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            movieDetailView.showOrHideProgress(false);
            MovieVideosResponse movieVideosResponse = GsonHelper.movieVideosResponse(response);
            movieDetailView.onTrailersLoaded(movieVideosResponse.getVideos());
        }
    };
    private Response.ErrorListener trailersErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            movieDetailView.showOrHideProgress(false);
            movieDetailView.trailersNetworkError();
        }
    };

    private Response.Listener<String> reviewsSuccessListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            movieDetailView.showOrHideProgress(false);
            MovieReviewsResponse movieReviewsResponse = GsonHelper.movieReviewsResponse(response);
            movieDetailView.onReviewsLoaded(movieReviewsResponse.getReviews());
        }
    };
    private Response.ErrorListener reviewsErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            movieDetailView.showOrHideProgress(false);
            movieDetailView.reviewsNetworkError();
        }
    };

    public void shareFirstTrailer(ArrayList<Video> videoArrayList, Context context) {
        if (videoArrayList != null) {
            if (videoArrayList.get(0) != null) {
                if (!TextUtils.isEmpty(videoArrayList.get(0).getKey())) {
                    Intent intent = ShareCompat.IntentBuilder.from((Activity) context).setChooserTitle(R.string.chooserTitle)
                            .setType("text/plain")
                            .setText(String.format("%s%s", Utils.YOU_TUBE_URL, videoArrayList.get(0).getKey())).getIntent();
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    } else {
                        movieDetailView.sharingError(R.string.noAppToHandleShare);
                    }
                } else {
                    movieDetailView.sharingError(R.string.noTrailerToShare);
                }
            } else {
                movieDetailView.sharingError(R.string.noTrailerToShare);
            }
        } else {
            movieDetailView.sharingError(R.string.noTrailerToShare);
        }
    }

    private ArrayList<Video> getTrailers(Cursor cursor) {
        ArrayList<Video> videoArrayList = new ArrayList<>();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            Video video = new Video();
            video.setKey(cursor.getString(TRAILER_KEY_INDEX));
            videoArrayList.add(video);
            cursor.moveToNext();
        }
        cursor.close();
        return videoArrayList;
    }

    private ArrayList<Review> getReviews(Cursor cursor) {
        ArrayList<Review> reviewArrayList = new ArrayList<>();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            Review review = new Review();
            review.setContent(cursor.getString(REVIEW_CONTENT_INDEX));
            review.setUrl(cursor.getString(REVIEW_URL_INDEX));
            reviewArrayList.add(review);
            cursor.moveToNext();
        }
        cursor.close();
        return reviewArrayList;
    }

    public void addOrDeleteMovieFromDB(Context context,
                                       boolean isFavorite,
                                       Movie movie,
                                       ArrayList<Video> videoArrayList,
                                       ArrayList<Review> reviewArrayList, BitmapDrawable imgPoster) {
        if (isFavorite) {
            deleteMovieDB(context, movie.getId());
        } else {
            addMovieDataToDB(context, movie, videoArrayList, reviewArrayList, imgPoster);
        }
    }

    private void deleteMovieDB(Context context, int id) {
        Uri movieUri = MOVIES_CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        Uri trailerUri = TRAILERS_CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        Uri reviewUri = REVIEWS_CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        context.getContentResolver().delete(movieUri, null, null);
        context.getContentResolver().delete(trailerUri, null, null);
        context.getContentResolver().delete(reviewUri, null, null);
        movieDetailView.movieExistOrNot(false);
    }

    private void addMovieDataToDB(Context context,
                                  Movie movie,
                                  ArrayList<Video> videoArrayList,
                                  ArrayList<Review> reviewArrayList, BitmapDrawable imgPoster) {
        movieDetailView.showOrHideProgress(true);
        Uri uri = addMovieToDB(context, movie, imgPoster);
        if (uri != null) {
            movieDetailView.movieExistOrNot(true);
            if (videoArrayList != null) {
                int trailerRows = addTrailersToDB(context, movie, videoArrayList);
                if (trailerRows == videoArrayList.size()) {
                    handleAddingReviewsInDB(context, movie, reviewArrayList);
                } else {
                    movieDetailView.showOrHideProgress(false);
                    Toast.makeText(context, R.string.addingTrailersFailed, Toast.LENGTH_LONG).show();
                }
            } else {
                movieDetailView.showOrHideProgress(false);
                Toast.makeText(context, R.string.addingTrailersFailed, Toast.LENGTH_LONG).show();
            }

        } else {
            movieDetailView.showOrHideProgress(false);
            Toast.makeText(context, R.string.addingFailed, Toast.LENGTH_LONG).show();
        }
    }

    private Uri addMovieToDB(Context context, Movie movie, BitmapDrawable imgPoster) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        (imgPoster).getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, stream);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MOVIE_ID_COLUMN, movie.getId());
        contentValues.put(MOVIE_TITLE_COLUMN, movie.getTitle());
        contentValues.put(MOVIE_OVERVIEW_COLUMN, movie.getOverview());
        contentValues.put(MOVIE_DATE_COLUMN, movie.getReleaseDate());
        contentValues.put(MOVIE_RATE_COLUMN, movie.getVoteAverage());
        contentValues.put(MOVIE_POSTER_URL_COLUMN, movie.getPosterPath());
        contentValues.put(MOVIE_POSTER_COLUMN, stream.toByteArray());
        return context.getContentResolver().insert(MOVIES_CONTENT_URI, contentValues);
    }

    private int addTrailersToDB(Context context, Movie movie, ArrayList<Video> videoArrayList) {
        ContentValues[] trailersContentValues = new ContentValues[videoArrayList.size()];
        for (int i = 0; i < videoArrayList.size(); i++) {
            ContentValues trailerContentValues = new ContentValues();
            trailerContentValues.put(MOVIE_ID_COLUMN, movie.getId());
            trailerContentValues.put(TRAILER_KEY_COLUMN, videoArrayList.get(i).getKey());
            trailersContentValues[i] = trailerContentValues;
        }
        return context.getContentResolver().bulkInsert(TRAILERS_CONTENT_URI, trailersContentValues);
    }

    private int addReviewsToDB(Context context, Movie movie, ArrayList<Review> reviewArrayList) {
        ContentValues[] reviewsContentValues = new ContentValues[reviewArrayList.size()];
        for (int i = 0; i < reviewArrayList.size(); i++) {
            ContentValues reviewContentValues = new ContentValues();
            reviewContentValues.put(MOVIE_ID_COLUMN, movie.getId());
            reviewContentValues.put(REVIEW_CONTENT_COLUMN, reviewArrayList.get(i).getContent());
            reviewContentValues.put(REVIEW_URL_COLUMN, reviewArrayList.get(i).getUrl());
            reviewsContentValues[i] = reviewContentValues;
        }
        return context.getContentResolver().bulkInsert(REVIEWS_CONTENT_URI, reviewsContentValues);
    }

    private void handleAddingReviewsInDB(Context context, Movie movie, ArrayList<Review> reviewArrayList) {
        if (reviewArrayList != null) {
            int reviewRows = addReviewsToDB(context, movie, reviewArrayList);
            if (reviewRows == reviewArrayList.size()) {
                movieDetailView.showOrHideProgress(false);
                Toast.makeText(context, R.string.addingSuccess, Toast.LENGTH_LONG).show();
            } else {
                movieDetailView.showOrHideProgress(false);
                Toast.makeText(context, R.string.addingReviewsFailed, Toast.LENGTH_LONG).show();
            }
        }
    }
}
