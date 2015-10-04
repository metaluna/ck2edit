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
package io.github.metaluna.ck2edit.dataaccess.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A parser accepts a single file and produces a tree of values.
 */
public class Parser {

  /**
   * Parse the file
   *
   * @return the root node or empty if the file is empty
   */
  public Node parse() {
    LOG.entry();
    final Node root = Node.createRoot();

    try (BufferedReader reader = Files.newBufferedReader(path, CHARSET)) {
      parseNested(reader, root);
    } catch (IOException ex) {
      LOG.catching(ex);
    } catch (Exception ex) {
      LOG.error("Exception on line #%d: %s", lineNumber, ex);
      throw ex;
    }

    LOG.debug("Finished parsing on line %d", lineNumber);

    return LOG.exit(root);
  }

  // ---vvv--- PACKAGE-PRIVATE ---vvv---
  static final String VALUE_SEPARATOR = "=";
  static final String COMMENT_MARKER = "#";
  static final String NESTING_START = "{";
  static final String NESTING_END = "}";
  static final String STRING_MARKER = "\"";
  static final List<String> RESTRICTED_CHARACTERS
          = Arrays.asList(Parser.VALUE_SEPARATOR,
                  Parser.NESTING_START,
                  Parser.NESTING_END,
                  Parser.COMMENT_MARKER,
                  Parser.STRING_MARKER);

  /**
   * Constructor
   *
   * @param path the file to read
   * @param mvParser the parser to scan values
   */
  Parser(Path path, MultiValueParser mvParser) {
    LOG.entry(path);
    this.path = Objects.requireNonNull(path);
    if (!Files.exists(path)) {
      throw new IllegalArgumentException(String.format("File '%s' does not exist", path));
    }
    this.valueParser = Objects.requireNonNull(mvParser);
    LOG.exit();
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();
  private static final Charset CHARSET = Charset.forName("Windows-1252");

  private final Path path;
  private final MultiValueParser valueParser;

  /**
   * Line counter
   */
  private int lineNumber;

  /**
   * Reads the next line.
   *
   * @param reader the reader providing the input
   * @param parent the parent all new nodes are attached to
   * @return <code>true</code> if a closing brace was encountered or the file
   * ended
   * @throws IOException if there is an error reading the input
   */
  private boolean parseNextLine(BufferedReader reader, Node parent) throws IOException {
    LOG.entry(reader, lineNumber+1, parent);
    String line = nextLine(reader);
    if (line == null) {
      return true;
    }

    line = valueParser.stripComment(line).trim();

    if (line.isEmpty()) {
      // skip empty line
      return LOG.exit(false);
    } else if (line.startsWith(NESTING_END)) {
      // signal that there are no more children of this parent
      return LOG.exit(true);
    } else if (line.startsWith(NESTING_START)) {
      // add to previous property
      parseNested(reader, parent.getLastChild().get());
      return LOG.exit(false);
    }

    String[] tokens = valueParser.splitValues(line);

    if (tokens.length == 1) {
      String unparsed = valueParser.parseListValue(line, parent);
      if (NESTING_END.equals(unparsed)) {
        return LOG.exit(true);
      } else {
        return LOG.exit(false);
      }
    } else {
      final String name = tokens[0].trim();
      Node newNode = new Node(name);
      parent.addChild(newNode);

      final String value = tokens[1].trim();
      if (value.equals(NESTING_START)) {
        parseNested(reader, newNode);
      } else if (!value.isEmpty()) {
        valueParser.parse(value, newNode, lineNumber);
      }

      return LOG.exit(false);
    }
  }

  private void parseNested(BufferedReader reader, Node newNode) throws IOException {
    LOG.entry(reader, newNode);
    boolean nestingEnded = false;
    while (!nestingEnded) {
      nestingEnded = parseNextLine(reader, newNode);
    }
    LOG.exit();
  }

  private String nextLine(BufferedReader reader) throws IOException {
    String line = reader.readLine();
    lineNumber++;
    return line;
  }

}
