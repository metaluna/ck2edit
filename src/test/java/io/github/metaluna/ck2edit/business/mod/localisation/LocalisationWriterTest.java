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

import io.github.metaluna.ck2edit.support.FileTestHelpers;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class LocalisationWriterTest {

  private LocalisationWriter writer;

  @Before
  public void setUp() throws IOException {
    FileTestHelpers.setUpTestDirectory();
  }

  @After
  public void tearDown() {
    FileTestHelpers.tearDownTestDirectory();
  }

  @Test(expected = NullPointerException.class)
  public void doesNotCreateWithoutLocalisationFile() {
    writer = new LocalisationWriter(null);
  }

  @Test
  public void createsValidFile() {
    LocalisationFile file = new LocalisationFile(generateFileName());
    Localisation localisation = new Localisation("KEY");
    localisation.setLanguage(Localisation.Language.ENGLISH, "english column");
    localisation.setLanguage(Localisation.Language.FRENCH, "french column");
    localisation.setLanguage(Localisation.Language.GERMAN, "german column");
    localisation.setLanguage(Localisation.Language.SPANISH, "spanish column");
    file.add(localisation);

    writer = new LocalisationWriter(file);
    writer.write();

    assertTrue(file.getPath().toFile().exists());
    
    Path expFile = FileTestHelpers.fetchFile("reader", "localisation", "valid_localisation.csv");
    String got = FileTestHelpers.readAsString(file.getPath());
    String exp = FileTestHelpers.readAsString(expFile);
    assertEquals(exp, got);    
  }

  // ---vvv--- PRIVATE ---vvv---
  private Path generateFileName() {
    return FileTestHelpers.generateFileName("localisation", "csv");
  }

}
