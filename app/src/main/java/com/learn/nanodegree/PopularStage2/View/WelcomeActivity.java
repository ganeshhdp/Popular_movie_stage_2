package com.learn.nanodegree.PopularStage2.View;

import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.learn.nanodegree.PopularStage2.MovieLoadingService;
import com.learn.nanodegree.PopularStage2.R;
import com.learn.nanodegree.PopularStage2.data.MovieAdapter;
import com.learn.nanodegree.PopularStage2.data.MovieConsts;
import com.learn.nanodegree.PopularStage2.data.MovieInfo;
import com.learn.nanodegree.PopularStage2.database.MovieContract;

import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, MovieAdapter.MovieClickListener{

    MovieAdapter mAdapter;
    @BindView(R.id.toolbar)
    Toolbar toolBar;
    @BindView(R.id.rv_posters)
    RecyclerView gridView;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageDisplay;


    String tabselected=MovieConsts.MOVIE_POPULAR;
    private static final int POPULAR_ID =100;
    private static final int TOP_RATED_ID= 101;
    private static final int FAVORITE_ID = 102;
    private static int mTabSelected;

    static final String[] MOVIE_COLUMNS = {
            MovieContract.PopularMovies._ID,
            MovieContract.PopularMovies.COLUMN_MOVIE_ID,
            MovieContract.PopularMovies.COLUMN_OVERVIEW,
            MovieContract.PopularMovies.COLUMN_POSTER_PATH,
            MovieContract.PopularMovies.COLUMN_RELEASE_DATE,
            MovieContract.PopularMovies.COLUMN_BACKDROP_PATH,
            MovieContract.PopularMovies.COLUMN_ORIGINAL_TITLE,
            MovieContract.PopularMovies.COLUMN_VOTE_AVERAGE,
            MovieContract.PopularMovies.COLUMN_TRAILER,
            MovieContract.PopularMovies.COLUMN_REVIEW,
            MovieContract.PopularMovies.COLUMN_REVIEW_NAME
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        setUpTabs();
        showLoading();
        if(savedInstanceState != null ){
            mTabSelected = savedInstanceState.getInt("tabselected");
            getSupportActionBar().setSelectedNavigationItem(mTabSelected);
        }
        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(3000);
        int numOfCols = 2;
        GridLayoutManager layoutManager;
        layoutManager = new GridLayoutManager(this,numOfCols);
        gridView.setLayoutManager(layoutManager);
        gridView.setHasFixedSize(true);
        mAdapter = new MovieAdapter(this);
        gridView.setAdapter(mAdapter);
        Intent serviceIntent = new Intent(this, MovieLoadingService.class);
        serviceIntent.putExtra("sample","sample");
        startService(serviceIntent) ;
        getSupportLoaderManager().restartLoader(POPULAR_ID,null,this);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("tabselected",mTabSelected);
        super.onSaveInstanceState(outState);
    }

    private void setUpTabs() {
        setSupportActionBar(toolBar);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.popular)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.top_rated)));
        tabLayout.addTab(tabLayout.newTab().setText("Favourites"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mTabSelected = tab.getPosition();
                if(tab.getText().equals(getResources().getString(R.string.popular))){
                    tabselected = MovieConsts.MOVIE_POPULAR;
                    restartLoader(POPULAR_ID);
                   // loadMovieData(false);
                }
                if(tab.getText().equals(getResources().getString(R.string.top_rated))){
                   // loadMovieData(true);
                    tabselected = MovieConsts.MOVIE_TOP_RATED;
                    restartLoader(TOP_RATED_ID);
                }
                if(tab.getText().equals("Favourites")){
                   // loadMovieData(false);
                    tabselected = MovieConsts.MOVIE_FAVORITE;
                    restartLoader(FAVORITE_ID);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void restartLoader(int id) {
        if(id == POPULAR_ID){
            getSupportLoaderManager().restartLoader(POPULAR_ID,null,this);
        }
        else if(id==TOP_RATED_ID){
            getSupportLoaderManager().restartLoader(TOP_RATED_ID,null,this);
        }
        else if(id == FAVORITE_ID){
            getSupportLoaderManager().restartLoader(FAVORITE_ID,null,this);
        }
        else {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieSelected(MovieInfo movie) {
        Intent intent=new Intent(this,MovieDetailActivity.class);
        Bundle b = new Bundle();
        b.putParcelable("Movie",movie);
        b.putString("tab",tabselected);
        intent.putExtras(b);
        startActivity(intent);
    }
    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        gridView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        gridView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri mContentUri;
        String sortOrder;
        switch(id){
            case POPULAR_ID:
                mContentUri = MovieContract.PopularMovies.CONTENT_URI;
               // sortOrder = MovieContract.PopularMovies.COLUMN_POPULAR_INDEX + "=DESC";
                return new CursorLoader(this,mContentUri,MOVIE_COLUMNS,null,null,null);
            case TOP_RATED_ID:
                mContentUri = MovieContract.TopratedMovies.CONTENT_URI;
                sortOrder = MovieContract.TopratedMovies.COLUMN_POPULAR_INDEX + "=DESC";
                return  new CursorLoader(this,mContentUri,MOVIE_COLUMNS,null,null,null);
            case FAVORITE_ID:
                mContentUri = MovieContract.FavoriteMovie.CONTENT_URI;
                sortOrder = MovieContract.CommonMovieCol.COLUMN_POPULAR_INDEX + "=DESC";
                return new CursorLoader(this,mContentUri,MOVIE_COLUMNS,null,null,null);
        }
        return  null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.getCount() > 0){
            showMovieDataView();
            mAdapter.swapCursor(data);
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
