package com.learn.nanodegree.PopularStage2.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import com.learn.nanodegree.PopularStage2.MovieLoadingService;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ganesh on 8/9/17.
 */

    public class BackgroundFetchTask {

        public static ContentValues[] fetchMovieDetails(String... params) {
           ContentValues[] mContentValues=null;
            String movie_type = params[0];
            try {
                URL mUrl = NetworkUtil.buildUrl(movie_type);
                String result = NetworkUtil.getResponseFromHttpUrl(mUrl);
                mContentValues = MovieJsonUtils.getMovieDataFromJsonString(result);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mContentValues;
        }


    }

