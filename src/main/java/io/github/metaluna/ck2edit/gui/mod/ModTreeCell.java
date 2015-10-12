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

import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class ModTreeCell extends TreeCell<Object> {

  public ModTreeCell(Consumer<ModFileTreeItem> openHandler, Consumer<ModFileTreeItem> deleteHandler
  , Consumer<CategoryTreeItem> addHandler, ResourceBundle resources) {
    LOG.entry(openHandler, deleteHandler, addHandler);
    this.openHandler = Objects.requireNonNull(openHandler);
    this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
      if (event.getClickCount() == 2) {
        open();
      }
    });
    this.deleteHandler = Objects.requireNonNull(deleteHandler);
    this.addHandler = Objects.requireNonNull(addHandler);
    this.resources = Objects.requireNonNull(resources);
    LOG.exit();
  }

  @Override
  protected void updateItem(Object item, boolean empty) {
    LOG.entry(item, empty);
    super.updateItem(item, empty);
    if (empty || item == null) {
      setText(null);
      setGraphic(null);
    } else {
      if (isFile(this.getTreeItem())) {
        setContextMenu(createContextMenu());
      } else if (isCategory(this.getTreeItem())) {
        setContextMenu(createCategoryContextMenu());
      }
      setText(item.toString());
    }
    LOG.exit();
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();
  private final ResourceBundle resources;
  private final Consumer<ModFileTreeItem> openHandler;
  private final Consumer<ModFileTreeItem> deleteHandler;
  private final Consumer<CategoryTreeItem> addHandler;
  
  private void open() {
    LOG.entry();
    if (isFile(this.getTreeItem())) {
      this.openHandler.accept((ModFileTreeItem) this.getTreeItem());
    }
    LOG.exit();
  }

  private boolean isFile(TreeItem treeItem) {
    LOG.entry(treeItem);
    boolean result = treeItem instanceof ModFileTreeItem;
    return LOG.exit(result);
  }

  private boolean isCategory(TreeItem treeItem) {
    LOG.entry(treeItem);
    boolean result = treeItem instanceof CategoryTreeItem;
    return LOG.exit(result);
  }

  private ContextMenu createContextMenu() {
    LOG.entry();
    final ContextMenu result = new ContextMenu();

    MenuItem openMenuItem = new MenuItem(resources.getString("contextMenuOpen"));
    openMenuItem.setOnAction(e -> open());
    result.getItems().add(openMenuItem);
    
    MenuItem deleteMenuItem = new MenuItem(resources.getString("contextMenuDelete"));
    deleteMenuItem.setOnAction(e -> deleteHandler.accept((ModFileTreeItem) this.getTreeItem()));
    result.getItems().add(deleteMenuItem);
    
    return LOG.exit(result);
  }

  private ContextMenu createCategoryContextMenu() {
    LOG.entry();
    final ContextMenu result = new ContextMenu();

    MenuItem addMenuItem = new MenuItem(resources.getString("contextMenuNewFile"));
    addMenuItem.setOnAction(e -> addHandler.accept((CategoryTreeItem) this.getTreeItem()));
    result.getItems().add(addMenuItem);
    
    return LOG.exit(result);
  }

}
