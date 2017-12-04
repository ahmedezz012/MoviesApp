package com.ezz.moviesapp.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by samar ezz on 11/10/2017.
 */

public class Utils {

    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185/";
    public static final String YOU_TUBE_URL="https://www.youtube.com/watch?v=";

    public static boolean checkIfConnectedToTheInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetInfo != null && activeNetInfo.isAvailable()
                && activeNetInfo.isConnected();

    }

    public static String getYearFromString(String dateٍString) {
        Calendar calendar = Calendar.getInstance();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            Date date = format.parse(dateٍString);
            calendar.setTime(date);
            return String.valueOf(calendar.get(Calendar.YEAR));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
