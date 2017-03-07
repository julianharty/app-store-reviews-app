package com.commercetest.reviewreviews;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class TriageActivity extends AppCompatActivity {

    /**
     * The {@link Tracker} used to record screen views.
     */
    private Tracker mTracker;

    SQLiteDatabase db;
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

    @Override
    protected void onPause() {
        if (cursor != null) {
            cursor.close();
        }

        if (db != null) {
            db.close();
        }
        super.onPause();
    }

    ItemTouchHelper.SimpleCallback simpleCallbackTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT ) {
        Drawable background;

              @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                  int position = viewHolder.getAdapterPosition();
                  Log.d(TAG, "I should be removing position: " + position);
                  // We can't remove an item from a cursor :( cursor.moveToPosition(position);
//                  adapter.notifyItemChanged(position);
                  adapter.notifyItemRemoved(position);
              }

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    // We don't want to move any entries
                    return false;
                }

                @Override
                public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                        float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    View itemView = viewHolder.itemView;

                    if (viewHolder.getAdapterPosition() == -1) {
                        return;
                    }
                    background = new ColorDrawable(Color.RED);
                    int itemHeight = itemView.getBottom() - itemView.getTop();
                    background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                    background.draw(c);
                    // Tried the following to show text on the RED background, instead the text is part of the swiped row.
//                    Button undoButton = (Button) viewHolder.itemView.findViewById(R.id.undo_button);
//                    undoButton.setVisibility(View.VISIBLE);

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            };

    private Cursor readFromDB() {
        db = ReviewsDatabaseHelper.getDatabase(this);

        String[] projection = {
                "_id",
                "star_rating",
                "review_submitted",
                "review_title",
                "review_text",
                "device"
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
