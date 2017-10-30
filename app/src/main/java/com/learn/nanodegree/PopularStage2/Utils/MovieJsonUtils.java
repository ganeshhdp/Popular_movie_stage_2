package com.learn.nanodegree.PopularStage2.Utils;

import android.content.ContentValues;

import com.learn.nanodegree.PopularStage2.data.MovieConsts;
import com.learn.nanodegree.PopularStage2.database.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

/**
 * Created by ganesh on 8/9/17.
 */

public class MovieJsonUtils {

    /**
     * This method returns the vector with all the movie details in the content values
     * @param json
     * @return
     */

    private int vote_count;
    private float vote_average;
    private String mtitle;
    private String poster_path;
    private String original_title;
    private String backdrop_path;
    private String mOverView;
    private String mReleaseDate;
    final static String MOVIE_RESULT = "results";
    public static ContentValues[] getMovieDataFromJsonString ( String json ) throws JSONException {
        Vector mVector = new Vector();
        JSONObject movieJson = new JSONObject(json);
        JSONArray weatherArray = movieJson.getJSONArray(MOVIE_RESULT);
        ContentValues[] mContentValues = new ContentValues[weatherArray.length()];
        for (int i = 0; i < weatherArray.length(); i++) {
            JSONObject movieData = weatherArray.getJSONObject(i);
            mContentValues[i] = new ContentValues();
            mContentValues[i].put(MovieContract.CommonMovieCol.COLUMN_VOTE_AVERAGE,movieData.getString(MovieConsts.VOTE_AVERAGE));
            mContentValues[i].put(MovieContract.CommonMovieCol.COLUMN_POSTER_PATH,movieData.getString(MovieConsts.POSTER_PATH));
            mContentValues[i].put(MovieContract.CommonMovieCol.COLUMN_ORIGINAL_TITLE,movieData.getString(MovieConsts.ORIGINAL_TITLE));
            mContentValues[i].put(MovieContract.CommonMovieCol.COLUMN_BACKDROP_PATH,movieData.getString(MovieConsts.BACKDROP_PATH));
            mContentValues[i].put(MovieContract.CommonMovieCol.COLUMN_OVERVIEW,movieData.getString(MovieConsts.OVERVIEW));
            mContentValues[i].put(MovieContract.CommonMovieCol.COLUMN_RELEASE_DATE,movieData.getString(MovieConsts.RELEASE_DATE));
            int movie_id = movieData.getInt(MovieConsts.MOVIEDB_ID);
            mContentValues[i].put(MovieContract.CommonMovieCol.COLUMN_MOVIE_ID,movie_id);
            String trailerJson = fetchMovieTrailerInfo(movie_id);
            if(trailerJson!=null)
            mContentValues[i].put(MovieContract.CommonMovieCol.COLUMN_TRAILER,trailerJson);
            else {
                mContentValues[i].put(MovieContract.CommonMovieCol.COLUMN_TRAILER,"No trailer");
            }
            String[] reviewJson = fetchMovieReviewInfo(movie_id);
            if(reviewJson==null){
                mContentValues[i].put(MovieContract.CommonMovieCol.COLUMN_REVIEW_NAME, "No reviewer");
                mContentValues[i].put(MovieContract.CommonMovieCol.COLUMN_REVIEW, "No review");
            }
            else {
                mContentValues[i].put(MovieContract.CommonMovieCol.COLUMN_REVIEW_NAME,reviewJson[0]);
                mContentValues[i].put(MovieContract.CommonMovieCol.COLUMN_REVIEW,reviewJson[1]);
            }
        }
        return mContentValues;
    }

    public static String fetchMovieTrailerInfo(int id){
        String result = null;
        StringBuilder videoKey = new StringBuilder();
        try {
            URL mUrl = NetworkUtil.buildTrailerUrl(id);
            result = NetworkUtil.getResponseFromHttpUrl(mUrl);
            JSONObject movieJson = new JSONObject(result);
            JSONArray resultArray = movieJson.getJSONArray(MOVIE_RESULT);
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject movieVideo = resultArray.getJSONObject(i);
                String type = movieVideo.getString("type");
                if("Trailer".equals(type) ){
                    videoKey.append( movieVideo.getString("key"));
                    videoKey.append(MovieConsts.MOVIEDB_DELIMINATOR);
                }
            }
           // result = resultArray.getString("key");
        }  catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return videoKey.toString();
    }


    private static String[] fetchMovieReviewInfo(int id){
        StringBuilder result[] = new StringBuilder[2];
        String[] stringResult = new String[2];
        String json=null;
        for(int i=0;i<2;i++){
            result[i] = new StringBuilder();
        }
        try {
            URL mUrl = NetworkUtil.buildReviewUrl(id);
            json = NetworkUtil.getResponseFromHttpUrl(mUrl);
            JSONObject resultJson = new JSONObject(json);
            JSONArray resultArray = resultJson.getJSONArray("results");
            if(resultArray.length()!=0) {
                for(int i=0;i<resultArray.length();i++) {
                    result[0].append(resultArray.getJSONObject(i).getString("author"));
                    result[0].append(MovieConsts.MOVIEDB_DELIMINATOR);
                    result[1].append(resultArray.getJSONObject(i).getString("content"));
                    result[1].append(MovieConsts.MOVIEDB_DELIMINATOR);
                }
            }else{
                return null;
            }
        }  catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i=0;i<2;i++){
            stringResult[i]=result[i].toString();
        }
        return stringResult;
    }

}
