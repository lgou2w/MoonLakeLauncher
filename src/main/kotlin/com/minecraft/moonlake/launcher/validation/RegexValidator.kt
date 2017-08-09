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

package com.minecraft.moonlake.launcher.validation

import javafx.beans.DefaultProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.scene.control.TextInputControl

@DefaultProperty("icon")
class RegexValidator: ValidatorBase(), ValidatorValuable {

    /**************************************************************************
     *
     * Override Implements
     *
     **************************************************************************/

    override fun eval() {
        if(srcNode.get() is TextInputControl)
            evalTextInputField()
    }

    /**************************************************************************
     *
     * Properties
     *
     **************************************************************************/

    private var value: StringProperty = SimpleStringProperty(".*?")

    fun valueProperty(): StringProperty
            = value

    override fun getValue(): String
            = value.get()

    fun setValue(value: String)
            = this.value.set(value)

    /**************************************************************************
     *
     * Private Implements
     *
     **************************************************************************/

    private fun evalTextInputField() {
        val textField = srcNode.get() as TextInputControl
        if(textField.text == null || textField.text.isEmpty()) {
            hasErrors.set(false)
            return
        }
        hasErrors.set(Regex(value.get()).matches(textField.text).not())
    }
}
