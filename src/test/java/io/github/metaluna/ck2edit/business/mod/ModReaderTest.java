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
package io.github.metaluna.ck2edit.business.mod;

import io.github.metaluna.ck2edit.business.mod.opinionmodifier.OpinionModifierFile;
import io.github.metaluna.ck2edit.business.mod.opinionmodifier.OpinionModifierManager;
import io.github.metaluna.ck2edit.dataaccess.parser.Parser;
import io.github.metaluna.ck2edit.dataaccess.parser.ParserFactory;
import io.github.metaluna.ck2edit.support.FileLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ModReaderTest {

  private ModReader modReader;

  @Before
  public void setUp() {
  }

  @Test
  public void readsDescriptionFile() {
    modReader = parse("complete.mod");
    Mod mod = modReader.read();
    assertThat(mod.getName(), is("\"Complete Mod\""));
    assertThat(mod.getPath(), is("\"mod/completemod\""));
    assertThat(mod.getUserDir(), is("\"completemod\""));
    assertThat(mod.getPicture(), is("\"completemod.tga\""));
    // parallel processing -> don't rely on specific order -> "contains" won't work
    assertThat(mod.getReplacePaths(), hasItem("\"events\""));
    assertThat(mod.getReplacePaths(), hasItem("\"music\""));
    assertThat(mod.getTags(), hasItem("\"Tag 1\""));
    assertThat(mod.getTags(), hasItem("\"Tag 2\""));
    assertThat(mod.getDependencies(), hasItem("\"Parent Mod 1\""));
    assertThat(mod.getDependencies(), hasItem("\"Parent Mod 2\""));
  }
  
  @Test
  public void readsOpinionModifiers() {
    modReader = parse("demo.mod");
    Mod mod = modReader.read();
    
    List<ModFile> files = mod.getOpinionModifiers();
    assertThat(files, notNullValue());
    assertThat(files.get(0).getName(), is("demo_opinion_modifiers.txt"));
  }

  // ---vvv--- PRIVATE ---vvv---
  private ModReader parse(String file) {
    Path path = FileLoader.fetchFile("/reader/mod", file);
    Parser parser = new ParserFactory().fromFile(path);
    OpinionModifierManager omManager = mock(OpinionModifierManager.class);
    OpinionModifierFile omFile = new OpinionModifierFile(Paths.get("demo_opinion_modifiers.txt"));
    when(omManager.fromFile(any())).thenReturn(omFile);    
    return new ModReader(path, parser, omManager);
  }


}
