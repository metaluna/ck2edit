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

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Persists a {@link LocalisationFile} to disk.
 */
class LocalisationWriter {

  /**
   * Constructor.
   * @param localisationFile the localisation file to persist
   */
  public LocalisationWriter(LocalisationFile localisationFile) {
    LOG.entry(localisationFile);
    this.localisationFile = Objects.requireNonNull(localisationFile);
    LOG.exit();
  }

  /**
   * Writes the file to disk.
   */
  public void write() {
    LOG.entry();
    try (BufferedWriter writer = Files.newBufferedWriter(this.localisationFile.getPath(), CHARSET)) {
      writeHeader(writer);
      
      for (Localisation localisation : this.localisationFile.getLocalisations()) {
        writeLocalisation(writer, localisation);
      }
      
    } catch (IOException ex) {
      LOG.catching(ex);
    }
    LOG.exit();
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();
  private static final Charset CHARSET = Charset.forName("Windows-1252");
  private static final String NEWLINE = "\r\n";
  private static final String HEADER = "#CODE;ENGLISH;FRENCH;GERMAN;;SPANISH;;;;;;;;;x";
  private static final String COLUMN_SEPARATOR = ";";
  private static final int COLUMN_COUNT = 15;
  
  private final LocalisationFile localisationFile;

  private void addLine(BufferedWriter writer, String line) throws IOException {
    LOG.entry(writer, line);
    writer.write(line);
    writer.write(NEWLINE);
    LOG.exit();
  }
  
  private void writeHeader(BufferedWriter writer) throws IOException {
    LOG.entry(writer);
    addLine(writer, HEADER);
    LOG.exit();
  }

  private void writeLocalisation(BufferedWriter writer, Localisation localisation) throws IOException {
    LOG.entry(writer, localisation);
    final List<String> line = new ArrayList<>(COLUMN_COUNT);
    for (int i = 0; i < COLUMN_COUNT; i++) {
      line.add(localisation.getLanguage(i));
    }
    String result = String.join(COLUMN_SEPARATOR, line);
    addLine(writer, result);
    LOG.exit();
  }
}
