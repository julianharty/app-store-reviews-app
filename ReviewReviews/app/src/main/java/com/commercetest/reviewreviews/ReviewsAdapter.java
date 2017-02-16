package com.commercetest.reviewreviews;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

/**
 * Adapter for the RecyclerView used to display reviews from the Google Play Store.
 *
 * Still very much a work-in-progress.
 * Created by julianharty_air on 07/12/2016.
 */

class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView reviewTextView;
        Button undoButton;

        ViewHolder(View itemView) {
            super(itemView);

            reviewTextView = (TextView) itemView.findViewById(R.id.review_text);
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
        int rating = mCursor.getInt(mCursor.getColumnIndexOrThrow("star_rating"));

        if (reviewTitle == null || reviewTitle.length() == 0) {
            reviewTitle = "(none) rating = " + rating;
        }

        // Update the row with the details from this review
        TextView textView = holder.reviewTextView;
        textView.setText(reviewTitle);
        Button button = holder.undoButton;
        button.setText("UnDo"); // Needs translating, etc.
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
}
