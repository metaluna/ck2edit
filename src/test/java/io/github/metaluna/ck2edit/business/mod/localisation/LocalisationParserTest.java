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

package io.github.metaluna.ck2edit.business.mod.localisation;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class LocalisationParserTest {

  private LocalisationParser parser;
  
  @Before
  public void setUp() {
    parser = new LocalisationParser();
  }

  @Test
  public void parsesValidLines() {
    String[] columns = new String[15];
    columns[0] = "code";
    columns[1] = "english";
    columns[2] = "french";
    columns[3] = "german";
    columns[5] = "spanish";
    columns[14] = "x";
    
    Localisation gotLocalisation = parser.parseLine(columns);
    
    assertThat(gotLocalisation.getId(), is("code"));
    assertThat(gotLocalisation.getLanguage(1), is("english"));
    assertThat(gotLocalisation.getLanguage(2), is("french"));
    assertThat(gotLocalisation.getLanguage(3), is("german"));
    assertThat(gotLocalisation.getLanguage(5), is("spanish"));
    assertThat(gotLocalisation.getLanguage(14), is("x"));
  }
  
  @Test
  public void lineIsComment() {
    String[] columns = new String[15];
    columns[0] = "#COMMENT LINE";
    
    boolean gotComment = parser.isComment(columns);
    assertThat(gotComment, is(true));
  }
  
  @Test
  public void readsShortLines() {
    String[] columns = new String[6];
    columns[0] = "SHORT_LINE";
    columns[1] = "english";
    columns[5] = "x";
    
    Localisation gotLocalisation = parser.parseLine(columns);
    
    assertThat(gotLocalisation.getId(), is("SHORT_LINE"));
    assertThat(gotLocalisation.getLanguage(1), is("english"));
    assertThat(gotLocalisation.getLanguage(5), is(""));
  }

  @Test
  public void filtersOutLineEndMarkersInBetween() {
    String[] columns = new String[6];
    columns[0] = "CODE";
    columns[1] = "english";
    columns[2] = "x";
    columns[3] = "german";
    
    Localisation gotLocalisation = parser.parseLine(columns);
    
    assertThat(gotLocalisation.getId(), is("CODE"));
    assertThat(gotLocalisation.getLanguage(1), is("english"));
    assertThat(gotLocalisation.getLanguage(2), is(""));
    assertThat(gotLocalisation.getLanguage(3), is("german"));
    
  }
}