package com.commercetest.reviewreviews;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class TriageActivity extends AppCompatActivity {

    /**
     * The {@link Tracker} used to record screen views.
     */
    private Tracker mTracker;

    Cursor cursor;
    ReviewsAdapter adapter;
    private RecyclerView rvReviews;
    private ItemTouchHelper itemTouchHelper;
    private final String TAG = "TriageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triage);


        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        // [START screen_view_hit]
        Log.i(TAG, "Setting screen name: " + TAG);
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        // [END screen_view_hit]

        rvReviews = (RecyclerView) findViewById(R.id.reviews);

        itemTouchHelper = new ItemTouchHelper(simpleCallbackTouchHelper);

        cursor = readFromDB();
        adapter = new ReviewsAdapter(cursor);

        rvReviews.setAdapter(adapter);
        // TODO 20170216 (jharty) see if we can define the layout mgr in XML as Google recommends
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        itemTouchHelper.attachToRecyclerView(rvReviews);
    }

    ItemTouchHelper.SimpleCallback simpleCallbackTouchHelper =
            new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT) {
              @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                  int position = viewHolder.getAdapterPosition();
                  Log.d(TAG, "I should be removing position: " + position);
                  // We can't remove an item from a cursor :( cursor.moveToPosition(position);
                  adapter.notifyItemChanged(position);
              }

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    // We don't want to move any entries
                    return false;
                }
            };

    private Cursor readFromDB() {
        SQLiteDatabase db = ReviewsDatabaseHelper.getDatabase(this);

        String[] projection = {
                "_id",
                "star_rating",
                "review_submitted",
                "review_title",
                "review_text"
        };

        Cursor cursor = db.query(DatabaseConstants.GOOGLE_PLAY_REVIEW,
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
