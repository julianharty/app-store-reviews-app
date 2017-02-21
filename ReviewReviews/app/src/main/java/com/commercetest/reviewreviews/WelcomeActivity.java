package com.commercetest.reviewreviews;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/*
Next steps for this class include:
1) Move less frequent options e.g. for testing and administration off the main menu.
2) Provide users greater control over deleting all data
3) Revise the Welcome screen to be more welcoming :)
 */

public class WelcomeActivity extends AppCompatActivity {
    private static final String TAG = "WelcomeActivity";
    private long numberOfReviews;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        numberOfReviews = ReviewsDatabaseHelper.reviewCount(this);
        TextView noReviewsMessage = (TextView) findViewById(R.id.no_reviews_yet);
        if (numberOfReviews == 0) {
            noReviewsMessage.setVisibility(View.VISIBLE);
        } else {
            noReviewsMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);
        updateMenuOptions();
        return true;
    }

    private void updateMenuOptions() {
        // Precautionary call, perhaps could be optimised away - TBD.
        numberOfReviews = ReviewsDatabaseHelper.reviewCount(this);

        MenuItem triageMenu = menu.findItem(R.id.triage);
        MenuItem deleteMenu = menu.findItem(R.id.delete_all_reviews);
        if (numberOfReviews == 0) {
            triageMenu.setEnabled(false);
            deleteMenu.setEnabled(false);
        } else {
            triageMenu.setEnabled(true);
            deleteMenu.setEnabled(true);
        }
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
            int reviewsDeleted = db.delete(DatabaseConstants.GOOGLE_PLAY_REVIEW, "1", null);
            Log.i(TAG, reviewsDeleted + " records were deleted OK.");
            int importsDeleted = db.delete(DatabaseConstants.FILE_IMPORT, "1", null);
            Log.i(TAG, importsDeleted + " details of file imports were deleted OK.");
            int statusRecordsDeleted = db.delete(DatabaseConstants.REVIEW_STATUS, "1", null);
            Log.i(TAG, statusRecordsDeleted + " status records deleted OK.");
            int statusHistoryRecordsDeleted = db.delete(DatabaseConstants.REVIEW_HISTORY, "1", null);
            Log.i(TAG, statusHistoryRecordsDeleted + " status history records were deleted OK");
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
        // Update the menu as necessary.
        updateMenuOptions();
        return super.onOptionsItemSelected(item);
    }

}
