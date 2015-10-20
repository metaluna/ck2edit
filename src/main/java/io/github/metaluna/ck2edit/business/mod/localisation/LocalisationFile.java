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

import io.github.metaluna.ck2edit.business.mod.ModFileImpl;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * File containing any number of localisations.
 */
public class LocalisationFile extends ModFileImpl {

  public LocalisationFile(Path path) {
    super(path);
    this.localisations = new ArrayList<>();
  }

  /**
   * @return an unmodifiable list of localisations contained in this file
   */
  public List<Localisation> getLocalisations() {
    return Collections.unmodifiableList(localisations);
  }

  /**
   * Adds a new localisation to this file. Must not be <code>null</code>. If a
   * localisation is added a second time it will be silently ignored.
   *
   * @param localisation the localisation to add
   */
  public void add(Localisation localisation) {
    LOG.entry(localisation);
    Objects.requireNonNull(localisation);
    if (this.localisations.contains(localisation)) {
      LOG.info("Localisation already in this file: %s", localisation);
    } else {
      this.localisations.add(localisation);
    }
    LOG.exit();
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();
  private final List<Localisation> localisations;
}
