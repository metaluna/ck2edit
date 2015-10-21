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

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Parses localisation lines and produces localisations.
 */
class LocalisationParser {
  
  public Localisation parseLine(String[] columns) {
    LOG.entry(Arrays.asList(columns));
    final Localisation result = new Localisation(columns[0]);

    for (int i = 1; i < columns.length-1; i++) {
      String translation = columns[i];
      
      if (Localisation.END_MARKER.equals(translation)) {
        continue;
      }
      
      result.setLanguage(i, translation);
    }
    
    return LOG.exit(result);
  }
  
  public boolean isComment(String[] columns) {
    return columns[0].trim().startsWith(COMMENT_MARKER);
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();
  private static final String COMMENT_MARKER = "#";

}
