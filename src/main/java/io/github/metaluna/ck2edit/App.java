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
package io.github.metaluna.ck2edit;

import io.github.metaluna.ck2edit.gui.main.MainView;
import io.github.metaluna.ck2edit.util.FxUtil;
import io.github.metaluna.ck2edit.util.LoggingUtil;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App extends Application {

  public App() {
    LoggingUtil.configureLogging();
    LOG = LogManager.getFormatterLogger();
  }

  @Override
  public void start(Stage stage) throws Exception {

    LOG.debug("Loading main view...");
    MainView mainView = new MainView();
    Scene scene = new Scene(mainView.getView());
    
    stage.setTitle(WINDOW_TITLE);
    stage.setScene(scene);
    LOG.debug("Showing window...");
    stage.show();
    FxUtil.loadGlobalStylesheet(DEFAULT_STYLES);
  }

  /**
   * The main() method is ignored in correctly deployed JavaFX application.
   * main() serves only as fallback in case the application can not be launched
   * through deployment artifacts, e.g., in IDEs with limited FX support.
   * NetBeans ignores main().
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    Application.launch(args);
  }

  // ---vvv--- PRIVATE ---vvv---
  private final Logger LOG;

  private static final String DEFAULT_STYLES = "io/github/metaluna/ck2edit/app.css";
  private static final String WINDOW_TITLE = "Crusader Kings II Editor";
  
}
