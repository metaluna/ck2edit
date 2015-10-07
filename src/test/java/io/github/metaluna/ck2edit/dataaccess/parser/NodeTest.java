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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
  
  @Test
  public void addChildReturnsParentNode() {
    Node child = new Node("childnode");
    Node gotNode = node.addChild(child);
    assertThat(gotNode, is(this.node));
  }
  
  @Test(expected = NullPointerException.class)
  public void doesNotAddNullChild() {
    node.addChild((Node) null);
  }

  @Test
  public void addsAndGetsStringNodeChild() {
    String expName = "childnode";
    node.addChild(expName);
    assertThat(node.getChildren().get(0).getName(), is(expName));
  }
  
  @Test(expected = NullPointerException.class)
  public void doesNotAddNullStringChild() {
    node.addChild((String) null);
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
  
  @Test
  public void addsStringValue() {
    String expName = "property", expValue = "value";
    
    node.addPair(expName, expValue);
    
    assertThat(node.getChildren(), hasSize(1));
    Node pair = node.getChildren().get(0);
    assertThat(pair.getName(), is(expName));
    assertThat(pair.getChildren().get(0).getName(), is(expValue));
  }
  
  @Test(expected = NullPointerException.class)
  public void doesNotAddPairWithoutName() {
    node.addPair(null, "value");
  }
  
  @Test(expected = NullPointerException.class)
  public void doesNotAddPairWithoutValue() {
    node.addPair("property", null);
  }

  @Test
  public void addsListValue() {
    String expName = "property", li1 = "a", li2 = "b";
    List<String> expValues = Arrays.asList(li1, li2);
    
    node.addList(expName, expValues);
    
    assertThat(node.getChildren(), hasSize(1));
    Node pair = node.getChildren().get(0);
    assertThat(pair.getName(), is(expName));
    assertThat(pair.getChildren(), hasSize(2));
    assertThat(pair.getChildren().get(0).getName(), is(li1));
    assertThat(pair.getChildren().get(1).getName(), is(li2));
  }

  @Test(expected = NullPointerException.class)
  public void doesNotAddListWithoutName() {
    node.addList(null, Arrays.asList("a"));
  }
  
  @Test(expected = NullPointerException.class)
  public void doesNotAddListWithoutValues() {
    node.addList("property", null);
  }
  
  @Test
  public void doesNotAddListWithEmptyValuesList() {
    node.addList("property", Collections.emptyList());
    assertThat(node.getChildren(), is(empty()));
  }
  
}
