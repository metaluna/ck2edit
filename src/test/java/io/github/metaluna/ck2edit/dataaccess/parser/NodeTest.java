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

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class NodeTest {

  private Node node;
  
  @Before
  public void setUp() {
    node = new Node("emptynode");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void doesNotCreateWithValueSeparatorAsName() {
    node = new Node("=");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void doesNotCreateWithNestingStartMarkerAsName() {
    node = new Node("{");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void doesNotCreateWithNestingEndMarkerAsName() {
    node = new Node("}");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void doesNotCreateWithCommentMarkerAsName() {
    node = new Node("#");
  }
  
  @Test
  public void rootFactoryProducesRoot() {
    node = Node.createRoot();
    assertTrue(node.isRoot());
  }
  
  @Test
  public void getsName() {
    String expName = "nodename";
    node = new Node(expName);
    assertThat(node.getName(), is(expName));
  }
  
  @Test
  public void isNotRootByDefault() {
    assertFalse(node.isRoot());
  }

  @Test
  public void getsEmptyListOfChildren() {
    assertThat(node.getChildren(), is(empty()));
  }

  @Test
  public void addsAndGetsChild() {
    Node child = new Node("childnode");
    node.addChild(child);
    assertThat(node.getChildren(), contains(child));
  }
  
  @Test(expected = NullPointerException.class)
  public void doesNotAddNullChild() {
    node.addChild((Node) null);
  }

  @Test
  public void emptyNodeIsLeaf() {
    assertTrue(node.isLeaf());
  }
  
  @Test
  public void nodeWithChildrenIsNotLeaf() {
    node.addChild(new Node("blubb"));
    assertFalse(node.isLeaf());
  }
  
}
