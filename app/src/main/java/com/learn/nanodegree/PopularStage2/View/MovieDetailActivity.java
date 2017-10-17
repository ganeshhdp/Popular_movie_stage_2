package com.learn.nanodegree.PopularStage2.View;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.learn.nanodegree.PopularStage2.CustomPagerAdapter;
import com.learn.nanodegree.PopularStage2.MovieReview;
import com.learn.nanodegree.PopularStage2.MovieSelectedReviewAdapter;
import com.learn.nanodegree.PopularStage2.R;
import com.learn.nanodegree.PopularStage2.SummaryFullActivity;
import com.learn.nanodegree.PopularStage2.data.MovieConsts;
import com.learn.nanodegree.PopularStage2.data.MovieInfo;
import com.learn.nanodegree.PopularStage2.database.MovieContract;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements MovieSelectedReviewAdapter.MovieSelectedReviewOnClickHandler {

    MovieInfo selectedMovie;
    String tabSelected;


    @BindView(R.id.movie_thumbnail)
    ImageView mMovieThumbnail;
    @BindView(R.id.main_trailer_pager_view)
    ViewPager mBackdrop;
    @BindView(R.id.favorite)
    ToggleButton mFavorite;
    @BindView(R.id.overview)
    TextView mOverView;
    @BindView(R.id.vote_average)
    TextView mVote_average;
    @BindView(R.id.original_title)
    TextView mOriginal_title;
    @BindView(R.id.recyclerview_reviews)
    RecyclerView mReviews;


    private int movieId;

    private static final String TAG = "popularmoviesstagetwo";

    private String summaryText;
    private RecyclerView mRecyclerView;
    private Uri mSelectedMovieQueryUri;
    private Cursor mMovieSelectedCursor;
    public static ProgressBar mProgressBar;
    private CustomPagerAdapter mCustomPagerAdapter;
    private MovieSelectedReviewAdapter mMovieSelectedAdapter;
    private GregorianCalendar mCalendar = new GregorianCalendar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_selected);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b!=null) {
            selectedMovie = b.getParcelable("Movie");
            tabSelected = b.getString("tab");
        }

       summaryText = selectedMovie.getmOverView();
        if (tabSelected.equals(MovieConsts.MOVIE_POPULAR)) {
            mSelectedMovieQueryUri = MovieContract.PopularMovies.CONTENT_URI;
        } else if (tabSelected.equals(MovieConsts.MOVIE_TOP_RATED)) {
            mSelectedMovieQueryUri = MovieContract.TopratedMovies.CONTENT_URI;
        } else {
            mSelectedMovieQueryUri = MovieContract.FavoriteMovie.CONTENT_URI;
        }
        movieId = selectedMovie.getId();
        mSelectedMovieQueryUri = mSelectedMovieQueryUri.buildUpon().appendPath(String.valueOf(movieId)).build();

        mMovieSelectedCursor = getContentResolver().query(
                mSelectedMovieQueryUri,
                null,
                null,
                null,
                null
        );
        mMovieSelectedCursor.moveToFirst();
        String poster = selectedMovie.getPoster_path();
        mCustomPagerAdapter = new CustomPagerAdapter(this);
        mBackdrop.setAdapter(mCustomPagerAdapter);

        Picasso.with(getApplicationContext())
                .load("http://image.tmdb.org/t/p/w185/" + poster)
                .error(R.drawable.ic_no_wifi)
                .placeholder(R.drawable.ic_loading)
                .into(mMovieThumbnail);

        if (summaryText != null) {
            mOverView.setText(summaryText);
        }

        String releaseDateText =selectedMovie.getmReleaseDate();
        String originalTitleText = selectedMovie.getOriginal_title();

        SpannableString ss1 = new SpannableString(originalTitleText);
        ss1.setSpan(new RelativeSizeSpan(1f), 0, originalTitleText.length(), 0); // set size
        SpannableString ss2 = new SpannableString(releaseDateText);
        ss2.setSpan(new RelativeSizeSpan(0.6f), 0, releaseDateText.length(), 0); // set size
        ss2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorReleaseDate)), 0, releaseDateText.length(), 0);// set color


        if (originalTitleText != null && releaseDateText != null) {
            mOriginal_title.setText(ss1);
            mOriginal_title.append(ss2);
        }
        String vote_average = selectedMovie.getVote_average()+"/10";
        mVote_average.setText(vote_average);


        mProgressBar = (ProgressBar) findViewById(R.id.movie_selected_pb_loading_indicator);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_reviews);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mMovieSelectedAdapter = new MovieSelectedReviewAdapter(getApplicationContext(), this);
        mRecyclerView.setAdapter(mMovieSelectedAdapter);
        loadMovieData(mMovieSelectedCursor);
        setFavImage();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onFavorited(View view) {
        view.setSelected(!view.isSelected());
        ContentValues favoriteValue = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(mMovieSelectedCursor, favoriteValue);
        Uri favMovieWithId = MovieContract.FavoriteMovie.buildSelectedFavoriteMovieUri(movieId);

        Cursor mCursor = getContentResolver().query(favMovieWithId,
                        null,
                        null,
                        null,
                        null,
                        null
                );
        if (mCursor.getCount() <= 0) {
            getApplicationContext().getContentResolver().
                    insert(MovieContract.FavoriteMovie.CONTENT_URI,
                            favoriteValue);
        } else {
            getApplicationContext().getContentResolver()
                    .delete(favMovieWithId, null, null);
        }
        setFavImage();
    }

    public void onSummaryClicked(View view) {
        Intent summaryIntent = new Intent(this, SummaryFullActivity.class);
        summaryIntent.putExtra("movieSummary", summaryText);
        startActivity(summaryIntent);
    }

    private void loadMovieData(Cursor mCursor) {

            String[] movieData = {
                    selectedMovie.getTrailer(),selectedMovie.getReview_name(),selectedMovie.getReview()};
            mCustomPagerAdapter.setMovieDetails(movieData);
            mMovieSelectedAdapter.setMovieData(
                    selectedMovie.getReview_name().split(MovieConsts.MOVIEDB_DELIMINATOR),
                    selectedMovie.getReview().split(MovieConsts.MOVIEDB_DELIMINATOR));

        }

    @Override
    public void onClick(String reviewAuthor, String reviewContent) {
        Context context = this;
        Intent movieSelectedIntent = new Intent(context, MovieReview.class);
        movieSelectedIntent.putExtra("reviewAuthor", reviewAuthor);
        movieSelectedIntent.putExtra("reviewContent", reviewContent);
        startActivity(movieSelectedIntent);
    }

    private void setFavImage() {
        Uri movieSelectedFavUri = MovieContract.FavoriteMovie.buildSelectedFavoriteMovieUri(movieId);
        Cursor mCursor = getApplicationContext().getContentResolver().
                query(movieSelectedFavUri,
                        null,
                        null,
                        null,
                        null,
                        null
                );
        if (mCursor.getCount() <= 0) {
            mFavorite.setBackgroundResource(R.drawable.ic_favorite);
        } else {
            mFavorite.setBackgroundResource(R.drawable.ic_favorited);
        }
    }
}
