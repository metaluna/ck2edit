package io.github.metaluna.ck2edit.business.mod;

import io.github.metaluna.ck2edit.dataaccess.parser.Node;
import io.github.metaluna.ck2edit.dataaccess.parser.Parser;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Reads mod description files (*.mod)
 */
class ModReader {

  /**
   * Reads a complete mod from a file on disk.
   *
   * @param modFile the mod file to read
   * @return the parsed mod
   */
  public Mod read() {
    LOG.entry();
    Objects.requireNonNull(modFile);
    Mod result = parse();
    return LOG.exit(result);
  }

  // ---vvv--- PACKAGE-PRIVATE ---vvv---
  ModReader(File modFile, Parser parser) {
    LOG.entry(modFile, parser);
    this.modFile = Objects.requireNonNull(modFile);
    this.parser = Objects.requireNonNull(parser);
    this.initializeAttributeMap();
    LOG.exit();
  }

  private void initializeAttributeMap() {
    if (!ATTRIBUTE_MAP.isEmpty()) {
      return;
    }
    
    ATTRIBUTE_MAP.put("name", new AttributeSetter<>(Mod::setName, ValueType.SIMPLE));
    ATTRIBUTE_MAP.put("path", new AttributeSetter<>(Mod::setPath, ValueType.SIMPLE));
    ATTRIBUTE_MAP.put("user_dir", new AttributeSetter<>(Mod::setUserDir, ValueType.SIMPLE));
    ATTRIBUTE_MAP.put("replace_path", new AttributeSetter<>(Mod::addReplacePath, ValueType.SIMPLE));
    ATTRIBUTE_MAP.put("archive", new AttributeSetter<>(Mod::setArchive, ValueType.SIMPLE));
    ATTRIBUTE_MAP.put("picture", new AttributeSetter<>(Mod::setPicture, ValueType.SIMPLE));
    ATTRIBUTE_MAP.put("tags", new AttributeSetter<>(Mod::setTags, ValueType.LIST));
    ATTRIBUTE_MAP.put("dependencies", new AttributeSetter<>(Mod::setDependencies, ValueType.LIST));
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();
  private static final Map<String, AttributeSetter<?>> ATTRIBUTE_MAP = new HashMap<>();
  
  private final File modFile;
  private final Parser parser;

  private enum ValueType { SIMPLE, LIST };
  
  private class AttributeSetter<T> {
    final BiConsumer<Mod, T> setter;
    final ValueType type;
    AttributeSetter(BiConsumer<Mod, T> setter, ValueType valueType) {
      this.setter = setter;
      this.type = valueType;
    }
  }

  @SuppressWarnings("unchecked")
  private Mod parse() {
    LOG.entry();
    final Mod result = new ModImpl();
    Node root = parser.parse();

    for (Node node : root.getChildren()) {
      AttributeSetter attribute = ATTRIBUTE_MAP.get(node.getName());
      if (attribute != null) {
        final Object nameValue;
        if (attribute.type == ValueType.SIMPLE) {
          nameValue = getSimpleValue(node);
        } else {
          nameValue = getListValue(node);
        }
        attribute.setter.accept(result, nameValue);
      }
    }

    return LOG.exit(result);
  }

  private List<String> getListValue(Node node) {
    LOG.entry(node);
    List<String> result = node.getChildren().stream()
            .map(Node::getName)
            .collect(Collectors.toList());
    return LOG.exit(result);
  }

  private String getSimpleValue(Node node) {
    LOG.entry(node);
    String result = node.getChildren().get(0).getName();
    return LOG.exit(result);
  }

}
