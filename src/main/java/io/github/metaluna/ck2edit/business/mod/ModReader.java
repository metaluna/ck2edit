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

import io.github.metaluna.ck2edit.business.mod.opinionmodifier.OpinionModifierFile;
import io.github.metaluna.ck2edit.business.mod.opinionmodifier.OpinionModifierManager;
import io.github.metaluna.ck2edit.dataaccess.parser.Node;
import io.github.metaluna.ck2edit.dataaccess.parser.Parser;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Reads mod description files (*.mod)
 */
class ModReader extends ModFileReader {

  /**
   * Reads a complete mod from a file on disk.
   *
   * @return the parsed mod
   */
  public Mod read() {
    LOG.entry();
    Node root = parser.parse();
    Mod result = parseDescription(root);
    fetchFileList(result);
    return LOG.exit(result);
  }

  // ---vvv--- PACKAGE-PRIVATE ---vvv---
  ModReader(Path modFile, Parser parser, OpinionModifierManager opinionModifierManager) {
    LOG.entry(modFile, parser);
    this.modFile = Objects.requireNonNull(modFile);
    this.parser = Objects.requireNonNull(parser);
    this.opinionModifierManager = Objects.requireNonNull(opinionModifierManager);
    this.initializeAttributeMap();
    LOG.exit();
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();
  private static final Map<String, AttributeSetter<Mod, ?>> ATTRIBUTE_MAP = new HashMap<>();

  private final Path modFile;
  private final Parser parser;
  private final OpinionModifierManager opinionModifierManager;
  
  private void initializeAttributeMap() {
    if (!ATTRIBUTE_MAP.isEmpty()) {
      return;
    }

    ATTRIBUTE_MAP.put("name", new AttributeSetter<>(Mod::setName, ValueType.STRING));
    ATTRIBUTE_MAP.put("path", new AttributeSetter<>(Mod::setPath, ValueType.STRING));
    ATTRIBUTE_MAP.put("user_dir", new AttributeSetter<>(Mod::setUserDir, ValueType.STRING));
    ATTRIBUTE_MAP.put("replace_path", new AttributeSetter<>(Mod::addReplacePath, ValueType.STRING));
    ATTRIBUTE_MAP.put("archive", new AttributeSetter<>(Mod::setArchive, ValueType.STRING));
    ATTRIBUTE_MAP.put("picture", new AttributeSetter<>(Mod::setPicture, ValueType.STRING));
    ATTRIBUTE_MAP.put("tags", new AttributeSetter<>(Mod::setTags, ValueType.LIST));
    ATTRIBUTE_MAP.put("dependencies", new AttributeSetter<>(Mod::setDependencies, ValueType.LIST));
  }

  @SuppressWarnings("unchecked")
  private Mod parseDescription(Node root) {
    LOG.entry();
    final Mod result = new ModImpl();
    result.setDescriptionFile(this.modFile);

    root.getChildren().parallelStream()
            .filter(n -> ATTRIBUTE_MAP.containsKey(n.getName()))
            .forEach(n -> {
              AttributeSetter attribute = ATTRIBUTE_MAP.get(n.getName());
              Object nameValue = fetchAttributeValue(attribute, n);
              attribute.setter.accept(result, nameValue);
            });

    return LOG.exit(result);
  }

  /**
   * Adds all files in the mod's directory to the mod object.
   * <p>
   * The directory being scanned is determined by taking the parent's parent of
   * the description file and appending the path specified in the path attribute
   * of the mod.
   *
   * @param mod the mod to add files to
   */
  private void fetchFileList(Mod mod) {
    LOG.entry(mod);
    String modPath = mod.getPath().replace("\"", "");
    Path path = this.modFile.getParent().getParent().resolve(modPath);
    
    try {
      Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
        private static final String OPINION_MODIFIERS_DIR = "opinion_modifiers";

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          if (file.getParent().endsWith(OPINION_MODIFIERS_DIR)) {
            LOG.trace("Adding opinion modifier %s", file.getFileName());
            mod.addOpinionModifier(opinionModifierManager.fromFile(file));
          }
          return FileVisitResult.CONTINUE;
        }
        
      });
    } catch (IOException ex) {
      throw new ModReadingException(mod, ex);
    }
    LOG.exit();
  }

}
