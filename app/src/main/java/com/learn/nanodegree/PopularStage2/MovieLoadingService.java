package com.learn.nanodegree.PopularStage2;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;

import com.learn.nanodegree.PopularStage2.Utils.BackgroundFetchTask;
import com.learn.nanodegree.PopularStage2.data.MovieConsts;
import com.learn.nanodegree.PopularStage2.database.MovieContract;

import java.net.URI;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MovieLoadingService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public MovieLoadingService() {
        super("MovieLoadingService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
          ContentValues[] mPopularValues = BackgroundFetchTask.fetchMovieDetails(MovieConsts.MOVIE_POPULAR);
            ContentValues[] mTopRatedValues = BackgroundFetchTask.fetchMovieDetails(MovieConsts.MOVIE_TOP_RATED);
 //           System.out.println("ganesh in onHandleZIntent::"+mTopRatedValues.toString());
            getContentResolver().delete(MovieContract.PopularMovies.CONTENT_URI,null,null);
            getContentResolver().delete(MovieContract.TopratedMovies.CONTENT_URI,null,null);
            if(mPopularValues!=null)
            getContentResolver().bulkInsert(MovieContract.PopularMovies.CONTENT_URI,mPopularValues);
            if(mTopRatedValues!=null)
            getContentResolver().bulkInsert(MovieContract.TopratedMovies.CONTENT_URI,mTopRatedValues);

        }
    }

}
