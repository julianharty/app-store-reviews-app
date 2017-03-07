package com.commercetest.reviewreviews;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.commercetest.reviewreviews.DatabaseConstants.*;

/**
 * Code to manage the creation, update and initial population of the database.
 */

public class ReviewsDatabaseHelper extends SQLiteOpenHelper {

    ReviewsDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    public static SQLiteDatabase getDatabase(Context context) {
        SQLiteOpenHelper reviewsDatabaseHelper = new ReviewsDatabaseHelper(context);
        return reviewsDatabaseHelper.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_REVIEWS_TABLE = CreateTableForAndroidReviews();
        db.execSQL(CREATE_REVIEWS_TABLE);

        final String CREATE_FILE_IMPORT_TABLE = CreateTableForFileImports();
        db.execSQL(CREATE_FILE_IMPORT_TABLE);

        final String CREATE_REVIEW_STATUS_TABLE = CreateTableForReviewStatus();
        db.execSQL(CREATE_REVIEW_STATUS_TABLE);

        final String CREATE_REVIEW_HISTORY_TABLE = CreateTableForReviewHistory();
        db.execSQL(CREATE_REVIEW_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Crude, but good enough while the app isn't launched.
        final String DROP_REVIEW_TABLE = "DROP TABLE IF EXISTS " + GOOGLE_PLAY_REVIEW + ";";
        db.execSQL(DROP_REVIEW_TABLE);

        final String DROP_FILE_HISTORY_TABLE = "DROP TABLE IF EXISTS " + FILE_IMPORT + ";";
        db.execSQL(DROP_FILE_HISTORY_TABLE);

        final String DROP_REVIEW_STATUS_TABLE = "DROP TABLE IF EXISTS " + REVIEW_STATUS + ";";
        db.execSQL(DROP_REVIEW_STATUS_TABLE);

        final String DROP_REVIEW_HISTORY_TABLE = "DROP TABLE IF EXISTS " + REVIEW_HISTORY + ";";
        db.execSQL(DROP_REVIEW_HISTORY_TABLE);

        onCreate(db);
    }

    /**
     * Queries the database to count the reviews.
     * @param context
     * @return the count of reviews.
     */
    static long reviewCount(Context context) {
        SQLiteDatabase db = ReviewsDatabaseHelper.getDatabase(context);
        return DatabaseUtils.queryNumEntries(db, GOOGLE_PLAY_REVIEW);
    }

    static long fileImportCount(Context context) {
        SQLiteDatabase db = ReviewsDatabaseHelper.getDatabase(context);
        return DatabaseUtils.queryNumEntries(db, FILE_IMPORT);
    }
    /**
     * Add a review from Google Play App Store to the database
     * @param db open database connection
     * @param javaPackageName the Java package name of the app that was reviewed (a mandatory field)
     * @param appVersionCode the app's version code
     * @param appVersionName the app's name as reported in the review
     * @param reviewerLanguage the language setting of the reviewer's device
     * @param device the device model
     * @param reviewSubmitted when the review was submitted as formatted text (a mandatory field)
     * @param reviewSubmittedTimeInMsecs when the review was submitted in milli-seconds
     * @param reviewLastUpdated when the review was last updated as formatted text
     * @param reviewLastUpdatedInMsecs when the review was last updated in milli-seconds
     * @param starRating the star rating of the review (a mandatory field)
     * @param reviewTitle the title of the review
     * @param reviewText the text of the review in the original language
     * @param developerReply when the developer replied as formatted text
     * @param developerReplyTimeInMsecs when the developer replied in milli-seconds
     * @param developerReplyText what the developer wrote
     * @param reviewURL link to the review online
     */
    public static boolean insertGooglePlayReview(SQLiteDatabase db, String javaPackageName,
                                    String appVersionCode, int appVersionName,
                                    String reviewerLanguage, String device,
                                    String reviewSubmitted, int reviewSubmittedTimeInMsecs,
                                    String reviewLastUpdated, int reviewLastUpdatedInMsecs,
                                    int starRating, String reviewTitle, String reviewText,
                                    String developerReply, int developerReplyTimeInMsecs,
                                    String developerReplyText, String reviewURL) {
        ContentValues reviewValues = new ContentValues();
        reviewValues.put(PACKAGE, javaPackageName);
        reviewValues.put(APP_VERSION_CODE, appVersionCode);
        reviewValues.put(APP_VERSION_NAME, appVersionName);
        reviewValues.put(REVIEWER_LANGUAGE, reviewerLanguage);
        reviewValues.put(DEVICE, device);
        reviewValues.put(REVIEW_SUBMITTED, reviewSubmitted);
        reviewValues.put(REVIEW_SUBMITTED_MILLIS, reviewSubmittedTimeInMsecs);
        reviewValues.put(REVIEW_LAST_UPDATED, reviewLastUpdated);
        reviewValues.put(REVIEW_LAST_UPDATED_MILLIS, reviewLastUpdatedInMsecs);
        reviewValues.put(STAR_RATING, starRating);
        reviewValues.put(REVIEW_TITLE, reviewTitle);
        reviewValues.put(REVIEW_TEXT, reviewText);
        reviewValues.put(DEVELOPER_REPLY, developerReply);
        reviewValues.put(DEVELOPER_REPLY_MILLIS, developerReplyTimeInMsecs);
        reviewValues.put(DEVELOPER_REPLY_TEXT, developerReplyText);
        reviewValues.put(REVIEW_LINK, reviewURL);

        return insertReview(db, reviewValues);
    }

    /**
     * insertGooglePlayReview
     * @param db existing, writable connection to SQLite database
     * @param review to add to the database
     * @return true if the insert returned a row ID, else false
     */
    public static boolean insertGooglePlayReview(SQLiteDatabase db, Review review) {
        ContentValues reviewValues = new ContentValues();
        reviewValues.put(PACKAGE, review.getPackageName());
        reviewValues.put(APP_VERSION_CODE, review.getAppVersionCode());
        reviewValues.put(APP_VERSION_NAME, review.getAppVersionName());
        reviewValues.put(REVIEWER_LANGUAGE, review.getReviewerLanguage());
        reviewValues.put(DEVICE, review.getDevice());
        reviewValues.put(REVIEW_SUBMITTED, review.getReviewSubmitted());
        reviewValues.put(REVIEW_SUBMITTED_MILLIS, review.getReviewSubmittedMillis());
        reviewValues.put(REVIEW_LAST_UPDATED, review.getLastUpdated());
        reviewValues.put(REVIEW_LAST_UPDATED_MILLIS, review.getLastUpdatedMillis());
        reviewValues.put(STAR_RATING, review.getRating());
        reviewValues.put(REVIEW_TITLE, review.getTitle());
        reviewValues.put(REVIEW_TEXT, review.getReviewText());
        reviewValues.put(DEVELOPER_REPLY, review.getDeveloperReplied());
        reviewValues.put(DEVELOPER_REPLY_MILLIS, review.getDeveloperRepliedMillis());
        reviewValues.put(DEVELOPER_REPLY_TEXT, review.getDeveloperReplyText());
        if (review.getReviewLink() != null) {
            reviewValues.put(REVIEW_LINK, review.getReviewLink().toString());
        }

        return insertReview(db, reviewValues);
    }

    private static boolean insertReview(SQLiteDatabase db, ContentValues reviewValues) {
        final long rowId;
        // Wrapped both inserts as a SQL transaction as a precaution.
        db.beginTransaction();
        try {
            rowId = db.insert(GOOGLE_PLAY_REVIEW, null, reviewValues);
            long count = DatabaseUtils.queryNumEntries(db, GOOGLE_PLAY_REVIEW);
            Log.i("Review added", "Row ID: [" + rowId + "] Count now: " + count);
            if (rowId != -1) {
                ContentValues statusValues = new ContentValues();
                statusValues.put(REVIEW_ID, rowId);
                statusValues.put(STATUS, ReviewStatus.INITIAL.toString());
                final long rowIdForStatus = db.insert(REVIEW_STATUS, null, statusValues);
            }
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }
        return (rowId != -1);
    }

    private String CreateTableForAndroidReviews() {
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("CREATE TABLE " + GOOGLE_PLAY_REVIEW + " (");
        sqlStatement.append("_id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sqlStatement.append( PACKAGE + " TEXT NOT NULL, ");
        sqlStatement.append(APP_VERSION_CODE + " INTEGER, ");
        sqlStatement.append(APP_VERSION_NAME + " TEXT, ");
        sqlStatement.append(REVIEWER_LANGUAGE + " TEXT, ");
        sqlStatement.append(DEVICE + " TEXT, ");
        sqlStatement.append(REVIEW_SUBMITTED + " TEXT NOT NULL, ");
        sqlStatement.append(REVIEW_SUBMITTED_MILLIS + " INTEGER, ");
        sqlStatement.append(REVIEW_LAST_UPDATED + " TEXT, ");
        sqlStatement.append(REVIEW_LAST_UPDATED_MILLIS + " INTEGER, ");
        sqlStatement.append(STAR_RATING + " INTEGER NOT NULL, ");
        sqlStatement.append(REVIEW_TITLE + " TEXT, ");
        sqlStatement.append(REVIEW_TEXT + " TEXT, ");
        sqlStatement.append(DEVELOPER_REPLY + " TEXT, ");
        sqlStatement.append(DEVELOPER_REPLY_MILLIS + " INTEGER, ");
        sqlStatement.append(DEVELOPER_REPLY_TEXT + " TEXT, ");
        sqlStatement.append(REVIEW_LINK + " TEXT, ");
        sqlStatement.append("CONSTRAINT unique_review UNIQUE (" + PACKAGE + ", " +
                REVIEW_LAST_UPDATED_MILLIS + ", " + STAR_RATING + ")");
        sqlStatement.append(");");
        return sqlStatement.toString();
    }

    private String CreateTableForFileImports() {
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("CREATE TABLE " + FILE_IMPORT + " (");
        sqlStatement.append("_id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sqlStatement.append(FILE_IDENTIFIER + " TEXT NOT NULL, ");
        sqlStatement.append(FILE_SIZE + " INTEGER NOT NULL, ");
        sqlStatement.append(FILE_TIMESTAMP_MILLIS + " INTEGER NOT NULL, ");
        sqlStatement.append(NUM_ACCEPTED + " INTEGER NOT NULL DEFAULT -1, ");
        sqlStatement.append(NUM_REJECTED + " INTEGER NOT NULL DEFAULT -1, ");
        sqlStatement.append(LOADED_AT + " INTEGER(4) DEFAULT (CAST(strftime('%s','now') AS INT))");
        sqlStatement.append(");");
        return sqlStatement.toString();
    }

    private String CreateTableForReviewStatus() {
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("CREATE TABLE " + REVIEW_STATUS + " (");
        sqlStatement.append("_id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sqlStatement.append(REVIEW_ID + " INTEGER NOT NULL, ");
        sqlStatement.append(STATUS + " TEXT NOT NULL, ");
        sqlStatement.append(STATUS_TIMESTAMP + " INTEGER(4) DEFAULT (CAST(strftime('%s','now') AS INT)), ");
        sqlStatement.append("FOREIGN KEY (" + REVIEW_ID + ") REFERENCES " + GOOGLE_PLAY_REVIEW + "(_id)");
        sqlStatement.append(");");
        return sqlStatement.toString();
    }

    private String CreateTableForReviewHistory() {
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("CREATE TABLE " + REVIEW_HISTORY + " (");
        sqlStatement.append("_id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sqlStatement.append(REVIEW_ID + " INTEGER NOT NULL, ");
        sqlStatement.append(STATUS + " TEXT NOT NULL, ");
        sqlStatement.append(HISTORY_TIMESTAMP + " INTEGER(4) DEFAULT (CAST(strftime('%s','now') AS INT)), ");
        sqlStatement.append("FOREIGN KEY (" + REVIEW_ID + ") REFERENCES " + GOOGLE_PLAY_REVIEW + "(_id)");
        sqlStatement.append(");");
        return sqlStatement.toString();
    }
}
