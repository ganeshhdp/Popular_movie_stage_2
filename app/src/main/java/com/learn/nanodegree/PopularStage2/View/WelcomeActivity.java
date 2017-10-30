package com.learn.nanodegree.PopularStage2.View;

import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
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

public class WelcomeActivity extends AppCompatActivity implements ActionBar.TabListener,LoaderManager.LoaderCallbacks<Cursor>, MovieAdapter.MovieClickListener{

    MovieAdapter mAdapter;
    @BindView(R.id.rv_posters)
    RecyclerView gridView;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageDisplay;
    @BindView(R.id.no_fav)
            TextView noFav;


    String tabselected=MovieConsts.MOVIE_POPULAR;
    static public boolean onResumeCalled;
    private static final int POPULAR_ID =0;
    private static final int TOP_RATED_ID= 1;
    private static final int FAVORITE_ID = 2;
    private static int mTabSelected=0;
    private int mPosition = RecyclerView.NO_POSITION;

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

    private int[] recyclerItemPosition = new int[3];
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        setUpTabs();
        showLoading();
        if(savedInstanceState != null ){
            mTabSelected = savedInstanceState.getInt("mTabselected");

            recyclerItemPosition=savedInstanceState.getIntArray("scrollPosition");
            tabselected = savedInstanceState.getString("tabSelected");
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
    }

    @Override
    protected void onResume() {
        getSupportActionBar().setSelectedNavigationItem(mTabSelected);
        onResumeCalled = true;
        restartLoader(mTabSelected);
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("mTabselected",mTabSelected);
        recyclerItemPosition[mTabSelected]=((GridLayoutManager)gridView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        outState.putIntArray("scrollPosition",recyclerItemPosition);
        outState.putString("tabSelected",tabselected);
        super.onSaveInstanceState(outState);
    }

    private void setUpTabs() {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.popular)).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.top_rated)).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Favourites").setTabListener(this));
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
        noFav.setVisibility(View.INVISIBLE);
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
        }else{
            if(data!=null) {
                if (tabselected.equals(MovieConsts.MOVIE_FAVORITE) ) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    gridView.setVisibility(View.INVISIBLE);
                    noFav.setVisibility(View.VISIBLE);
                } else {
                    showLoading();
                }
            }
        }
        mPosition = recyclerItemPosition[mTabSelected];
        if (mPosition == RecyclerView.NO_POSITION) {
            mPosition = 0;
            recyclerItemPosition[mTabSelected] = 0;
        }
        gridView.smoothScrollToPosition(recyclerItemPosition[mTabSelected]);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mTabSelected = tab.getPosition();
        if(onResumeCalled) {
            if (tab.getText().equals(getResources().getString(R.string.popular))) {
                tabselected = MovieConsts.MOVIE_POPULAR;
                restartLoader(POPULAR_ID);
                // loadMovieData(false);
            }
            if (tab.getText().equals(getResources().getString(R.string.top_rated))) {
                // loadMovieData(true);
                tabselected = MovieConsts.MOVIE_TOP_RATED;
                restartLoader(TOP_RATED_ID);
            }
            if (tab.getText().equals("Favourites")) {
                // loadMovieData(false);
                tabselected = MovieConsts.MOVIE_FAVORITE;
                restartLoader(FAVORITE_ID);
            }
        }
    }

    @Override
    protected void onPause() {
        onResumeCalled=false;
        recyclerItemPosition[mTabSelected]=((GridLayoutManager) gridView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        if(((GridLayoutManager) gridView.getLayoutManager())!=null) {
            recyclerItemPosition[tab.getPosition()] = ((GridLayoutManager) gridView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
