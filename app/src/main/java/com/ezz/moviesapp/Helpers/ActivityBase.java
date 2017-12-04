package com.ezz.moviesapp.Helpers;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.ezz.moviesapp.Movies.MoviesActivity;
import com.ezz.moviesapp.R;

/**
 * Created by samar ezz on 11/10/2017.
 */

public abstract class ActivityBase extends AppCompatActivity {

    private Toolbar toolbar;

    protected void setToolBar(Toolbar toolBar, String title, boolean showUpButton) {
        this.toolbar = toolBar;
        setSupportActionBar(toolbar);
        if (TextUtils.isEmpty(title)) {
            MoviesActivity.MoviesSort moviesSort = SharedPreferencesHelper.getMoviesSort(ActivityBase.this, SharedPreferencesHelper.USER_LAST_CHOICE);
            if (moviesSort == MoviesActivity.MoviesSort.POPULAR) {
                getSupportActionBar().setTitle(getString(R.string.popularMovies));
            } else if (moviesSort == MoviesActivity.MoviesSort.TOP_RATED) {
                getSupportActionBar().setTitle(getString(R.string.topRatedMovies));
            } else if (moviesSort == MoviesActivity.MoviesSort.FAVORITES) {
                getSupportActionBar().setTitle(getString(R.string.favoritesMovies));
            }
        } else
            getSupportActionBar().setTitle(title);


        if (showUpButton) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public Fragment getFragmentByTag(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager.findFragmentByTag(tag);
    }

    protected abstract void initializeView();

    protected abstract void loadFragment(Fragment fragment);
}
