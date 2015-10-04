package io.github.metaluna.ck2edit.business.mod;

import java.util.ArrayList;
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

}
