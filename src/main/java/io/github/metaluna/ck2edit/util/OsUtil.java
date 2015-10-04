package io.github.metaluna.ck2edit.util;

import java.io.IOException;
import java.io.InputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class OsUtil {

  public enum OS {
    UNIX, WINDOWS, MAC, OTHER
  }

  public static OS getCurrentOS() {
    if (isWindows()) {
      return OS.WINDOWS;
    } else if (isMac()) {
      return OS.MAC;
    } else if (isUnix()) {
      return OS.UNIX;
    } else {
      return OS.OTHER;
    }
  }

  public static boolean isWindows() {
    return (OS_NAME.contains("win"));
  }

  public static boolean isMac() {
    return (OS_NAME.contains("mac"));
  }

  public static boolean isUnix() {
    return (OS_NAME.contains("nix") || OS_NAME.contains("nux") || OS_NAME.contains("aix"));
  }

  public static String readRegistryKey(String key) {
    try {
      Process p = Runtime.getRuntime().exec(String.format("reg query \"%s\" /v personal", key));
      p.waitFor();

      InputStream in = p.getInputStream();
      byte[] b = new byte[in.available()];
      in.read(b);
      in.close();

      return new String(b).split("\\s\\s+")[4];
    } catch (IOException | InterruptedException ex) {
      LOG.error(ex);
    }

    return "";
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();
  private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

  /**
   * Hide constructor to prevent instantiation.
   */
  private OsUtil() {

  }
}
