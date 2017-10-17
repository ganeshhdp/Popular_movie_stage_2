package com.learn.nanodegree.PopularStage2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MovieSelectedReviewAdapter extends RecyclerView.Adapter<MovieSelectedReviewAdapter.MovieReviewViewHolder> {
    private String[] mReviewAuthor;
    private String[] mReviewContent;
    private Context mContext;
    final MovieSelectedReviewOnClickHandler mClickHandler;

    public interface MovieSelectedReviewOnClickHandler{
        void onClick(String reviewAuthor, String reviewContent);
    }

    public MovieSelectedReviewAdapter(Context context, MovieSelectedReviewOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    class MovieReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView mReviewAuthorView;
        private final TextView mReviewContentView;
        public MovieReviewViewHolder(View itemView)  {
            super(itemView);
            mReviewAuthorView = (TextView) itemView.findViewById(R.id.review_author);
            mReviewContentView = (TextView) itemView.findViewById(R.id.review_content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();

            String reviewAuthor = mReviewAuthor[adapterPosition];
            String reviewContent = mReviewContent[adapterPosition];
            mClickHandler.onClick(reviewAuthor,reviewContent);
        }
    }
    @Override
    public MovieSelectedReviewAdapter.MovieReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieReviewViewHolder holder, int position) {
        holder.mReviewAuthorView.setText(mReviewAuthor[position]);
        holder.mReviewContentView.setText(mReviewContent[position]);
    }

    @Override
    public int getItemCount() {
        if (null == mReviewAuthor) {
            return 0;
        }
        return mReviewAuthor.length;
    }


    public void setMovieData(String[] reviewAuthor, String[] reviewContent){
        mReviewAuthor = reviewAuthor;
        mReviewContent = reviewContent;
        notifyDataSetChanged();
    }
}
