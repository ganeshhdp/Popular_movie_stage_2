package com.learn.nanodegree.PopularStage2.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by ganesh on 10/8/17.
 */

public class MovieProvider extends ContentProvider {

    public static final int MOVIE_POPLUAR_LIST = 100;
    public static final int POPULAR_MOVIE_WITH_ID = 101;
    public static final int MOVIE_TOP_RATED_LIST = 110;
    public static final int MOVIE_TOP_RATED_WITH_ID = 111;
    public static final int MOVIE_FAVORITE_LIST = 120;
    public static final int MOVIE_FAVORITE_WITH_ID = 121;
    MovieHelper helper;

    private static final String sMoviePopularIdSelection =
            MovieContract.PopularMovies.TABLE_NAME +
                    "." + MovieContract.PopularMovies.COLUMN_MOVIE_ID + " = ? ";

    private static final String sMovieTopRatedIdSelection =
            MovieContract.TopratedMovies.TABLE_NAME +
                    "." + MovieContract.TopratedMovies.COLUMN_MOVIE_ID + " = ? ";

    private static final String sMovieFavoriteIdSelection =
            MovieContract.FavoriteMovie.TABLE_NAME +
                    "." + MovieContract.CommonMovieCol.COLUMN_MOVIE_ID + " = ? ";

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        Log.d(TAG,"buildUriMatcher");
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        uriMatcher.addURI(authority, MovieContract.PATH_POPULAR, MOVIE_POPLUAR_LIST);
        uriMatcher.addURI(authority, MovieContract.PATH_POPULAR + "/#", POPULAR_MOVIE_WITH_ID);
        uriMatcher.addURI(authority, MovieContract.PATH_TOP_RATED, MOVIE_TOP_RATED_LIST);
        uriMatcher.addURI(authority, MovieContract.PATH_TOP_RATED + "/#", MOVIE_TOP_RATED_WITH_ID);
        uriMatcher.addURI(authority, MovieContract.PATH_FAVORITE, MOVIE_FAVORITE_LIST);
        uriMatcher.addURI(authority, MovieContract.PATH_FAVORITE + "/#", MOVIE_FAVORITE_WITH_ID);
        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        helper = new MovieHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_POPLUAR_LIST: {
                cursor = helper.getReadableDatabase().query(
                        MovieContract.PopularMovies.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        sortOrder);

                break;
            }
            case MOVIE_TOP_RATED_LIST: {
                cursor = helper.getReadableDatabase().query(
                        MovieContract.TopratedMovies.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }
            case MOVIE_TOP_RATED_WITH_ID: {
                cursor = getMovieById(uri, projection, sortOrder, MOVIE_TOP_RATED_WITH_ID);
                break;
            }
            case MOVIE_FAVORITE_LIST: {
                cursor = helper.getReadableDatabase().query(
                        MovieContract.FavoriteMovie.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }
            case MOVIE_FAVORITE_WITH_ID: {
                cursor = getMovieById(uri, projection, sortOrder, MOVIE_FAVORITE_WITH_ID);
                break;
            }
            case POPULAR_MOVIE_WITH_ID: {
                cursor = getMovieById(uri, projection, sortOrder, POPULAR_MOVIE_WITH_ID);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = helper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        long id;
        switch (match) {
            case MOVIE_POPLUAR_LIST:
                id = db.insert(MovieContract.PopularMovies.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieContract.PopularMovies.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + id);
                }
                break;
            case MOVIE_TOP_RATED_LIST:
                id = db.insert(MovieContract.TopratedMovies.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieContract.TopratedMovies.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + id);
                }
                break;
            case MOVIE_FAVORITE_LIST:
                id = db.insert(MovieContract.FavoriteMovie.TABLE_NAME, null, values);

                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieContract.FavoriteMovie.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + id);
                }
                break;

            default:
                throw new RuntimeException("We are not implementing insert in Sunshine. Use bulkInsert instead");
        }
        ContentResolver resolver = getContext().getContentResolver();
        synchronized (resolver) {
            resolver.notify();
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted = 0;
        if (null == selection) selection = "1";
        switch (sUriMatcher.match(uri)) {
            case MOVIE_POPLUAR_LIST:
                numRowsDeleted = helper.getWritableDatabase().delete(
                        MovieContract.PopularMovies.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case MOVIE_TOP_RATED_LIST:
                numRowsDeleted = helper.getWritableDatabase().delete(
                        MovieContract.PopularMovies.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case MOVIE_FAVORITE_WITH_ID:
                String favMovie_id = MovieContract.FavoriteMovie.getMovieIDFromUri(uri);
                String deleteOneRowFavoriteSelectionArgs[] = {favMovie_id};
                selection = MovieContract.FavoriteMovie.TABLE_NAME +
                        "." + MovieContract.CommonMovieCol.COLUMN_MOVIE_ID + " = ? ";
                numRowsDeleted = helper.getWritableDatabase().delete(
                        MovieContract.FavoriteMovie.TABLE_NAME,
                        selection,
                        deleteOneRowFavoriteSelectionArgs);
                break;
            case POPULAR_MOVIE_WITH_ID:
                String movie_id = uri.getPathSegments().get(1);
                String deleteOneRowSelectionArgs[] = {movie_id};
                numRowsDeleted = helper.getWritableDatabase().delete(
                        MovieContract.PopularMovies.TABLE_NAME,
                        selection,
                        deleteOneRowSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted = 0;
        if (null == selection) selection = "1";
        switch (sUriMatcher.match(uri)) {
            case MOVIE_POPLUAR_LIST:
                numRowsDeleted = helper.getWritableDatabase().update(
                        MovieContract.PopularMovies.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;

            case POPULAR_MOVIE_WITH_ID:
                String movie_id = uri.getPathSegments().get(1);
                String deletOneRowSelectionArgs[] = {movie_id};
                numRowsDeleted = helper.getWritableDatabase().update(
                        MovieContract.TopratedMovies.TABLE_NAME,
                        values,
                        sMoviePopularIdSelection,
                        deletOneRowSelectionArgs);
                break;
            case MOVIE_FAVORITE_LIST:
                numRowsDeleted = helper.getWritableDatabase().update(
                        MovieContract.FavoriteMovie.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsDeleted;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = helper.getWritableDatabase();
        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned
        int rowsInserted;
        switch (match) {
            case MOVIE_POPLUAR_LIST:
                rowsInserted = 0;
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.PopularMovies.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            case MOVIE_TOP_RATED_LIST:
                db.beginTransaction();
                rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.TopratedMovies.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    private Cursor getMovieById(Uri uri, String[] projection, String sortOrder, int movieType) {
        String movie_id;
        String selection = null;
        String selectionArgs[] = new String[1];
        String table = null;
        switch (movieType) {
            case POPULAR_MOVIE_WITH_ID:
                movie_id = uri.getPathSegments().get(1);
                selection = sMoviePopularIdSelection;
                selectionArgs[0] = movie_id;
                table = MovieContract.PopularMovies.TABLE_NAME;
                break;

            case MOVIE_TOP_RATED_WITH_ID:
                movie_id = uri.getPathSegments().get(1);
                selection = sMovieTopRatedIdSelection;
                selectionArgs[0] = movie_id;
                table = MovieContract.TopratedMovies.TABLE_NAME;
                break;

            case MOVIE_FAVORITE_WITH_ID:
                movie_id = MovieContract.FavoriteMovie.getMovieIDFromUri(uri);
                selection = sMovieFavoriteIdSelection;
                selectionArgs[0] = movie_id;
                table = MovieContract.FavoriteMovie.TABLE_NAME;
                break;

        }
        return helper.getReadableDatabase().query(
                table,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }
}
