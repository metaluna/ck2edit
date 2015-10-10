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
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
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
    this.modTreeView.setCellFactory(treeView -> new ModTreeCell(this::onModFileOpen));
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
    if (this.currentFile instanceof OpinionModifierFile) {
      modFactory.saveFile((OpinionModifierFile) this.currentFile);
    }
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

  private Optional<Mod> currentMod;
  private ModFile currentFile;
  private String baseTitle;

  private void onModFileOpen(ModFile modFile) {
    LOG.entry(modFile);
    this.currentFile = modFile;
    OpinionModifiersView view = new OpinionModifiersView();
    OpinionModifiersPresenter presenter = (OpinionModifiersPresenter) view.getPresenter();
    presenter.load((OpinionModifierFile) modFile);
    this.modOpenFilesPane.setCenter(view.getView());
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
}
