package io.github.metaluna.ck2edit.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class GamePaths {

  static {
    LOG = LogManager.getFormatterLogger();
    STEAM_APP_ID = "203770";

    switch (OsUtil.getCurrentOS()) {
      case UNIX:
        MOD_DIR = getLinuxModDir();
        GAME_DIR = getLinuxGameDir();
        break;
      case WINDOWS:
        MOD_DIR = getWindowsModDir();
        GAME_DIR = getWindowsGameDir();
        break;
      case MAC:
        MOD_DIR = getMacModDir();
        GAME_DIR = getMacGameDir();
        break;
      default:
        MOD_DIR = null;
        GAME_DIR = null;
    }
  }

  public static Path getModDirectory() {
    return MOD_DIR;
  }

  public static Path getGameDirectory() {
    return GAME_DIR;
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG;
  private static final Path MOD_DIR;
  private static final Path GAME_DIR;
  private static final String STEAM_APP_ID;

  /**
   * Hide constructor to prevent instantiation
   */
  private GamePaths() {
  }

  private static Path getLinuxModDir() {
    return Paths.get(System.getProperty("user.home"), ".paradoxinteractive", "Crusader Kings II", "mod");
  }

  private static Path getLinuxGameDir() {
    return Paths.get(System.getProperty("user.home"), ".local", "share", "Steam", "SteamApps", "common", "Crusader Kings II");
  }

  private static Path getWindowsModDir() {
    final String myDocumentsA = locateDocumentsWindowsA();

    final Path result;
    if (!myDocumentsA.isEmpty() && Paths.get(myDocumentsA).toFile().exists()) {
      result = Paths.get(myDocumentsA, "Paradox Interactive", "Crusader Kings II", "mod");
    } else {
      final String myDocumentsB = locateDocumentsWindowsB();
      if (!myDocumentsB.isEmpty() && Paths.get(myDocumentsB).toFile().exists()) {
        result = Paths.get(myDocumentsB, "Paradox Interactive", "Crusader Kings II", "mod");
      } else {
        result = Paths.get(System.getProperty("user.home"));
      }
    }

    return result;
  }

  private static Path getWindowsGameDir() {
    final String gameDir = OsUtil.readRegistryKey("HKCU\\Software\\Valve\\Steam\\Apps\\" + STEAM_APP_ID);
    final Path result;
    if (!gameDir.isEmpty() && Paths.get(gameDir).toFile().exists()) {
      result = Paths.get(gameDir);
    } else {
      result = null;
    }
    return result;
  }

  private static String locateDocumentsWindowsA() {
    return OsUtil.readRegistryKey("HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders");
  }

  private static String locateDocumentsWindowsB() {
    return System.getProperty("user.home") + File.pathSeparator + "My Documents";
  }

  private static Path getMacModDir() {
    return Paths.get(System.getProperty("user.home"), "Documents");
  }

  private static Path getMacGameDir() {
    return null;
  }

}
