package com.ezz.moviesapp.MovieDetails;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezz.moviesapp.Helpers.FragmentBase;
import com.ezz.moviesapp.Helpers.ServicesCaller;
import com.ezz.moviesapp.Helpers.Utils;
import com.ezz.moviesapp.Models.Movies.Movie;
import com.ezz.moviesapp.Models.Reviews.Review;
import com.ezz.moviesapp.Models.Videos.Video;
import com.ezz.moviesapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.ezz.moviesapp.Movies.MoviesFragment.IS_FAVORITE;


public class MovieDetailFragment extends FragmentBase implements MovieDetailView {

    private TextView txtMovieName, txtYear, txtRate, txtOverview;
    private ImageView imgMoviePoster;
    private RecyclerView rvTrailers, rvReviews;
    private RelativeLayout rlProgress;
    private Button btnFavourites;
    private Movie movie;
    private static final String ARG_MOVIE = "ARG_MOVIE";
    private MovieDetailPresenter movieDetailPresenter;
    private Context context;
    private ArrayList<Video> videoArrayList;
    private ArrayList<Review> reviewArrayList;
    public static final String VIDEOS = "VIDEOS";
    public static final String REVIEWS = "REVIEWS";
    private boolean isFavorite = false;

    private TrailerAdapter.TrailerClickListener trailerClickListener = new TrailerAdapter.TrailerClickListener() {
        @Override
        public void onTrailerClicked(int position) {
            intentActionView(
                    Uri.parse(String.format("%s%s", Utils.YOU_TUBE_URL, videoArrayList.get(position).getKey())));
        }
    };

    private ReviewsAdapter.ReviewClickListener reviewClickListener = new ReviewsAdapter.ReviewClickListener() {

        @Override
        public void onReviewClicked(int position) {
            intentActionView(Uri.parse(reviewArrayList.get(position).getUrl()));
        }
    };

    private void intentActionView(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(context, R.string.noAppToHandleLink, Toast.LENGTH_LONG).show();
        }
    }

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    public static MovieDetailFragment newInstance(Movie movie) {
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE, movie);
        movieDetailFragment.setArguments(args);
        return movieDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = getArguments().getParcelable(ARG_MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        context = getActivity();
        initializeViews(view);
        setListeners();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        movieDetailPresenter = new MovieDetailPresenter(this);
        bindData();
        if (savedInstanceState != null) {
            isFavorite = savedInstanceState.getBoolean(IS_FAVORITE);
            if (savedInstanceState.containsKey(VIDEOS)) {
                videoArrayList = savedInstanceState.getParcelableArrayList(VIDEOS);
                onTrailersLoaded(videoArrayList);
            } else {
                movieDetailPresenter.getTrailers(context, movie.getId());
            }
            if (savedInstanceState.containsKey(REVIEWS)) {
                reviewArrayList = savedInstanceState.getParcelableArrayList(REVIEWS);
                onReviewsLoaded(reviewArrayList);
            } else {
                movieDetailPresenter.getReviews(context, movie.getId());
            }
        } else
            movieDetailPresenter.getTrailersAndReviews(context, movie.getId());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_FAVORITE, isFavorite);
        if (videoArrayList != null)
            outState.putParcelableArrayList(VIDEOS, videoArrayList);
        if (reviewArrayList != null)
            outState.putParcelableArrayList(REVIEWS, reviewArrayList);
    }

    @Override
    protected void setListeners() {
        btnFavourites.setOnClickListener(btnFavouritesClickListener);
    }

    @Override
    protected void initializeViews(View view) {
        imgMoviePoster = view.findViewById(R.id.imgMoviePoster);
        txtYear = view.findViewById(R.id.txtYear);
        txtMovieName = view.findViewById(R.id.txtMovieName);
        txtOverview = view.findViewById(R.id.txtOverview);
        txtRate = view.findViewById(R.id.txtRate);
        rvTrailers = view.findViewById(R.id.rvTrailers);
        rvReviews = view.findViewById(R.id.rvReviews);
        rlProgress = view.findViewById(R.id.rlProgress);
        ViewCompat.setNestedScrollingEnabled(rvReviews, false);
        ViewCompat.setNestedScrollingEnabled(rvTrailers, false);
        btnFavourites = view.findViewById(R.id.btnFavourites);
    }

    @Override
    public void bindData() {
        txtMovieName.setText(movie.getTitle());
        txtRate.setText(String.format("%s/10", String.valueOf(movie.getVoteAverage())));
        txtOverview.setText(movie.getOverview());
        txtYear.setText(Utils.getYearFromString(movie.getReleaseDate()));
        Picasso.with(context).load(String.format("%s%s", Utils.BASE_IMAGE_URL, movie.getPosterPath())).into(imgMoviePoster);
    }

    @Override
    public void showOrHideProgress(boolean show) {
        if (show) {
            rlProgress.setVisibility(View.VISIBLE);
        } else {
            rlProgress.setVisibility(View.GONE);
        }
        btnFavourites.setEnabled(!show);
    }

    @Override
    public void movieExistOrNot(boolean isFavorite) {
        this.isFavorite = isFavorite;
        if (isFavorite)
            btnFavourites.setText(R.string.unMark);
        else
            btnFavourites.setText(R.string.markAsFavourite);
    }

    @Override
    public void trailersNetworkError() {
        Toast.makeText(context, R.string.networkingError, Toast.LENGTH_LONG).show();
    }

    @Override
    public void reviewsNetworkError() {
        Toast.makeText(context, R.string.networkingError, Toast.LENGTH_LONG).show();
    }


    @Override
    public void noInternet() {
        Toast.makeText(context, R.string.noInternetConnection, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTrailersLoaded(ArrayList<Video> videoArrayList) {
        this.videoArrayList = videoArrayList;
        rvTrailers.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvTrailers.setAdapter(new TrailerAdapter(context, videoArrayList, trailerClickListener));
    }

    @Override
    public void onReviewsLoaded(ArrayList<Review> reviewArrayList) {
        this.reviewArrayList = reviewArrayList;
        rvReviews.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvReviews.setAdapter(new ReviewsAdapter(context, reviewArrayList, reviewClickListener));
    }

    @Override
    public void sharingError(int message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ServicesCaller.getInstance(context).getRequestQueue().cancelAll(ServicesCaller.Tag.Trailers);
        ServicesCaller.getInstance(context).getRequestQueue().cancelAll(ServicesCaller.Tag.Reviews);
    }

    private View.OnClickListener btnFavouritesClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            movieDetailPresenter.addOrDeleteMovieFromDB(context,
                    isFavorite,
                    movie, videoArrayList, reviewArrayList,
                    (BitmapDrawable) imgMoviePoster.getDrawable());
        }
    };

    public void shareFirstTrailer() {
        movieDetailPresenter.shareFirstTrailer(videoArrayList, context);
    }
}
