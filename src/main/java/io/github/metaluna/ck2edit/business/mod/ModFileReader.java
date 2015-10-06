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
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Base class for all mod file reader.
 */
public abstract class ModFileReader {
  
  protected enum ValueType {

    STRING, LIST, INTEGER, BOOLEAN
  };
  
  protected Object fetchAttributeValue(AttributeSetter<?, ?> attribute, Node node) {
    LOG.entry(attribute, node);
    final Object result;
    if (attribute.type == ValueType.STRING) {
      result = getStringValue(node);
    } else if (attribute.type == ValueType.INTEGER) {
      result = getIntegerValue(node);
    } else if (attribute.type == ValueType.BOOLEAN) {
      result = getBooleanValue(node);
    } else if (attribute.type == ValueType.LIST) {
      result = getListValue(node);
    } else {
      throw new AssertionError(String.format("Encountered unknown value type %s while parsing node %s", attribute.type, node));
    }
    return LOG.exit(result);
  }
  
  protected class AttributeSetter<S, T> {

    public final BiConsumer<S, T> setter;
    public final ValueType type;

    public AttributeSetter(BiConsumer<S, T> setter, ValueType valueType) {
      super();
      this.setter = setter;
      this.type = valueType;
    }
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();

  private List<String> getListValue(Node node) {
    LOG.entry(node);
    List<String> result = node.getChildren().stream().map(Node::getName).collect(Collectors.toList());
    return LOG.exit(result);
  }

  private String getStringValue(Node node) {
    LOG.entry(node);
    String result = node.getChildren().get(0).getName();
    return LOG.exit(result);
  }

  /**
   * Converts a node's value to a boolean. Only accepts "yes" as
   * <code>true</code>
   *
   * @param node the node whose child be converted to a boolean
   * @return the node's boolean value
   */
  private boolean getBooleanValue(Node node) {
    LOG.entry(node);
    boolean result = node.getChildren().get(0).getName().equals("yes");
    return LOG.exit(result);
  }

  /**
   * Please catch the NumberFormatException!
   *
   * @param node the node whose child will be converted to an integer
   * @return the node's integer value
   */
  private int getIntegerValue(Node node) {
    LOG.entry(node);
    int result = Integer.parseInt(node.getChildren().get(0).getName());
    return LOG.exit(result);
  }
}
