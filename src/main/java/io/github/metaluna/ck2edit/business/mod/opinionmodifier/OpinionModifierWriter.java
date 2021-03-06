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

import io.github.metaluna.ck2edit.business.mod.ModFileWriter;
import io.github.metaluna.ck2edit.dataaccess.parser.Node;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Persists an {@link OpinionModifierFile} to disk.
 */
class OpinionModifierWriter extends ModFileWriter {

  // ---vvv--- PROTECTED ---vvv---
  @Override
  protected Node print() {
    LOG.entry();
    final Node result = Node.createRoot();

    for (OpinionModifier modifier : this.opinionModifierFile.getOpinionModifiers()) {
      final Node n = Node.create(modifier.getName());
      addSimpleValue(n, "opinion", modifier.getOpinion());
      addSimpleValue(n, "months", modifier.getDuration());
      addSimpleValue(n, "prison_reason", modifier.isPrisonReason());
      addSimpleValue(n, "banish_reason", modifier.isBanishReason());
      addSimpleValue(n, "execute_reason", modifier.isExecuteReason());
      addSimpleValue(n, "revoke_reason", modifier.isRevokeReason());
      addSimpleValue(n, "divorce_reason", modifier.isDivorceReason());
      addSimpleValue(n, "inherit", modifier.isInherited());
      addSimpleValue(n, "enemy", modifier.isEnemy());
      addSimpleValue(n, "crime", modifier.isCrime());
      result.addChild(n);
    }
    
    return LOG.exit(result);
  }

  @Override
  protected void validate() {
    if (opinionModifierFile.getOpinionModifiers().isEmpty()) {
      throw new IllegalStateException(
              String.format(
                      "Cannot save opinion modifier file '%s': no opinion modifiers defined.", 
                      opinionModifierFile.getName()));
    }
  }
  
  // ---vvv--- PACKAGE-PRIVATE ---vvv---
  OpinionModifierWriter(OpinionModifierFile file) {
    super(file.getPath());
    LOG.entry(file);
    this.opinionModifierFile = Objects.requireNonNull(file);
    LOG.exit();
  }
  
  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();

  private final OpinionModifierFile opinionModifierFile;

  
}
