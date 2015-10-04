package io.github.metaluna.ck2edit.business.mod;

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
}
