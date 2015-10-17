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

public class LocalisationTest {

  private Localisation localisation;
  private String id;
  
  @Before
  public void setUp() {
    this.id = "test_key";
    this.localisation = new Localisation(id);
  }

  @Test(expected = NullPointerException.class)
  public void doesNotCreateWithoutId() {
    localisation = new Localisation(null);
  }
  
  @Test
  public void getsId() {
    assertThat(localisation.getId(), is(id));
  }
  
  @Test(expected = NullPointerException.class)
  public void doesNotSetNullId() {
    localisation.setId(null);
  }
  
  @Test
  public void getsTranslationForSpecifiedLanguage() {
    String translation = "bla";
    localisation.setLanguage(Localisation.Language.ENGLISH, translation);
    
    assertThat(localisation.getLanguage(Localisation.Language.ENGLISH), is(translation));
  }
  
  @Test
  public void getsEmptyStringsForUntranslatedLanguages() {
    assertThat(localisation.getLanguage(Localisation.Language.ENGLISH), is(""));
  }
  
  @Test
  public void getsTranslationForSpecifiedColumn() {
    int column = 1;
    String translation = "blubb";
    localisation.setLanguage(column, translation);
    
    assertThat(localisation.getLanguage(column), is(translation));
  }
  
  @Test
  public void getEmptyStringForUntranslatedColumn() {
    assertThat(localisation.getLanguage(1), is(""));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void doesNotSetTranslationOfIdColumn() {
    localisation.setLanguage(0, "illegal column");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void doesNotSetTranslationOfNegativeColumn() {
    localisation.setLanguage(-1, "illegal column");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void doesNotSetTranslationOfLastColumn() {
    localisation.setLanguage(14, "illegal column");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void doesNotSetTranslationOfColumOutsideArrayBounds() {
    localisation.setLanguage(15, "illegal column");
  }
  
  @Test
  public void setsNullTranslationToEmptyString() {
    localisation.setLanguage(Localisation.Language.FRENCH, null);
    assertThat(localisation.getLanguage(Localisation.Language.FRENCH), is(""));
  }
}