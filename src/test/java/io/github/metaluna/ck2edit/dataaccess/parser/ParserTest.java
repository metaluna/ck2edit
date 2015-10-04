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

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ParserTest {

  private Parser parser;

  @Before
  public void setUp() {

  }

  @Test(expected = NullPointerException.class)
  public void doesNotCreateWithoutFilePath() {
    parser = new Parser(null, mock(MultiValueParser.class));
  }

  @Test(expected = IllegalArgumentException.class)
  public void doesNotCreateWithNonExistingFile() {
    parser = new Parser(Paths.get(System.getProperty("java.io.tmpdir"), "non-existing"), mock(MultiValueParser.class));
  }

  @Test
  public void returnsNullIfEmpty() throws IOException {
    parser = new Parser(Files.createTempFile("parsertest", null), mock(MultiValueParser.class));
    Node result = parser.parse();
    assertTrue(result.isLeaf());
  }

  @Test
  public void parsesTopEntries() {
    parser = loadParser("simple_parser.txt");
    Node result = parser.parse();

    String expName = "simple";
    String expValue = "value";

    Node property = result.getChildren().get(0);
    assertThat(property.getName(), is(expName));
    assertThat(property.getChildren().get(0).getName(), is(expValue));
  }

  @Test
  public void ignoresCommentsAtTheStartOfLine() {
    parser = loadParser("comment_at_start.txt");
    Node result = parser.parse();
    assertTrue(result.isLeaf());
  }

  @Test
  public void ignoresCommentsAtTheEnd() {
    parser = loadParser("comment_at_end.txt");
    Node result = parser.parse();

    String expName = "simple";
    String expValue = "value";

    Node property = result.getChildren().get(0);
    assertThat(property.getName(), is(expName));
    assertThat(property.getChildren().get(0).getName(), is(expValue));
  }

  @Test
  public void parsesMultipleSimpleValues() {
    parser = loadParser("multiple_simple.txt");

    Node result = parser.parse();

    assertThat(result.getChildren(), hasSize(3));
    assertThat(result.getChildren().get(0).getName(), is("property1"));
    assertThat(result.getChildren().get(0).getChildren().get(0).getName(), is("value1"));
    assertThat(result.getChildren().get(1).getName(), is("property2"));
    assertThat(result.getChildren().get(1).getChildren().get(0).getName(), is("value2"));
    assertThat(result.getChildren().get(2).getName(), is("property3"));
    assertThat(result.getChildren().get(2).getChildren().get(0).getName(), is("value3"));
  }

  @Test
  public void parsesShallowlyNestedValues() {
    parser = loadParser("simple_nested.txt");

    Node level0 = parser.parse();

    assertThat(level0.getChildren(), hasSize(1));
    Node level1 = level0.getChildren().get(0);
    assertThat(level1.getChildren(), hasSize(2));
  }

  @Test
  public void parsesDeeplyNestedValues() {
    parser = loadParser("deep_nesting.txt");

    Node level0 = parser.parse();

    assertThat(level0.getChildren(), hasSize(1));
    Node level1 = level0.getChildren().get(0);
    assertThat(level1.getChildren(), hasSize(1));
    Node level2 = level1.getChildren().get(0);
    assertThat(level2.getChildren(), hasSize(1));
    Node level3 = level2.getChildren().get(0);
    assertThat(level3.getChildren(), hasSize(1));
    Node level4 = level3.getChildren().get(0);
    assertThat(level4.getChildren(), hasSize(1));
    Node level5 = level4.getChildren().get(0);
    assertThat(level5.getChildren(), hasSize(1));
    Node level6 = level5.getChildren().get(0);
    assertTrue(level6.isLeaf());
  }

  @Test
  public void combinesLeavesAndNodes() {
    parser = loadParser("combined_nesting.txt");

    Node level0 = parser.parse();

    assertThat(level0.getChildren(), hasSize(2));
    Node level1 = level0.getChildren().get(0);
    assertThat(level1.getChildren(), hasSize(1));
    Node level2 = level1.getChildren().get(0);
    assertThat(level2.getChildren(), hasSize(1));
    Node level3 = level2.getChildren().get(0);
    assertTrue(level3.isLeaf());
  }

  @Test
  public void parsesInlineNestingWithOneValue() {
    parser = loadParser("inline_nesting_1.txt");

    Node level0 = parser.parse();

    assertThat(level0.getChildren(), hasSize(1));
    Node level1 = level0.getChildren().get(0);
    assertThat(level1.getChildren(), hasSize(1));
    Node level2 = level1.getChildren().get(0);
    assertThat(level2.getChildren(), hasSize(1));
    Node level3 = level2.getChildren().get(0);
    assertTrue(level3.isLeaf());
  }

  @Test
  public void parsesInlineNestingWithMultipleValues() {
    parser = loadParser("inline_nesting_2.txt");

    Node level0 = parser.parse();

    assertThat(level0.getChildren(), hasSize(1));

    Node level1 = level0.getChildren().get(0);
    assertThat(level1.getChildren(), hasSize(2));

    Node level2a = level1.getChildren().get(0);
    assertThat(level2a.getChildren(), hasSize(1));
    Node level3a = level2a.getChildren().get(0);
    assertTrue(level3a.isLeaf());

    Node level2b = level1.getChildren().get(1);
    assertThat(level2b.getChildren(), hasSize(1));
    Node level3b = level2b.getChildren().get(0);
    assertTrue(level3b.isLeaf());
  }

  @Test
  public void parseInlineNestingStartWithComment() {
    parser = loadParser("nested_with_comment.txt");

    Node level0 = parser.parse();

    assertThat(level0.getChildren(), hasSize(1));

    Node level1 = level0.getChildren().get(0);
    assertThat(level1.getChildren(), hasSize(1));

    Node level2 = level1.getChildren().get(0);
    assertThat(level2.getChildren(), hasSize(1));
    Node level3 = level2.getChildren().get(0);
    assertTrue(level3.isLeaf());
  }

  @Test
  public void parseNestingStartOnNewLine() {
    parser = loadParser("nesting_on_newline.txt");

    Node level0 = parser.parse();

    assertThat(level0.getChildren(), hasSize(1));

    Node level1 = level0.getChildren().get(0);
    assertThat(level1.getChildren(), hasSize(1));

    Node level2 = level1.getChildren().get(0);
    assertThat(level2.getChildren(), hasSize(1));
    Node level3 = level2.getChildren().get(0);
    assertTrue(level3.isLeaf());
  }

//  @Test
//  public void parsesComplexFile() {
//    parser = loadParser("complex_file.txt");
//    
//    Node level0 = parser.parse();
//    
//  }
  @Test
  public void parsesListOnSameLine() {
    parser = loadParser("simple_list.txt");

    Node root = parser.parse();
    
    assertThat(root.getChildren(), hasSize(2));
    assertListOfLeaves(root.getChildren().get(0), 3);
  }

  @Test
  public void parsesListStartingOnNewLine() {
    parser = loadParser("list_on_newline.txt");

    Node root = parser.parse();
    
    assertThat(root.getChildren(), hasSize(2));
    assertListOfLeaves(root.getChildren().get(0), 3);
  }
  
  @Test
  public void parsesListOnMultipleLines() {
    parser = loadParser("list_on_multiple_lines.txt");

    Node root = parser.parse();
    
    assertThat(root.getChildren(), hasSize(2));
    assertListOfLeaves(root.getChildren().get(0), 4);
  }
  
  @Test
  public void parsesListEndOnSameLine() {
    parser = loadParser("list_ending_on_same_line.txt");
    
    Node root = parser.parse();
    
    assertThat(root.getChildren(), hasSize(2));
    assertListOfLeaves(root.getChildren().get(0), 3);
  }
  
  // ---vvv--- PACKAGE-PRIVATE ---vvv---
  private Parser loadParser(String file) {
    URL resourceUrl = getClass().getResource("/parser/" + file);
    Path path = null;
    try {
      path = Paths.get(resourceUrl.toURI());
    } catch (URISyntaxException ex) {
      fail(String.format("Unable to load file '%s'", resourceUrl.toString()));
    }
    return new Parser(path, new MultiValueParser());
  }

  private void assertListOfLeaves(Node parent, int itemCount) {
    parent.getChildren().get(0);
    assertThat(parent.getChildren(), hasSize(itemCount));

    parent.getChildren().stream().forEach(n -> assertTrue(n.isLeaf()));
  }
  
}
