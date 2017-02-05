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
    private static final int FIND_FILE_REQUEST_CODE = 8888;
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
            SQLiteDatabase db = getDatabase();
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
            findReviewsToLoad();
        }

        if (item.getItemId() == R.id.settings) {
            Log.i("Settings", "Settings requested");
        }
        return super.onOptionsItemSelected(item);
    }

    private SQLiteDatabase getDatabase() {
        SQLiteOpenHelper reviewsDatabaseHelper = new ReviewsDatabaseHelper(this);
        return reviewsDatabaseHelper.getWritableDatabase();
    }

    public void findReviewsToLoad() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, FIND_FILE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        if (requestCode == FIND_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    ReviewReader r = ReviewReader.fromStream(inputStream);
                    SQLiteDatabase db = getDatabase();
                    Review review = null;
                    while ((review = r.next()) != null) {
                        ReviewsDatabaseHelper.insertGooglePlayReview(db, review);
                    }
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "Problem accessing file of reviews", e);
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e(TAG, "Problem accessing file of reviews", e);
                    e.printStackTrace();
                }
            }
        }
    }
}
