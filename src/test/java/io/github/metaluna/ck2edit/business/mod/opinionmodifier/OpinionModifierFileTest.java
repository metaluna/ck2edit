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
  private Path path;
  
  @Before
  public void setUp() throws IOException {
    path = Files.createTempFile(null, null);
    path.toFile().deleteOnExit();
    opinionModifierFile = new OpinionModifierFile(path);
  }

  @Test(expected = NullPointerException.class)
  public void doesNotCreateWithoutPath() {
    opinionModifierFile = new OpinionModifierFile(null);
  }
  
  @Test
  public void getsName() throws IOException {
    String expName = path.getFileName().toString();
    opinionModifierFile = new OpinionModifierFile(path);
    assertThat(opinionModifierFile.getName(), is(expName));
  }

  @Test
  public void toStringContainsFileName() throws IOException {
    String expName = path.getFileName().toString();
    opinionModifierFile = new OpinionModifierFile(path);
    assertThat(opinionModifierFile.toString(), containsString(expName));
    
  }
  
  @Test
  public void addsValidOpinionModifier() {
    OpinionModifier modifier = new OpinionModifier("test");
    opinionModifierFile.add(modifier);
    
    assertThat(opinionModifierFile.getOpinionModifiers(), contains(modifier));
  }
  
  @Test
  public void addsMultipleModifiers() {
    OpinionModifier modifier = new OpinionModifier("test");
    opinionModifierFile.add(modifier);
    OpinionModifier modifier2 = new OpinionModifier("test 2");
    opinionModifierFile.add(modifier2);
    
    assertThat(opinionModifierFile.getOpinionModifiers(), contains(modifier, modifier2));
  }
  
  @Test(expected = NullPointerException.class)
  public void doesNotAddNullModifier() {
    opinionModifierFile.add(null);
  }
  
  @Test
  public void doesNotAddModifierOnceMore() {
    OpinionModifier modifier = new OpinionModifier("test");
    opinionModifierFile.add(modifier);
    
    opinionModifierFile.add(modifier);
    
    assertThat(opinionModifierFile.getOpinionModifiers(), hasSize(1));
  }

}