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

package io.github.metaluna.ck2edit.business.mod.opinionmodifier;

import io.github.metaluna.ck2edit.business.mod.ModFile;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * File containing a number of opinion modifiers.
 */
public class OpinionModifierFile implements ModFile {

  /**
   * Constructor
   * @param path the path to the file 
   */
  public OpinionModifierFile(Path path) {
    this.path = Objects.requireNonNull(path);
    this.opinionModifiers = new ArrayList<>();
  }

  @Override
  public String getName() {
    return this.path.getFileName().toString();
  }
  
  @Override
  public String toString() {
    return this.getName();
  }

  /**
   * @return an unmodifiable list of opinion modifiers contained in this file
   */
  public List<OpinionModifier> getOpinionModifiers() {
    return Collections.unmodifiableList(this.opinionModifiers);
  }

  public void add(OpinionModifier modifier) {
    Objects.requireNonNull(modifier);
    this.opinionModifiers.add(modifier);
  }
  
  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();
  
  /** the path to the file */
  private final Path path;
  /** the list of opinion modifiers */
  private final List<OpinionModifier> opinionModifiers;

}
