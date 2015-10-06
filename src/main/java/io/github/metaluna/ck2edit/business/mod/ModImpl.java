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

import com.google.common.base.MoreObjects;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ModImpl implements Mod {

  @Override
  public String getName() {
    return this.name;
  }
  
  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getPath() {
    return path;
  }

  @Override
  public void setPath(String path) {
    this.path = path;
  }

  @Override
  public String getUserDir() {
    return userDir;
  }

  @Override
  public void setUserDir(String userDir) {
    this.userDir = userDir;
  }

  @Override
  public String getArchive() {
    return archive;
  }

  @Override
  public void setArchive(String archive) {
    this.archive = archive;
  }

  @Override
  public List<String> getReplacePaths() {
    return replacePaths;
  }

  @Override
  public void setReplacePaths(List<String> replacePaths) {
    this.replacePaths = replacePaths;
  }

  @Override
  public List<String> getDependencies() {
    return dependencies;
  }

  @Override
  public void setDependencies(List<String> dependencies) {
    this.dependencies = dependencies;
  }

  @Override
  public String getPicture() {
    return picture;
  }

  @Override
  public void setPicture(String picture) {
    this.picture = picture;
  }

  @Override
  public List<String> getTags() {
    return tags;
  }

  @Override
  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  @Override
  public void addReplacePath(String replacePath) {
    if (replacePath == null || replacePath.isEmpty()) {
      return;
    }
    if (this.replacePaths == null) {
      this.replacePaths = new ArrayList<>();
    }
    this.replacePaths.add(replacePath);
  }

  @Override
  public void addTag(String tag) {
    if (tag == null || tag.isEmpty()) {
      return;
    }
    if (this.tags == null) {
      this.tags = new ArrayList<>();
    }
    this.tags.add(tag);
  }

  @Override
  public void addDependency(String dependency) {
    if (dependency == null || dependency.isEmpty()) {
      return;
    }
    if (this.dependencies == null) {
      this.dependencies = new ArrayList<>();
    }
    this.dependencies.add(dependency);
  }

  @Override
  public Path getDescriptionFile() {
    return this.descriptionFile;
  }
  
  @Override
  public void setDescriptionFile(Path descriptionFile) {
    this.descriptionFile = descriptionFile;
  }

  @Override
  public List<Path> getOpinionModifiers() {
    if (this.opinionModifiers == null) {
      return Collections.emptyList();
    }
    return this.opinionModifiers;
  }

  @Override
  public void addOpinionModifier(Path file) {
    if (file == null) {
      return;
    }
    if (this.opinionModifiers == null) {
      this.opinionModifiers = new ArrayList<>();
    }
    this.opinionModifiers.add(file);
  }

  @Override
  public String toString() {
    MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this)
            .omitNullValues()
            .add("name", name)
            .add("path", path)
            .add("user_dir", userDir)
            .add("archive", archive)
            .add("replace_paths", replacePaths)
            .add("dependencies", dependencies)
            .add("picture", picture)
            .add("tags", tags)
            .add("description file", descriptionFile);
    
    int fileCount = opinionModifiers != null ? opinionModifiers.size() : 0;
    helper.add("opinion modifiers", fileCount);
    
    return helper.toString();
  }
  
  // ---vvv--- PRIVATE ---vvv---
  /** required */
  private String name;
  /** required */
  private String path;
  /** optional */
  private String userDir;
  /** optional */
  private String archive;
  /** optional, any number */
  private List<String> replacePaths;
  /** optional */
  private List<String> dependencies;
  /** optional, list */
  private String picture;
  /** optional, list */
  private List<String> tags;
  /** administrative */
  private Path descriptionFile;
  /** administrative */
  private List<Path> opinionModifiers;

}
