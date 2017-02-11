package com.commercetest.reviewreviews;

import android.util.Log;

import java.io.IOException;

/**
 * This is a first attempt at adding some detection of the incoming file encodings.
 *
 * It's pretty basic and could be fooled I expect. However it has enabled the app to process
 * files in ASCII, UTF8 (without the Byte Order Mark information), and UTF-16.
 *
 * Created by julianharty_air on 11/02/2017.
 */

public class GuessEncoding {

    public static String guessFor(byte[] header) throws IOException {
        String encoding = "UTF8"; // We'll start with UTF8

        if (header.length < 3) {
            throw new IllegalArgumentException("Programmer error: insufficient data provided.");
        }

        if (((header[0] & 0xff) == 0xff && (header[1] & 0xff) == 0xfe) /* UTF-16, little endian */ ||
                ((header[0] & 0xff) == 0xfe && (header[1] & 0xff) ==  0xff) /* UTF-16, big endian */) {
            encoding = "UTF-16";
        }

        // TODO 20170211 (jharty) find UTF8 file to test this with.
        if ((header[0] & 0xff) == 0xef && (header[1] & 0xff) == 0xbb && (header[2] & 0xff) == 0xbf) /* UTF-8 */ {
            encoding = "UTF8";
        }

        if (    (header[0] >= 32 ) && (header[0] <= 127) &&
                (header[1] >= 32 ) && (header[1] <= 127) &&
                (header[2] >= 32 ) && (header[2] <= 127)) {
            encoding = "US-ASCII";
        }

        // TODO 20170211 (jharty) add detection for ASCII

        Log.i("FILE-ENCODING", "Encoding:" + encoding);
        return encoding;
    }
}
