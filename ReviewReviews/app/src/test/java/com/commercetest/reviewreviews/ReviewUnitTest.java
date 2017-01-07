package com.commercetest.reviewreviews;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.*;

/**
 * Test the Review class to ensure he basic functionality works as expected.
 * A local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ReviewUnitTest {

    private static final String PACKAGE_NAME = "com.commercetest.reviewreviews";
    private static final String ACTUAL_TIME_SHOULD_GO_HERE = "2016-actual-time-should-go-here";
    private static final int FAKE_TIMESTAMP = 3;
    private static final int FAKE_RATING = 4;
    public static final String TITLE = "When will this ever work?";
    public static final String REVIEW_TEXT = "How long must we wait for this app to be launched?";

    private Review createSkeletalReview() {
        return new Review.Builder(
                    PACKAGE_NAME,
                    ACTUAL_TIME_SHOULD_GO_HERE,
                    FAKE_TIMESTAMP,
                    FAKE_RATING
            ).build();
    }

    @Test
    public void we_can_create_a_review_with_only_mandatory_elements() throws Exception {
        Review review = createSkeletalReview();
        assertEquals("App Java package name incorrect", PACKAGE_NAME, review.getPackageName());
        assertEquals("Timestamp information incorrect", ACTUAL_TIME_SHOULD_GO_HERE,
                review.getReviewSubmitted());
        assertEquals("Timestamp in milli seconds incorrect", FAKE_TIMESTAMP,
                review.getReviewSubmittedMillis());
        assertEquals("Rating incorrect", FAKE_RATING, review.getRating());
        //TODO 2016-01-07 (jharty) decide whether to check all other fields are null
        //Note this is partly a design decision - how to handle empty and blank inputs in a review.
    }

    @Test
    public void we_can_create_a_fully_loaded_review_correctly() throws Exception {
        Review.Builder temp = new Review.Builder(
                PACKAGE_NAME,
                ACTUAL_TIME_SHOULD_GO_HERE,
                FAKE_TIMESTAMP,
                FAKE_RATING)
                .appVersionCode(31)
                .appVersionName("3.1")
                .developerReplied(ACTUAL_TIME_SHOULD_GO_HERE)
                .developerRepliedMillis(314)
                .developerReplyText("I write the code that makes the other developers cry")
                .device("Google Nexus S")
                .lastUpdated(ACTUAL_TIME_SHOULD_GO_HERE)
                .lastUpdatedMillis(3141)
                .reviewerLanguage("EN")
                .title(TITLE)
                .reviewText(REVIEW_TEXT)
                .reviewLink(new URL("http://www.themobileanalyticsplaybook.com/"));
        Review review = temp.build();
        // TODO 2016-01-07 (jharty) decide how thoroughly to check the field allocation.
        assertEquals("Review title incorrect", TITLE, review.getTitle());
        assertEquals("Review content incorrect", REVIEW_TEXT, review.getReviewText());
    }
}