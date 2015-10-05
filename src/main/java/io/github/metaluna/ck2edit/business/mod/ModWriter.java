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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Persists a mod file. This does only write the mod description file to disk
 * (*.mod), not any actual modded files.
 */
class ModWriter {

  /**
   * Writes the file.
   */
  public void write() {
    LOG.entry();
    this.validate();
    Node root = print(this.mod);
    try (BufferedWriter writer = Files.newBufferedWriter(this.file, CHARSET)) {
      writer.write(root.toString());
    } catch (IOException ex) {
      LOG.catching(ex);
    }
    LOG.exit();
  }

  // ---vvv--- PACKAGE-PRIVATE ---vvv---
  /**
   * Constructor
   *
   * @param file the path to the file including the name
   * @param mod the mod to persist
   */
  ModWriter(Path file, Mod mod) {
    LOG.entry(file, mod);
    this.file = Objects.requireNonNull(file);
    this.mod = Objects.requireNonNull(mod);
    LOG.exit();
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();
  private static final Charset CHARSET = Charset.forName("Windows-1252");

  private final Path file;
  private final Mod mod;

  private Node print(Mod mod) {
    LOG.entry(mod);
    final Node result = Node.createRoot();

    addSimpleValue(result, "name", mod.getName());
    addSimpleValue(result, "path", mod.getPath());
    addSimpleValue(result, "user_dir", mod.getUserDir());
    addSimpleValue(result, "archive", mod.getArchive());

    if (mod.getReplacePaths() != null) {
      mod.getReplacePaths().stream()
              .map(p -> Node.create(p))
              .map(n -> Node.create("replace_path").addChild(n))
              .forEach(n -> result.addChild(n));
    }
    addSimpleValue(result, "picture", mod.getPicture());
    addList(result, "tags", mod.getTags());
    addList(result, "dependencies", mod.getDependencies());

    return LOG.exit(result);
  }

  private void addSimpleValue(Node result, String name, String value) {
    LOG.entry(result, name, value);
    if (value == null) {
      return;
    }
    final Node n = Node.create(name).addChild(value);
    result.addChild(n);
    LOG.exit();
  }

  private void addList(Node result, String name, List<String> values) {
    LOG.entry(result, name, values);
    if (values == null || values.isEmpty()) {
      return;
    }
    final Node n = Node.create(name);
    values.stream().forEach(v -> n.addChild(v));
    result.addChild(n);
    LOG.exit();
  }

  private void validate() {
    if (!validString(this.mod.getName())) {
      throw new IllegalStateException("Cannot save mod. Name is required.");
    }
    if (!validString(this.mod.getPath())) {
      throw new IllegalStateException(String.format("Cannot save mod %s. Path is required.", this.mod.getName()));
    }
  }

  private boolean validString(String test) {
    return test != null && !test.isEmpty();
  }
}
