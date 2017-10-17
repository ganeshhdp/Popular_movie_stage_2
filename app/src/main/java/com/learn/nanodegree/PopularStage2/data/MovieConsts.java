package com.learn.nanodegree.PopularStage2.data;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * Created by ganesh on 8/6/17.
 */


public class MovieConsts {

    public static final String MOVIE_URL = "http://api.themoviedb.org/3/movie/";
    public static final String API_KEY="";
    public static final String MOVIE_POPULAR = "popular";
    public static final String MOVIE_TOP_RATED = "top_rated";
    public static final String MOVIE_FAVORITE = "favorite";
    public static final String VOTE_COUNT = "vote_count";
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String TITLE = "title";
    public static final String POSTER_PATH = "poster_path";
    public static final String ORIGINAL_TITLE = "original_title";
    public static final String BACKDROP_PATH = "backdrop_path";
    public static final String OVERVIEW = "overview";
    public static final String RELEASE_DATE = "release_date";
    public static final String MOVIEDB_ID = "id";
    public static final String POSTER_URL = "http://image.tmdb.org/t/p/w185//";
    public static final String ACTION_POPULAR_MOVIE = "com.popularmovies.popular";
    public static final String ACTION_TOP_RATED_MOVIE = "com.popularmovies.top_rated";
    public static final String ACTION_FAVORITE_MOVIE = "com.popularmovies.favorite";
    public static final String MOVIEDB_DELIMINATOR = ":::";

    public static String getUrlString(String movieType) {
        String mUrl = null;
        switch (movieType) {
            case MovieConsts.MOVIE_POPULAR:
                mUrl =  MOVIE_URL + MOVIE_POPULAR;
                break;
            case MovieConsts.MOVIE_TOP_RATED:
                mUrl = MOVIE_URL + MOVIE_TOP_RATED;
            break;
            default:
                break;
        }
        return  mUrl;
    }
    public static String getTrailerUrlString(int id) {
        String mUrl = null;
        mUrl=MOVIE_URL+Integer.toString(id)+"/videos";
        return mUrl;
    }

    public static String getReviewUrl(int id){
        String mUrl = null;
        mUrl = MOVIE_URL+Integer.toString(id)+"/reviews";
        return mUrl;
    }

    public static void watchYoutubeVideo(String id, Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            if (MovieConsts.isAppInstalled("com.google.android.youtube", context)) {
                intent.setClassName("com.google.android.youtube", "com.google.android.youtube.WatchActivity");
            }
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            context.startActivity(intent);
        }
    }


    public static boolean isAppInstalled(String uri, Context context) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }
}
