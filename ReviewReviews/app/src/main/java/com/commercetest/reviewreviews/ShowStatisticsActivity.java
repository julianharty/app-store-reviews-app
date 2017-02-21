package com.commercetest.reviewreviews;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static com.commercetest.reviewreviews.DatabaseConstants.FILE_IMPORT;
import static com.commercetest.reviewreviews.DatabaseConstants.GOOGLE_PLAY_REVIEW;
import static com.commercetest.reviewreviews.DatabaseConstants.NUM_ACCEPTED;
import static com.commercetest.reviewreviews.DatabaseConstants.NUM_REJECTED;

/**
 * Shows statistics to the user based on what's recorded in the local database.
 *
 * Please add additional statistics here as we increase the functionality.
 */
public class ShowStatisticsActivity extends AppCompatActivity {
    SQLiteDatabase db;
    TextView totalReviews;
    TextView totalFilesImported;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_statistics);

        totalReviews = (TextView) findViewById(R.id.total_reviews_in_db);
        long countOfReviews = ReviewsDatabaseHelper.reviewCount(this);
        totalReviews.setText(String.format("%,d", countOfReviews));

        totalFilesImported = (TextView) findViewById(R.id.total_files_imported);
        long imports = ReviewsDatabaseHelper.fileImportCount(this);

        // TODO 20170221 (jharty) consider moving this perhaps misplaced code.
        String[] projection = {
                "SUM(" + NUM_ACCEPTED + ") AS ACCEPTED_COUNT",
                "SUM(" + NUM_REJECTED + ") AS REJECTED_COUNT"
        };
        db = ReviewsDatabaseHelper.getDatabase(this);
        Cursor statistics = db.query(DatabaseConstants.FILE_IMPORT,
                projection,
                null,
                null,
                null,
                null,
                null,
                null);
        statistics.moveToFirst();
        long accepted = statistics.getInt(statistics.getColumnIndexOrThrow("ACCEPTED_COUNT"));
        long rejected = statistics.getInt(statistics.getColumnIndexOrThrow("REJECTED_COUNT"));
        totalFilesImported.setText(
                String.format("files %,d, accepted %,d, rejected %,d",
                    imports, accepted, rejected));
    }
}
