package com.commercetest.reviewreviews;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class TriageActivity extends AppCompatActivity {
    RecyclerView rvReviews;
    private final String TAG = "TriageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triage);
        rvReviews = (RecyclerView) findViewById(R.id.reviews);

        Cursor cursor = readFromDB();
        ReviewsAdapter adapter = new ReviewsAdapter(cursor);

        rvReviews.setAdapter(adapter);
        // TODO 20170216 (jharty) see if we can define the layout mgr in XML as Google recommends
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
    }

    private Cursor readFromDB() {
        SQLiteDatabase db = ReviewsDatabaseHelper.getDatabase(this);

        String[] projection = {
                "_id",
                "star_rating",
                "review_submitted",
                "review_title",
                "review_text"
        };

        Cursor cursor = db.query(ReviewsDatabaseHelper.GOOGLE_PLAY_REVIEW,
                projection,
                null,
                null,
                null,
                null,
                null,
                "1000");
        Log.d(TAG, "The cursor count is " + cursor.getCount());
        return cursor;
    }
}
