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
