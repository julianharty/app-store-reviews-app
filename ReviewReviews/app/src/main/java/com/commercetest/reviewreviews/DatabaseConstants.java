package com.commercetest.reviewreviews;

/**
 * A Helper class so I can keep the database, table and column names together.
 *
 * Created by julianharty_air on 20/02/2017.
 */

final class DatabaseConstants {
    static final String DB_NAME = "reviews";

    static final String GOOGLE_PLAY_REVIEW = "GOOGLE_PLAY_REVIEW";
    static final String PACKAGE = "package";
    static final String APP_VERSION_CODE = "app_version_code";
    static final String APP_VERSION_NAME = "app_version_name";
    static final String REVIEWER_LANGUAGE = "reviewer_language";
    static final String DEVICE = "device";
    static final String REVIEW_SUBMITTED = "review_submitted";
    static final String REVIEW_SUBMITTED_MILLIS = "review_submitted_millis";
    static final String REVIEW_LAST_UPDATED = "review_last_updated";
    static final String REVIEW_LAST_UPDATED_MILLIS = "review_last_updated_millis";
    static final String STAR_RATING = "star_rating";
    static final String REVIEW_TITLE = "review_title";
    static final String REVIEW_TEXT = "review_text";
    static final String DEVELOPER_REPLY = "developer_reply";
    static final String DEVELOPER_REPLY_MILLIS = "developer_reply_millis";
    static final String DEVELOPER_REPLY_TEXT = "developer_reply_text";
    static final String REVIEW_LINK = "review_link";

    static final String FILE_IMPORT = "FILE_IMPORT";
    static final String FILE_IDENTIFIER = "file_identifier";
    static final String FILE_SIZE = "file_size";
    static final String FILE_TIMESTAMP_MILLIS = "timestamp";
    static final String NUM_ACCEPTED = "number_accepted";
    static final String NUM_REJECTED = "number_rejected";
    static final String LOADED_AT = "loaded_at";

    static final String REVIEW_STATUS = "REVIEW_STATUS";
    static final String REVIEW_ID = "review_id";    // Also used in REVIEW_HISTORY
    static final String STATUS = "status";          // Also used in REVIEW_HISTORY
    static final String STATUS_TIMESTAMP = "status_timestamp";

    static final String REVIEW_HISTORY = "REVIEW_HISTORY";
    static final String HISTORY_TIMESTAMP = "history_timestamp";

    //I've placed DB_VERSION here so I can report the value easily in the app
    static final int DB_VERSION = 4;
}
