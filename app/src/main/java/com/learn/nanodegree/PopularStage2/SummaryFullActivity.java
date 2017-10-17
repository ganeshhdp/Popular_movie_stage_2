package com.learn.nanodegree.PopularStage2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SummaryFullActivity extends AppCompatActivity {
    @BindView(R.id.full_summary_text) TextView mSummaryText;
    String summaryText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_full);
        ButterKnife.bind(this);
        summaryText = this.getIntent().getExtras().getString("movieSummary");
        mSummaryText.setText(summaryText);

    }
}
