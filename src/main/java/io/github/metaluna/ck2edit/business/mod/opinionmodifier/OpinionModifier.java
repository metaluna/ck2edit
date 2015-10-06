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

/**
 * Represents a single opinion modifier
 */
public class OpinionModifier {

  public OpinionModifier(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
  
  public int getOpinion() {
    return opinion;
  }

  public void setOpinion(int opinion) {
    this.opinion = opinion;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public boolean isPrisonReason() {
    return prisonReason;
  }

  public void setPrisonReason(boolean prisonReason) {
    this.prisonReason = prisonReason;
  }

  public boolean isBanishReason() {
    return banishReason;
  }

  public void setBanishReason(boolean banishReason) {
    this.banishReason = banishReason;
  }

  public boolean isExecuteReason() {
    return executeReason;
  }

  public void setExecuteReason(boolean executeReason) {
    this.executeReason = executeReason;
  }

  public boolean isRevokeReason() {
    return revokeReason;
  }

  public void setRevokeReason(boolean revokeReason) {
    this.revokeReason = revokeReason;
  }

  public boolean isDivorceReason() {
    return divorceReason;
  }

  public void setDivorceReason(boolean divorceReason) {
    this.divorceReason = divorceReason;
  }

  public boolean isInherited() {
    return inherited;
  }

  public void setInherited(boolean inherited) {
    this.inherited = inherited;
  }

  public boolean isEnemy() {
    return enemy;
  }

  public void setEnemy(boolean enemy) {
    this.enemy = enemy;
  }

  public boolean isCrime() {
    return crime;
  }

  public void setCrime(boolean crime) {
    this.crime = crime;
  }

  // ---vvv--- PRIVATE ---vvv---
  /** required */
  private final String name;
  /** required */
  private int opinion;
  /** required */
  private int duration;
  private boolean prisonReason;
  private boolean banishReason;
  private boolean executeReason;
  private boolean revokeReason;
  private boolean divorceReason;
  private boolean inherited;
  private boolean enemy;
  private boolean crime;
}
