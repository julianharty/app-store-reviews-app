package com.commercetest.reviewreviews;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Wrap up reading a csv file or stream.
 *
 * @author damienkallison@gmail.com
 */
public class SimpleCsvReader {

  private final BufferedReader reader;

  private SimpleCsvReader(BufferedReader reader) {
    this.reader = reader;
  }

  public static SimpleCsvReader fromFilename(String filename)
      throws FileNotFoundException {
    return fromStream(new FileInputStream(filename));
  }

  public static SimpleCsvReader fromStream(InputStream stream) {
    return new SimpleCsvReader(new BufferedReader(new InputStreamReader(new BufferedInputStream(stream))));
  }

  public String[] nextRow() throws IOException {
    String line = reader.readLine();
    return null == line? null : line.split(",");
  }
}
