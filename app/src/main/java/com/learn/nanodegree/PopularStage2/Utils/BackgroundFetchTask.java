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



  /*  public interface Delegate {
            void onMovieDataDelegated(Vector<ContentValues> contentValues) ;
        }

        Delegate delegate = null;
        public boolean sortOrder;

        public BackgroundFetchTask(Delegate delegate){
            this.delegate = delegate;
        }

        public void sortOrder(
            boolean sort) {
            sortOrder = sort;
        }
        @Override
        protected Vector<ContentValues> doInBackground(Void... params) {
            Vector<ContentValues> vector = null;
            try {
                URL mUrl = NetworkUtil.buildUrl(sortOrder);
                String result = NetworkUtil.getResponseFromHttpUrl(mUrl);
                vector = MovieJsonUtils.getMovieDataFromJsonString(result);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return vector;
        }

        @Override
        protected void onPostExecute(Vector<ContentValues> contentValues) {
            super.onPostExecute(contentValues);
            delegate.onMovieDataDelegated(contentValues);
        }*/

        public static ContentValues[] fetchMovieDetails(String... params) {
            System.out.println("ganesh in fetchMovieDetails");
           ContentValues[] mContentValues=null;
            String movie_type = params[0];
            try {
                URL mUrl = NetworkUtil.buildUrl(movie_type);
                String result = NetworkUtil.getResponseFromHttpUrl(mUrl);
                mContentValues = MovieJsonUtils.getMovieDataFromJsonString(result);
                System.out.println("ganesh in the fetchMovieDetails ::"+mContentValues.toString());
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

