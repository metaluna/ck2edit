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

package io.github.metaluna.ck2edit.business.mod.opinionmodifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class OpinionModifierFileTest {

  private OpinionModifierFile opinionModifierFile;
  
  @Before
  public void setUp() {
  }

  @Test(expected = NullPointerException.class)
  public void doesNotCreateWithoutPath() {
    opinionModifierFile = new OpinionModifierFile(null);
  }
  
  @Test
  public void getsName() throws IOException {
    Path path = Files.createTempFile(null, null);
    path.toFile().deleteOnExit();
    String expName = path.getFileName().toString();
    opinionModifierFile = new OpinionModifierFile(path);
    assertThat(opinionModifierFile.getName(), is(expName));
  }

  @Test
  public void toStringContainsFileName() throws IOException {
    Path path = Files.createTempFile(null, null);
    path.toFile().deleteOnExit();
    String expName = path.getFileName().toString();
    opinionModifierFile = new OpinionModifierFile(path);
    assertThat(opinionModifierFile.toString(), containsString(expName));
    
  }

}