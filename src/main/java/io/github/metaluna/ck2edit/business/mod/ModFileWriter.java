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
package io.github.metaluna.ck2edit.business.mod;

import io.github.metaluna.ck2edit.dataaccess.parser.Node;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Writer base class.
 */
public abstract class ModFileWriter {

  /**
   * Writes the file to disk. Template method using {@link #print() } and
   * {@link #validate()}.
   */
  public void write() {
    LOG.entry();
    this.validate();
    Node root = print();
    try (BufferedWriter writer = Files.newBufferedWriter(this.file, CHARSET)) {
      writer.write(root.toString());
    } catch (IOException ex) {
      LOG.catching(ex);
    }
    LOG.exit();
  }

  // ---vvv--- PROTECTED ---vvv---
  /**
   * Converts the mod file's data into a node structure needed for persisting.
   */
  protected abstract Node print();

  /**
   * Will be called before {@link #print()}. Throw any exceptions in here.
   */
  protected abstract void validate();

  /**
   * Constructor
   * @param file the location of the file 
   */
  protected ModFileWriter(Path file) {
    LOG.entry(file);
    this.file = Objects.requireNonNull(file);
    LOG.exit();
  }

  protected void addSimpleValue(Node result, String name, Optional<Integer> value) {
    LOG.entry(result, name, value);
    value.ifPresent((Integer v) -> addSimpleValue(result, name, v));
    LOG.exit();
  }

  protected void addSimpleValue(Node result, String name, String value) {
    LOG.entry(result, name, value);
    if (value == null) {
      return;
    }
    result.addPair(name, value);
    LOG.exit();
  }

  protected void addSimpleValue(Node result, String name, boolean value) {
    LOG.entry(result, name, value);
    if (value) {
      addSimpleValue(result, name, "yes");
    } else {
      LOG.trace("Skipping negative boolean for %s", name);
    }
    LOG.exit();
  }

  protected void addSimpleValue(Node result, String name, int value) {
    LOG.entry(result, name, value);
    String stringValue = Integer.toString(value);
    addSimpleValue(result, name, stringValue);
    LOG.exit();
  }

  protected void addList(Node result, String name, List<String> values) {
    LOG.entry(result, name, values);
    if (values == null || values.isEmpty()) {
      return;
    }
    result.addList(name, values);
    LOG.exit();
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();
  private static final Charset CHARSET = Charset.forName("Windows-1252");

  private final Path file;
}
