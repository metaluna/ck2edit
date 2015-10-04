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
