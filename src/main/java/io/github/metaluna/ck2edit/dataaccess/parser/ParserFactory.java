package io.github.metaluna.ck2edit.dataaccess.parser;

import java.nio.file.Path;

public class ParserFactory {

  public Parser fromFile(Path path) {
    return new Parser(path, new MultiValueParser());
  }

}
