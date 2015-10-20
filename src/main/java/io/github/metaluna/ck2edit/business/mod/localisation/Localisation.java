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
package io.github.metaluna.ck2edit.business.mod.localisation;

import java.util.Objects;

/**
 * Represents a key with its translations.
 */
public class Localisation {

  /**
   * Specifies shortcuts to languages implemented in the base game.
   */
  public enum Language {

    ENGLISH(1), FRENCH(2), GERMAN(3), SPANISH(5);

    private Language(int column) {
      this.column = column;
    }

    private final int column;
  }

  /**
   * Constructor
   *
   * @param id the key to this row of translations
   */
  public Localisation(String id) {
    this.columns[ID_COLUMN] = Objects.requireNonNull(id);
    this.columns[END_MARKER_COLUMN] = "x";
  }

  /**
   * @return the key of this row of translations
   */
  public String getId() {
    return this.columns[ID_COLUMN];
  }

  /**
   * @param id the new key of this row of translations. Must not be
   * <code>null</code>
   */
  public void setId(String id) {
    this.columns[ID_COLUMN] = Objects.requireNonNull(id);
  }

  /**
   * Retrieves the translation of the specified language.
   *
   * @param language the language to retrieve
   * @return the translation. Never <code>null</code>
   */
  public String getLanguage(Language language) {
    return this.getLanguage(language.column);
  }

  /**
   * Retrieves the translation in the specified column.
   *
   * @param column the column to retrieve
   * @return the translation. Never <code>null</code>
   */
  public String getLanguage(int column) {
    String result = this.columns[column];
    if (result == null) {
      result = "";
    }
    return result;
  }

  /**
   * Sets the text of the specified language. The text must not contain any
   * semicolons. If the translation is <code>null</code> it will be replaced by
   * an empty string.
   *
   * @param language the language to set
   * @param translation the new translation
   * @throws IllegalArgumentException if the translation contains a semicolon
   */
  public void setLanguage(Language language, String translation) {
    setLanguage(language.column, translation);
  }

  /**
   * Sets the text of the specified column. The column must be > 0 and < 14. The
   * text must not contain any semicolons. If the translation is
   * <code>null</code> it will be replaced by an empty string.
   *
   * @param column the column to set
   * @param translation the new translation
   * @throws IllegalArgumentException if the translation contains a semicolon
   */
  public void setLanguage(int column, String translation) {
    if (column == ID_COLUMN) {
      throw new IllegalArgumentException("ID must be set via the setId() method.");
    } else if (column < 0) {
      throw new IllegalArgumentException("Column must not be negative but is " + column);
    } else if (column == END_MARKER_COLUMN) {
      throw new IllegalArgumentException(
              "Cannot set the text of the last column because it must be 'x'.");
    } else if (column > END_MARKER_COLUMN) {
      throw new IllegalArgumentException(
              String.format("Column must not be larger than %d but was %d", END_MARKER_COLUMN, column));
    }
    if (translation == null) {
      translation = "";
    } else if (translation.contains(";")) {
      throw new IllegalArgumentException(String.format("String must not contain semicolons but was '%s' (column: %d)", translation, column));
    }
    this.columns[column] = translation;

  }

  // ---vvv--- PRIVATE ---vvv---
  private static final int ID_COLUMN = 0;
  private static final int END_MARKER_COLUMN = 14;
  private final String[] columns = new String[END_MARKER_COLUMN + 1];
}
