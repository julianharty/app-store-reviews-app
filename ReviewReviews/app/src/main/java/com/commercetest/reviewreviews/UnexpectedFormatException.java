package com.commercetest.reviewreviews;

import static android.text.TextUtils.join;

/**
 * Simple checked exception to help caller know unrecognised CSV content was detected.
 *
 * Created by julianharty_air on 10/02/2017.
 */

public class UnexpectedFormatException extends Exception {

    UnexpectedFormatException(String expected, String[] actual) {
        super ("Could not find: " + expected + " in header[" + join(",", actual) + "]");
    }
}
