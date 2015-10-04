package io.github.metaluna.ck2edit.dataaccess.parser;

import java.util.Arrays;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Parses one or multiple values of one line.
 */
class MultiValueParser {

  /**
   * Parse the contents of a value.
   *
   * @param input the value to parse
   * @param parent the parent node all parsed children will be added to
   * @param lineNumber the line number used for debugging purposes
   */
  public void parse(String input, Node parent, int lineNumber) {
    LOG.entry(input, parent, lineNumber);

    Objects.requireNonNull(input);
    if (input.trim().isEmpty()) {
      throw new IllegalArgumentException("Value must not be an empty string.");
    }
    Objects.requireNonNull(parent);

    String value = prepare(input);

    while (null != value && !value.trim().isEmpty()) {
      value = nextNode(value, parent, lineNumber);
    }
    LOG.exit();
  }

  // ---vvv--- PACKAGE-PRIVATE ---vvv---
  /**
   * Strips all characters coming after the comment marker from the value.
   *
   * @param value the value to strip off of comments
   * @return the cleaned up value
   */
  String stripComment(String value) {
    LOG.entry(value);
    int comment = value.indexOf(Parser.COMMENT_MARKER);
    String result = value;
    if (comment > -1) {
      LOG.trace("Comment found: '%s'", value.substring(comment));
      result = result.substring(0, comment);
    }
    return LOG.exit(result);
  }

  /**
   * Parses a value as a list of leaf values
   *
   * @param input the input to parse
   * @param parent the parent of the new nodes
   */
  String parseListValue(String input, Node parent) {
    LOG.entry(input, parent);
    String value = input;
    int end;
    while (0 != (end = findEnd(value))) {
      String name = value.substring(0, end);
      if (Parser.NESTING_END.equals(name)) {
        return LOG.exit(value);
      }
      parent.addChild(new Node(name));
      value = value.substring(end).trim();
    }
    return LOG.exit(value.substring(end).trim());
  }

  /**
   * Split a string separated by the value separator. If none is present the
   * whole value will be returned as the first array item. Regardless of the
   * actual number of separators there will at most be two items returned.
   *
   * @param value the value to split
   * @return the split values
   */
  String[] splitValues(String value) {
    LOG.entry(value);
    String[] tokens = value.split(Parser.VALUE_SEPARATOR, 2);
    LOG.exit(Arrays.toString(tokens));
    return tokens;
  }
  
  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();

  /**
   * Strip all comments and braces from the value
   *
   * @param input the value to parse
   * @return the cleaned string
   */
  private String prepare(String input) {
    LOG.entry(input);
    String result = stripComment(input);
    result = stripBraces(result);
    return LOG.exit(result);
  }

  /**
   * Parses the next part of the initial value. The value is first split at the
   * value separator and two nodes will be created.
   *
   * @param input the value to parse
   * @param parent the parent node
   * @param lineNumber the current line number
   * @return the unparsed part of the input value
   */
  private String nextNode(String input, Node parent, int lineNumber) {
    LOG.entry(input, parent);
    String[] tokens = splitValues(input);

    String result;
    if (tokens.length == 2) {
      result = parseSimpleValue(tokens[0].trim(), tokens[1].trim(), parent);
    } else {
      Node localParent = parent;
      if (listContinues(parent)) {
        localParent = parent.getLastChild().get();
      }
      result = parseListValue(input.trim(), localParent);
    }
    return LOG.exit(result);
  }

  private static boolean listContinues(Node parent) {
    return !parent.isLeaf() && parent.getLastChild().get().isLeaf();
  }

  /**
   * Parses the first pair of values and returns the rest of the value for
   * further processing.
   *
   * @param name the name of the property
   * @param value the value of the property
   * @param parent the parent of the new node
   * @return the unparsed rest of the value. May be empty.
   */
  private String parseSimpleValue(String name, String value, Node parent) {
    LOG.entry(name, value, parent);
    Node newNode = new Node(name);
    parent.addChild(newNode);

    final int end = findEnd(value);
    if (end > 0) {
      Node childNode = new Node(value.substring(0, end));
      newNode.addChild(childNode);
    }
    String result = value.substring(end).trim();
    return LOG.exit(result);
  }

  /**
   * Detect the end of string marked by the first whitespace character. If
   * none is found the length of the value is returned. Whitespace inside of "
   * will be ignored.
   *
   * @param value the value to scan
   * @return the first occurrence of a space character or the length of the
   * value
   */
  private int findEnd(final String value) {
    LOG.entry(value);
    int end;
    if (value.startsWith(Parser.STRING_MARKER)) {
      end = value.substring(1, value.length()).indexOf(Parser.STRING_MARKER);
      if (end > -1) {
        end += 2;
      }
    } else {
      end = value.indexOf(" ");
    }
    
    if (end == -1) {
      end = value.length();
    }
    
    int result = Math.min(end, value.length());
    return LOG.exit(result);
  }

  /**
   * Removes all braces (meaning '{' and '}') from the value.
   *
   * @param value the value to clean
   * @return the cleaned up value
   */
  private String stripBraces(String value) {
    LOG.entry(value);
    String result = value.replace(Parser.NESTING_START, "").replace(Parser.NESTING_END, "");
    return LOG.exit(result);
  }

}
