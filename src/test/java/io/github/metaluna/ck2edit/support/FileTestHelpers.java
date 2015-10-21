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
package io.github.metaluna.ck2edit.support;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import static org.junit.Assert.fail;

public class FileTestHelpers {

  /**
   * Fetches a file as a resource. Fails the test automatically if the file
   * was not found.
   * @param parent
   * @param file
   * @return the file
   */
  public static Path fetchFile(String... elements) {
    String resourcePath = File.separator + String.join(File.separator, elements);
    URL resourceUrl = FileTestHelpers.class.getResource(resourcePath);
    if (resourceUrl == null) {
      fail(String.format("Unable to find file '%s'. Resource does not exist.", resourcePath));
      return null;
    }

    Path path = null;
    try {
      path = Paths.get(resourceUrl.toURI());
    } catch (URISyntaxException ex) {
      fail(String.format("Unable to load file '%s': %s", resourceUrl.toString(), ex));
    }
    return path;
  }
  
  /**
   * Reads a file as a string. Fails the test automatically if the file was not
   * found or there was an error.
   * @param path the path to the file
   * @return the file as a string.
   */
  public static String readAsString(Path path) {
    String got = null;
    try {
      got = new String(Files.readAllBytes(path), Charset.forName("Windows-1252"));
    } catch (IOException ex) {
      ex.printStackTrace();
      fail();
    }
    return got;
  }  

  /**
   * Generates the path to a new file inside the temporary directory. This does
   * <strong>not</strong> create a new file.
   * 
   * @param prefix the text that will be put in front of the file name
   * @param extension the file type extension without the dot
   * @return the path to a new file
   */
  public static Path generateFileName(String prefix, String extension) {
    String fileName = prefix + "_" + System.currentTimeMillis() + "." + extension;
    Path gotFile = TEST_DIR.resolve(fileName);
    gotFile.toFile().deleteOnExit();
    return gotFile;
  }

  public static void setUpTestDirectory() throws IOException {
    Files.createDirectories(TEST_DIR);
  }

  public static void tearDownTestDirectory() {
    FileUtils.deleteQuietly(FileTestHelpers.TEST_DIR.toFile());
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Path TEST_DIR = Paths.get(System.getProperty("java.io.tmpdir"), "ck2edit-test");
  
  private FileTestHelpers() {
  }
}
