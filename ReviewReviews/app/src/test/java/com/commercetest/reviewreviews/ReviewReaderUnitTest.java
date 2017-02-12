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
        String[] values = splitCsvLine(CONTAINS_COMMAS);
        assertEquals("Expected the string would be split correctly", 3, values.length);
    }

    private String[] splitCsvLine(String line) {
        String[] initialTokens = line.split(",", -1);
        ArrayList<String> revisedTokens = new ArrayList<String>();
        boolean inQuotes = false;
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < initialTokens.length; i++) {
            final String value = initialTokens[i];
            int countOfDoubleQuotes = countOccurrences(value, '"');
            // TODO: I don't yet account for double, contiguous quotes
            boolean isOdd = (countOfDoubleQuotes % 2) == 1;
            if (isOdd) {
                inQuotes = true;
                temp.append(initialTokens[i]);
                int lookAheadLocation = i+1;
                while ((lookAheadLocation < initialTokens.length) && inQuotes) {
                    String nextValue = initialTokens[lookAheadLocation];
                    if (nextValue.contains("\"")) {
                        inQuotes = false;
                    }
                    temp.append(',');
                    temp.append(nextValue);
                    i++;
                    lookAheadLocation++;
                }
                revisedTokens.add(temp.toString());
            } else {
                revisedTokens.add(value);
            }
        }
        return revisedTokens.toArray(new String[0]);
    }
}
