package io.github.metaluna.ck2edit.util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class JarPath {

  public static File getPath() {
    URL url = methodA();
    try {
      return new File(url.toURI()).getParentFile();
    } catch (URISyntaxException ex) {
      LOG.error("Failed to retrieve URL with method 1. URL: " + url, ex);

      url = methodB();
      try {
        return new File(url.toURI()).getParentFile();
      } catch (URISyntaxException ex1) {
        LOG.error("Failed to retrieve URL with method 2. URL: " + url, ex1);
        return null;
      }
    }
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();

  private JarPath() {
  }

  private static URL methodA() {
    return JarPath.class.getProtectionDomain().getCodeSource().getLocation();
  }

  private static URL methodB() {
    return JarPath.class.getResource(JarPath.class.getSimpleName() + ".class");
  }
}
