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
    node.addChild(null);
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
