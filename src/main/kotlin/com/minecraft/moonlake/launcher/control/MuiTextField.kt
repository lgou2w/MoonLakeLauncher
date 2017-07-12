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

package com.minecraft.moonlake.launcher.control

import com.minecraft.moonlake.launcher.skin.MuiTextFieldSkin
import com.minecraft.moonlake.launcher.validation.ValidatorBase
import com.sun.javafx.css.converters.PaintConverter
import javafx.beans.property.*
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.css.*
import javafx.scene.Node
import javafx.scene.control.Control
import javafx.scene.control.Skin
import javafx.scene.control.TextField
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.text.Font
import java.util.*

class MuiTextField: TextField {

    /**************************************************************************
     *
     * Constructor
     *
     **************************************************************************/

    constructor() : super()
    constructor(text: String?) : super(text)

    init {
        isFocusTraversable = false
        styleClass.add("mui-text-field")
    }

    /**************************************************************************
     *
     * Public API
     *
     **************************************************************************/

    fun validate(): Boolean {
        validators.forEach {
            if(it.getSrcNode() !is Node)
                it.setSrcNode(this)
            it.validate()
            if(it.getHasErrors()) {
                activeValidator.set(it)
                return false
            }
        }
        activeValidator.set(null)
        return true
    }

    fun resetValidation() {
        pseudoClassStateChanged(ValidatorBase.PSEUDO_CLASS_ERROR, false)
        activeValidator.set(null)
    }

    /**************************************************************************
     *
     * Override Implements
     *
     **************************************************************************/

    override fun createDefaultSkin(): Skin<*>
            = MuiTextFieldSkin(this)

    /**************************************************************************
     *
     * Properties
     *
     **************************************************************************/

    private var activeValidator: ReadOnlyObjectWrapper<ValidatorBase>
            = ReadOnlyObjectWrapper()

    fun activeValidatorProperty(): ReadOnlyObjectWrapper<ValidatorBase>
            = activeValidator

    fun getActiveValidator(): ValidatorBase?
            = if(activeValidator is ReadOnlyObjectWrapper) activeValidator.get() else null

    private var validators: ObservableList<ValidatorBase>
            = FXCollections.observableArrayList()

    fun getValidators(): ObservableList<ValidatorBase>
            = validators

    fun setValidators(vararg validators: ValidatorBase)
            = this.validators.setAll(*validators)

    private var focusColor: StyleableObjectProperty<Paint>
            = SimpleStyleableObjectProperty(StyleableProperties.FOCUS_COLOR, this, "focusColor", Color.valueOf("#4285f4"))

    fun focusColorProperty(): StyleableObjectProperty<Paint>
            = focusColor

    fun getFocusColor(): Paint
            = if(focusColor is StyleableObjectProperty) focusColor.get() else Color.valueOf("#4059a9")

    fun setFocusColor(focusColor: Paint)
            = this.focusColor.set(focusColor)

    private var unFocusColor: StyleableObjectProperty<Paint>
            = SimpleStyleableObjectProperty(StyleableProperties.UNFOCUS_COLOR, this, "unFocusColor", Color.valueOf("#cccccc"))

    fun unFocusColorProperty(): StyleableObjectProperty<Paint>
            = unFocusColor

    fun getUnFocusColor(): Paint
            = if(unFocusColor is StyleableObjectProperty) unFocusColor.get() else Color.valueOf("#cccccc")

    fun setUnFocusColor(unFocusColor: Paint)
            = this.unFocusColor.set(unFocusColor)

    private var disableAnimation: BooleanProperty
            = SimpleBooleanProperty(false)

    fun disableAnimationProperty(): BooleanProperty
            = disableAnimation

    fun isDisableAnimation(): Boolean
            = disableAnimation is BooleanProperty && disableAnimation.get()

    fun setDisableAnimation(disableAnimation: Boolean)
            = this.disableAnimation.set(disableAnimation)

    private var labelFloat: BooleanProperty
            = SimpleBooleanProperty(false)

    fun labelFloatProperty(): BooleanProperty
            = labelFloat

    fun isLabelFloat(): Boolean
            = labelFloat is BooleanProperty && labelFloat.get()

    fun setLabelFloat(labelFloat: Boolean)
            = this.labelFloat.set(labelFloat)

    /**************************************************************************
     *
     * Stylesheet Handling
     *
     **************************************************************************/

    internal class StyleableProperties {
        companion object {
            internal val FOCUS_COLOR = object: CssMetaData<MuiTextField, Paint>("-mui-focus-color", PaintConverter.getInstance(), Color.valueOf("#4059a9")) {
                override fun isSettable(styleable: MuiTextField): Boolean
                        = styleable.focusColor !is StyleableObjectProperty || !styleable.focusColor.isBound
                override fun getStyleableProperty(styleable: MuiTextField): StyleableProperty<Paint>
                        = styleable.focusColorProperty()
            }
            internal val UNFOCUS_COLOR = object: CssMetaData<MuiTextField, Paint>("-mui-unfocus-color", PaintConverter.getInstance(), Color.valueOf("#cccccc")) {
                override fun isSettable(styleable: MuiTextField): Boolean
                        = styleable.unFocusColor !is StyleableObjectProperty || !styleable.unFocusColor.isBound
                override fun getStyleableProperty(styleable: MuiTextField): StyleableProperty<Paint>
                        = styleable.unFocusColorProperty()
            }
            internal val CHILD_STYLEABLES: MutableList<CssMetaData<out Styleable, *>> get() {
                val styleables: MutableList<CssMetaData<out Styleable, *>> = ArrayList(Control.getClassCssMetaData())
                Collections.addAll(styleables, FOCUS_COLOR, UNFOCUS_COLOR)
                return Collections.unmodifiableList(styleables)
            }
        }
    }

    private var STYLEABLES: MutableList<CssMetaData<out Styleable, *>>? = null

    override fun getControlCssMetaData(): MutableList<CssMetaData<out Styleable, *>> {
        if(STYLEABLES == null) {
            val styleables: MutableList<CssMetaData<out Styleable, *>> = ArrayList(Control.getClassCssMetaData())
            styleables.addAll(getClassCssMetaData())
            styleables.addAll(TextField.getClassCssMetaData())
            STYLEABLES = Collections.unmodifiableList(styleables)
        }
        return STYLEABLES!!
    }

    /**************************************************************************
     *
     * Static
     *
     **************************************************************************/

    companion object {
        fun getClassCssMetaData(): MutableList<CssMetaData<out Styleable, *>>
                = StyleableProperties.CHILD_STYLEABLES
    }
}
