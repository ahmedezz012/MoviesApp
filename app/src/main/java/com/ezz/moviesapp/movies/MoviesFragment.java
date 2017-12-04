package com.ezz.moviesapp.movies;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ezz.moviesapp.helpers.FragmentBase;
import com.ezz.moviesapp.helpers.ServicesCaller;
import com.ezz.moviesapp.helpers.SharedPreferencesHelper;
import com.ezz.moviesapp.models.movies.Movie;
import com.ezz.moviesapp.movies.MoviesActivity.MoviesSort;
import com.ezz.moviesapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends FragmentBase implements MoviesView {


    private static final int GRID_COLUMNS_NUMS = 2;
    public static final String MOVIES = "MOVIES", IS_FAVORITE = "IS_FAVORITE";
    public static final String SCROLL_POSITION = "SCROLL_POSITION";
    private RecyclerView rvMovies;
    private boolean isFavourite;
    private ProgressBar pbLoading;
    private MoviesPresenter moviesPresenter;
    private TextView txtNoFavourites;
    private Context context;
    private ArrayList<Movie> movieArrayList;
    private MovieClickListener movieClickListener = new MovieClickListener() {
        @Override
        public void onMovieClicked(Movie movie) {
            ((MovieClickListener) context).onMovieClicked(movie);
        }
    };

    public static MoviesFragment newInstance() {
        return new MoviesFragment();
    }

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        context = getActivity();
        initializeViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        moviesPresenter = new MoviesPresenter(this);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MOVIES)) {
                getMoviesFromSaveInstanceState(savedInstanceState);
            } else {
                getMovies(SharedPreferencesHelper.getMoviesSort(context, SharedPreferencesHelper.USER_LAST_CHOICE));
            }
        } else {
            getMovies(SharedPreferencesHelper.getMoviesSort(context, SharedPreferencesHelper.USER_LAST_CHOICE));
        }
    }

    private void getMoviesFromSaveInstanceState(Bundle savedInstanceState) {
        movieArrayList = savedInstanceState.getParcelableArrayList(MOVIES);
        isFavourite = savedInstanceState.getBoolean(IS_FAVORITE);
        onMoviesLoaded(movieArrayList, isFavourite);
        rvMovies.scrollToPosition(savedInstanceState.getInt(SCROLL_POSITION));
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (movieArrayList != null) {
            outState.putInt(SCROLL_POSITION, ((LinearLayoutManager) rvMovies.getLayoutManager()).findFirstVisibleItemPosition());
            outState.putParcelableArrayList(MOVIES, movieArrayList);
            outState.putBoolean(IS_FAVORITE, isFavourite);
        }
    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected void initializeViews(View view) {
        rvMovies = view.findViewById(R.id.rvMovies);
        pbLoading = view.findViewById(R.id.pbLoading);
        txtNoFavourites = view.findViewById(R.id.txtNoFavourites);
    }

    @Override
    public void showOrHideProgress(boolean show) {
        if (show) {
            pbLoading.setVisibility(View.VISIBLE);
            rvMovies.setVisibility(View.GONE);
            txtNoFavourites.setVisibility(View.GONE);
        } else {
            pbLoading.setVisibility(View.GONE);
        }
    }

    @Override
    public void networkError() {
        showOrHideProgress(false);
        txtNoFavourites.setVisibility(View.GONE);
        Toast.makeText(context, R.string.networkingError, Toast.LENGTH_LONG).show();
    }

    @Override
    public void noInternet() {
        showOrHideProgress(false);
        txtNoFavourites.setVisibility(View.GONE);
        Toast.makeText(context, R.string.noInternetConnection, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMoviesLoaded(ArrayList<Movie> movieArrayList, boolean isFavourite) {
        this.isFavourite = isFavourite;
        this.movieArrayList = movieArrayList;
        showOrHideProgress(false);
        txtNoFavourites.setVisibility(View.GONE);
        rvMovies.setVisibility(View.VISIBLE);
        rvMovies.setLayoutManager(new GridLayoutManager(context, GRID_COLUMNS_NUMS));
        rvMovies.setAdapter(new MoviesAdapter(context, movieArrayList, movieClickListener, isFavourite));
    }

    @Override
    public void noFavourites() {
        showOrHideProgress(false);
        txtNoFavourites.setVisibility(View.VISIBLE);
    }

    public void getMovies(MoviesSort moviesSort) {
        moviesPresenter.getMovies(context, moviesSort);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ServicesCaller.getInstance(context).getRequestQueue().cancelAll(ServicesCaller.Tag.Movies);
    }
}
