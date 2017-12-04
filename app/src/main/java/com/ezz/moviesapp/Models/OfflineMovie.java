package com.ezz.moviesapp.Models;

import com.ezz.moviesapp.Models.Movies.Movie;

import java.sql.Blob;

/**
 * Created by samar ezz on 11/22/2017.
 */

public class OfflineMovie extends Movie {

    private int id;

    private String title;

    private String overView;

    private String year;

    private double rate;

    private String posterUrl;



    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

}
