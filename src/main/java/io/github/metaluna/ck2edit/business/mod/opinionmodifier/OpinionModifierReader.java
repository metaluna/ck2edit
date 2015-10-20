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
package io.github.metaluna.ck2edit.business.mod.opinionmodifier;

import io.github.metaluna.ck2edit.business.mod.ModFileReader;
import io.github.metaluna.ck2edit.dataaccess.parser.Node;
import io.github.metaluna.ck2edit.dataaccess.parser.Parser;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Reads a opinion modifier file.
 */
class OpinionModifierReader extends ModFileReader {

  /**
   * Reads a file from disk.
   *
   * @return the parsed opinion modifier file
   */
  public OpinionModifierFile read() {
    LOG.entry();
    Node root = parser.parse();
    OpinionModifierFile result = parseOpinionModifiers(root);
    return LOG.exit(result);
  }

  // ---vvv--- PACKAGE-PRIVATE ---vvv---
  OpinionModifierReader(Path modFile, Parser parser) {
    LOG.entry(modFile, parser);
    this.modFile = Objects.requireNonNull(modFile);
    this.parser = Objects.requireNonNull(parser);
    this.initializeAttributeMap();
    LOG.exit();
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();
  private static final Map<String, AttributeSetter<OpinionModifier, ?>> ATTRIBUTE_MAP = new HashMap<>();

  private final Path modFile;
  private final Parser parser;

  private void initializeAttributeMap() {
    if (!ATTRIBUTE_MAP.isEmpty()) {
      return;
    }
    
    ATTRIBUTE_MAP.put("opinion", new AttributeSetter<>(OpinionModifier::setOpinion, ValueType.INTEGER));
    ATTRIBUTE_MAP.put("months", new AttributeSetter<>(OpinionModifier::setDuration, ValueType.INTEGER));
    ATTRIBUTE_MAP.put("prison_reason", new AttributeSetter<>(OpinionModifier::setPrisonReason, ValueType.BOOLEAN));
    ATTRIBUTE_MAP.put("banish_reason", new AttributeSetter<>(OpinionModifier::setBanishReason, ValueType.BOOLEAN));
    ATTRIBUTE_MAP.put("execute_reason", new AttributeSetter<>(OpinionModifier::setExecuteReason, ValueType.BOOLEAN));
    ATTRIBUTE_MAP.put("revoke_reason", new AttributeSetter<>(OpinionModifier::setRevokeReason, ValueType.BOOLEAN));
    ATTRIBUTE_MAP.put("divorce_reason", new AttributeSetter<>(OpinionModifier::setDivorceReason, ValueType.BOOLEAN));
    ATTRIBUTE_MAP.put("inherit", new AttributeSetter<>(OpinionModifier::setInherited, ValueType.BOOLEAN));
    ATTRIBUTE_MAP.put("enemy", new AttributeSetter<>(OpinionModifier::setEnemy, ValueType.BOOLEAN));
    ATTRIBUTE_MAP.put("crime", new AttributeSetter<>(OpinionModifier::setCrime, ValueType.BOOLEAN));
  }

  @SuppressWarnings("unchecked")
  private OpinionModifierFile parseOpinionModifiers(Node root) {
    LOG.entry(root);
    final OpinionModifierFile result = new OpinionModifierFile(modFile);
    for (Node n : root.getChildren()) {
      OpinionModifier modifier = new OpinionModifier(n.getName());
      n.getChildren().parallelStream()
            .filter(c -> ATTRIBUTE_MAP.containsKey(c.getName()))
            .forEach(c -> {
              AttributeSetter attribute = ATTRIBUTE_MAP.get(c.getName());
              Object nameValue = fetchAttributeValue(attribute, c);
              attribute.setter.accept(modifier, nameValue);
            });
      result.add(modifier);
    }
    return LOG.exit(result);
  }
  
}
