package com.ezz.moviesapp.Movies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ezz.moviesapp.Helpers.ActivityBase;
import com.ezz.moviesapp.Helpers.SharedPreferencesHelper;
import com.ezz.moviesapp.Models.Movies.Movie;
import com.ezz.moviesapp.MovieDetails.MovieDetailActivity;
import com.ezz.moviesapp.R;

public class MoviesActivity extends ActivityBase implements MovieClickListener {

    private static final String MOVIES_FRAGMENT_TAG = "MOVIES_FRAGMENT_TAG";
    private MoviesFragment moviesFragment;
    private Toolbar toolBar;

    @Override
    public void onMovieClicked(Movie movie) {
        MovieDetailActivity.startActivity(MoviesActivity.this, movie);
    }

    public enum MoviesSort {
        POPULAR, TOP_RATED, FAVORITES
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        initializeView();
        setToolBar(toolBar, null, false);

        if (savedInstanceState == null) {
            moviesFragment = MoviesFragment.newInstance();
            loadFragment(moviesFragment);
        } else {
            moviesFragment = (MoviesFragment) getFragmentByTag(MOVIES_FRAGMENT_TAG);
        }
    }


    @Override
    protected void initializeView() {
        toolBar = (Toolbar) findViewById(R.id.toolBar);
    }

    @Override
    protected void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frmFragmentContainer, fragment, MOVIES_FRAGMENT_TAG);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mostPopular:
                loadMovies(MoviesSort.POPULAR);
                setToolbarTitle(getString(R.string.popularMovies));
                break;
            case R.id.topRated:
                loadMovies(MoviesSort.TOP_RATED);
                setToolbarTitle(getString(R.string.topRatedMovies));
                break;
            case R.id.favorites:
                loadMovies(MoviesSort.FAVORITES);
                setToolbarTitle(getString(R.string.favoritesMovies));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMovies(MoviesSort moviesSort) {
        SharedPreferencesHelper.addEnum(MoviesActivity.this, SharedPreferencesHelper.USER_LAST_CHOICE, moviesSort);
        moviesFragment.getMovies(moviesSort);
    }
}
