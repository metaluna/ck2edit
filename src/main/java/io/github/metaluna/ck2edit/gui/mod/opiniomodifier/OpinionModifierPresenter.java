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
package io.github.metaluna.ck2edit.gui.mod.opiniomodifier;

import io.github.metaluna.ck2edit.business.mod.opinionmodifier.OpinionModifier;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OpinionModifierPresenter {

  public void initialize() {
    opinionSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-1000, 1000, 0, 5));
    durationSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-1000, 1000));
  }

  public void setOpinionModifier(OpinionModifier opinionModifier) {
    LOG.entry(opinionModifier);
    this.opinionModifier = opinionModifier;
    refresh();
    bindAttributes();
    LOG.exit();
  }

  // ---vvv--- PRIVATE ---vvv---
  private static final Logger LOG = LogManager.getFormatterLogger();
  private OpinionModifier opinionModifier;

  @FXML
  private TextField nameTextField;
  @FXML
  private Spinner<Integer> opinionSpinner;
  @FXML
  private Spinner<Integer> durationSpinner;
  @FXML
  private CheckBox prisonReasonCheckBox;
  @FXML
  private CheckBox banishReasonCheckBox;
  @FXML
  private CheckBox executeReasonCheckBox;
  @FXML
  private CheckBox revokeReasonCheckBox;
  @FXML
  private CheckBox divorceReasonCheckBox;
  @FXML
  private CheckBox inheritCheckBox;
  @FXML
  private CheckBox crimeCheckBox;
  @FXML
  private CheckBox enemyCheckBox;

  private void refresh() {
    this.nameTextField.setText(this.opinionModifier.getName());
    this.opinionSpinner.getValueFactory().setValue(this.opinionModifier.getOpinion());
    this.durationSpinner.getValueFactory().setValue(this.opinionModifier.getDuration().orElse(null));
    this.prisonReasonCheckBox.setSelected(this.opinionModifier.isPrisonReason());
    this.banishReasonCheckBox.setSelected(this.opinionModifier.isBanishReason());
    this.executeReasonCheckBox.setSelected(this.opinionModifier.isExecuteReason());
    this.revokeReasonCheckBox.setSelected(this.opinionModifier.isRevokeReason());
    this.divorceReasonCheckBox.setSelected(this.opinionModifier.isDivorceReason());
    this.inheritCheckBox.setSelected(this.opinionModifier.isInherited());
    this.crimeCheckBox.setSelected(this.opinionModifier.isCrime());
    this.enemyCheckBox.setSelected(this.opinionModifier.isEnemy());
  }

  private void bindAttributes() {
    LOG.entry();
    this.nameTextField.textProperty().addListener((ObservableValue<? extends String> o, String oldV, String newV) -> opinionModifier.setName(newV));
    this.opinionSpinner.getValueFactory().valueProperty().addListener((ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) -> {
      if (newValue != null) {
        opinionModifier.setOpinion(newValue);
      } else {
        this.opinionSpinner.getValueFactory().setValue(0);
      }
    });
    this.durationSpinner.getValueFactory().valueProperty().addListener((ObservableValue<? extends Integer> o, Integer oldV, Integer newV) -> opinionModifier.setDuration(newV));
    this.prisonReasonCheckBox.selectedProperty().addListener((ObservableValue<? extends Boolean> o, Boolean oldV, Boolean newV) -> opinionModifier.setPrisonReason(newV));
    this.banishReasonCheckBox.selectedProperty().addListener((ObservableValue<? extends Boolean> o, Boolean oldV, Boolean newV) -> opinionModifier.setBanishReason(newV));
    this.executeReasonCheckBox.selectedProperty().addListener((ObservableValue<? extends Boolean> o, Boolean oldV, Boolean newV) -> opinionModifier.setExecuteReason(newV));
    this.revokeReasonCheckBox.selectedProperty().addListener((ObservableValue<? extends Boolean> o, Boolean oldV, Boolean newV) -> opinionModifier.setRevokeReason(newV));
    this.divorceReasonCheckBox.selectedProperty().addListener((ObservableValue<? extends Boolean> o, Boolean oldV, Boolean newV) -> opinionModifier.setDivorceReason(newV));
    this.inheritCheckBox.selectedProperty().addListener((ObservableValue<? extends Boolean> o, Boolean oldV, Boolean newV) -> opinionModifier.setInherited(newV));
    this.crimeCheckBox.selectedProperty().addListener((ObservableValue<? extends Boolean> o, Boolean oldV, Boolean newV) -> opinionModifier.setCrime(newV));
    this.enemyCheckBox.selectedProperty().addListener((ObservableValue<? extends Boolean> o, Boolean oldV, Boolean newV) -> opinionModifier.setEnemy(newV));
    LOG.exit();
  }

}
