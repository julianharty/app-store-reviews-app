package com.commercetest.reviewreviews;

import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static com.commercetest.reviewreviews.ReviewsDatabaseHelper.GOOGLE_PLAY_REVIEW;

public class ShowStatisticsActivity extends AppCompatActivity {
    SQLiteDatabase db;
    TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_statistics);

        db = ReviewsDatabaseHelper.getDatabase(this);
        total = (TextView) findViewById(R.id.total_reviews_in_db);
        long count = DatabaseUtils.queryNumEntries(db, GOOGLE_PLAY_REVIEW);
        total.setText(String.format("%,d", count));
    }
}
