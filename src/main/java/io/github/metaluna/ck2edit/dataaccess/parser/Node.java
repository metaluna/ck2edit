package io.github.metaluna.ck2edit.dataaccess.parser;

import java.util.ArrayList;
import java.util.Arrays;
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
   * @return the
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
  static Node createRoot() {
    return new Node(ROOT_NAME, true);
  }

  Node(String name) {
    this(name, false);
  }

  void addChild(Node child) {
    LOG.entry(this.name, child);
    Objects.requireNonNull(child);

    if (this.children == null) {
      this.children = new ArrayList<>();
    }
    this.children.add(child);
    LOG.exit();
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();
  private static final String ROOT_NAME = "[ROOT]";
  private static final String INDENTATION_PREFIX = "\t";

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
    builder.append(this.children.get(0).getName());
    builder.append("\n");
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
    builder.append("{ \n");

    // print children
    int nextIndentation = indentations + 1;
    builder.append(this.children.stream()
            .map(c -> c.print(nextIndentation))
            .collect(Collectors.joining())
    );
    // closing brackets
    indent(builder, indentations);
    builder.append("}\n");
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
            .append(" }\n");
    LOG.exit();
  }

}
