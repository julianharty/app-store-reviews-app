package com.commercetest.reviewreviews;

import org.junit.Test;

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
    private static final int FAKE_TIMESTAMP = 5;
    private static final int FAKE_RATING = 3;

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void we_can_create_a_review_with_only_mandatory_elements() throws Exception {
        Review review = new Review.Builder(
                PACKAGE_NAME,
                ACTUAL_TIME_SHOULD_GO_HERE,
                FAKE_TIMESTAMP,
                FAKE_RATING
        ).build();
        assertEquals("App Java package name incorrect", PACKAGE_NAME, review.getPackageName());
        assertEquals("Timestamp information incorrect", ACTUAL_TIME_SHOULD_GO_HERE,
                review.getReviewSubmitted());
        assertEquals("Timestamp in milli seconds incorrect", FAKE_TIMESTAMP,
                review.getReviewSubmittedMillis());
        assertEquals("Rating incorrect", FAKE_RATING, review.getRating());
    }
}