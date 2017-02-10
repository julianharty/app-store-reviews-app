package com.commercetest.reviewreviews;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class LoadReviewsActivity extends AppCompatActivity {
    private static final String TAG = "LoadReviews";
    private static final int FIND_FILE_REQUEST_CODE = 8888;
    private Button findFileToLoadButton;
    TextView messageBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_reviews);
        messageBox = (TextView) findViewById(R.id.results_of_file_load);
        findFileToLoadButton = (Button) findViewById(R.id.findFileToLoad);
        findFileToLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findReviewsToLoad();
            }
        });
    }

    public void findReviewsToLoad() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/comma-separated-values");
        startActivityForResult(intent, FIND_FILE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        if (requestCode == FIND_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                int recordsAdded = 0;
                int recordsRejected = 0;
                String message;
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    ReviewReader r = ReviewReader.fromStream(inputStream);
                    SQLiteDatabase db = ReviewsDatabaseHelper.getDatabase(this);
                    Review review = null;
                    while ((review = r.next()) != null) {
                        boolean OK = ReviewsDatabaseHelper.insertGooglePlayReview(db, review);
                        if (OK) {
                            recordsAdded++;
                        } else {
                            recordsRejected++;
                        }
                    }
                    final String msg = "Import completed, added:" + recordsAdded + ", rejected: " + recordsRejected;
                    messageBox.setText(msg);
                    Log.i(TAG, msg);
                    findFileToLoadButton.setText(R.string.loadAnotherFileText);
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "Problem accessing file of reviews", e);
                    e.printStackTrace();
                    message = e.getLocalizedMessage();
                    messageBox.setText(message);
                } catch (IOException e) {
                    Log.e(TAG, "Problem accessing file of reviews", e);
                    e.printStackTrace();
                    message = e.getLocalizedMessage();
                    messageBox.setText(message);
                } catch (UnexpectedFormatException ufe) {
                    Log.w(TAG, "Unexpected CSV file detected, possibly incorrect content", ufe);
                    message = ufe.getLocalizedMessage();
                    messageBox.setText(message);
                }
            }
        }
    }
}
