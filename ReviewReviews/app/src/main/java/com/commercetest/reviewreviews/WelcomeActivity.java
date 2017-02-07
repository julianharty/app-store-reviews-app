package com.commercetest.reviewreviews;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;

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
        if (item.getItemId() == R.id.add_review) {
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

        if (item.getItemId() == R.id.load_reviews) {
            Log.i("LoadReviews", "Load Reviews requested");
            Intent intent = new Intent(this, LoadReviewsActivity.class);
            startActivity(intent);
            // findReviewsToLoad();
        }

        if (item.getItemId() == R.id.settings) {
            Log.i("Settings", "Settings requested");
        }
        return super.onOptionsItemSelected(item);
    }

}
