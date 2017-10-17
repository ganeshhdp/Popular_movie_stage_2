package com.learn.nanodegree.PopularStage2.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ganesh on 10/8/17.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.learn.nanodegree.PopularStage2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_POPULAR = "popular";
    public static final String PATH_TOP_RATED = "top_rated";
    public static final String PATH_FAVORITE = "favorite";
    public static final String PATH_MOVIE = "movie";


    public static class PopularMovies implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_POPULAR)
                .build();

        public static final String TABLE_NAME = "popular";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_POPULAR_INDEX = "popular_index";
        public static final String COLUMN_TOP_RATED_INDEX = "top_rated_index";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_TRAILER = "trailers";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_FAVORITE_TIMESTAMP = "favorite_timestamp";
        public static final String COLUMN_DETAILS_LOADED = "details_loaded";
        public static final String COLUMN_REVIEW = "reviews";
        public static final String COLUMN_REVIEW_NAME = "review_name";

    }

    public static class TopratedMovies implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TOP_RATED)
                .build();

        public static final String TABLE_NAME = "top_Rated";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_POPULAR_INDEX = "popular_index";
        public static final String COLUMN_TOP_RATED_INDEX = "top_rated_index";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_TRAILER = "trailers";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_FAVORITE_TIMESTAMP = "favorite_timestamp";
        public static final String COLUMN_DETAILS_LOADED = "details_loaded";
        public static final String COLUMN_REVIEW = "reviews";
        public static final String COLUMN_REVIEW_NAME = "review_name";


    }

    public static class FavoriteMovie implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE)
                .build();
        public static final String TABLE_NAME = "favorite";
        public static Uri buildSelectedFavoriteMovieUri(int movieId) {
            return CONTENT_URI.buildUpon()
                    .appendPath(String.valueOf(movieId))
                    .build();
        }
        public static String getMovieIDFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static final class CommonMovieCol implements BaseColumns {
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_POPULAR_INDEX = "popular_index";
        public static final String COLUMN_TOP_RATED_INDEX = "top_rated_index";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_TRAILER = "trailers";
        public static final String COLUMN_FAVORITE_TIMESTAMP = "favorite_timestamp";
        public static final String COLUMN_REVIEW = "reviews";
        public static final String COLUMN_REVIEW_NAME = "review_name";
        public static String getMovieIDFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
