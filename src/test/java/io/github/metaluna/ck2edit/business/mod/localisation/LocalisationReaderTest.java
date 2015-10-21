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

import io.github.metaluna.ck2edit.support.FileLoader;
import java.nio.file.Path;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class LocalisationReaderTest {

  private LocalisationReader reader;
  private LocalisationParser parser;
  
  @Before
  public void setUp() {
    parser = new LocalisationParser();
  }
  
  @Test(expected = NullPointerException.class)
  public void doesNotCreateWithoutPath() {
    reader = new LocalisationReader(null, parser);
  }

  @Test(expected = NullPointerException.class)
  public void doesNotCreateWithoutParser() {
    Path file = FileLoader.fetchFile(DIRECTORY, VALID_FILE);
    reader = new LocalisationReader(file, null);
  }

  @Test
  public void readsValidFile() {
    Path file = FileLoader.fetchFile(DIRECTORY, VALID_FILE);
    reader = new LocalisationReader(file, parser);
    
    LocalisationFile gotLocalisationFile = reader.read();
    assertThat(gotLocalisationFile.getLocalisations(), hasSize(1));
    
    Localisation localisation = gotLocalisationFile.getLocalisations().get(0);
    assertThat(localisation.getId(), is("KEY"));
    assertThat(localisation.getLanguage(Localisation.Language.ENGLISH), is("english column"));
    assertThat(localisation.getLanguage(Localisation.Language.FRENCH), is("french column"));
    assertThat(localisation.getLanguage(Localisation.Language.GERMAN), is("german column"));
    assertThat(localisation.getLanguage(Localisation.Language.SPANISH), is("spanish column"));
  }
  
  // ---vvv--- PRIVATE ---vvv---
  private static final String VALID_FILE = "valid_localisation.csv";
  private static final String DIRECTORY = "/reader/localisation";

}