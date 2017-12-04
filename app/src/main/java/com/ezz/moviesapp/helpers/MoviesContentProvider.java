package com.ezz.moviesapp.helpers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

public class MoviesContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.ezz.moviesapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private static final int ALL_MOVIES = 100, MOVIE_WITH_ID = 101;
    private static final int ALL_TRAILERS = 200, ALL_TRAILERS_BY_ID = 201;
    private static final int ALL_REVIEWS = 300, ALL_REVIEWS_BY_ID = 301;

    public static final Uri MOVIES_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(SqliteHelper.MOVIES_TABLE_NAME).build();
    public static final Uri TRAILERS_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(SqliteHelper.TRAILERS_TABLE_NAME).build();
    public static final Uri REVIEWS_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(SqliteHelper.REVIEWS_TABLE_NAME).build();

    private UriMatcher uriMatcher = buildURIMatcher();
    private SqliteHelper sqliteHelper;

    public MoviesContentProvider() {
    }

    public static UriMatcher buildURIMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, SqliteHelper.MOVIES_TABLE_NAME, ALL_MOVIES);
        uriMatcher.addURI(AUTHORITY, SqliteHelper.MOVIES_TABLE_NAME + "/#", MOVIE_WITH_ID);

        uriMatcher.addURI(AUTHORITY, SqliteHelper.TRAILERS_TABLE_NAME, ALL_TRAILERS);
        uriMatcher.addURI(AUTHORITY, SqliteHelper.TRAILERS_TABLE_NAME + "/#", ALL_TRAILERS_BY_ID);

        uriMatcher.addURI(AUTHORITY, SqliteHelper.REVIEWS_TABLE_NAME, ALL_REVIEWS);
        uriMatcher.addURI(AUTHORITY, SqliteHelper.REVIEWS_TABLE_NAME + "/#", ALL_REVIEWS_BY_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        Context context = getContext();
        sqliteHelper = new SqliteHelper(context);
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Uri returnUri = null;
        int match = uriMatcher.match(uri);
        switch (match) {
            case ALL_MOVIES:
                long id = sqliteHelper.insertRow(SqliteHelper.MOVIES_TABLE_NAME, null, values);
                if (id > 0)
                    returnUri = ContentUris.withAppendedId(MOVIES_CONTENT_URI, id);
                break;
            default:
                throw new UnsupportedOperationException("UnKnow uri");
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case ALL_TRAILERS:
                long id = 0;
                for (int i = 0; i < values.length; i++) {
                    id = sqliteHelper.insertRow(SqliteHelper.TRAILERS_TABLE_NAME, null, values[i]);
                    if (id <= 0) {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }
                }
                break;
            case ALL_REVIEWS:
                long idReview = 0;
                for (int i = 0; i < values.length; i++) {
                    idReview = sqliteHelper.insertRow(SqliteHelper.REVIEWS_TABLE_NAME, null, values[i]);
                    if (idReview <= 0) {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }
                }
                break;
            default:
                throw new UnsupportedOperationException("UnKnow uri");
        }
        return values.length;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        String mSelection = "movieId=?", movieId;
        String[] mSelectionArgs;
        movieId = uri.getPathSegments().get(1);
        mSelectionArgs = new String[]{movieId};
        int rows = 0;
        switch (match) {
            case MOVIE_WITH_ID:
                rows = sqliteHelper.deleteRow(SqliteHelper.MOVIES_TABLE_NAME, mSelection, mSelectionArgs);
                break;
            case ALL_TRAILERS_BY_ID:
                rows = sqliteHelper.deleteRow(SqliteHelper.TRAILERS_TABLE_NAME, mSelection, mSelectionArgs);
                break;
            case ALL_REVIEWS_BY_ID:
                rows = sqliteHelper.deleteRow(SqliteHelper.REVIEWS_TABLE_NAME, mSelection, mSelectionArgs);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rows;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int match = uriMatcher.match(uri);
        String mSelection = "movieId=?", movieId;
        String[] mSelectionArgs;
        Cursor cursor;
        switch (match) {
            case ALL_MOVIES:
                cursor = sqliteHelper.selectAll(SqliteHelper.MOVIES_TABLE_NAME);
                break;
            case MOVIE_WITH_ID:
                movieId = uri.getPathSegments().get(1);
                mSelectionArgs = new String[]{movieId};
                cursor = sqliteHelper.selectRow(SqliteHelper.MOVIES_TABLE_NAME, projection, mSelection, mSelectionArgs
                        , null, null, null);
                break;
            case ALL_TRAILERS_BY_ID:
                movieId = uri.getPathSegments().get(1);
                mSelectionArgs = new String[]{movieId};
                cursor = sqliteHelper.selectRow(SqliteHelper.TRAILERS_TABLE_NAME, projection, mSelection, mSelectionArgs
                        , null, null, null);
                break;
            case ALL_REVIEWS_BY_ID:
                movieId = uri.getPathSegments().get(1);
                mSelectionArgs = new String[]{movieId};
                cursor = sqliteHelper.selectRow(SqliteHelper.REVIEWS_TABLE_NAME, projection, mSelection, mSelectionArgs
                        , null, null, null);
                break;
            default:
                throw new UnsupportedOperationException("UnKnow uri");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
