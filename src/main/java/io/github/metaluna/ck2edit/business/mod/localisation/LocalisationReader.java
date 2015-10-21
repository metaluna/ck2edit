/*
 * The MIT License
 *
 * Copyright 2015 Simon Hardijanto.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.metaluna.ck2edit.business.mod.localisation;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Reads a localisation file (CSV format).
 */
class LocalisationReader {

  /**
   * Creates a reader for the specified file.
   *
   * @param path the file to read
   */
  public LocalisationReader(Path path, LocalisationParser parser) {
    LOG.entry(path, parser);
    this.path = Objects.requireNonNull(path);
    this.parser = Objects.requireNonNull(parser);
    LOG.exit();
  }

  /**
   * Reads the localisation file.
   *
   * @return the localisation file. May be empty if an error occurred.
   */
  public LocalisationFile read() {
    LOG.entry();
    final LocalisationFile result = new LocalisationFile(this.path);

    // read the file
    try (BufferedReader reader = Files.newBufferedReader(this.path, CHARSET)) {
      String line;
      // line by line
      while (null != (line = reader.readLine())) {

        // split it!
        String[] columns = line.split(FIELD_SEPARATOR);

        // skip comments
        if (parser.isComment(columns)) {
          LOG.trace("Skipping comment line '%s'", line);
          continue;
        }
        
        Localisation localisation = parser.parseLine(columns);
        result.add(localisation);
      }
    } catch (IOException ex) {
      LOG.catching(ex);
    }

    return LOG.exit(result);
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();
  private static final Charset CHARSET = Charset.forName("Windows-1252");
  private static final String FIELD_SEPARATOR = ";";
  
  private final Path path;
  private final LocalisationParser parser;

}
