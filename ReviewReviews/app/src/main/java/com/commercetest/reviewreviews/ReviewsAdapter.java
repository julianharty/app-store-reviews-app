package com.commercetest.reviewreviews;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import org.w3c.dom.Text;

/**
 * Adapter for the RecyclerView used to display reviews from the Google Play Store.
 *
 * Still very much a work-in-progress.
 * Created by julianharty_air on 07/12/2016.
 */

class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder>
    implements View.OnClickListener {

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView reviewRatingView;
        TextView reviewTextView;
        TextView reviewTitleView;
        Button undoButton;

        ViewHolder(View itemView) {
            super(itemView);

            reviewRatingView = (TextView) itemView.findViewById(R.id.review_rating);
            reviewTextView = (TextView) itemView.findViewById(R.id.review_text);
            reviewTitleView = (TextView) itemView.findViewById(R.id.review_title);
            undoButton = (Button) itemView.findViewById(R.id.undo_button);
        }
    }

    private Cursor mCursor;

    ReviewsAdapter(Cursor cursor) {
        mCursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout for an individual App Store review
        View reviewView = inflater.inflate(R.layout.review_row, parent, false);
        return new ViewHolder(reviewView);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ViewHolder holder, int position) {
        // Get the data model based on the position
        mCursor.moveToPosition(position);
        String reviewTitle = mCursor.getString(mCursor.getColumnIndexOrThrow("review_title"));
        String reviewText = mCursor.getString(mCursor.getColumnIndexOrThrow("review_text"));
        int rating = mCursor.getInt(mCursor.getColumnIndexOrThrow("star_rating"));
        int id = mCursor.getInt(mCursor.getColumnIndexOrThrow("_id"));

        // Update the row with the details from this review
        TextView tempView = holder.reviewRatingView;
        tempView.setText(Integer.toString(rating));

        tempView = holder.reviewTitleView;

        if (reviewTitle == null || reviewTitle.length() == 0) {
            tempView.setText(R.string.none);
        } else {
            tempView.setText(reviewTitle);
        }

        tempView = holder.reviewTextView;
        if (reviewText == null || reviewText.length() == 0) {
            tempView.setText(R.string.none);
        } else {
            tempView.setText(reviewText);
        }
        
        // Hide the undo button for now, once we've located it.
        Button button = holder.undoButton;
        button.setVisibility(View.INVISIBLE);
        button.setText(R.string.undo);
    }

    @Override
    public void onClick(View view) {
        // Nothing to do here.
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
}
