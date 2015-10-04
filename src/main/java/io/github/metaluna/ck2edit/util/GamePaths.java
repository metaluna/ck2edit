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
