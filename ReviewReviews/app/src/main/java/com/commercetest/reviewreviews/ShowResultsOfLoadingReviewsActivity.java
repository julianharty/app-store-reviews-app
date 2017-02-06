package com.commercetest.reviewreviews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ShowResultsOfLoadingReviewsActivity extends AppCompatActivity {
    public static final String message = "message";
    private static final String TAG = "ShowResults";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_of_loading_reviews_review);
        Intent intent = getIntent();
        String result = intent.getStringExtra(message);
        Log.d(TAG, result);
        TextView message = (TextView) findViewById(R.id.results_of_file_load);
        message.setText(result);
    }
}
