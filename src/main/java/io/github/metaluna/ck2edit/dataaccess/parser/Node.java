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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A node consists of a name and an arbitrary number of sub-nodes.
 */
public class Node {

  /**
   * @return a new root node
   */
  public static Node createRoot() {
    return new Node(ROOT_NAME, true);
  }
  
  public static Node create(String name) {
    return new Node(name);
  }

  /**
   * @return the name of this node
   */
  public String getName() {
    return this.name;
  }

  /**
   * @return an unmodifiable list of children or an empty list if this node is a
   * leaf
   * @see #isLeaf()
   */
  public List<Node> getChildren() {
    if (this.children != null) {
      return Collections.unmodifiableList(this.children);
    } else {
      return Collections.emptyList();
    }
  }

  /**
   * @return the last child node or an empty optional.
   */
  public Optional<Node> getLastChild() {
    LOG.entry();
    if (this.isLeaf()) {
      return Optional.empty();
    }
    return LOG.exit(Optional.of(this.children.get(this.children.size() - 1)));
  }
  
  /**
   * @return <code>true</code> if this node has no children
   */
  public boolean isLeaf() {
    return this.children == null || this.children.isEmpty();
  }

  /**
   * @return <code>true</code> if this node is the root node
   */
  public boolean isRoot() {
    return LOG.exit(this.isRoot);
  }

  /**
   * Prints the current node and all of its sub-nodes. This will be formatted in
   * the same format as the input format.
   *
   * @param indentations the number of indentations prefixing all output
   * @return the finished output
   */
  public String print(int indentations) {
    LOG.entry(indentations);
    if (indentations < 0) {
      throw new IllegalArgumentException(String.format("Number of indentations must not be < 0, but is %d", indentations));
    }

    StringBuilder result = new StringBuilder();
    // add indentations
    indent(result, indentations);

    result.append(this.name);

    if (!this.isLeaf()) {
      result.append(" = ");

      // add children
      boolean isSimpleValue = this.children.size() == 1 && this.children.get(0).isLeaf();
      boolean isList = this.children.size() > 1 && this.children.get(0).isLeaf();

      if (isList) {
        printAsList(result);
      } else if (!isSimpleValue) {
        printAsComplexValue(result, indentations);
      } else {
        printAsSimpleValue(result);
      }
    }

    return LOG.exit(result.toString());
  }

  /**
   * Adds a child node to this node.
   * @param child the child node
   * @return a reference to this node
   */
  public Node addChild(Node child) {
    LOG.entry(this.name, child);
    Objects.requireNonNull(child);

    if (this.children == null) {
      this.children = new ArrayList<>();
    }
    this.children.add(child);
    return LOG.exit(this);
  }

  /**
   * Adds a new node with the specified name to this node.
   * @param name the name of the node
   * @return a reference to this node
   */
  public Node addChild(String name) {
    LOG.entry(name);
    this.addChild(new Node(name));
    return LOG.exit(this);
  }

  /**
   * Adds a node with a single child node.
   * @param name the name of the direct child
   * @param value the name of the child's child
   * @return a reference to this node
   */
  public Node addPair(String name, String value) {
    LOG.entry(name, value);
    Objects.requireNonNull(name);
    Objects.requireNonNull(value);
    final Node n = Node.create(name).addChild(value);
    this.addChild(n);
    return LOG.exit(this);
  }

  /**
   * Adds a node with a list of leaves as children. If the list of values
   * is empty nothing will be added.
   * @param name the name of the direct child
   * @param values the names of the leaves.
   * @return a reference to this node
   */
  public Node addList(String name, List<String> values) {
    LOG.entry(name, values);
    Objects.requireNonNull(name);
    Objects.requireNonNull(values);
    if (values.isEmpty()) {
      LOG.warn("Tried to add empty list of values with property name '%s' to node: ", name, this);
    } else {
      final Node n = Node.create(name);
      values.stream().forEach(v -> n.addChild(v));
      this.addChild(n);
    }
    return LOG.exit(this);
  }
  
  @Override
  public String toString() {
    LOG.entry();
    if (this.isRoot) {
      if (!isLeaf()) {
        return this.children.stream()
                .map(c -> c.print(0))
                .collect(Collectors.joining());
      } else {
        return this.name;
      }
    } else {
      return print(0);
    }
  }
  
  // ---vvv--- PACKAGE-PRIVATE ---vvv---
  Node(String name) {
    this(name, false);
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();
  private static final String ROOT_NAME = "[ROOT]";
  private static final String INDENTATION_PREFIX = "\t";
  private static final String NEW_LINE = "\r\n";

  private final String name;
  private final boolean isRoot;

  private List<Node> children;

  private Node(String name, boolean isRoot) {
    LOG.entry(name, isRoot);
    this.name = Objects.requireNonNull(name);
    if (Parser.RESTRICTED_CHARACTERS.contains(name.trim())) {
      throw new IllegalArgumentException(String.format("Name must not be a restricted character but is '%s'", name));
    }
    this.isRoot = isRoot;
    LOG.exit();
  }

  /**
   * Adds the current indentation level to the string builder by repeatedly
   * outputting the indentation prefix.
   *
   * @param builder the string builder to append to
   * @param indentations the number of indentations
   */
  private void indent(StringBuilder builder, int indentations) {
    LOG.entry(builder, indentations);
    for (int i = 0; i < indentations; ++i) {
      builder.append(INDENTATION_PREFIX);
    }
    LOG.exit();
  }

  /**
   * Print as a simple value.
   *
   * @param builder the string builder to append to
   */
  private void printAsSimpleValue(StringBuilder builder) {
    LOG.entry(builder);
    builder.append(this.children.get(0).getName())
            .append(NEW_LINE);
    LOG.exit();
  }

  /**
   * Print children of this node by calling their print method. The output of
   * the children is enclosed in braces. Also the current number of indentations
   * is increased by one.
   *
   * @param builder the string builder to append to
   * @param indentations the current level of indentations
   */
  private void printAsComplexValue(StringBuilder builder, int indentations) {
    LOG.entry(builder, indentations);
    // opening brackets
    builder.append("{ ").append(NEW_LINE);

    // print children
    int nextIndentation = indentations + 1;
    builder.append(this.children.stream()
            .map(c -> c.print(nextIndentation))
            .collect(Collectors.joining())
    );
    // closing brackets
    indent(builder, indentations);
    builder.append("}").append(NEW_LINE);
    LOG.exit();
  }

  /**
   * Print as a list of leaves, i.e. { a b c }
   *
   * @param builder the string builder to append to
   */
  private void printAsList(StringBuilder builder) {
    LOG.entry(builder);
    builder.append("{ ")
            .append(this.children.stream()
                    .map(c -> c.getName())
                    .collect(Collectors.joining(" "))
            )
            .append(" }")
            .append(NEW_LINE);
    LOG.exit();
  }

}
