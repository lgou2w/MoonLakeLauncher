/*
 * Copyright (C) 2017 The MoonLake Authors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.minecraft.moonlake.launcher.skin

import com.minecraft.moonlake.launcher.animation.MuiCachedTransition
import com.minecraft.moonlake.launcher.control.MuiLabel
import com.minecraft.moonlake.launcher.control.MuiTextField
import com.minecraft.moonlake.launcher.layout.MuiHBox
import com.minecraft.moonlake.launcher.layout.MuiStackPane
import com.minecraft.moonlake.launcher.util.MuiUtils
import com.minecraft.moonlake.launcher.validation.ValidatorBase
import com.minecraft.moonlake.launcher.validation.ValidatorValuable
import com.sun.javafx.scene.control.skin.TextFieldSkin
import javafx.animation.*
import javafx.beans.binding.Bindings
import javafx.beans.value.ObservableValue
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text
import javafx.scene.transform.Scale
import javafx.util.Duration
import java.util.concurrent.Callable

class MuiTextFieldSkin: TextFieldSkin {

    /**************************************************************************
     *
     * Private Member
     *
     **************************************************************************/

    private var invalid = true
    private var initScale = .05
    private var line = MuiStackPane()
    private var errorLabel = MuiLabel()
    private var errorIcon = MuiStackPane()
    private var errorContainer = MuiHBox()
    private var focusedLine = MuiStackPane()
    private var promptContainer = MuiStackPane()

    private var scaleLine = Timeline()
    private var scaleLess = Timeline()
    private var textPane: Pane? = null
    private var promptText: Text? = null
    private var oldPromptTextFill: Paint? = null
    private var transition: ParallelTransition? = null
    private var promptTextUpTransition: MuiCachedTransition? = null
    private var promptTextDownTransition: MuiCachedTransition? = null
    private var promptTextColorTransition: MuiCachedTransition? = null
    private var errorHideTransition = Timeline(KeyFrame(Duration(80.0), KeyValue(errorContainer.opacityProperty(), 0, Interpolator.LINEAR)))
    private var errorShowTransition = Timeline(KeyFrame(Duration(80.0), KeyValue(errorContainer.opacityProperty(), 1, Interpolator.EASE_OUT)))
    private var usePromptText = Bindings.createBooleanBinding(Callable { usePromptText() }, skinnable.textProperty(), skinnable.promptTextProperty())

    private val scale = Scale(initScale, 1.0)
    private val errorContainerClip = Rectangle()
    private val promptTextScale = Scale(1.0, 1.0, .0, .0)
    private val errorContainerClipScale = Scale(1.0, .0, .0, .0)

    // lines animation
    private var linesAnimation = Timeline(
            KeyFrame(Duration.ZERO, KeyValue(scale.xProperty(), 0, Interpolator.EASE_BOTH), KeyValue(focusedLine.opacityProperty(), 0, Interpolator.EASE_BOTH)),
            KeyFrame(Duration(1.0), KeyValue(focusedLine.opacityProperty(), 1.0, Interpolator.EASE_BOTH)),
            KeyFrame(Duration(160.0), KeyValue(scale.xProperty(), 1, Interpolator.EASE_BOTH)))

    /**************************************************************************
     *
     * Constructor
     *
     **************************************************************************/

    constructor(muiTextField: MuiTextField) : super(muiTextField) {
        muiTextField.background = Background(BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY))
        muiTextField.padding = Insets(4.0, .0, 4.0, .0)

        errorLabel.styleClass.add("error-label")
        line.styleClass.add("input-line")
        focusedLine.styleClass.add("input-focused-line")

        line.prefHeight = 1.0
        line.translateY = 1.0
        line.background = Background(BackgroundFill(muiTextField.getUnFocusColor(), CornerRadii.EMPTY, Insets.EMPTY))
        if(muiTextField.isDisabled) {
            line.background = Background(BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY))
            line.border = Border(BorderStroke(muiTextField.getUnFocusColor(), BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths(1.0)))
        }
        focusedLine.prefHeight = 2.0
        focusedLine.translateY = .0
        focusedLine.opacity = .0
        focusedLine.transforms.add(scale)
        focusedLine.background = Background(BackgroundFill(muiTextField.getFocusColor(), CornerRadii.EMPTY, Insets.EMPTY))

        errorContainer.children.setAll(errorIcon, MuiStackPane(errorLabel))
        errorContainer.alignment = Pos.CENTER_LEFT
        errorContainer.spacing = 2.0
        errorContainer.padding = Insets(4.0, .0, .0, .0)
        errorContainer.isVisible = false
        errorContainer.opacity = .0
        errorContainer.isManaged = false
        errorLabel.alignment = Pos.TOP_LEFT
        HBox.setHgrow(errorLabel, Priority.ALWAYS)
        errorContainerClip.transforms.add(errorContainerClipScale)
        errorContainer.clip = if(muiTextField.isDisableAnimation()) null else errorContainerClip

        children.addAll(line, focusedLine, promptContainer, errorContainer)

        muiTextField.labelFloatProperty().addListener { _, _, newValue -> run {
            if(newValue)
                MuiUtils.runInFx(Runnable { createLabelFloating() })
            else
                promptText!!.visibleProperty().bind(usePromptText)
            createFocusTransition()
        }}
        muiTextField.activeValidatorProperty().addListener { _: ObservableValue<out ValidatorBase>?, oldValue: ValidatorBase?, newValue: ValidatorBase? ->  run {
            if(textPane != null) {
                if(!skinnable().isDisableAnimation()) {
                    if(newValue != null) {
                        errorHideTransition.setOnFinished { _ -> run {
                            showError(newValue)
                            val width = skinnable.width
                            val errorContainerHeight = computeErrorHeight(computeErrorWidth(width))
                            if(errorLabel.isWrapText) {
                                if(errorContainerHeight < errorContainer.height) {
                                    scaleLess.keyFrames.setAll(createSmallerScaleFrame(errorContainerHeight))
                                    scaleLess.setOnFinished { _ -> run {
                                        updateErrorContainerSize(width, errorContainerHeight)
                                        errorContainerClipScale.y = 1.0
                                    }}
                                    val transition = SequentialTransition(scaleLess, errorShowTransition)
                                    transition.play()
                                } else {
                                    errorContainerClipScale.y = if(oldValue == null) .0 else errorContainer.height / errorContainerHeight
                                    updateErrorContainerSize(width, errorContainerHeight)
                                    scaleLine.keyFrames.setAll(createScaleToOneFrames())
                                    val parallelTransition = ParallelTransition()
                                    parallelTransition.children.addAll(scaleLine, errorShowTransition)
                                    parallelTransition.play()
                                }
                            } else {
                                errorContainerClipScale.y = 1.0
                                updateErrorContainerSize(width, errorContainerHeight)
                                val transition = SequentialTransition(errorShowTransition)
                                transition.play()
                            }
                        }}
                        errorHideTransition.play()
                    } else {
                        errorHideTransition.onFinished = null
                        if(errorLabel.isWrapText) {
                            scaleLess.keyFrames.setAll(KeyFrame(Duration(100.0), KeyValue(errorContainerClipScale.yProperty(), 0, Interpolator.EASE_BOTH)))
                            scaleLess.setOnFinished { _ -> run {
                                hideError()
                                errorContainerClipScale.y = .0
                            }}
                            val transition = SequentialTransition(scaleLess)
                            transition.play()
                        } else {
                            errorContainerClipScale.y = .0
                        }
                    }
                } else {
                    if(newValue != null)
                        MuiUtils.runInFxWait(Runnable { showError(newValue) })
                    else
                        MuiUtils.runInFxWait(Runnable { hideError() })
                }
            }
        }}
        muiTextField.focusColorProperty().addListener { _, _, newValue -> run {
            if(newValue != null) {
                focusedLine.background = Background(BackgroundFill(newValue, CornerRadii.EMPTY, Insets.EMPTY))
                if(skinnable().isLabelFloat()) {
                    promptTextColorTransition = object: MuiCachedTransition(textPane!!, Timeline(KeyFrame(Duration(1300.0), KeyValue(promptTextFill, newValue, Interpolator.EASE_BOTH)))) {
                        init {
                            cycleDuration = Duration(160.0)
                            delay = Duration(.0)
                        }
                        override fun starting() {
                            super.starting()
                            oldPromptTextFill = promptTextFill.get()
                        }
                    }
                    resetFocusTransition()
                }
            }
        }}
        muiTextField.unFocusColorProperty().addListener { _, _, newValue -> run {
            if(newValue != null)
                line.background = Background(BackgroundFill(newValue, CornerRadii.EMPTY, Insets.EMPTY))
        }}
        muiTextField.focusedProperty().addListener { _, _, newValue -> run {
            if(newValue) focus()
            else unFocus()
            skinnable().validate()
        }}
        muiTextField.textProperty().addListener { _, _, newValue -> run {
            if(!skinnable.isFocused && skinnable().isLabelFloat()) {
                if(newValue == null || newValue.isEmpty())
                    animateLabelFloating(false)
                else
                    animateLabelFloating(true)
            }
        }}
        muiTextField.disabledProperty().addListener { _, _, newValue -> run {
            line.background = Background(BackgroundFill(if(newValue) Color.TRANSPARENT else skinnable().getUnFocusColor(), CornerRadii.EMPTY, Insets.EMPTY))
            line.border = if(newValue) Border(BorderStroke(skinnable().getUnFocusColor(), BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths(line.height))) else Border.EMPTY
        }}
        promptTextFill.addListener { _, oldValue, newValue -> run {
            if(newValue == Color.TRANSPARENT && skinnable().isLabelFloat())
                promptTextFill.set(oldValue)
        }}
        registerChangeListener(muiTextField.disableAnimationProperty(), "DISABLE_ANIMATION")
    }

    /**************************************************************************
     *
     * Override Implements
     *
     **************************************************************************/

    override fun handleControlPropertyChanged(propertyReference: String) {
        if("DISABLE_ANIMATION" == propertyReference)
            errorContainer.clip = if(skinnable().isDisableAnimation()) null else errorContainerClip
        else
            super.handleControlPropertyChanged(propertyReference)
    }

    override fun layoutChildren(x: Double, y: Double, w: Double, h: Double) {
        super.layoutChildren(x, y, w, h)
        if(transition == null || transition!!.status == Animation.Status.STOPPED) {
            if(skinnable.isFocused && skinnable().isLabelFloat())
                promptTextFill.set(skinnable().getFocusColor())
        }
        if(invalid) {
            invalid = false
            textPane = children[0] as Pane
            createLabelFloating()
            val activeValidator = skinnable().getActiveValidator()
            if(activeValidator != null) {
                showError(activeValidator)
                val errorContainerWidth = w - errorIcon.prefWidth(-1.0)
                errorContainer.opacity = 1.0
                errorContainer.resize(w, computeErrorHeight(errorContainerWidth))
                errorContainerClip.width = w
                errorContainerClip.height = errorContainer.height
                errorContainerClipScale.y = 1.0
            }
            super.layoutChildren(x, y, w, h)
            createFocusTransition()
            if(skinnable.isFocused)
                focus()
        }
        val height = skinnable.height
        val focusedLineHeight = focusedLine.prefHeight(-1.0)
        focusedLine.resizeRelocate(x, height, w, focusedLineHeight)
        line.resizeRelocate(x, height, w, line.prefHeight(-1.0))
        errorContainer.relocate(x, height + focusedLineHeight)
        if(skinnable().isDisableAnimation())
            errorContainer.resize(w, computeErrorHeight(computeErrorWidth(w)))
        scale.pivotX = w / 2
    }

    /**************************************************************************
     *
     * Private Implements
     *
     **************************************************************************/

    private fun skinnable(): MuiTextField
            = skinnable as MuiTextField

    private fun computeErrorWidth(width: Double): Double
            = width - errorIcon.prefWidth(-1.0)

    private fun computeErrorHeight(errorContainerWidth: Double): Double
            = errorLabel.prefHeight(errorContainerWidth) + errorContainer.snappedBottomInset() + errorContainer.snappedTopInset()

    private fun updateErrorContainerSize(width: Double, errorContainerHeight: Double) {
        errorContainerClip.width = width
        errorContainerClip.height = errorContainerHeight
        errorContainer.resize(width, errorContainerHeight)
    }

    private fun createSmallerScaleFrame(errorContainerHeight: Double): KeyFrame
            = KeyFrame(Duration(100.0), KeyValue(errorContainerClipScale.yProperty(), errorContainerHeight / errorContainer.height, Interpolator.EASE_BOTH))

    private fun createScaleToOneFrames(): KeyFrame
            = KeyFrame(Duration(100.0), KeyValue(errorContainerClipScale.yProperty(), 1, Interpolator.EASE_BOTH))

    private fun createLabelFloating() {
        if(skinnable().isLabelFloat()) {
            if(promptText == null) {
                var triggerLabelFloat = false
                if(textPane!!.children[0] is Text) {
                    promptText = textPane!!.children[0] as Text
                } else {
                    try {
                        val field = TextFieldSkin::class.java.getDeclaredField("promptNode")
                        field.isAccessible = true
                        field.set(this, createPromptNode())
                        triggerLabelFloat = true
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                promptText!!.transforms.add(promptTextScale)
                promptContainer.children.add(promptText)
                if(triggerLabelFloat) {
                    promptText!!.translateY = -textPane!!.height
                    promptTextScale.x = .85
                    promptTextScale.y = .85
                }
            }
            promptTextUpTransition = object: MuiCachedTransition(textPane!!, Timeline(
                    KeyFrame(Duration(1300.0),
                            KeyValue(promptText!!.translateYProperty(), -textPane!!.height, Interpolator.EASE_BOTH),
                            KeyValue(promptTextScale.xProperty(), .85, Interpolator.EASE_BOTH),
                            KeyValue(promptTextScale.yProperty(), .85, Interpolator.EASE_BOTH)))) {
                init {
                    cycleDuration = Duration(240.0)
                    delay = Duration(.0)
                }
            }
            promptTextDownTransition = object: MuiCachedTransition(textPane!!, Timeline(
                    KeyFrame(Duration(1300.0),
                            KeyValue(promptText!!.translateYProperty(), 0, Interpolator.EASE_BOTH),
                            KeyValue(promptTextScale.xProperty(), 1, Interpolator.EASE_BOTH),
                            KeyValue(promptTextScale.yProperty(), 1, Interpolator.EASE_BOTH)))) {
                init {
                    cycleDuration = Duration(240.0)
                    delay = Duration(.0)
                }
            }
            promptTextColorTransition = object: MuiCachedTransition(textPane!!, Timeline(
                    KeyFrame(Duration(1300.0),
                            KeyValue(promptTextFill, skinnable().getFocusColor(), Interpolator.EASE_BOTH)))) {
                init {
                    cycleDuration = Duration(160.0)
                    delay = Duration(.0)
                }

                override fun starting() {
                    super.starting()
                    oldPromptTextFill = promptTextFill.get()
                }
            }
            promptTextDownTransition!!.setOnFinished { _ -> run {
                promptText!!.translateY = .0
                promptTextScale.x = 1.0
                promptTextScale.y = 1.0
            }}
            promptText!!.visibleProperty().unbind()
            promptText!!.visibleProperty().set(true)
        }
    }

    private fun createPromptNode(): Text {
        promptText = Text()
        promptText!!.isManaged = false
        promptText!!.styleClass.add("text")
        promptText!!.visibleProperty().bind(usePromptText)
        promptText!!.fontProperty().bind(skinnable.fontProperty())
        promptText!!.textProperty().bind(skinnable.promptTextProperty())
        promptText!!.fillProperty().bind(promptTextFill)
        promptText!!.layoutX = 1.0
        return promptText!!
    }

    private fun createFocusTransition() {
        transition = ParallelTransition()
        if(skinnable().isLabelFloat()) {
            transition!!.children.add(promptTextUpTransition)
            transition!!.children.add(promptTextColorTransition)
        }
        transition!!.children.add(linesAnimation)
    }

    private fun animateLabelFloating(isUp: Boolean) {
        if(promptText == null) {
            MuiUtils.runInFxLater(Runnable { animateLabelFloating(isUp) })
        } else {
            resetFocusTransition()
            if(isUp && promptText!!.translateY == .0) {
                promptTextDownTransition!!.stop()
                promptTextUpTransition!!.play()
            } else if(!isUp) {
                promptTextUpTransition!!.stop()
                promptTextDownTransition!!.play()
            }
        }
    }

    private fun usePromptText(): Boolean {
        val text = skinnable.text
        val promptText = skinnable.promptText
        return (text == null || text.isEmpty()) &&
                promptText != null && !promptText.isEmpty() && promptTextFill.get() != Color.TRANSPARENT
    }

    private fun focus() {
        if(textPane == null) {
            MuiUtils.runInFxLater(Runnable { focus() })
        } else {
            if(transition == null)
                createFocusTransition()
            transition!!.play()
        }
    }

    private fun unFocus() {
        if(transition != null)
            transition!!.stop()
        scale.x = initScale
        focusedLine.opacity = .0
        if(skinnable().isLabelFloat() && oldPromptTextFill != null) {
            promptTextFill.set(oldPromptTextFill)
            if(usePromptText())
                promptTextDownTransition!!.play()
        }
    }

    private fun resetFocusTransition() {
        if(transition != null) {
            transition!!.stop()
            transition!!.children.remove(promptTextUpTransition)
            transition = null
        }
    }

    private fun showError(validator: ValidatorBase) {
        if(validator is ValidatorValuable) {
            errorLabel.text = validator.getMessage().replace("\${value}", validator.getValue())
        } else {
            errorLabel.text = validator.getMessage()
        }
        val icon = validator.getIcon()
        errorIcon.children.clear()
        if(icon is Node) {
            errorIcon.children.setAll(icon)
            StackPane.setAlignment(icon, Pos.CENTER_RIGHT)
        }
        errorContainer.isVisible = true
    }

    private fun hideError() {
        errorLabel.text = null
        errorIcon.children.clear()
        errorContainer.isVisible = false
    }
}
