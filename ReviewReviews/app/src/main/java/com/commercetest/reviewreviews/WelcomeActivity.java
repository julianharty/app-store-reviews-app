package com.commercetest.reviewreviews;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/*
Next steps for this class include:
1) Move less frequent options e.g. for testing and administration off the main menu.
2) Provide users greater control over deleting all data
3) Revise the Welcome screen to be more welcoming :)
 */

public class WelcomeActivity extends AppCompatActivity {
    private static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selection = item.getItemId();
        if (selection == R.id.add_review) {
            Log.i("AddReview", "AddReview requested");
            SQLiteDatabase db = ReviewsDatabaseHelper.getDatabase(this);
            int now = (int) System.currentTimeMillis();
            ReviewsDatabaseHelper.insertGooglePlayReview(
                    db, "org.julianharty.revieweviews",
                    "V0.1", 0,
                    "en-GB", "Nexus-0",
                    "2016-12-05Z21:46:00", now,
                    "(null)",0, 4,
                    "My first review", "A great start for this new app - keep going!",
                    "", 0, "","");
        }

        if (selection == R.id.delete_all_reviews) {
            Log.i("DeleteReviews", "Delete All Reviews selected");
            // For now I'll simply delete them. in future we'll ask the user to confirm, etc.
            SQLiteDatabase db = ReviewsDatabaseHelper.getDatabase(this);
            int recordsDeleted = db.delete(ReviewsDatabaseHelper.GOOGLE_PLAY_REVIEW, "1", null);
            Log.i(TAG, recordsDeleted + " records were deleted successfully.");
        }

        if (selection == R.id.load_reviews) {
            Log.i("LoadReviews", "Load Reviews requested");
            Intent intent = new Intent(this, LoadReviewsActivity.class);
            startActivity(intent);
        }

        if (selection == R.id.triage) {
            Log.i("Triage", "I've been chosen! time to serve my masters");
            Intent intent = new Intent(this, TriageActivity.class);
            startActivity(intent);
        }

        if (selection == R.id.show_statistics) {
            Log.i("ShowStatistics", "Show Statistics requested");
            Intent intent = new Intent(this, ShowStatisticsActivity.class);
            startActivity(intent);
        }

        if (selection == R.id.settings) {
            Log.i("Settings", "Settings requested");
        }
        return super.onOptionsItemSelected(item);
    }

}
