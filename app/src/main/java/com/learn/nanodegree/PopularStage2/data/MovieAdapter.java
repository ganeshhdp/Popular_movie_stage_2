package com.learn.nanodegree.PopularStage2.data;

import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.learn.nanodegree.PopularStage2.R;
import com.learn.nanodegree.PopularStage2.Utils.BackgroundFetchTask;
import com.learn.nanodegree.PopularStage2.database.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by ganesh on 7/24/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private static final String LOGTAG = "MovieAdapter";
    private MovieInfo[] movieList;
    Context context;
    int lastPosition;

    private final MovieClickListener movieClickListener;

    public interface MovieClickListener {
        void onMovieSelected(MovieInfo movieSelected);
    }

    public MovieAdapter(MovieClickListener listener){
        movieClickListener = listener;
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item,parent,false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        holder.bindView(position);
        animate(holder);
    }

    private void animate(MovieHolder holder) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(holder.itemView,"alpha",0,1);
        animator.setDuration(2500);
        animator.start();
    }

    public void swapCursor( Cursor cursor) {
       this.movieList = loadMovies(cursor);
        setMovieData(cursor);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        if (movieList == null ) return 0;
        return movieList.length;
    }

    public void setMovieData(Cursor data) {

        if(data != null){
            data.moveToFirst();
            this.movieList = loadMovies(data);
        }
        lastPosition=0;

        notifyDataSetChanged();
    }

    private MovieInfo[] loadMovies(Cursor cursor){
        MovieInfo[] movieInfoList = new MovieInfo[cursor.getCount()];
        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++){
                movieInfoList[i] = new MovieInfo();
                movieInfoList[i].setMtitle(cursor.getString(cursor.getColumnIndex(MovieContract.CommonMovieCol.COLUMN_ORIGINAL_TITLE)));
                movieInfoList[i].setBackdrop_path(cursor.getString(cursor.getColumnIndex(MovieContract.CommonMovieCol.COLUMN_BACKDROP_PATH)));
                movieInfoList[i].setOriginal_title(cursor.getString(cursor.getColumnIndex(MovieContract.CommonMovieCol.COLUMN_ORIGINAL_TITLE)));
                movieInfoList[i].setmOverView(cursor.getString(cursor.getColumnIndex(MovieContract.CommonMovieCol.COLUMN_OVERVIEW)));
                movieInfoList[i].setPoster_path(cursor.getString(cursor.getColumnIndex(MovieContract.CommonMovieCol.COLUMN_POSTER_PATH)));
                movieInfoList[i].setmReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.CommonMovieCol.COLUMN_RELEASE_DATE)));
                movieInfoList[i].setVote_average(cursor.getInt(cursor.getColumnIndex(MovieContract.CommonMovieCol.COLUMN_VOTE_AVERAGE)));
                movieInfoList[i].setId(cursor.getInt(cursor.getColumnIndex(MovieContract.CommonMovieCol.COLUMN_MOVIE_ID)));
                movieInfoList[i].setTrailer(cursor.getString(cursor.getColumnIndex(MovieContract.CommonMovieCol.COLUMN_TRAILER)));
                movieInfoList[i].setReview_name(cursor.getString(cursor.getColumnIndex(MovieContract.CommonMovieCol.COLUMN_REVIEW_NAME)));
                movieInfoList[i].setReview(cursor.getString(cursor.getColumnIndex(MovieContract.CommonMovieCol.COLUMN_REVIEW)));

            cursor.moveToNext();
            }
           return movieInfoList;
        }
    public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View itemView;
        public MovieHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(this);
        }
       public void bindView(int pos) {
           ImageView image = (ImageView) itemView.findViewById(R.id.movie_image);
           String imagePath = movieList[pos].getPoster_path();
           imagePath = MovieConsts.POSTER_URL+imagePath;
           Picasso.with(context).load(imagePath)
                   .error(R.drawable.ic_no_wifi)
                   .placeholder(R.drawable.ic_loading)
                   .into(image);
       }

        @Override
        public void onClick(View v) {
            movieClickListener.onMovieSelected(movieList[getAdapterPosition()]);

        }
    }
}
