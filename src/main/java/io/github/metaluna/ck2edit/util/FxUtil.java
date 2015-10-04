package io.github.metaluna.ck2edit.util;

import com.sun.javafx.css.StyleManager;
import javafx.application.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class FxUtil {

  /**
   * Loads a CSS file applied to all stages. This needs to called AFTER the 
   * first call to stage.show().
   *
   * @param pathToStylesheet the path to the stylesheet
   */
  public static void loadGlobalStylesheet(String pathToStylesheet) {
    LOG.debug("Loading global stylesheet from %s...", pathToStylesheet);
    Application.setUserAgentStylesheet(null);
    StyleManager.getInstance().addUserAgentStylesheet(pathToStylesheet);
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();
  /**
   * Hide constructor in order to prevent instantiation.
   */
  private FxUtil() {
  }
}
