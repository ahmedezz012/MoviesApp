package com.ezz.moviesapp.MovieDetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ezz.moviesapp.Helpers.ActivityBase;
import com.ezz.moviesapp.Models.Movies.Movie;
import com.ezz.moviesapp.R;

public class MovieDetailActivity extends ActivityBase {

    private static final String MOVIE = "MOVIE";
    private static final String MOVIE_DETAIL_FRAGMENT = "MOVIE_DETAIL_FRAGMENT";
    private Toolbar toolBar;
    private MovieDetailFragment movieDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        initializeView();
        setToolBar(toolBar, getString(R.string.movieDetail), true);
        if (getIntent() != null) {
            Movie movie = getIntent().getParcelableExtra(MOVIE);
            if (savedInstanceState == null) {
                movieDetailFragment = MovieDetailFragment.newInstance(movie);
                loadFragment(movieDetailFragment);
            }else
            {
                movieDetailFragment = (MovieDetailFragment) getFragmentByTag(MOVIE_DETAIL_FRAGMENT);
            }
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
        fragmentTransaction.replace(R.id.frmFragmentContainer, fragment,MOVIE_DETAIL_FRAGMENT);
        fragmentTransaction.commit();
    }

    public static void startActivity(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(MOVIE, movie);
        context.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                movieDetailFragment.shareFirstTrailer();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
