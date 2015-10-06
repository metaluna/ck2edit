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

import io.github.metaluna.ck2edit.dataaccess.parser.Parser;
import io.github.metaluna.ck2edit.dataaccess.parser.ParserFactory;
import io.github.metaluna.ck2edit.support.FileLoader;
import java.nio.file.Path;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class OpinionModifierReaderTest {

  private static final String VALID_FILE = "valid_opinion_modifiers.txt";
  private OpinionModifierReader opinionModifierReader;

  @Before
  public void setUp() {
  }

  @Test(expected = NullPointerException.class)
  public void doesNotCreateWithoutFile() {
    opinionModifierReader = new OpinionModifierReader(null,
            new ParserFactory().fromFile(
                    fetchFile(VALID_FILE)
            )
    );
  }

  @Test(expected = NullPointerException.class)
  public void doesNotCreateWithoutParser() {
    opinionModifierReader = new OpinionModifierReader(fetchFile(VALID_FILE), null);
  }

  @Test
  public void readsValidFile() {
    opinionModifierReader = parse(VALID_FILE);
    
    OpinionModifierFile modifiersFile = opinionModifierReader.read();
    assertThat(modifiersFile, notNullValue());
    List<OpinionModifier> modifiers = modifiersFile.getOpinionModifiers();
    assertThat(modifiers, hasSize(2));
    
    OpinionModifier modifier1 = modifiers.get(0);
    assertThat(modifier1.getName(), is("opinion_minimal_features"));
    assertThat(modifier1.getOpinion(), is(100));
    
    OpinionModifier modifier2 = modifiers.get(1);
    assertThat(modifier2.getName(), is("opinion_all_features"));
    assertThat(modifier2.getOpinion(), is(-10));
    assertThat(modifier2.getDuration(), is(12));
    assertThat(modifier2.isBanishReason(), is(true));
    assertThat(modifier2.isCrime(), is(true));
    assertThat(modifier2.isDivorceReason(), is(true));
    assertThat(modifier2.isEnemy(), is(true));
    assertThat(modifier2.isExecuteReason(), is(true));
    assertThat(modifier2.isInherited(), is(true));
    assertThat(modifier2.isPrisonReason(), is(true));
    assertThat(modifier2.isRevokeReason(), is(true));
    
  }

  // ---vvv--- PRIVATE ---vvv---
  private Path fetchFile(String file) {
    return FileLoader.fetchFile("/reader/opinionmodifier", file);
  }

  private OpinionModifierReader parse(String file) {
    Path path = FileLoader.fetchFile("/reader/opinionmodifier", file);
    Parser parser = new ParserFactory().fromFile(path);
    return new OpinionModifierReader(path, parser);
  }
}
