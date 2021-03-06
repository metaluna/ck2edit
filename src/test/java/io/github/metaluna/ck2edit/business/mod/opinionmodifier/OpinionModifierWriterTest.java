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

import io.github.metaluna.ck2edit.support.FileTestHelpers;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import org.junit.After;
import static org.junit.Assert.*;

public class OpinionModifierWriterTest {

  private OpinionModifierWriter opinionModifierWriter;
  private OpinionModifier opinionModifier;

  @Before
  public void setUp() throws IOException {
    FileTestHelpers.setUpTestDirectory();
    opinionModifier = createOpinionModifier();
  }

  @After
  public void tearDown() {
    FileTestHelpers.tearDownTestDirectory();
  }

  @Test(expected = NullPointerException.class)
  public void doesNotCreateWithoutFile() {
    opinionModifierWriter = new OpinionModifierWriter(null);
  }

  @Test
  public void createsCompleteOpinionModifierFile() {
    OpinionModifierFile file = createOpinionModifierFile(createOpinionModifier("test_modifier"));
    opinionModifierWriter = new OpinionModifierWriter(file);

    opinionModifierWriter.write();

    assertTrue(file.getPath().toFile().exists());

    Path expFile = FileTestHelpers.fetchFile("reader", "opinionmodifier", "single_opinion_modifier.txt");
    String got = FileTestHelpers.readAsString(file.getPath());
    String exp = FileTestHelpers.readAsString(expFile);
    assertEquals(exp, got);
  }

  @Test(expected = IllegalStateException.class)
  public void doesNotSaveFileWithoutAnyOpinionModifiers() {
    OpinionModifierFile file = new OpinionModifierFile(generateFileName());
    opinionModifierWriter = new OpinionModifierWriter(file);
    opinionModifierWriter.write();
  }

  @Test
  public void doesNotWriteUnsetProperties() {
    String expName = "minimal_modifier";
    int expOpinion = 33;
    opinionModifier = new OpinionModifier(expName);
    opinionModifier.setOpinion(expOpinion);
    OpinionModifierFile file = createOpinionModifierFile(opinionModifier);
    opinionModifierWriter = new OpinionModifierWriter(file);
    
    opinionModifierWriter.write();
    
    assertTrue(file.getPath().toFile().exists());

    String got = FileTestHelpers.readAsString(file.getPath());
    assertThat(got, containsString(expName));
    assertThat(got, containsString("opinion"));
    assertThat(got, containsString(Integer.toString(expOpinion)));
    
    assertThat(got, not(containsString("months")));
    assertThat(got, not(containsString("prison_reason")));
    assertThat(got, not(containsString("banish_reason")));
    assertThat(got, not(containsString("execute_reason")));
    assertThat(got, not(containsString("revoke_reason")));
    assertThat(got, not(containsString("divorce_reason")));
    assertThat(got, not(containsString("inherit")));
    assertThat(got, not(containsString("crime")));
    assertThat(got, not(containsString("enemy")));
  }

  // ---vvv--- PRIVATE ---vvv---
  private Path generateFileName() {
    return FileTestHelpers.generateFileName("opinion_writer", "txt");
  }

  private OpinionModifier createOpinionModifier() {
    return this.createOpinionModifier("test_modifier_" + System.currentTimeMillis());
  }

  private OpinionModifier createOpinionModifier(String name) {
    final OpinionModifier result = new OpinionModifier(name);
    result.setOpinion(42);
    result.setDuration(12);
    result.setBanishReason(true);
    result.setCrime(true);
    result.setDivorceReason(true);
    result.setEnemy(true);
    result.setExecuteReason(true);
    result.setInherited(true);
    result.setPrisonReason(true);
    result.setRevokeReason(true);
    return result;
  }

  private OpinionModifierFile createOpinionModifierFile(OpinionModifier opinionModifier) {
    final OpinionModifierFile result = new OpinionModifierFile(generateFileName());
    result.add(opinionModifier);
    return result;
  }
}
