package io.github.metaluna.ck2edit.util;

import java.io.File;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

public final class LoggingUtil {

  static {
    // disable until logging is initialized in order to prevent a message
    // saying that logger initialisation failed
    disableStatusMessages();
  }

  public static void configureLogging() {
    configureLogging(new File(JarPath.getPath(), "log4j2.xml"));
  }
  
  public static void configureLogging(String pathToLoggingConfig) {
    configureLogging(new File(pathToLoggingConfig));
  }
  
  public static void configureLogging(File loggerConfig) {
    if (loggerConfig.exists()) {
      LoggerContext context = (LoggerContext) LogManager.getContext(false);
      context.setConfigLocation(loggerConfig.toURI());
      enableStatusMessages();
    } else {
      try {
        System.err.println(String.format("Failed to load logging config from '%s'", loggerConfig.getCanonicalPath()));
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }

  // ---vvv--- PRIVATE ---vvv---
  /**
   * Hide constructor in order to prevent instantiation.
   */
  private LoggingUtil() {
  }

  private static void disableStatusMessages() {
    toggleStatusMessages(false);
  }
  
  private static void enableStatusMessages() {
    toggleStatusMessages(true);
  }
  
  private static void toggleStatusMessages(boolean enable) {
    System.setProperty("org.apache.logging.log4j.simplelog.StatusLogger.level", enable ? "ALL" : "OFF");
  }
  
}
