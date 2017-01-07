package com.commercetest.reviewreviews;

import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Created by julianharty_air on 07/01/2017.
 */

public class ReviewReaderUnitTest {

    private static final String SAMPLE_REVIEW_HEADING =
            ReviewReader.PACKAGE_NAME + "," +
                    ReviewReader.APP_VERSION_CODE + "," +
                    ReviewReader.APP_VERSION_NAME + "," +
                    ReviewReader.REVIEWER_LANGUAGE + "," +
                    ReviewReader.DEVICE + "," +
                    ReviewReader.REVIEW_DATE_TIME + "," +
                    ReviewReader.REVIEW_MILLIS + "," +
                    ReviewReader.REVIEW_LAST_UPDATE_DATETIME + "," +
                    ReviewReader.REVIEW_LAST_UPDATE_MILLIS + "," +
                    ReviewReader.STAR_RATING + "," +
                    ReviewReader.REVIEW_TITLE + "," +
                    ReviewReader.REVIEW_TEXT + "," +
                    ReviewReader.DEVELOPER_REPLY_DATETIME + "," +
                    ReviewReader.DEVELOPER_REPLY_MILLIS + "," +
                    ReviewReader.DEVELOPER_REPLY + "," +
                    ReviewReader.REVIEW_LINK +
                    "\r\n";
    private static final String SAMPLE_REVIEW_ROW_1 =
                "org.kiwix.kiwixmobile,29,1.99,pt,Q6T10IN,2016-06-01T00:24:44Z,1464740684029," +
                        "2016-06-01T00:24:44Z,1464740684029,5,,,,,," +
                        "\r\n"
            ;
    @Test
    public void create_review_from_sample_review_contents() throws Exception {

        final String MINI_FILE_CONTENTS = SAMPLE_REVIEW_HEADING + SAMPLE_REVIEW_ROW_1;
        StringReader review_file = new StringReader(MINI_FILE_CONTENTS);
        ReviewReader reader = ReviewReader.fromStringReaderForTesting(review_file);

        reader = reader.index();
        String[] review1 = reader.nextRow();
        assertEquals("Expected the app's java package name", review1[0], "org.kiwix.kiwixmobile");
        assertNull("Didn't expect another review after the first review", reader.nextRow());
        // TODO 2016-01-07 (jharty) continue working on this test and the functionality.
    }
}
