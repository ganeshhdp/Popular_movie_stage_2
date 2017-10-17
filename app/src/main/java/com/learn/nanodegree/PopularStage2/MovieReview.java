package com.learn.nanodegree.PopularStage2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieReview extends AppCompatActivity {
    @BindView(R.id.review_content)
    TextView mReviewContentView;
    @BindView(R.id.review_author)
    TextView mReviewAuthorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_review);
        ButterKnife.bind(this);
        Intent callingIntent = getIntent();
        mReviewAuthorView.setText(callingIntent.getStringExtra("reviewAuthor"));
        mReviewContentView.setText(callingIntent.getStringExtra("reviewContent"));
    }
}
