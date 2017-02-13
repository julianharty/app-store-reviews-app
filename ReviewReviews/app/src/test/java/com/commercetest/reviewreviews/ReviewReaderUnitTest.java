package com.commercetest.reviewreviews;

import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;

import static com.commercetest.reviewreviews.CsvUtilities.countOccurrences;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
    public void parse_review_contents_from_sample_review() throws Exception {

        final String MINI_FILE_CONTENTS = SAMPLE_REVIEW_HEADING + SAMPLE_REVIEW_ROW_1;
        StringReader review_file = new StringReader(MINI_FILE_CONTENTS);
        ReviewReader reader = ReviewReader.fromStringReaderForTesting(review_file);

        reader = reader.index();
        String[] review1 = reader.nextRow();
        assertEquals("Expected the app's java package name", review1[0], "org.kiwix.kiwixmobile");
        assertNull("Didn't expect another review after the first review", reader.nextRow());
        // TODO 2016-01-07 (jharty) continue working on this test and the functionality.
    }

    @Test
    public void create_review_from_sample_review() throws Exception {

        final String MINI_FILE_CONTENTS = SAMPLE_REVIEW_HEADING + SAMPLE_REVIEW_ROW_1;
        StringReader review_file = new StringReader(MINI_FILE_CONTENTS);
        ReviewReader reader = ReviewReader.fromStringReaderForTesting(review_file);

        reader = reader.index();
        Review review = reader.next();

    }

    @Test
    public void correctly_split_csv_containing_commas_in_quotes() throws Exception {
        final String CONTAINS_COMMAS = "\"Field One\",\"Field Two\",\"Red,Blue\"";
        ReviewReader reader = ReviewReader.fromStringReaderForTesting(new StringReader(CONTAINS_COMMAS));
        String[] values = reader.splitCsvLine(CONTAINS_COMMAS);
        assertEquals("Expected the string would be split correctly", 3, values.length);
    }

    // A long, involved review follows (from reviews_org.kiwix.kiwixmobile_201607.csv).
    // It includes embedded double-quotes and lots of text. Once the code can parse this
    // I hope it'll be ""good enough"".
    // org.kiwix.kiwixmobile,29,1.99,en,delos3geur,2016-07-18T17:28:36Z,1468862916592,2016-07-18T17:28:36Z,1468862916592,4,Close to perfect,"Very practical and fast to use, even on my ""tankphone"". What's most appreciated is the ability to load Wikivoyage files from the SD card after installing, one thing the official ""Wikivoyage offline"" app doesn't allow (won't even install on my phone due to its size). What to improve? *Add the possibility to edit the online version right from the app *Switch from one language to another on the same article *Keep only one item of each article searched in the history. Thumbs up!",2016-07-21T07:01:16Z,1469084476450,"Thank you for your rating and detailed comment. We agree with your wishes but not all of them are easy to implement. That said we go in these directions. The last bug on your list should be fixed in next release. To keep in touch, you can follow us on: * Twitter: https://twitter.com/KiwixOffline * Facebook: https://www.facebook.com/KiwixOffline/",https://play.google.com/apps/publish?account=9116215767541857492#ReviewDetailsPlace:p=org.kiwix.kiwixmobile&reviewid=gp:AOqpTOG-7xqX1E0d94zVrvq3zFT-8U7K2861Mkq2MSnZT2RMgA_OuzYvsSD5c8_C-HXGh1QndXhY5-SUuw4vMF0

    final String complexReview =
            "org.kiwix.kiwixmobile,29,1.99,en,delos3geur,2016-07-18T17:28:36Z,1468862916592,2016-07-18T17:28:36Z,1468862916592,4,Close to perfect,\"Very practical and fast to use, even on my \"\"tankphone\"\". What's most appreciated is the ability to load Wikivoyage files from the SD card after installing, one thing the official \"\"Wikivoyage offline\"\" app doesn't allow (won't even install on my phone due to its size). What to improve? *Add the possibility to edit the online version right from the app *Switch from one language to another on the same article *Keep only one item of each article searched in the history. Thumbs up!\",2016-07-21T07:01:16Z,1469084476450,\"Thank you for your rating and detailed comment. We agree with your wishes but not all of them are easy to implement. That said we go in these directions. The last bug on your list should be fixed in next release. To keep in touch, you can follow us on: * Twitter: https://twitter.com/KiwixOffline * Facebook: https://www.facebook.com/KiwixOffline/\",https://play.google.com/apps/publish?account=9116215767541857492#ReviewDetailsPlace:p=org.kiwix.kiwixmobile&reviewid=gp:AOqpTOG-7xqX1E0d94zVrvq3zFT-8U7K2861Mkq2MSnZT2RMgA_OuzYvsSD5c8_C-HXGh1QndXhY5-SUuw4vMF0";

    @Test
    public void copes_with_doublequotes() throws Exception {
        ReviewReader reader = ReviewReader.fromStringReaderForTesting(new StringReader(complexReview));
        String[] values = reader.splitCsvLine(complexReview);
        assertEquals("Expected 16 fields in this review", 16, values.length);
    }
}
