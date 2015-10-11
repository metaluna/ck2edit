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
import io.github.metaluna.ck2edit.gui.mod.opiniomodifier.OpinionModifiersPresenter;
import io.github.metaluna.ck2edit.gui.mod.opiniomodifier.OpinionModifiersView;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SplitPane;
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
    this.modTreeView.setCellFactory(treeView -> new ModTreeCell(this::onModFileOpen, this::onModFileDelete));
    // defined here because tree cells don't receive any key events
    this.modTreeView.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
      if (event.getCode() == KeyCode.ENTER) {
        TreeItem<Object> selected = this.modTreeView.getSelectionModel().getSelectedItem();
        if (selected.getValue() instanceof ModFile) {
          ModFile modFile = (ModFile) selected.getValue();
          onModFileOpen(modFile);
        }
      }
    });
  }

  public void load(Path modFile) {
    Mod mod;
    LOG.info("Loading mod file '%s'...", modFile.toString());
    mod = modFactory.fromFile(modFile);
    this.currentMod = Optional.of(mod);
    setWindowTitle(mod.getName());
    loadTreeFromMod(mod);
  }

  public void saveFile() {
    LOG.entry();
    this.currentFile
            .filter(f -> f instanceof OpinionModifierFile)
            .ifPresent(f -> modFactory.saveFile((OpinionModifierFile) f));
    LOG.exit();
  }

  public void saveAllFiles() {
    LOG.entry();
    throw new UnsupportedOperationException("Not supported yet.");
//    LOG.exit();
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();

  @Inject
  private ModManager modFactory;

  @FXML
  private SplitPane centerSplitPane;
  @FXML
  private TreeView<Object> modTreeView;
  @FXML
  private BorderPane modOpenFilesPane;

  private Optional<Mod> currentMod = Optional.empty();
  private Optional<ModFile> currentFile = Optional.empty();
  private String baseTitle;

  private void onModFileOpen(ModFile modFile) {
    LOG.entry(modFile);
    this.currentFile = Optional.of(modFile);
    OpinionModifiersView view = new OpinionModifiersView();
    OpinionModifiersPresenter presenter = (OpinionModifiersPresenter) view.getPresenter();
    presenter.load((OpinionModifierFile) modFile);
    this.modOpenFilesPane.setCenter(view.getView());
    LOG.exit();
  }

  private void onModFileDelete(ModFile modFile) {
    LOG.entry(modFile);
    if (showConfirmationDialog(modFile)) {
      deleteFile(modFile);
      removeFileFromTree(this.modTreeView.getRoot(), modFile);
      closeOpenFile(modFile);
      this.currentFile = Optional.empty();
    }
    LOG.exit();
  }

  private void loadTreeFromMod(Mod mod) {
    final TreeItem<Object> root = new TreeItem<>(mod.getName());

    List<ModFile> opinionModifiers = mod.getOpinionModifiers();
    if (!opinionModifiers.isEmpty()) {
      TreeItem<Object> opinionModifierRoot = new TreeItem<>("Opinion Modifiers");
      opinionModifierRoot.setExpanded(true);
      opinionModifierRoot.getChildren().addAll(opinionModifiers.stream()
              .map(m -> new TreeItem<Object>(m))
              .collect(Collectors.toList())
      );
      root.getChildren().add(opinionModifierRoot);
    }
    this.modTreeView.setRoot(root);
    root.setExpanded(true);
  }

  private void setWindowTitle(String name) {
    Stage stage;
    try {
      stage = getStage().get();
    } catch (Exception e) {
      LOG.catching(e);
      return;
    }

    if (baseTitle == null) {
      baseTitle = stage.getTitle();
    }
    stage.setTitle(name + " | " + baseTitle);
  }

  private Optional<Stage> getStage() {
    return Optional.ofNullable((Stage) centerSplitPane.getScene().getWindow());
  }

  private boolean showConfirmationDialog(ModFile modFile) {
    LOG.entry(modFile);
    boolean result;
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setHeaderText(String.format("Deleting the file %s", modFile));
    alert.setContentText("Do you really want to delete this file? This cannot be undone.");
    Optional<ButtonType> answer = alert.showAndWait();
    if (answer.isPresent() && answer.get() == ButtonType.OK) {
      result = true;
    } else {
      result = false;
    }
    return LOG.exit(result);
  }

  private void deleteFile(ModFile modFile) {
    LOG.entry(modFile);
    try {
      Files.delete(modFile.getPath());
    } catch (IOException ex) {
      LOG.catching(ex);
    }
    LOG.exit();
  }

  private boolean removeFileFromTree(TreeItem<Object> node, ModFile modFile) {
    LOG.entry(node, modFile);
    // only check leaves
    if (node.isLeaf()) {
      // found it!
      if (node.getValue() == modFile) {
        node.getParent().getChildren().remove(node);
        return LOG.exit(true);
      } else {
        return LOG.exit(false);
      }
    } else {
      // check all children
      for (TreeItem<Object> child : node.getChildren()) {
        boolean isFound = removeFileFromTree(child, modFile);
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
}
