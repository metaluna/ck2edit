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
