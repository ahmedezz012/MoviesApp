package com.ezz.moviesapp.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ezz.moviesapp.Movies.MoviesActivity.MoviesSort;

/**
 * Created by samar ezz on 11/11/2017.
 */

public class SharedPreferencesHelper {
    public static final String USER_LAST_CHOICE = "USER_LAST_CHOICE";


    public static void addEnum(Context context, String key, MoviesSort moviesSort) {
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putString(key, moviesSort.toString());
        editor.apply();
    }

    public static MoviesSort getMoviesSort(Context context, String key) {
        SharedPreferences sharedPreferences;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return MoviesSort.valueOf(sharedPreferences.getString(key, MoviesSort.POPULAR.toString()));
    }
}
