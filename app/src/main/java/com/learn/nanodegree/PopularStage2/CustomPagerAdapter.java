package com.learn.nanodegree.PopularStage2;

import android.content.Context;
import android.database.Cursor;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.learn.nanodegree.PopularStage2.data.MovieConsts;
import com.squareup.picasso.Picasso;


public class CustomPagerAdapter extends PagerAdapter{
    Context mContext;
    //private MovieItem mMovieItem;
    LayoutInflater mLayoutInflater;
    String[] mTrailerArray;
    String mMovieId;



    public CustomPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(mTrailerArray == null) return 0;
        return mTrailerArray.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ConstraintLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        ImageView trailerImageView = (ImageView) itemView.findViewById(R.id.trialer_image_view);
        Button playButton = (Button) itemView.findViewById(R.id.play_button);

        Picasso.with(mContext)
                .load("http://img.youtube.com/vi/" + mTrailerArray[position] + "/mqdefault.jpg")
                .error(R.drawable.ic_no_wifi)
                .placeholder(R.drawable.ic_loading)
                .into(trailerImageView);
        container.addView(itemView);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieConsts.watchYoutubeVideo(mTrailerArray[position],mContext);
            }
        });


        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }

    public void setMovieDetails(String[] movieDetail) {
        try {
            mTrailerArray = movieDetail[0].split(MovieConsts.MOVIEDB_DELIMINATOR);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onTrailerClicked(View view){
    }

    public void swapCursor(Cursor mCursor){

    }
}
