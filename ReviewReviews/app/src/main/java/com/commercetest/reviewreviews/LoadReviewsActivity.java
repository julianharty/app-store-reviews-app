package com.commercetest.reviewreviews;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.provider.OpenableColumns.DISPLAY_NAME;
import static com.commercetest.reviewreviews.DatabaseConstants.*;

/*
Next steps for this class include:
1) Removing hardcoded strings
2) Creating a structure (an enum?) to collect the meta-data for the selected uri
3) Present this information, formatted suitably, to users
4) Record details of files selected, and the subset that were loaded, together with the results
5) Incorporate mobile analytics into the class
6) Refine the code based on code quality tools
7) Check if the app has already loaded a file and inform the user so they can choose to reload it.
8) Improve the GUI presented to the user, it's scruffy currently.
 */

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
                ContentValues fileInformation = obtainFileMetaData(uri);
                int recordsAdded = 0;
                int recordsRejected = 0;
                String message;
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    final int BYTES_TO_READ = 3;
                    byte[] buffer = new byte[BYTES_TO_READ];
                    inputStream.read(buffer,0,BYTES_TO_READ);
                    String encoding = GuessEncoding.guessFor(buffer);
                    inputStream.close();
                    inputStream = getContentResolver().openInputStream(uri);
                    ReviewReader r = ReviewReader.fromStream(inputStream, encoding);
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
                    inputStream.close();
                    final String msg = "Import completed, added:" + recordsAdded + ", rejected: " + recordsRejected;
                    fileInformation.put(NUM_ACCEPTED, recordsAdded);
                    fileInformation.put(NUM_REJECTED, recordsRejected);
                    recordFileImport(db, fileInformation);
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

    private boolean recordFileImport(SQLiteDatabase db, ContentValues importDetails) {
        final long rowId = db.insert(FILE_IMPORT, null, importDetails);
        return (rowId != -1);
    }

    /**
     * dumpFileMetaData is based on an Android example
     * https://developer.android.com/guide/topics/providers/document-provider.html
     *
     * Perhaps we could use it to provide data on a set of files e.g. CSV files in a folder to
     * help the app and users pick only files with new and changed contents.
     * @param uri
     */
    private ContentValues obtainFileMetaData(Uri uri) {

        // The query, since it only applies to a single document, will only return
        // one row. There's no need to filter, sort, or select fields, since we want
        // all fields for one document.
        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null, null);

        ContentValues fileInformation = new ContentValues();

        try {
            // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (cursor != null && cursor.moveToFirst()) {

                // Note it's called "Display Name".  This is
                // provider-specific, and might not necessarily be the file name.
                String displayName = cursor.getString(
                        cursor.getColumnIndex(DISPLAY_NAME));
                Log.i(TAG, "Display Name: " + displayName);

                // Position 0 is the filename including some sort of location
                // Position 1 is the mime-type
                // Position 2 is the filename
                // Position 3 is the timestamp of the file in mSecs
                // See http://www.freeformatter.com/epoch-timestamp-to-date-converter.html to convert
                // Position 4 seems to be set to "70"
                // Position 5 is the length of the file
                String filename = cursor.getString(2);
                fileInformation.put(FILE_IDENTIFIER, filename);

                String fileTimestamp = cursor.getString(3);
                fileInformation.put(FILE_TIMESTAMP_MILLIS, fileTimestamp);

                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                // If the size is unknown, the value stored is null.  But since an
                // int can't be null in Java, the behavior is implementation-specific,
                // which is just a fancy term for "unpredictable".  So as
                // a rule, check if it's null before assigning to an int.  This will
                // happen often:  The storage API allows for remote files, whose
                // size might not be locally known.
                String size = null;
                long sz = -1;
                if (!cursor.isNull(sizeIndex)) {
                    // Technically the column stores an int, but cursor.getString()
                    // will do the conversion automatically.
                    size = cursor.getString(sizeIndex);
                    sz = cursor.getLong(sizeIndex);
                } else {
                    size = "Unknown";
                    sz = -1;
                }
                fileInformation.put(FILE_SIZE, sz);
                Log.i(TAG, "Size: " + size + ":" + sz);
            }
        } finally {
            cursor.close();
        }
        return fileInformation;
    }

}
