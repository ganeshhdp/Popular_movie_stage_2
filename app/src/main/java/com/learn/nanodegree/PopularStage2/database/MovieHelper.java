package com.learn.nanodegree.PopularStage2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.learn.nanodegree.PopularStage2.database.MovieContract.CommonMovieCol;
import com.learn.nanodegree.PopularStage2.database.MovieContract.PopularMovies;
import com.learn.nanodegree.PopularStage2.database.MovieContract.TopratedMovies;

import static com.learn.nanodegree.PopularStage2.database.MovieContract.PopularMovies.*;

/**
 * Created by ganesh on 10/8/17.
 */

public class MovieHelper extends SQLiteOpenHelper {
    public static final String dbName = "movie.db";
    private static final int db_version = 6;
    private static final String LOGTAG = "MovieHelper";

    public MovieHelper(Context context) {

        super(context,dbName,null,db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(LOGTAG,"onCreate");

        final String SQL_CREATE_POPULAR_MOVIE_TABLE = "CREATE TABLE " + PopularMovies.TABLE_NAME + " (" +
                PopularMovies._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +  // REQUIRED!!!
                PopularMovies.COLUMN_MOVIE_ID + " INTEGER NOT NULL UNIQUE ON CONFLICT REPLACE, " +
                PopularMovies.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                PopularMovies.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                TopratedMovies.COLUMN_TRAILER + " TEXT, " +
                PopularMovies.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, "+
                PopularMovies.COLUMN_RELEASE_DATE + " TEXT, " +
                PopularMovies.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                PopularMovies.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL ," +
                TopratedMovies.COLUMN_REVIEW + " TEXT, " +
                TopratedMovies.COLUMN_REVIEW_NAME + " TEXT" +

                " );";

        final String SQL_CREATE_TOP_RATED_MOVIE_TABLE = "CREATE TABLE " + TopratedMovies.TABLE_NAME + " (" +
                TopratedMovies._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +  // REQUIRED!!!
                TopratedMovies.COLUMN_MOVIE_ID + " INTEGER NOT NULL UNIQUE ON CONFLICT REPLACE, " +
              /*  TopratedMovies.COLUMN_POPULAR_INDEX + " INTEGER, " +
                TopratedMovies.COLUMN_TOP_RATED_INDEX + " INTEGER, " +*/
                TopratedMovies.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                TopratedMovies.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                TopratedMovies.COLUMN_RELEASE_DATE + " TEXT, " +
                TopratedMovies.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                TopratedMovies.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                TopratedMovies.COLUMN_TRAILER + " TEXT, " +
                TopratedMovies.COLUMN_BACKDROP_PATH + " TEXT ,"+
               // TopratedMovies.COLUMN_FAVORITE_TIMESTAMP + " INTEGER, " +
                TopratedMovies.COLUMN_REVIEW + " TEXT, " +
                TopratedMovies.COLUMN_REVIEW_NAME + " TEXT" +
                " );";


        final String SQL_CREATE_FAVORITE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.FavoriteMovie.TABLE_NAME + " (" +
                MovieContract.FavoriteMovie._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +  // REQUIRED!!!
                CommonMovieCol.COLUMN_MOVIE_ID + " INTEGER NOT NULL UNIQUE ON CONFLICT REPLACE, " +
//                CommonMovieCol.COLUMN_POPULAR_INDEX + " INTEGER, " +
//                CommonMovieCol.COLUMN_TOP_RATED_INDEX + " INTEGER, " +
                CommonMovieCol.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                CommonMovieCol.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                CommonMovieCol.COLUMN_RELEASE_DATE + " TEXT, " +
                CommonMovieCol.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                CommonMovieCol.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                CommonMovieCol.COLUMN_TRAILER + " TEXT, " +
                CommonMovieCol.COLUMN_BACKDROP_PATH+" TEXT, "+
               // CommonMovieCol.COLUMN_FAVORITE_TIMESTAMP + " TEXT, " +
                CommonMovieCol.COLUMN_REVIEW + " TEXT, " +
                CommonMovieCol.COLUMN_REVIEW_NAME + " TEXT" +
                " );";
        db.execSQL(SQL_CREATE_POPULAR_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_TOP_RATED_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_FAVORITE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOGTAG,"onUpgrade");
    }

}
