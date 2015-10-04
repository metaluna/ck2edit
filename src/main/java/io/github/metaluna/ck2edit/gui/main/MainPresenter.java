package io.github.metaluna.ck2edit.gui.main;

import io.github.metaluna.ck2edit.gui.mod.ModPresenter;
import io.github.metaluna.ck2edit.gui.mod.ModView;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainPresenter {
  
  public void initialize() {
    this.modView = new ModView();
    this.root.setCenter(modView.getView());
    ((ModPresenter) this.modView.getPresenter()).load(previousMod);
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();

  @FXML
  private BorderPane root;
  
  private File previousMod = new File("/home/monsi/Projekte/mods/crusaderkings2/better gender law mod/descriptor.mod");
  private ModView modView;

  @FXML
  private void onOpenFile(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open File");
    fileChooser.getExtensionFilters().addAll(
            new ExtensionFilter("Crusader King II Mod", "*.mod"),
            new ExtensionFilter("All Files", "*.*"));
    if (this.previousMod != null) {
      fileChooser.setInitialDirectory(this.previousMod.getParentFile());
    }
    
    Window window = root.getScene().getWindow();
    File selectedFile = fileChooser.showOpenDialog(window);
    
    if (selectedFile != null) {
      this.previousMod = selectedFile;
      ((ModPresenter) this.modView.getPresenter()).load(selectedFile);
    }
  }
}
