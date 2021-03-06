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

import io.github.metaluna.ck2edit.business.mod.ModManager;
import io.github.metaluna.ck2edit.business.mod.Mod;
import io.github.metaluna.ck2edit.business.mod.ModFile;
import io.github.metaluna.ck2edit.business.mod.opinionmodifier.OpinionModifierFile;
import io.github.metaluna.ck2edit.gui.mod.opinionmodifier.OpinionModifierTreeItem;
import io.github.metaluna.ck2edit.util.GamePaths;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModPresenter {

  public void initialize() {
    this.modTreeView.setCellFactory(treeView -> new ModTreeCell(this::onModFileOpen, 
            this::onModFileDelete, this::onModFileAdd, this.resources));
    // defined here because tree cells don't receive any key events
    this.modTreeView.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
      if (event.getCode() == KeyCode.ENTER) {
        LOG.trace("ENTER key pressed");
        TreeItem<Object> selected = this.modTreeView.getSelectionModel().getSelectedItem();
        if (selected instanceof ModFileTreeItem) {
          onModFileOpen((ModFileTreeItem) selected);
        }
      }
    });
  }

  public void load(Path modFile) {
    Mod mod;
    LOG.info("Loading mod file '%s'...", modFile.toString());
    mod = modManager.fromFile(modFile);
    this.currentMod = Optional.of(mod);
    setWindowTitle(mod.getName());
    loadTreeFromMod(mod);
  }

  public void saveFile() {
    LOG.entry();
    this.currentFile
            .filter(f -> f instanceof OpinionModifierFile)
            .ifPresent(f -> modManager.saveFile((OpinionModifierFile) f));
    LOG.exit();
  }

  public void saveAllFiles() {
    LOG.entry();
    throw new UnsupportedOperationException("Not supported yet.");
//    LOG.exit();
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();
  private static final String CAT_OPINION_MODIFIERS = "Opinion Modifiers";

  @Inject
  private ModManager modManager;

  @FXML
  private ResourceBundle resources;
  @FXML
  private SplitPane centerSplitPane;
  @FXML
  private TreeView<Object> modTreeView;
  @FXML
  private BorderPane modOpenFilesPane;

  private Optional<Mod> currentMod = Optional.empty();
  private Optional<ModFile> currentFile = Optional.empty();
  private String baseTitle;

  private void onModFileOpen(ModFileTreeItem modFileItem) {
    LOG.entry(modFileItem);
    this.currentFile = Optional.of(modFileItem.getFile());
    this.modOpenFilesPane.setCenter(modFileItem.createView().getView());
    LOG.exit();
  }

  private void onModFileDelete(ModFileTreeItem modFileItem) {
    LOG.entry(modFileItem);
    ModFile modFile = modFileItem.getFile();
    if (showConfirmationDialog(modFile)) {
      removeFileFromTree(this.modTreeView.getRoot(), modFileItem);
      closeOpenFile(modFile);
      this.currentFile = Optional.empty();
      modManager.deleteFile(modFile);
    }
    LOG.exit();
  }

  private void onModFileAdd(CategoryTreeItem categoryItem) {
    LOG.entry(categoryItem);
    if (categoryItem.getValue().equals(CAT_OPINION_MODIFIERS)) {
      Optional<String> name = showFileNameDialog();
      if (name.isPresent() && !name.get().isEmpty()) {
        OpinionModifierFile modFile = addFileToMod(name.get(), this.currentMod.get());
        addFileToTree(categoryItem, modFile);
      }
    }
    LOG.exit();
  }
  
  private void loadTreeFromMod(Mod mod) {
    final TreeItem<Object> root = new TreeItem<>(mod.getName());

    List<ModFile> opinionModifiers = mod.getOpinionModifiers();
    if (!opinionModifiers.isEmpty()) {
      CategoryTreeItem opinionModifierRoot = new CategoryTreeItem(CAT_OPINION_MODIFIERS);
      opinionModifierRoot.setExpanded(true);
      opinionModifierRoot.getChildren().addAll(opinionModifiers.stream()
              .map(m -> new OpinionModifierTreeItem((OpinionModifierFile) m))
              .collect(Collectors.toList())
      );
      root.getChildren().add(opinionModifierRoot);
    }
    this.modTreeView.setRoot(root);
    root.setExpanded(true);
  }

  private void setWindowTitle(String name) {
    Stage stage;
    if (!getStage().isPresent()) {
      return;
    }
    stage = getStage().get();

    if (baseTitle == null) {
      baseTitle = stage.getTitle();
    }
    stage.setTitle(name + " | " + baseTitle);
  }

  private Optional<Stage> getStage() {
    Optional<Stage> result;
    try {
      result = Optional.ofNullable((Stage) centerSplitPane.getScene().getWindow());
    } catch (Exception e) {
      result = Optional.empty();
    }
    return result;
  }

  private boolean showConfirmationDialog(ModFile modFile) {
    LOG.entry(modFile);
    boolean result;
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setHeaderText(String.format(resources.getString("dialogDeleteFileHeader"), modFile));
    alert.setContentText(resources.getString("dialogDeleteFileContent"));
    Optional<ButtonType> answer = alert.showAndWait();
    result = answer.isPresent() && answer.get() == ButtonType.OK;
    return LOG.exit(result);
  }

  private boolean removeFileFromTree(TreeItem<Object> node, ModFileTreeItem modFileItem) {
    LOG.entry(node, modFileItem);
    // only check leaves
    if (node.isLeaf()) {
      // found it!
      if (node == modFileItem) {
        node.getParent().getChildren().remove(node);
        return LOG.exit(true);
      } else {
        return LOG.exit(false);
      }
    } else {
      // check all children
      for (TreeItem<Object> child : node.getChildren()) {
        boolean isFound = removeFileFromTree(child, modFileItem);
        if (isFound) {
          return LOG.exit(true);
        }
      }
      // nothing found in this branch
      return LOG.exit(false);
    }
  }

  private void closeOpenFile(ModFile modFile) {
    LOG.entry(modFile);
    this.currentFile
            .filter(f -> f == modFile)
            .ifPresent(f -> {
              LOG.trace("Closing view of opened mod file '%s'", f);
              this.modOpenFilesPane.setCenter(null);
            });
    LOG.exit();
  }

  private Optional<String> showFileNameDialog() {
   LOG.entry();
   TextInputDialog dialog = new TextInputDialog();
   dialog.setHeaderText(resources.getString("dialogAddFileHeader"));
   dialog.setContentText(resources.getString("dialogAddFileContent"));
   Optional<String> result = dialog.showAndWait();
   return LOG.exit(result);
  }

  private OpinionModifierFile addFileToMod(String name, Mod mod) {
    LOG.entry(name, mod);
    String modPath = mod.getPath().replace("\"", "");
    String categoryPath = "common" + File.separator + "opinion_modifiers";
    Path path = GamePaths.getModDirectory().getParent()
            .resolve(modPath).resolve(categoryPath).resolve(name);
    OpinionModifierFile result = new OpinionModifierFile(path);
    mod.addOpinionModifier(result);
    return LOG.exit(result);
  }

  private void addFileToTree(TreeItem<Object> parent, OpinionModifierFile modFile) {
    LOG.entry(parent, modFile);
    parent.getChildren().add(new OpinionModifierTreeItem(modFile));
    LOG.exit();
  }
}
