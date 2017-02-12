package com.commercetest.reviewreviews;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Wrap up reading a csv file or stream.
 *
 * @author damienkallison@gmail.com
 */
public class ReviewReader {

    public static final String
            PACKAGE_NAME = "Package Name",
            APP_VERSION_CODE = "App Version Code",
            APP_VERSION_NAME = "App Version Name",
            REVIEWER_LANGUAGE = "Reviewer Language",
            DEVICE = "Device",
            REVIEW_DATE_TIME = "Review Submit Date and Time",
            REVIEW_MILLIS = "Review Submit Millis Since Epoch",
            REVIEW_LAST_UPDATE_DATETIME = "Review Last Update Date and Time",
            REVIEW_LAST_UPDATE_MILLIS = "Review Last Update Millis Since Epoch",
            STAR_RATING = "Star Rating",
            REVIEW_TITLE = "Review Title",
            REVIEW_TEXT = "Review Text",
            DEVELOPER_REPLY_DATETIME = "Developer Reply Date and Time",
            DEVELOPER_REPLY_MILLIS = "Developer Reply Millis Since Epoch",
            DEVELOPER_REPLY = "Developer Reply Text",
            REVIEW_LINK = "Review Link";

    private final BufferedReader reader;
    private final Map<String, Integer> fields = new HashMap<String, Integer>();

    private ReviewReader(BufferedReader reader) {
        this.reader = reader;
    }

    public static ReviewReader fromFilename(String filename)
            throws IOException, UnexpectedFormatException {
        return fromStream(new FileInputStream(filename), "UTF8");
    }

    public static ReviewReader fromStream(InputStream stream, String encoding) throws IOException, UnexpectedFormatException {
        return new ReviewReader(new BufferedReader(new InputStreamReader(new BufferedInputStream(stream), encoding))).index();
    }

    static ReviewReader fromStringReaderForTesting(StringReader contents) {
        return new ReviewReader(new BufferedReader(contents));
    }

    public Review next() throws IOException {
        return fromRow(nextRow());
    }

    private Review fromRow(String[] row) {
        if (null == row) {
            return null;
        }
        // TODO Map optional fields from the CSV header
        final long reviewMillis = Long.parseLong(row[fields.get(REVIEW_MILLIS)]);
        Review.Builder temp = new Review.Builder(
                row[fields.get(PACKAGE_NAME)],
                row[fields.get(REVIEW_DATE_TIME)],
                reviewMillis,
                Integer.parseInt(row[fields.get(STAR_RATING)]));

        // TODO 20170212 (jharty) This looks like an excellent place to try lambdas
        String tmp = row[fields.get(APP_VERSION_CODE)];
        if (tmp.length() > 0) { temp.appVersionCode(Integer.parseInt(tmp)); }

        tmp = row[fields.get(APP_VERSION_NAME)];
        if (tmp.length() > 0) { temp.appVersionName(tmp); }

        tmp = row[fields.get(REVIEWER_LANGUAGE)];
        if (tmp.length() > 0) { temp.reviewerLanguage(tmp); }

        tmp = row[fields.get(DEVICE)];
        if (tmp.length() > 0) { temp.device(tmp); }

        tmp = row[fields.get(REVIEW_LAST_UPDATE_DATETIME)];
        if (tmp.length() > 0) { temp.lastUpdated(tmp); }

        tmp = row[fields.get(REVIEW_LAST_UPDATE_MILLIS)];
        if (tmp.length() > 0) { temp.lastUpdatedMillis(Long.parseLong(tmp)); }

        tmp = row[fields.get(REVIEW_TITLE)];
        if (tmp.length() > 0) { temp.title(tmp); }

        tmp = row[fields.get(REVIEW_TEXT)];
        if (tmp.length() > 0) { temp.reviewText(tmp); }

        tmp = row[fields.get(DEVELOPER_REPLY_DATETIME)];
        if (tmp.length() > 0) { temp.developerReplied(tmp); }

        tmp = row[fields.get(DEVELOPER_REPLY_MILLIS)];
        if (tmp.length() > 0) { temp.developerRepliedMillis(Long.parseLong(tmp)); }

        tmp = row[fields.get(DEVELOPER_REPLY)];
        if (tmp.length() > 0) { temp.developerReplyText(tmp); }

        tmp = row[fields.get(REVIEW_LINK)];
        if (tmp.length() > 0) {
            try {
                URL url = new URL(tmp);
                temp.reviewLink(url);
            } catch (MalformedURLException e) {
                Log.w("WONKY URL rejected", tmp, e);
            }
        }

        return temp.build();
    }

    ReviewReader index() throws IOException, UnexpectedFormatException {
        String[] items = nextRow();
        findOrDie(PACKAGE_NAME, items);
        findOrDie(APP_VERSION_CODE, items);
        findOrDie(APP_VERSION_NAME, items);
        findOrDie(REVIEWER_LANGUAGE, items);
        findOrDie(DEVICE, items);
        findOrDie(REVIEW_DATE_TIME, items);
        findOrDie(REVIEW_MILLIS, items);
        findOrDie(REVIEW_LAST_UPDATE_DATETIME, items);
        findOrDie(REVIEW_LAST_UPDATE_MILLIS, items);
        findOrDie(STAR_RATING, items);
        findOrDie(REVIEW_TITLE, items);
        findOrDie(REVIEW_TEXT, items);
        findOrDie(DEVELOPER_REPLY_DATETIME, items);
        findOrDie(DEVELOPER_REPLY_MILLIS, items);
        findOrDie(DEVELOPER_REPLY, items);
        findOrDie(REVIEW_LINK, items);
        return this;
    }

    /**
     * findOrDie a column heading in the set of values read from the input.
     * <p>
     * Note: this is an experimental approach, coded to try it out.
     *
     * @param expected column heading
     * @param values   all headings read from input
     * @throws UnexpectedFormatException
     */
    private void findOrDie(String expected, String[] values) throws UnexpectedFormatException {
        if (index(expected, values) == -1) {
            throw new UnexpectedFormatException(expected, values);
        }
    }

    /**
     * Find the column index for expected column headings
     *
     * @param name   the name of the column heading to find the index of
     * @param values the set of column headings (typically read from a csv file)
     * @return the column index if a match is found, else -1
     */
    private int index(String name, String[] values) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(name)) {
                fields.put(name, i);
                return i;
            }
        }
        return -1;
    }

    String[] nextRow() throws IOException {
        String line = reader.readLine();
        return null == line ? null : line.split(",", -1);
    }
}
