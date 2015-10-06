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
package io.github.metaluna.ck2edit.business.mod;

import java.nio.file.Path;
import java.util.List;

public interface Mod {

  String getArchive();
  List<String> getDependencies();
  String getName();
  String getPath();
  String getPicture();
  List<String> getReplacePaths();
  List<String> getTags();
  String getUserDir();
  Path getDescriptionFile();
  List<ModFile> getOpinionModifiers();
  void setArchive(String archive);
  void setDependencies(List<String> dependencies);
  void setName(String name);
  void setPath(String path);
  void setPicture(String picture);
  void setReplacePaths(List<String> replacePaths);
  void setTags(List<String> tags);
  void setUserDir(String userDir);
  void addReplacePath(String replacePath);
  void addTag(String tag);
  void addDependency(String dependency);
  void setDescriptionFile(Path descriptionFile);
  void addOpinionModifier(ModFile file);
}
