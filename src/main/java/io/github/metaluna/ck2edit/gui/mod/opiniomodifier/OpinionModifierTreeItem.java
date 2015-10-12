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
package io.github.metaluna.ck2edit.gui.mod.opiniomodifier;

import io.github.metaluna.ck2edit.business.mod.opinionmodifier.OpinionModifierFile;
import io.github.metaluna.ck2edit.gui.mod.ModFileTreeItem;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OpinionModifierTreeItem extends ModFileTreeItem {

  public OpinionModifierTreeItem(OpinionModifierFile omFile) {
    super(omFile);
    LOG.entry(omFile);
    this.omFile = Objects.requireNonNull(omFile);
    LOG.exit();
  }

  @Override
  public OpinionModifierFile getFile() {
    LOG.entry();
    return LOG.exit(this.omFile);
  }

  @Override
  public OpinionModifiersView createView() {
    LOG.entry();
    OpinionModifiersView result = new OpinionModifiersView();
    OpinionModifiersPresenter presenter = result.getPresenter();
    presenter.load(this.omFile);
    return LOG.exit(result);
  }

  @Override
  public String toString() {
    return "OpinionModifierTreeItem{" + "omFile=" + omFile + '}';
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();
  
  private final OpinionModifierFile omFile;
}
