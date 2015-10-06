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
package io.github.metaluna.ck2edit.gui.mod;

import io.github.metaluna.ck2edit.business.mod.ModFile;
import java.nio.file.Path;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class ModTreeCell extends TreeCell<Object> {

  public ModTreeCell(Consumer<ModFile> openHandler) {
    this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
      if (event.getClickCount() == 2) {
        open(openHandler);
      }
    });

  }

  @Override
  protected void updateItem(Object item, boolean empty) {
    super.updateItem(item, empty);
    LOG.entry(item, empty);
    setText(item == null ? "" : item.toString());
    LOG.exit();
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();

  private void open(Consumer<ModFile> openHandler) {
    LOG.entry(openHandler);
    if (this.getTreeItem() != null && this.getTreeItem().getValue() instanceof ModFile) {
      openHandler.accept((ModFile) this.getTreeItem().getValue());
    }
    LOG.exit();
  }
}
