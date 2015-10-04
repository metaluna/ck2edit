package io.github.metaluna.ck2edit.gui.mod;

import io.github.metaluna.ck2edit.business.mod.ModManager;
import io.github.metaluna.ck2edit.business.mod.Mod;
import java.io.File;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModPresenter {

  public void initialize() {

  }

  public void load(File modFile) {
    Mod mod;
    LOG.info("Loading mod file '%s'...", modFile.toString());
    mod = modFactory.fromFile(modFile);
    this.currentMod = Optional.of(mod);
    setWindowTitle(mod.getName());
    loadTreeFromMod(mod);
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
  private AnchorPane modOpenFilesPane;

  private Optional<Mod> currentMod;
  private String baseTitle;

  private void loadTreeFromMod(Mod mod) {
    this.modTreeView.setRoot(new TreeItem<>(mod.getName()));
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
