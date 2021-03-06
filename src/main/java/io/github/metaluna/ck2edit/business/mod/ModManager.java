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
import io.github.metaluna.ck2edit.dataaccess.parser.ParserFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModManager {

  public Mod fromFile(Path modFile) {
    return new ModReader(modFile, parserFactory.fromFile(modFile), new OpinionModifierManager(parserFactory)).read();
  }
  
  public void saveFile(OpinionModifierFile omFile) {
    LOG.entry(omFile);
    new OpinionModifierManager(parserFactory).saveFile(omFile);
    LOG.exit();
  }

  public void deleteFile(ModFile modFile) {
    LOG.entry(modFile);
    try {
      Files.delete(modFile.getPath());
    } catch (IOException ex) {
      LOG.catching(ex);
    }
    LOG.exit();
  }
  
  // ---vvv--- PACKAGE-PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();
  
  @Inject
  ParserFactory parserFactory;
  
}
