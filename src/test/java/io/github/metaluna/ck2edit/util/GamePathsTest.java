package io.github.metaluna.ck2edit.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class GamePathsTest {

  @Before
  public void setUp() {
  }

  @Test
  public void getsTheLinuxModDirectory() {
    Assume.assumeTrue(OsUtil.isUnix());
    Path expPath = Paths.get(System.getProperty("user.home"), ".paradoxinteractive", "Crusader Kings II", "mod");
    assertThat(GamePaths.getModDirectory(), is(expPath));
  }

  @Test
  public void getsTheLinuxGameDirectory() {
    Assume.assumeTrue(OsUtil.isUnix());
    Path expPath = Paths.get(System.getProperty("user.home"), ".local", "share", "Steam", "SteamApps", "common", "Crusader Kings II");
    assertThat(GamePaths.getGameDirectory(), is(expPath));
  }

  @Test
  public void getsTheWindowsModDirectory() {
    Assume.assumeTrue(OsUtil.isWindows());
    Path expPath = Paths.get(System.getProperty("user.home"), "My Documents", "Paradox Interactive", "Crusader Kings II", "mod");
    assertThat(GamePaths.getModDirectory(), is(expPath));
  }

  @Test
  public void getsTheWindowsGameDirectory() {
    Assume.assumeTrue(OsUtil.isWindows());
    assertNotNull(GamePaths.getGameDirectory());
  }

}
