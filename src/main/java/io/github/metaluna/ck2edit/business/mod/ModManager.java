package io.github.metaluna.ck2edit.business.mod;

import io.github.metaluna.ck2edit.dataaccess.parser.ParserFactory;
import java.io.File;
import javax.inject.Inject;

public class ModManager {

  public Mod fromFile(File modFile) {
    return new ModReader(modFile, parserFactory.fromFile(modFile.toPath())).read();
  }
  
  // ---vvv--- PACKAGE-PRIVATE ---vvv---
  @Inject
  ParserFactory parserFactory;
  
}
