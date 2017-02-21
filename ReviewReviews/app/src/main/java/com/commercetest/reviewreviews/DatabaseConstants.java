package com.commercetest.reviewreviews;

/**
 * A Helper class so I can keep the database, table and column names together.
 *
 * Created by julianharty_air on 20/02/2017.
 */

public final class DatabaseConstants {
    public static final String DB_NAME = "reviews";

    public static final String GOOGLE_PLAY_REVIEW = "GOOGLE_PLAY_REVIEW";
    public static final String PACKAGE = "package";
    public static final String APP_VERSION_CODE = "app_version_code";
    public static final String APP_VERSION_NAME = "app_version_name";
    public static final String REVIEWER_LANGUAGE = "reviewer_language";
    public static final String DEVICE = "device";
    public static final String REVIEW_SUBMITTED = "review_submitted";
    public static final String REVIEW_SUBMITTED_MILLIS = "review_submitted_millis";
    public static final String REVIEW_LAST_UPDATED = "review_last_updated";
    public static final String REVIEW_LAST_UPDATED_MILLIS = "review_last_updated_millis";
    public static final String STAR_RATING = "star_rating";
    public static final String REVIEW_TITLE = "review_title";
    public static final String REVIEW_TEXT = "review_text";
    public static final String DEVELOPER_REPLY = "developer_reply";
    public static final String DEVELOPER_REPLY_MILLIS = "developer_reply_millis";
    public static final String DEVELOPER_REPLY_TEXT = "developer_reply_text";
    public static final String REVIEW_LINK = "review_link";

    public static final String FILE_IMPORT = "FILE_IMPORT";
    public static final String FILE_IDENTIFIER = "file_identifier";
    public static final String FILE_SIZE = "file_size";
    public static final String FILE_TIMESTAMP_MILLIS = "timestamp";
    public static final String NUM_ACCEPTED = "number_accepted";
    public static final String NUM_REJECTED = "number_rejected";
    public static final String LOADED_AT = "loaded_at";

    //I've placed DB_VERSION here so I can report the value easily in the app
    public static final int DB_VERSION = 2;
}
