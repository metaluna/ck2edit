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

import com.airhacks.afterburner.views.FXMLView;
import io.github.metaluna.ck2edit.business.mod.ModFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

/**
 * Encapsulates a {@link ModFile} and offers methods to generate a view.
 */
public abstract class ModFileTreeItem extends TreeItem<Object> {
  
  /**
   * Minimal constructor needed to initialize the tree item event listeners.
   * @param modFile the mod file inside this tree item
   */
  public ModFileTreeItem(ModFile modFile) {
    super(modFile);
  }
  
  /**
   * @return a new FXMLView
   */
  public abstract FXMLView createView();
  
  /**
   * @return the file contained in this tree item
   */
  public abstract ModFile getFile();

  @Override
  public ObservableList<TreeItem<Object>> getChildren() {
    return FXCollections.emptyObservableList();
  }

  @Override
  public boolean isLeaf() {
    return true;
  }
  
  
}
