package com.commercetest.reviewreviews;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.sql.Timestamp;

public class WelcomeActivity extends AppCompatActivity {

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
            SQLiteOpenHelper reviewsDatabaseHelper = new ReviewsDatabaseHelper(this);
            SQLiteDatabase db = reviewsDatabaseHelper.getWritableDatabase();
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
        if (item.getItemId() == R.id.settings) {
            Log.i("Settings", "Settings requested");
        }
        return super.onOptionsItemSelected(item);
    }
}
