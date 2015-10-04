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

public class MultiValueParserTest {

  private Node parent;
  private MultiValueParser mvParser;
  
  @Before
  public void setUp() {
    parent = Node.createRoot();
    mvParser = new MultiValueParser();
  }
  
  @Test(expected = NullPointerException.class)
  public void doesNotParseWithoutValue() {
    mvParser.parse(null, parent, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void doesNotParseWithEmptyValue() {
    mvParser.parse("", parent, 0);
  }

  @Test(expected = NullPointerException.class)
  public void doesNotParseWithoutParent() {
    mvParser.parse("bla", null, 0);
  }
  
  @Test
  public void parsesSimpleValue() {
    String expProperty = "property";
    String expValue = "value";
    String input = String.format("%s = %s", expProperty, expValue);
    
    mvParser.parse(input, parent, 0);
    
    assertThat(parent.getChildren(), hasSize(1));
    
    Node property = parent.getChildren().get(0);
    assertThat(property.getName(), is(expProperty));
    assertThat(property.getChildren().get(0).getName(), is(expValue));
  }
  
  @Test
  public void parsesOneNestedValue() {
    String expProperty = "property";
    String expValue = "value";
    String input = String.format("{ %s = %s }", expProperty, expValue);
    
    mvParser.parse(input, parent, 0);
    
    assertThat(parent.getChildren(), hasSize(1));
    
    Node property = parent.getChildren().get(0);
    assertThat(property.getName(), is(expProperty));
    assertThat(property.getChildren().get(0).getName(), is(expValue));
  }
  
  @Test
  public void parsesMultipleNestedValue() {
    String expProperty1 = "property1";
    String expValue1 = "value1";
    String expProperty2 = "property2";
    String expValue2 = "value2";
    String input = String.format("{ %s = %s %s = %s }", expProperty1, expValue1, expProperty2, expValue2);
    
    mvParser.parse(input, parent, 0);
    
    assertThat(parent.getChildren(), hasSize(2));
    
    Node property1 = parent.getChildren().get(0);
    assertThat(property1.getName(), is(expProperty1));
    assertThat(property1.getChildren().get(0).getName(), is(expValue1));

    Node property2 = parent.getChildren().get(1);
    assertThat(property2.getName(), is(expProperty2));
    assertThat(property2.getChildren().get(0).getName(), is(expValue2));
  }
  
  @Test
  public void ignoresCommentAfterNestingStart() {
    String expProperty = "property";
    String input = String.format("%s = { #comment", expProperty);
    
    mvParser.parse(input, parent, 0);
    
    assertThat(parent.getChildren(), hasSize(1));
    
    Node property = parent.getChildren().get(0);
    assertThat(property.getName(), is(expProperty));
    assertTrue(property.isLeaf());
  }
  
  @Test
  public void parsesLists() {
    String li1 = "li1", li2 = "li2", li3 = "li3";
    String input = String.format("{ %s %s %s }", li1, li2, li3);
    
    mvParser.parse(input, parent, 0);
    
    Node property = parent;
    assertThat(property.getChildren(), hasSize(3));
    assertThat(property.getChildren().get(0).getName(), is(li1));
    assertThat(property.getChildren().get(1).getName(), is(li2));
    assertThat(property.getChildren().get(2).getName(), is(li3));
    assertThat(property.getChildren(), hasSize(3));
  }
  
  @Test
  public void parsesStringWithSpaces() {
    String expValue = "\"O _ O\"";
    String input = String.format("property = %s", expValue);
    
    mvParser.parse(input, parent, 0);
    
    assertThat(parent.getChildren(), hasSize(1));
    
    Node property = parent.getChildren().get(0);
    assertThat(property.getChildren(), hasSize(1));
    assertThat(property.getChildren().get(0).getName(), is(expValue));
  }

}
