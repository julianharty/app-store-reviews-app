package com.commercetest.reviewreviews;

/**
 * Placeholder class to keep this method distinct from the rest of the project
 * as it came directly from StackOverflow.com and the licensing is complex, but permits the
 * code to be reused from what I understand on various posts on meta.
 *
 * Created by julianharty_air on 12/02/2017.
 */

public class CsvUtilities {

    /**
    This method is from Jon Skeet's answer to
    http://stackoverflow.com/questions/275944/java-how-do-i-count-the-number-of-occurrences-of-a-char-in-a-string
     */
    public static int countOccurrences(String haystack, char needle)
    {
        int count = 0;
        for (int i=0; i < haystack.length(); i++)
        {
            if (haystack.charAt(i) == needle)
            {
                count++;
            }
        }
        return count;
    }
}
